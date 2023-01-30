package com.example.webecom.services.implement;

import com.example.webecom.dto.request.AttributeRequestDTO;
import com.example.webecom.dto.response.AttributeProductResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.Attribute;
import com.example.webecom.entities.AttributeProduct;
import com.example.webecom.entities.Product;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.AttributeMapper;
import com.example.webecom.mapper.AttributeProductMapper;
import com.example.webecom.repositories.AttributeProductRepository;
import com.example.webecom.repositories.AttributeRepository;
import com.example.webecom.repositories.ProductRepository;
import com.example.webecom.services.AttributeProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AttributeProductServiceImpl implements AttributeProductService {
  @Autowired private AttributeRepository attributeRepository;
  @Autowired private AttributeProductRepository attributeProductRepository;
  @Autowired private ProductRepository productRepository;
  private final AttributeMapper attributeMapper = Mappers.getMapper(AttributeMapper.class);
  private final AttributeProductMapper attributeProductMapper = Mappers.getMapper(AttributeProductMapper.class);

  @Override
  public ResponseEntity<ResponseObject> createAttributeProduct(Long productId,
      AttributeRequestDTO attributeRequestDTO) {
    Attribute attribute = attributeMapper.attributeRequestDTOtoAttribute(attributeRequestDTO);
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    AttributeProductResponseDTO attributeProductResponseDTO = saveAttributeProduct(attribute, product);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create product attribute success!!!", attributeProductResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateAttributeProduct(Long productId, Long attributeId,
      AttributeRequestDTO attributeRequestDTO) {
    Attribute attribute = attributeMapper.attributeRequestDTOtoAttribute(attributeRequestDTO);
    Product getProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
    Attribute getAttribute = attributeRepository.findById(attributeId).orElseThrow(() -> new ResourceNotFoundException("Could not find attribute with ID = " + attributeId));
    AttributeProduct attributeProduct = attributeProductRepository.findAttributeProductByAttributeAndProduct(getAttribute, getProduct)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find address detail with ID product = " + productId + " and ID attribute = " + attributeId));
    this.attributeProductRepository.delete(attributeProduct);
    AttributeProductResponseDTO attributeProductResponseDTO = saveAttributeProduct(attribute, getProduct);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update product attribute success!!!", attributeProductResponseDTO));
  }

  @Override
  public ResponseEntity<?> getAttributeProductByProduct(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));

    List<AttributeProduct> attributeProductList = attributeProductRepository.findAttributeProductByProduct(product);
    List<AttributeProductResponseDTO> attributeProductResponseDTOList = new ArrayList<>();

    for (AttributeProduct attributeProduct : attributeProductList){
      AttributeProductResponseDTO attributeProductResponseDTO = attributeProductMapper.attributeToAttributeProductResponseDTO(attributeProduct);
      attributeProductResponseDTOList.add(attributeProductResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(attributeProductResponseDTOList);
  }

  private AttributeProductResponseDTO saveAttributeProduct(Attribute attribute, Product product){
    AttributeProduct attributeProduct = new AttributeProduct();
    attributeProduct.setProduct(product);
    //Check attribute of product is duplicate
    List<AttributeProduct> attributeProductList = attributeProductRepository.findAttributeProductByProduct(product);
    for (AttributeProduct attributeProductItem : attributeProductList){
      if (attributeProductItem.getAttribute().getName().equals(attribute.getName())){
        throw new ResourceAlreadyExistsException("Product already has this attribute");
      }
    }
    Optional<Attribute> getAttribute = attributeRepository.findAttributeByNameAndValue(attribute.getName(), attribute.getValue());
    //Check product attribute already exists
    if (getAttribute.isPresent()){
      attributeProduct.setAttribute(getAttribute.get());
    } else {
      Attribute attributeSaved = attributeRepository.save(attribute);
      attributeProduct.setAttribute(attributeSaved);
    }
    return attributeProductMapper.attributeToAttributeProductResponseDTO(attributeProductRepository.save(attributeProduct));
  }
}
