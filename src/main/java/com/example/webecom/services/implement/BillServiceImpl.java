package com.example.webecom.services.implement;

import com.example.webecom.dto.request.BillRequestDTO;
import com.example.webecom.dto.response.BillResponseDTO;
import com.example.webecom.dto.response.CartResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.Address;
import com.example.webecom.entities.AddressDetail;
import com.example.webecom.entities.Bill;
import com.example.webecom.entities.BillDetail;
import com.example.webecom.entities.Cart;
import com.example.webecom.entities.CartDetail;
import com.example.webecom.entities.Delivery;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import com.example.webecom.entities.User;
import com.example.webecom.exceptions.InvalidValueException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.BillMapper;
import com.example.webecom.models.Item;
import com.example.webecom.models.ItemProduct;
import com.example.webecom.repositories.AddressDetailRepository;
import com.example.webecom.repositories.AddressRepository;
import com.example.webecom.repositories.BillDetailRepository;
import com.example.webecom.repositories.BillRepository;
import com.example.webecom.repositories.CartDetailRepository;
import com.example.webecom.repositories.CartRepository;
import com.example.webecom.repositories.DeliveryRepository;
import com.example.webecom.repositories.ProductDiscountRepository;
import com.example.webecom.repositories.ProductRepository;
import com.example.webecom.repositories.UserRepository;
import com.example.webecom.services.BillService;
import com.example.webecom.utils.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {
  @Autowired
  private BillRepository billRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private BillDetailRepository billDetailRepository;

  @Autowired private CartDetailRepository cartDetailRepository;

  @Autowired private CartRepository cartRepository;

  @Autowired private AddressDetailRepository addressDetailRepository;
  @Autowired private AddressRepository addressRepository;
  @Autowired
  private ProductDiscountRepository productDiscountRepository;

  @Autowired private DeliveryRepository deliveryRepository;
  private final BillMapper billMapper = Mappers.getMapper(BillMapper.class);

  @Override
  public ResponseEntity<?> getBillByUser(Long userId) {
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
    List<Bill> billList = billRepository.findBillByUser(user);
    for (Bill bill : billList) {
      billResponseDTOList.add(setBillResponseDTO(bill));
    }
    return ResponseEntity.status(HttpStatus.OK).body(billResponseDTOList);
  }

  @Override
  public ResponseEntity<ResponseObject> createBill(Long userId, BillRequestDTO billRequestDTO, Long addressId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
    Bill bill = billMapper.billRequestDTOtoBill(billRequestDTO);
    bill.setUser(user);
    BigDecimal totalPrice = BigDecimal.valueOf(0.0);
    bill.setTotalPrice(totalPrice);
    Bill billSaved = billRepository.save(bill);

    //Delete product in cart
    Cart cart = cartRepository.findCartByUser(user).orElseThrow(() -> new ResourceNotFoundException("Could not find cart with user ID = " + userId));
    List<CartDetail> cartDetailList = cartDetailRepository.findCartDetailByCart(cart);
    cartDetailRepository.deleteAll(cartDetailList);

    // Add bill detail
    totalPrice = addBillDetail(billRequestDTO, billSaved).add(billRequestDTO.getFeeDelivery());
    billSaved.setTotalPrice(totalPrice);

    //Create delivery
    Delivery delivery = new Delivery();
    delivery.setBill(bill);
    delivery.setStatus("waiting");
    Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Could not find address with ID = " + addressId));
    delivery.setAddress(address);
    deliveryRepository.save(delivery);

    BillResponseDTO billResponseDTO = billMapper.billToBillResponseDTO(billRepository.save(billSaved));
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create bill success!", billResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateBill(Long billId, BillRequestDTO billRequestDTO) {
    Bill getBill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    Bill bill = billMapper.billRequestDTOtoBill(billRequestDTO);
    bill.setId(getBill.getId());
    bill.setTotalPrice(getBill.getTotalPrice());
    bill.setUser(getBill.getUser());
    BillResponseDTO billResponseDTO = billMapper.billToBillResponseDTO(billRepository.save(bill));
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update bill success!", billResponseDTO));
  }

  @Override
  public ResponseEntity<?> getAllBill() {
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    List<Bill> billList = billRepository.findAll();
    for (Bill bill : billList) {
      billResponseDTOList.add(setBillResponseDTO(bill));
    }
    return ResponseEntity.status(HttpStatus.OK).body(billResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getBillByStatus(String status) {
    List<BillResponseDTO> billResponseDTOList = new ArrayList<>();
    List<Bill> billList = billRepository.findBillsByStatus(status);
    for (Bill bill : billList) {
      billResponseDTOList.add(setBillResponseDTO(bill));
    }
    return ResponseEntity.status(HttpStatus.OK).body(billResponseDTOList);
  }

  private BillResponseDTO setBillResponseDTO(Bill bill){
    BillResponseDTO billResponseDTO = billMapper.billToBillResponseDTO(bill);

    List<BillDetail> billDetailList = billDetailRepository.findBillDetailByBill(bill);
    List<ItemProduct> itemProductList = new ArrayList<>();
    ItemProduct[] itemProducts = new ItemProduct[billDetailList.size()];
    //Get list product in bill
    for (BillDetail billDetail : billDetailList){
      ItemProduct itemProduct = new ItemProduct();
      itemProduct.setId(billDetail.getProduct().getId());
      itemProduct.setName(billDetail.getProduct().getName());
      itemProduct.setPrice(billDetail.getProduct().getStandCost());
      itemProduct.setQuantity(billDetail.getQuantity());
      itemProduct.setThumbnail(billDetail.getProduct().getThumbnail());
      itemProduct.setDiscount(0);
      Optional<ProductDiscount> productDiscount = productDiscountRepository.findAllDiscountByProductIdAndDate(billDetail.getProduct()
          .getId(), bill.getCreateDate());
      if (productDiscount.isPresent()){
        itemProduct.setDiscount(productDiscount.get().getDiscount().getPercent());
      }
      itemProduct.setPricePayed(Utils.getTotalPrice(billDetail.getProduct(), BigDecimal.valueOf(0.00), productDiscount, billDetail.getQuantity()));
      itemProductList.add(itemProduct);
    }
    billResponseDTO.setProducts(itemProductList.toArray(itemProducts));

    //Set status delivery status
    Delivery delivery = deliveryRepository.findDeliveryByBill(bill).orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with bill ID = " + bill.getId()));
    billResponseDTO.setStatusDelivery(delivery.getStatus());
    billResponseDTO.setDeliveryId(delivery.getId());
    billResponseDTO.setDeliveryCodeArea(delivery.getAddress().getCodeArea());
    //Get address delivery of bill
    String address = "";
    address = delivery.getAddress().getApartmentNumber() + "," + delivery.getAddress().getWard() + "," + delivery.getAddress().getDistrict() + "," + delivery.getAddress().getProvince();
    billResponseDTO.setAddress(address);

    return billResponseDTO;
  }

  @Override
  public ResponseEntity<ResponseObject> deleteBill(Long billId) {
    Bill getBill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));
    List<BillDetail> billDetailList = billDetailRepository.findBillDetailByBill(getBill);
    billDetailRepository.deleteAll(billDetailList);
    billRepository.delete(getBill);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete bill success!"));
  }

  @Override
  public ResponseEntity<?> getBillById(Long id) {
    Bill getBill = billRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + id));
    BillResponseDTO billResponseDTO = setBillResponseDTO(getBill);;
    return ResponseEntity.status(HttpStatus.OK).body(billResponseDTO);
  }

  private BigDecimal addBillDetail(BillRequestDTO billRequestDTO, Bill billSaved) {
    BigDecimal totalPrice = BigDecimal.valueOf(0.0);
    List<Item> itemList = billRequestDTO.getItems();
    for (Item item : itemList) {
      Product product = productRepository.findById(item.getId())
          .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + item.getId()));
      if (item.getQuantity() > product.getAmount()) {
        throw new InvalidValueException(
            "Amount product " + product.getName() + " must be less than amount product exists");
      }
      if (item.getQuantity() < 0) {
        throw new InvalidValueException("Amount product must greater than 1");
      }
      BillDetail billDetail = new BillDetail();
      billDetail.setBill(billSaved);
      billDetail.setProduct(product);
      billDetail.setQuantity(item.getQuantity());

      // Update amount product
      int newAmountProduct = product.getAmount() - item.getQuantity();
      product.setAmount(newAmountProduct);
      productRepository.save(product);

      // Total price of bill
      Optional<ProductDiscount> productDiscount = productDiscountRepository
          .findAllCurrentDiscountByProductId(product.getId());
      totalPrice = Utils.getTotalPrice(product, totalPrice, productDiscount,
          item.getQuantity());
      billDetailRepository.save(billDetail);
    }
    return totalPrice;
  }

  @Override
  public ResponseEntity<Integer> getNumberOfBill() {
    int numberOfBill = billRepository.getNumberOfBill().orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
    return ResponseEntity.status(HttpStatus.OK).body(numberOfBill);
  }

  @Override
  public ResponseEntity<Double> getSales() {
    double getSales = billRepository.getSales().orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
    return ResponseEntity.status(HttpStatus.OK).body(getSales);
  }
}
