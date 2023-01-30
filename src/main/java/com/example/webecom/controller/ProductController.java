package com.example.webecom.controller;

import com.example.webecom.dto.request.ProductRequestDTO;
import com.example.webecom.dto.response.ProductGalleryDTO;
import com.example.webecom.dto.response.ProductResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.ProductDiscountService;
import com.example.webecom.services.ProductService;
import com.example.webecom.utils.Utils;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/product")
public class ProductController {
  @Autowired
  private ProductService productService;

  @Autowired private ProductDiscountService productDiscountService;

  @Autowired private ImageStorageService imageStorageService;

  @GetMapping(value = "")
  public ResponseEntity<?> getAllProductOnTrading(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return productService.getAllProductOnTrading(pageable);
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createProduct(@ModelAttribute @Valid ProductRequestDTO productRequestDTO) {
    return productService.createProduct(productRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateProduct(@ModelAttribute @Valid ProductRequestDTO productRequestDTO,
      @RequestParam(name = "id") Long id) {
    return productService.updateProduct(productRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteProduct(@RequestParam(name = "id") Long id) {
    return productService.deleteProduct(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getProductById(@PathVariable(name = "id") Long id) {
    return productService.getProductById(id);
  }

  @GetMapping(value = "/statistic/product/vote")
  public ResponseEntity<?> getTop10ProductByVote() {
    return productService.getTop10ProductByVote();
  }

  @GetMapping(value = "/search")
  public ResponseEntity<?> findAll(@RequestParam(name = "search") String search,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    return this.productService.search(search, page, size);

  }

  @GetMapping(value = "/brand/{brandId}")
  public ResponseEntity<?> findAllProductByBrand(@PathVariable(name = "brandId") Long brandId,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return this.productService.getAllProductByBrand(brandId, pageable);
  }

  @GetMapping(value = "/category/{categoryId}")
  public ResponseEntity<?> findAllProductByCategory(@PathVariable(name = "categoryId") Long categoryId,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return this.productService.getAllProductByCategory(categoryId, pageable);
  }

  @GetMapping(value = "/discount")
  public ResponseEntity<?> findAllProductByCategory() {
    return this.productDiscountService.getProductDiscountCurrent();
  }
}
