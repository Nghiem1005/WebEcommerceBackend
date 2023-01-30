package com.example.webecom.services.implement;

import com.example.webecom.dto.request.BillRequestDTO;
import com.example.webecom.dto.request.PaymentRequestDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.Bill;
import com.example.webecom.entities.BillDetail;
import com.example.webecom.entities.Cart;
import com.example.webecom.entities.CartDetail;
import com.example.webecom.entities.Delivery;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.repositories.BillDetailRepository;
import com.example.webecom.repositories.BillRepository;
import com.example.webecom.repositories.CartDetailRepository;
import com.example.webecom.repositories.CartRepository;
import com.example.webecom.repositories.DeliveryRepository;
import com.example.webecom.services.BillService;
import com.example.webecom.services.PaymentService;
import com.mservice.config.Environment;
import com.mservice.enums.RequestType;
import com.mservice.models.PaymentResponse;
import com.mservice.processor.CreateOrderMoMo;
import com.mservice.shared.utils.LogUtils;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
  @Autowired private BillRepository billRepository;
  @Autowired private BillDetailRepository billDetailRepository;
  @Autowired private CartRepository cartRepository;
  @Autowired private CartDetailRepository cartDetailRepository;
  @Autowired private DeliveryRepository deliveryRepository;
  @Autowired APIContext apiContext;

  @Override
  public ResponseEntity<ResponseObject> createPaymentPaypal(PaymentRequestDTO paypalRequestDTO, String cancelUrl, String successUrl) throws PayPalRESTException{
    Amount amount = new Amount();
    amount.setCurrency("USD");
    BigDecimal price = paypalRequestDTO.getPrice().setScale(2, RoundingMode.UP);
    amount.setTotal(String.format("%.2f", price.doubleValue()));

    Transaction transaction = new Transaction();
    transaction.setDescription(paypalRequestDTO.getDescription());
    transaction.setAmount(amount);

    List<Transaction> transactionList = new ArrayList<>();
    transactionList.add(transaction);

    Payer payer = new Payer();
    payer.setPaymentMethod("paypal");

    Payment payment = new Payment();
    payment.setIntent("sale");
    payment.setPayer(payer);
    payment.setTransactions(transactionList);
    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setCancelUrl(cancelUrl);
    redirectUrls.setReturnUrl(successUrl);
    payment.setRedirectUrls(redirectUrls);
    Payment getPayment = payment.create(apiContext);
    String link = "";
    for (Links links : getPayment.getLinks()){
      if (links.getRel().equals("approval_url")){
        link = links.getHref();
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Redirect paypal", link));
  }

  public Payment executePayment(String paymentId, String payerId, String token) throws PayPalRESTException{
    Payment payment = new Payment();
    payment.setId(paymentId);
    PaymentExecution paymentExecution = new PaymentExecution();
    paymentExecution.setPayerId(payerId);
    //apiContext = new APIContext(token);
    return payment.execute(apiContext, paymentExecution);

  }

  public ResponseEntity<?> successPaypal(String paymentId, String payerId, String token)
      throws PayPalRESTException {
    Payment payment = executePayment(paymentId, payerId, token);
    if (payment.getState().equals("approved")){
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Payment is success"));
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, "Payment is not success"));
  }

  @Override
  public ResponseEntity<PaymentResponse> createPaymentMomo(PaymentRequestDTO paymentRequestDTO, String returnUrl) throws Exception{
    LogUtils.init();
    Environment environment = Environment.selectEnv("dev");
    String requestId = String.valueOf(System.currentTimeMillis());

    PaymentResponse responseObject = CreateOrderMoMo.process(environment, paymentRequestDTO.getBillId().toString(), requestId, paymentRequestDTO.getPrice().toString(), paymentRequestDTO.getDescription(), returnUrl, returnUrl, "", RequestType.CAPTURE_WALLET, true);
    return ResponseEntity.status(HttpStatus.OK).body(responseObject);
  }

  @Override
  public ResponseEntity<?> successMomo(Long billId, int resultCode) {
    String message = "";
    Bill bill = billRepository.findById(billId).orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    HttpStatus httpStatus = HttpStatus.OK;
    if (resultCode == 0){
      bill.setStatus("paid");
      billRepository.save(bill);
      message = "Payment success.";
    } else {
      //Restore cart
      List<BillDetail> billDetailList = billDetailRepository.findBillDetailByBill(bill);
      Cart cart = cartRepository.findCartByUser(bill.getUser()).orElseThrow(() -> new ResourceNotFoundException("Could not find cart with user ID = " + bill.getUser().getId()));
      for (BillDetail billDetail : billDetailList){
        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(cart);
        cartDetail.setProduct(billDetail.getProduct());
        cartDetail.setAmount(billDetail.getQuantity());
        cartDetailRepository.save(cartDetail);
        billDetailRepository.delete(billDetail);
      }

      //Delete delivery
      Delivery delivery = deliveryRepository.findDeliveryByBill(bill).orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with bill ID = " + bill.getId()));
      deliveryRepository.delete(delivery);

      billRepository.delete(bill);
      message = "Payment fail.";
      httpStatus = HttpStatus.BAD_REQUEST;
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(httpStatus, message));
  }
}
