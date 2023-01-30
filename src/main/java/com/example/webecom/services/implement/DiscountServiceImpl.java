package com.example.webecom.services.implement;

import com.example.webecom.dto.request.DiscountRequestDTO;
import com.example.webecom.dto.response.DiscountResponseDTO;
import com.example.webecom.dto.response.ProductDiscountResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.Discount;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.DiscountMapper;
import com.example.webecom.mapper.ProductDiscountMapper;
import com.example.webecom.models.ItemTotalPage;
import com.example.webecom.repositories.DiscountRepository;
import com.example.webecom.repositories.ProductDiscountRepository;
import com.example.webecom.repositories.ProductRepository;
import com.example.webecom.services.DiscountService;
import com.example.webecom.services.ProductDiscountService;
import com.example.webecom.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
  @Autowired
  private DiscountRepository discountRepository;
  @Autowired
  private ProductRepository productRepository;
  private final DiscountMapper mapper = Mappers.getMapper(DiscountMapper.class);

  @Override
  public ResponseEntity<?> getAllDiscountOnTrading(Pageable pageable) {
    Page<Discount> getDiscountList = discountRepository.findAll(pageable);
    List<Discount> discountList = getDiscountList.getContent();
    List<DiscountResponseDTO> discountResponseDTOList = new ArrayList<>();
    for (Discount discount : discountList) {
      DiscountResponseDTO discountResponseDTO = mapper.discountToDiscountResponseDTO(discount);
      discountResponseDTOList.add(discountResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(discountResponseDTOList, getDiscountList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getAllDiscountByProduct(Pageable pageable, Long productId) {
    return null;
  }

  @Override
  public ResponseEntity<ResponseObject> createDiscount(DiscountRequestDTO discountRequestDTO) {
    Discount discount = mapper.discountRequestDTOtoDiscount(discountRequestDTO);

    Discount discountSaved = discountRepository.save(discount);
    DiscountResponseDTO discountResponseDTO = mapper.discountToDiscountResponseDTO(discountSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create discount successfully!", discountResponseDTO));

  }

  @Override
  public ResponseEntity<ResponseObject> updateDiscount(DiscountRequestDTO discountRequestDTO, Long id) {
    Discount discount = mapper.discountRequestDTOtoDiscount(discountRequestDTO);
    Discount getDiscount = discountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + id));
    discount.setId(id);

    Discount discountSaved = discountRepository.save(discount);
    DiscountResponseDTO discountResponseDTO = mapper.discountToDiscountResponseDTO(discountSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update discount successfully!", discountResponseDTO));

  }

  @Override
  public ResponseEntity<ResponseObject> deleteDiscount(Long id) {
    Discount getDiscount = discountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + id));

    discountRepository.delete(getDiscount);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete discount successfully!"));
  }

  @Override
  public DiscountResponseDTO getDiscountById(Long id) {
    Discount discount = discountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + id));
    DiscountResponseDTO discountResponseDTO = mapper.discountToDiscountResponseDTO(discount);

    return discountResponseDTO;
  }

}
