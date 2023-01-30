package com.example.webecom.controller;

import com.example.webecom.dto.request.CategoryRequestDTO;
import com.example.webecom.dto.response.CategoryResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.services.CategoryService;
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
@RequestMapping(value = "/api/v1/category")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping(value = "/page")
  public ResponseEntity<?> getAllCategoryOnTradingPaging(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return categoryService.getAllCategoryOnTrading(pageable);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllCategory() {
    return categoryService.getAllCategory();
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createCategory(@ModelAttribute @Valid CategoryRequestDTO categoryRequestDTO) {
    return categoryService.createCategory(categoryRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateCategory(@ModelAttribute @Valid CategoryRequestDTO categoryRequestDTO,
      @RequestParam(name = "id") Long id) {
    return categoryService.updateCategory(categoryRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteCategory(@RequestParam(name = "id") Long id) {
    return categoryService.deleteCategory(id);
  }

  @GetMapping(value = "/{id}")
  public CategoryResponseDTO getCategoryById(@PathVariable(name = "id") Long id) {
    return categoryService.getCategoryById(id);
  }
}
