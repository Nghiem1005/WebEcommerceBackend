package com.example.webecom.services.implement;

import com.example.webecom.dto.request.BrandRequestDTO;
import com.example.webecom.dto.response.AuthResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.BrandResponseDTO;
import com.example.webecom.entities.Role;
import com.example.webecom.entities.Brand;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.BrandMapper;
import com.example.webecom.repositories.RoleRepository;
import com.example.webecom.repositories.BrandRepository;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.BrandService;
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
public class BrandServiceImpl implements BrandService {
  private final BrandMapper mapper = Mappers.getMapper(BrandMapper.class);

  @Autowired
  private BrandRepository brandRepository;

  @Override
  public ResponseEntity<?> getAllBrandOnTrading(Pageable pageable) {
    Page<Brand> getBrandList = brandRepository.findAll(pageable);
    List<Brand> brandList = getBrandList.getContent();
    List<BrandResponseDTO> brandResponseDTOList = new ArrayList<>();
    for (Brand brand : brandList) {
      BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(brand);
      brandResponseDTOList.add(brandResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(brandResponseDTOList);
  }

  @Override
  public ResponseEntity<?> getAllBrand() {
    List<Brand> getBrandList = brandRepository.findAll();
    List<BrandResponseDTO> brandResponseDTOList = new ArrayList<>();
    for (Brand brand : getBrandList) {
      BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(brand);
      brandResponseDTOList.add(brandResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(brandResponseDTOList);
  }

  @Override
  public ResponseEntity<ResponseObject> createBrand(BrandRequestDTO brandRequestDTO) {
    Brand brand = mapper.brandRequestDTOtoBrand(brandRequestDTO);
    brand = checkExists(brand);

    Brand brandSaved = brandRepository.save(brand);
    BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(brandSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create brand successfully!", brandResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateBrand(BrandRequestDTO brandRequestDTO, Long id) {
    Brand brand = mapper.brandRequestDTOtoBrand(brandRequestDTO);
    Brand getBrand = brandRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + id));
    brand.setId(id);
    brand = checkExists(brand);

    Brand brandSaved = brandRepository.save(brand);
    BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(brandSaved);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update brand successfully!", brandResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteBrand(Long id) {
    Brand getBrand = brandRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + id));

    brandRepository.delete(getBrand);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete brand successfully!"));
  }

  @Override
  public BrandResponseDTO getBrandById(Long id) {
    Brand brand = brandRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + id));
    BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(brand);

    return brandResponseDTO;
  }

  private Brand checkExists(Brand brand) {
    // Check brand name exists
    Optional<Brand> getBrand = brandRepository.findBrandByName(brand.getName());
    if (getBrand.isPresent()) {
      if (brand.getId() == null) {
        throw new ResourceAlreadyExistsException("Brand name already exists");
      } else {
        if (brand.getId() != getBrand.get().getId()) {
          throw new ResourceAlreadyExistsException("Brand name already exists");
        }
      }
    }
    return brand;
  }

}
