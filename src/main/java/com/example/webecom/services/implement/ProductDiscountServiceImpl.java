package com.example.webecom.services.implement;

import com.example.webecom.dto.response.ProductDiscountResponseDTO;
import com.example.webecom.dto.response.ProductGalleryDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.Discount;
import com.example.webecom.entities.Feedback;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.ProductDiscountMapper;
import com.example.webecom.repositories.DiscountRepository;
import com.example.webecom.repositories.FeedbackRepository;
import com.example.webecom.repositories.ProductDiscountRepository;
import com.example.webecom.repositories.ProductRepository;
import com.example.webecom.services.ProductDiscountService;
import com.example.webecom.utils.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductDiscountServiceImpl implements ProductDiscountService {
  @Autowired private ProductDiscountRepository productDiscountRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private DiscountRepository discountRepository;

  @Autowired private FeedbackRepository feedbackRepository;
  private final ProductDiscountMapper productDiscountMapper = Mappers.getMapper(ProductDiscountMapper.class);

  @Override
  public ResponseEntity<ResponseObject> createProductDiscount(Long productId, Long discountId) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    Discount discount = discountRepository.findById(discountId).orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + discountId));

    Optional<ProductDiscount> getProductDiscount = productDiscountRepository.findProductDiscountByDiscountAndProduct(discount, product);
    if (getProductDiscount.isPresent()){
      throw new ResourceAlreadyExistsException("Discount of product is exists");
    }
    ProductDiscount productDiscount = new ProductDiscount();
    productDiscount.setDiscount(discount);
    productDiscount.setProduct(product);
    ProductDiscountResponseDTO productDiscountResponseDTO = productDiscountMapper.productDiscountToProductDiscountResponseDTO(productDiscountRepository.save(productDiscount));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create product discount success!!!", productDiscountResponseDTO));
  }

  @Override
  public ResponseEntity<?> getProductDiscount(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    List<ProductDiscount> productDiscountList = productDiscountRepository.findProductDiscountByProduct(product);
    if (productDiscountList.size() == 0){
      throw new ResourceNotFoundException("Product hasn't discount!!!");
    }
    List<ProductDiscountResponseDTO> productDiscountResponseDTOList = new ArrayList<>();
    for (ProductDiscount productDiscount : productDiscountList){
      productDiscountResponseDTOList.add(productDiscountMapper.productDiscountToProductDiscountResponseDTO(productDiscount));
    }
    return ResponseEntity.status(HttpStatus.OK).body(productDiscountResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getProductDiscountCurrentByProduct(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    ProductDiscount productDiscount = productDiscountRepository
        .findAllCurrentDiscountByProductId(productId).orElseThrow(() -> new ResourceNotFoundException("Product hasn't discount!!!"));
    ProductDiscountResponseDTO productDiscountResponseDTO = new ProductDiscountResponseDTO();
    productDiscountResponseDTO = productDiscountMapper.productDiscountToProductDiscountResponseDTO(productDiscount);

    return ResponseEntity.status(HttpStatus.OK).body(productDiscountResponseDTO);
  }

  @Override
  public ResponseEntity<?> getProductDiscountCurrent() {
    Sort sort = Sort.by("discount.percent").descending();
    List<ProductDiscount> productDiscountList = productDiscountRepository.findAllCurrentProductDiscount(sort);
    List<ProductGalleryDTO> productGalleryDTOList = new ArrayList<>();
    for (ProductDiscount productDiscount : productDiscountList){
      productGalleryDTOList.add(toProductGalleryDTO(productDiscount.getProduct(), productDiscount.getDiscount()));
    }
    return ResponseEntity.status(HttpStatus.OK).body(productGalleryDTOList);
  }

  @Override
  public ResponseEntity<ResponseObject> deleteProductDiscount(Long productId, Long discountId) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    Discount discount = discountRepository.findById(discountId).orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + discountId));

    ProductDiscount getProductDiscount = productDiscountRepository.findProductDiscountByDiscountAndProduct(discount, product).orElseThrow(
        () -> new ResourceNotFoundException("Could not find product discount with ID product = " + productId + " and ID discount = " + discount)
    );
    this.productDiscountRepository.delete(getProductDiscount);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete product discount success!!!"));
  }

  private ProductGalleryDTO toProductGalleryDTO(Product product, Discount discount) {
    ProductGalleryDTO productGalleryDTO = new ProductGalleryDTO();
    productGalleryDTO.setId(product.getId());
    productGalleryDTO.setName(product.getName());
    productGalleryDTO.setStandCost(product.getStandCost());
    productGalleryDTO.setThumbnail(product.getThumbnail());
    productGalleryDTO.setDiscount(discount);
    BigDecimal price = product.getStandCost().multiply(BigDecimal.valueOf(discount.getPercent())).setScale(2, RoundingMode.UP);
    if (discount.getPercent() == 0) {
      price = product.getStandCost();
    }
    productGalleryDTO.setPrice(price);
    List<Feedback> feedbackList = feedbackRepository.findFeedbacksByProduct(product);
    productGalleryDTO.setVote(Utils.calculateAvgRate(feedbackList));

    return productGalleryDTO;
  }
}
