package com.example.webecom.controller;

import com.example.webecom.dto.request.BrandRequestDTO;
import com.example.webecom.dto.response.BrandResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.BrandService;
import com.example.webecom.utils.Utils;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/api/v1/brand")
public class BrandController {
  @Autowired
  private BrandService brandService;

  @GetMapping(value = "/page")
  public ResponseEntity<?> getAllBrandOnTrading(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return brandService.getAllBrandOnTrading(pageable);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllBrand() {
    return brandService.getAllBrand();
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createBrand(@ModelAttribute @Valid BrandRequestDTO brandRequestDTO) {
    return brandService.createBrand(brandRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateBrand(@ModelAttribute @Valid BrandRequestDTO brandRequestDTO,
      @RequestParam(name = "id") Long id) {
    return brandService.updateBrand(brandRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteBrand(@RequestParam(name = "id") Long id) {
    return brandService.deleteBrand(id);
  }

  @GetMapping(value = "/{id}")
  public BrandResponseDTO getBrandById(@PathVariable(name = "id") Long id) {
    return brandService.getBrandById(id);
  }
}
