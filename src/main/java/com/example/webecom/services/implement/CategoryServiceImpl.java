package com.example.webecom.services.implement;

import com.example.webecom.dto.request.CategoryRequestDTO;
import com.example.webecom.dto.response.AuthResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.CategoryResponseDTO;
import com.example.webecom.entities.Role;
import com.example.webecom.entities.Category;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.CategoryMapper;
import com.example.webecom.repositories.RoleRepository;
import com.example.webecom.repositories.CategoryRepository;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {
  private final CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);

  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private ImageStorageService imageStorageService;

  @Override
  public ResponseEntity<?> getAllCategoryOnTrading(Pageable pageable) {
    Page<Category> getCategoryList = categoryRepository.findAll(pageable);
    List<Category> categoryList = getCategoryList.getContent();
    List<CategoryResponseDTO> categoryResponseDTOList = new ArrayList<>();
    for (Category category : categoryList) {
      CategoryResponseDTO categoryResponseDTO = mapper.categoryToCategoryResponseDTO(category);
      categoryResponseDTOList.add(categoryResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getAllCategory() {
    List<Category> getCategoryList = categoryRepository.findAll();
    List<CategoryResponseDTO> categoryResponseDTOList = new ArrayList<>();
    for (Category category : getCategoryList) {
      CategoryResponseDTO categoryResponseDTO = mapper.categoryToCategoryResponseDTO(category);
      categoryResponseDTOList.add(categoryResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTOList);
  }

  @Override
  public ResponseEntity<ResponseObject> createCategory(CategoryRequestDTO categoryRequestDTO) {
    Category category = mapper.categoryRequestDTOtoCategory(categoryRequestDTO);
    category = checkExists(category);

    Category categorySaved = categoryRepository.save(category);
    CategoryResponseDTO categoryResponseDTO = mapper.categoryToCategoryResponseDTO(categorySaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create category successfully!", categoryResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateCategory(CategoryRequestDTO categoryRequestDTO, Long id) {
    Category category = mapper.categoryRequestDTOtoCategory(categoryRequestDTO);
    Category getCategory = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find category with ID = " + id));
    category.setId(id);
    category = checkExists(category);

    Category categorySaved = categoryRepository.save(category);
    CategoryResponseDTO categoryResponseDTO = mapper.categoryToCategoryResponseDTO(categorySaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update category successfully!", categoryResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteCategory(Long id) {
    Category getCategory = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find category with ID = " + id));

    categoryRepository.delete(getCategory);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete category successfully!"));
  }

  @Override
  public CategoryResponseDTO getCategoryById(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find category with ID = " + id));
    CategoryResponseDTO categoryResponseDTO = mapper.categoryToCategoryResponseDTO(category);

    return categoryResponseDTO;
  }

  private Category checkExists(Category category) {
    // Check category name exists
    Optional<Category> getCategory = categoryRepository.findCategoryByName(category.getName());
    if (getCategory.isPresent()) {
      if (category.getId() == null) {
        throw new ResourceAlreadyExistsException("Category name already exists");
      } else {
        if (category.getId() != getCategory.get().getId()) {
          throw new ResourceAlreadyExistsException("Category name already exists");
        }
      }
    }
    return category;
  }

}
