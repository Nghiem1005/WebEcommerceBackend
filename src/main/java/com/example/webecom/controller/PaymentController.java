package com.example.webecom.controller;

import com.example.webecom.dto.request.BillRequestDTO;
import com.example.webecom.dto.request.PaymentRequestDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.PaymentService;
import com.mservice.models.PaymentResponse;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
  public static final String SUCCESS_URL = "success";
  public static final String CANCEL_URL = "cancel";

  public static final String RETURN_URL = "delivery";
  @Autowired private PaymentService paymentService;
  @PostMapping(value = "/paypal")
  public ResponseEntity<ResponseObject> paypal(@RequestBody PaymentRequestDTO paypalRequestDTO) throws PayPalRESTException {
    return paymentService.createPaymentPaypal(paypalRequestDTO,
        "http://localhost:8080/" + CANCEL_URL, "http://localhost:8080/" + SUCCESS_URL);
  }

  @GetMapping(value = CANCEL_URL)
  public ResponseEntity<ResponseObject> cancelPay(){
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.BAD_REQUEST, "paypal payment not executed"));
  }

  @GetMapping(value = SUCCESS_URL)
  public ResponseEntity<?> successPay(@RequestParam(name = "paymentId") String paymentId, @RequestParam(name = "PayerID") String payerId, @RequestParam(name = "token") String token)
      throws PayPalRESTException {
    return paymentService.successPaypal(paymentId, payerId, token);
  }

  @PostMapping(value = "/momo")
  public ResponseEntity<PaymentResponse> momo(@RequestBody PaymentRequestDTO paymentRequestDTO) throws Exception{
    return paymentService.createPaymentMomo(paymentRequestDTO,
        "http://localhost:3001/" + RETURN_URL);
  }

  @GetMapping(value = "http://localhost:3001/" + RETURN_URL)
  public ResponseEntity<?> successPay(@RequestParam(name = "orderId") Long orderId, @RequestParam(name = "resultCode") int resultCode) {
    return paymentService.successMomo(orderId, resultCode);
  }
}
