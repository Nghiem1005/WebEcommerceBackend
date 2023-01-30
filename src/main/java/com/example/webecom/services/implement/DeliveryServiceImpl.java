package com.example.webecom.services.implement;

import com.example.webecom.dto.request.DeliveryRequestDTO;
import com.example.webecom.dto.response.AuthResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.DeliveryResponseDTO;
import com.example.webecom.entities.Address;
import com.example.webecom.entities.AddressDetail;
import com.example.webecom.entities.Role;
import com.example.webecom.entities.Bill;
import com.example.webecom.entities.Delivery;
import com.example.webecom.entities.User;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.DeliveryMapper;
import com.example.webecom.models.ItemTotalPage;
import com.example.webecom.repositories.AddressDetailRepository;
import com.example.webecom.repositories.AddressRepository;
import com.example.webecom.repositories.RoleRepository;
import com.example.webecom.repositories.BillRepository;
import com.example.webecom.repositories.DeliveryRepository;
import com.example.webecom.repositories.UserRepository;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.DeliveryService;
import com.example.webecom.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import net.bytebuddy.utility.RandomString;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
  private final DeliveryMapper mapper = Mappers.getMapper(DeliveryMapper.class);

  @Autowired
  private DeliveryRepository deliveryRepository;

  @Autowired private AddressDetailRepository addressDetailRepository;

  @Autowired private UserRepository userRepository;
  @Autowired private AddressRepository addressRepository;
  @Autowired
  private BillRepository billRepository;

  @Override
  public ResponseEntity<?> getAllDeliveryOnTrading(Pageable pageable) {
    Page<Delivery> getDeliveryList = deliveryRepository.findAll(pageable);
    List<Delivery> deliveryList = getDeliveryList.getContent();
    List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
    for (Delivery delivery : deliveryList) {
      DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);
      deliveryResponseDTOList.add(deliveryResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(deliveryResponseDTOList,
        getDeliveryList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> createDelivery(DeliveryRequestDTO deliveryRequestDTO) {
    Delivery delivery = mapper.deliveryRequestDTOtoDelivery(deliveryRequestDTO);

    Delivery deliverySaved = deliveryRepository.save(delivery);
    DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(deliverySaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create delivery successfully!", deliveryResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateDelivery(DeliveryRequestDTO deliveryRequestDTO, Long id) {
    Delivery delivery = mapper.deliveryRequestDTOtoDelivery(deliveryRequestDTO);
    Delivery getDelivery = deliveryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + id));
    delivery.setId(id);
    //delivery.setStatus("delivering");

    Bill bill = billRepository.findById(deliveryRequestDTO.getBillId()).orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + deliveryRequestDTO.getBillId()));
    delivery.setBill(bill);

    delivery.setAddress(getDelivery.getAddress());
    delivery.setShipper(getDelivery.getShipper());

    if (deliveryRequestDTO.getShipperId() != null){
      Optional<User> shipper = userRepository.findById(deliveryRequestDTO.getShipperId());
      if (shipper.isPresent()){
        delivery.setShipper(shipper.get());
      }
    }

    //Update bill payed
    if (deliveryRequestDTO.getBillStatus() != null){
      delivery.getBill().setStatus(deliveryRequestDTO.getBillStatus());
      delivery.getBill().setPayDate(deliveryRequestDTO.getPayDate());
      billRepository.save(delivery.getBill());
    }

    Delivery deliverySaved = deliveryRepository.save(delivery);
    DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(deliverySaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update delivery successfully!", deliveryResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteDelivery(Long id) {
    Delivery getDelivery = deliveryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + id));

    deliveryRepository.delete(getDelivery);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete delivery successfully!"));
  }

  @Override
  public DeliveryResponseDTO getDeliveryById(Long id) {
    Delivery delivery = deliveryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + id));
    DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);

    return deliveryResponseDTO;
  }

  @Override
  public ResponseEntity<?> getDeliveryByStatus(String status) {
    List<Delivery> deliveryList = deliveryRepository.findDeliveriesByStatus(status);
    List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
    for (Delivery delivery : deliveryList){
      DeliveryResponseDTO deliveryResponseDTO = new DeliveryResponseDTO();
      deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);
      deliveryResponseDTOList.add(deliveryResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getDeliveryByStatusAndShipper(Long shipperId, String status) {
    User shipper = userRepository.findById(shipperId).orElseThrow(() -> new ResourceNotFoundException("Could not find shipper with ID = " + shipperId));
    List<Delivery> deliveryList = deliveryRepository.findDeliveriesByStatusAndShipper(status, shipper);
    List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
    for (Delivery delivery : deliveryList){
      DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);
      deliveryResponseDTOList.add(deliveryResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getDeliveryByProvinceAndShipper(Long shipperId) {
    User shipper = userRepository.findById(shipperId).orElseThrow(() -> new ResourceNotFoundException("Could not find shipper with ID = " + shipperId));
    List<AddressDetail> addressDetail = addressDetailRepository.findAddressDetailByUser(shipper);
    List<Delivery> deliveryList = deliveryRepository.getDeliveryByProvinceAndShipper(addressDetail.get(0).getAddress().getProvince(), shipper.getId());
    List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
    for (Delivery delivery : deliveryList){
      DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);
      deliveryResponseDTOList.add(deliveryResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getDeliveryCheckedAndAddress(Long shipperId) {
    User shipper = userRepository.findById(shipperId).orElseThrow(() -> new ResourceNotFoundException("Could not find shipper with ID = " + shipperId));
    List<AddressDetail> addressDetailList = addressDetailRepository.findAddressDetailByUser(shipper);
    AddressDetail addressDetail = addressDetailList.get(0);
    List<Delivery> deliveryList = deliveryRepository.findDeliveryByCheckedAndAddress(addressDetail.getAddress().getProvince());
    List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
    for (Delivery delivery : deliveryList){
      DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);
      deliveryResponseDTOList.add(deliveryResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
  }

  @Override
  public DeliveryResponseDTO getDeliveryByBill(Long billId) {
    Bill bill = billRepository.findById(billId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find bill with ID = " + billId));

    Delivery delivery = deliveryRepository.findDeliveryByBill(bill).orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with bill ID = " + billId));
    DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);

    return deliveryResponseDTO;
  }

}
