package com.example.webecom.services.implement;

import com.example.webecom.dto.request.ProductRequestDTO;
import com.example.webecom.dto.response.ProductGalleryDTO;
import com.example.webecom.dto.response.ProductResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.entities.AttributeProduct;
import com.example.webecom.entities.Brand;
import com.example.webecom.entities.Category;
import com.example.webecom.entities.Discount;
import com.example.webecom.entities.Feedback;
import com.example.webecom.entities.ImageProduct;
import com.example.webecom.entities.Product;
import com.example.webecom.entities.ProductDiscount;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.ProductMapper;
import com.example.webecom.models.IProductVote;
import com.example.webecom.models.ItemTotalPage;
import com.example.webecom.repositories.AttributeProductRepository;
import com.example.webecom.repositories.BrandRepository;
import com.example.webecom.repositories.CategoryRepository;
import com.example.webecom.repositories.DiscountRepository;
import com.example.webecom.repositories.FeedbackRepository;
import com.example.webecom.repositories.ImageProductRepository;
import com.example.webecom.repositories.ProductDiscountRepository;
import com.example.webecom.repositories.ProductRepository;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.ProductService;
import com.example.webecom.specificationsBuilder.ProductSpecificationsBuilder;
import com.example.webecom.utils.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private DiscountRepository discountRepository;
  @Autowired
  private BrandRepository brandRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private FeedbackRepository feedbackRepository;
  @Autowired
  private ImageStorageService imageStorageService;
  @Autowired
  private ImageProductRepository imageProductRepository;
  @Autowired
  private AttributeProductRepository attributeProductRepository;
  @Autowired
  private ProductDiscountRepository productDiscountRepository;

  private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

  @Override
  public ResponseEntity<?> getAllProductOnTrading(Pageable pageable) {
    Page<Product> getProductList = productRepository.findAll(pageable);
    List<Product> productList = getProductList.getContent();
    List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
    for (Product product : productList) {
      ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(product);
      productResponseDTO.setDiscount(getDiscount(product));
      List<Feedback> feedbackList = feedbackRepository.findFeedbacksByProduct(product);
      productResponseDTO.setVote((int)Math.rint(Utils.calculateAvgRate(feedbackList)));
      productResponseDTOList.add(productResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(productResponseDTOList, getProductList.getTotalPages()));
  }

  private Discount getDiscount(Product product) {
    Discount discount = new Discount();
    Optional<ProductDiscount> getDiscount = productDiscountRepository.findAllCurrentDiscountByProductId(product.getId());
    if (getDiscount.isPresent()) {
      discount = getDiscount.get().getDiscount();
    }
    return discount;
  }

  /*private ItemTotalPage totalPage(List<?> list, int size){
    int totalPage =  (int) Math.ceil((double) list.size() / size);
    return new ItemTotalPage(list, totalPage);
  }*/

  @Override
  public ResponseEntity<?> getAllProductByCategory(Long categoryId, Pageable pageable) {
    List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find category with ID = " + categoryId));
    Page<Product> getProductList = productRepository.findProductByCategory(category, pageable);
    List<Product> productList = getProductList.getContent();
    for (Product product : productList) {
      ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(product);
      productResponseDTO.setDiscount(getDiscount(product));
      productResponseDTOList.add(productResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(productResponseDTOList, getProductList.getTotalPages()));
  }

  @Override
  public ResponseEntity<?> getAllProductByBrand(Long brandId, Pageable pageable) {
    List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
    Brand brand = brandRepository.findById(brandId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + brandId));
    Page<Product> getProductList = productRepository.findProductByBrand(brand, pageable);
    List<Product> productList = getProductList.getContent();
    for (Product product : productList) {
      ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(product);
      productResponseDTO.setDiscount(getDiscount(product));
      productResponseDTOList.add(productResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(productResponseDTOList, getProductList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> createProduct(ProductRequestDTO productRequestDTO) {
    Product product = mapper.productRequestDTOtoProduct(productRequestDTO);
    product = checkExists(product);
    // Set thumbnail
    if (productRequestDTO.getThumbnail() != null) {
      product.setThumbnail(imageStorageService.storeFile(productRequestDTO.getThumbnail(), "product/thumbnail"));
    }
    Product productSaved = productRepository.save(product);
    ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(productSaved);
    productResponseDTO.setDiscount(getDiscount(productSaved));
    if (productRequestDTO.getImages() != null) {
      productResponseDTO.setImages(saveImage(productRequestDTO, productSaved));
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create product successfully!", productResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateProduct(ProductRequestDTO productRequestDTO,
      Long id) {
    Product product = mapper.productRequestDTOtoProduct(productRequestDTO);
    Product getProduct = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + id));
    product.setId(id);
    product = checkExists(product);
    // Set thumbnail
    if (productRequestDTO.getThumbnail() != null) {
      imageStorageService.deleteFile(getProduct.getThumbnail(), "product/thumbnail");
      product.setThumbnail(imageStorageService.storeFile(productRequestDTO.getThumbnail(), "product/thumbnail"));
    } else {
      product.setThumbnail(getProduct.getThumbnail());
    }
    Product productSaved = productRepository.save(product);
    ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(productSaved);
    productResponseDTO.setDiscount(getDiscount(productSaved));
    // Save image
    if (productRequestDTO.getImages() != null) {
      List<ImageProduct> imageProductList = imageProductRepository.findImageProductByProduct(productSaved);
      for (ImageProduct imageProduct : imageProductList) {
        imageStorageService.deleteFile(imageProduct.getPath(), "product/thumbnail");
        imageProductRepository.delete(imageProduct);
      }
      productResponseDTO.setImages(saveImage(productRequestDTO, productSaved));
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update product successfully!", productResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteProduct(Long id) {
    Product getProduct = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + id));
    // Delete image of product
    List<ImageProduct> imageProductList = imageProductRepository.findImageProductByProduct(getProduct);
    for (ImageProduct imageProduct : imageProductList) {
      imageStorageService.deleteFile(imageProduct.getPath(), "product/thumbnail");
      imageProductRepository.delete(imageProduct);
    }
    // Delete attribute of product
    List<AttributeProduct> attributeProductList = attributeProductRepository.findAttributeProductByProduct(getProduct);
    this.attributeProductRepository.deleteAll(attributeProductList);
    // Delete discount of product
    List<ProductDiscount> productDiscountList = productDiscountRepository.findProductDiscountByProduct(getProduct);
    this.productDiscountRepository.deleteAll(productDiscountList);

    productRepository.delete(getProduct);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete product successfully!"));
  }

  @Override
  public ResponseEntity<?> getProductById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + id));
    ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(product);
    productResponseDTO.setDiscount(getDiscount(product));
    List<ImageProduct> imageProductList = imageProductRepository.findImageProductByProduct(product);
    String[] images = new String[imageProductList.size()];
    for (int i = 0; i < imageProductList.size(); i++) {
      images[i] = imageProductList.get(i).getPath();
    }
    productResponseDTO.setImages(images);
    return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
  }

  @Override
  public ResponseEntity<?> search(String search, int page, int size) {
    ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();
    Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(.+?),");
    Matcher matcher = pattern.matcher(search + ",");
    while (matcher.find()) {
      if (matcher.group(1).equals("brand")) {
        Brand brand = brandRepository.findById(Long.parseLong(matcher.group(3)))
            .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + matcher.group(3)));
        builder.with(matcher.group(1), matcher.group(2), brand);
      } else if (matcher.group(1).equals("category")) {
        Category category = categoryRepository.findById(Long.parseLong(matcher.group(3)))
            .orElseThrow(() -> new ResourceNotFoundException("Could not find category with ID = " + matcher.group(3)));
        builder.with(matcher.group(1), matcher.group(2), category);
      } else {
        builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
      }
    }

    Specification<Product> spec = builder.build();
    Page<Product> getProductList = productRepository
        .findAll(spec, PageRequest.of(page - 1, size, Sort.by("id").descending()));
    List<Product> productList = getProductList.getContent();
    if (productList.isEmpty()) {
      throw new ResourceNotFoundException("List product not found!");
    }
    List<ProductResponseDTO> result = new ArrayList<>();
    for (Product product : productList) {
      ProductResponseDTO productResponseDTO = new ProductResponseDTO();
      productResponseDTO = mapper.productToProductResponseDTO(product);
      productResponseDTO.setDiscount(getDiscount(product));
      result.add(productResponseDTO);
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(result, getProductList.getTotalPages()));
  }

  private Product checkExists(Product product) {
    // Check product name exists
    Optional<Product> getProduct = productRepository.findProductByName(product.getName());
    if (getProduct.isPresent()) {
      if (product.getId() == null) {
        throw new ResourceAlreadyExistsException("Product name is exists");
      } else {
        if (product.getId() != getProduct.get().getId()) {
          throw new ResourceAlreadyExistsException("Product name is exists");
        }
      }
    }
    // Check brand already exists
    Brand brand = brandRepository.findById(product.getBrand().getId()).orElseThrow(
        () -> new ResourceNotFoundException("Could not find brand with ID = " + product.getBrand().getId()));
    product.setBrand(brand);
    // Check category already exists
    Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(
        () -> new ResourceNotFoundException("Could not find category with ID = " + product.getCategory().getId()));
    product.setCategory(category);
    return product;
  }

  private String[] saveImage(ProductRequestDTO productRequestDTO, Product product) {
    int numberOfFile = productRequestDTO.getImages().length;
    String[] images = new String[numberOfFile];
    for (int i = 0; i < numberOfFile; i++) {
      images[i] = imageStorageService.storeFile(productRequestDTO.getImages()[i], "product/images");
    }

    for (String path : images) {
      ImageProduct imageProduct = new ImageProduct();
      imageProduct.setProduct(product);
      imageProduct.setPath(path);
      this.imageProductRepository.save(imageProduct);
    }
    return images;
  }

  /*private ProductGalleryDTO toProductGalleryDTO(Product product, double discount) {
    ProductGalleryDTO productGalleryDTO = new ProductGalleryDTO();
    productGalleryDTO.setId(product.getId());
    productGalleryDTO.setName(product.getName());
    productGalleryDTO.setStandCost(product.getStandCost());
    productGalleryDTO.setThumbnail(product.getThumbnail());
    productGalleryDTO.setDiscount(discount);
    BigDecimal price = product.getStandCost().multiply(BigDecimal.valueOf(discount)).setScale(2, RoundingMode.UP);
    if (discount == 0) {
      price = product.getStandCost();
    }
    productGalleryDTO.setPrice(price);
    List<Feedback> feedbackList = feedbackRepository.findFeedbacksByProduct(product);
    productGalleryDTO.setVote(Utils.calculateAvgRate(feedbackList));

    return productGalleryDTO;
  }*/

  @Override
  public ResponseEntity<?> getTop10ProductByVote() {
    List<IProductVote> productList = productRepository.getTop10ProductByVote();
    List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
    for (IProductVote iProductVote : productList) {
      Product product = productRepository.findById(iProductVote.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + iProductVote.getProductId()));
      ProductResponseDTO productResponseDTO = mapper.productToProductResponseDTO(product);
      productResponseDTO.setDiscount(getDiscount(product));
      productResponseDTOList.add(productResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
  }

  @Override
  public ResponseEntity<Integer> getNumberOfProduct() {
    int numberOfProduct = productRepository.getNumberOfProduct().orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    return ResponseEntity.status(HttpStatus.OK).body(numberOfProduct);
  }
}
