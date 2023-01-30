package com.example.webecom.services.implement;

import com.example.webecom.dto.request.UserRequestDTO;
import com.example.webecom.dto.response.AuthResponseDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.example.webecom.dto.response.UserResponseDTO;
import com.example.webecom.entities.AddressDetail;
import com.example.webecom.entities.Cart;
import com.example.webecom.entities.CartDetail;
import com.example.webecom.entities.Feedback;
import com.example.webecom.entities.ImageFeedback;
import com.example.webecom.entities.Role;
import com.example.webecom.entities.User;
import com.example.webecom.exceptions.ResourceAlreadyExistsException;
import com.example.webecom.exceptions.ResourceNotFoundException;
import com.example.webecom.mapper.UserMapper;
import com.example.webecom.models.ItemTotalPage;
import com.example.webecom.repositories.AddressDetailRepository;
import com.example.webecom.repositories.CartDetailRepository;
import com.example.webecom.repositories.CartRepository;
import com.example.webecom.repositories.FeedbackRepository;
import com.example.webecom.repositories.ImageFeedbackRepository;
import com.example.webecom.repositories.RoleRepository;
import com.example.webecom.repositories.UserRepository;
import com.example.webecom.services.AddressDetailService;
import com.example.webecom.services.ImageStorageService;
import com.example.webecom.services.UserService;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {
  private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ImageStorageService imageStorageService;
  @Autowired
  private CartDetailRepository cartDetailRepository;
  @Autowired
  private FeedbackRepository feedbackRepository;
  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private CartRepository cartRepository;
  @Autowired
  private ImageFeedbackRepository imageFeedbackRepository;
  @Autowired
  private AddressDetailRepository addressDetailRepository;

  @Override
  public AuthResponseDTO findUserByEmailAndPassword(String email, String password) {
    return null;
  }

  @Override
  public ResponseEntity<ResponseObject> saveUser(UserRequestDTO userRequestDTO, String siteUrl)
      throws MessagingException, UnsupportedEncodingException {
    User user = mapper.userRequestDTOtoUser(userRequestDTO);

    // Check phone user existed
    Optional<User> userCheckPhone = userRepository.findUserByPhone(userRequestDTO.getPhone());
    if (userCheckPhone.isPresent()) {
      throw new ResourceAlreadyExistsException("Phone user existed");
    }

    // Check email user existed
    Optional<User> userCheckEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
    if (userCheckEmail.isPresent()) {
      throw new ResourceAlreadyExistsException("Email user existed");
    }

    encodePassword(user);
    // Check role already exists
    Role role = roleRepository.findRoleById(user.getRole().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + user.getRole().getId()));
    user.setRole(role);
    user.setEnable(false);

    String randomCodeVerify = RandomString.make(64);
    user.setVerificationCode(randomCodeVerify);

    UserResponseDTO userResponseDTO = mapper.userToUserResponseDTO(userRepository.save(user));
    sendVerificationEmail(user, siteUrl);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Create user successfully!", userResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> updateUser(Long id, UserRequestDTO userRequestDTO) {
    User user = mapper.userRequestDTOtoUser(userRequestDTO);
    user.setId(id);
    User userExists = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));

    // Check email user existed
    if (!user.getEmail().equals(userExists.getEmail())) {
      Optional<User> userCheckEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
      if (userCheckEmail.isPresent()) {
        throw new ResourceAlreadyExistsException("Email user existed");
      }
    }

    // Check phone user existed
    if (!user.getPhone().equals(userExists.getPhone())) {
      Optional<User> userCheckPhone = userRepository.findUserByPhone(userRequestDTO.getPhone());
      if (userCheckPhone.isPresent()) {
        throw new ResourceAlreadyExistsException("Phone user existed");
      }
    }

    //Store image
    if (userRequestDTO.getImage() != null){
      user.setImages(
          imageStorageService.storeFile(userRequestDTO.getImage(), "user"));
    }

    //Update password
    if (userRequestDTO.getPassword() == null){
      user.setPassword(userExists.getPassword());
    } else {
      encodePassword(user);
    }

    //Update enable
    if (userRequestDTO.getEnable() == null){
      user.setEnable(userExists.isEnable());
    } else {
      user.setEnable(Boolean.parseBoolean(userRequestDTO.getEnable()));
    }

    // Check role already exists
    Role role = roleRepository.findRoleById(user.getRole().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find role with ID = " + user.getRole().getId()));
    user.setRole(role);
    UserResponseDTO userResponseDTO = mapper.userToUserResponseDTO(userRepository.save(user));

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update user successfully!",
        userResponseDTO));
  }

  @Override
  public ResponseEntity<?> getAllUser(Pageable pageable) {
    List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
    Page<User> getUserList = userRepository.findAll(pageable);
    List<User> userList = getUserList.getContent();
    for (User user : userList) {
      UserResponseDTO userResponseDTO = mapper.userToUserResponseDTO(user);
      userResponseDTOList.add(userResponseDTO);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(userResponseDTOList,
       getUserList.getTotalPages()));
  }

  @Override
  public ResponseEntity<ResponseObject> deleteUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
    // Delete cart by user
    Optional<Cart> getCart = cartRepository.findCartByUser(user);
    if (getCart.isPresent()) {
      List<CartDetail> cartDetailList = cartDetailRepository.findCartDetailByCart(getCart.get());
      cartDetailRepository.deleteAll(cartDetailList);
      cartRepository.delete(getCart.get());
    }
    // Delete feedback by user
    List<Feedback> feedbackList = feedbackRepository.findFeedbackByUser(user);
    for (Feedback feedback : feedbackList) {
      List<ImageFeedback> imageFeedbackList = imageFeedbackRepository.findImageFeedbackByFeedback(feedback);
      for (ImageFeedback imageFeedback : imageFeedbackList) {
        imageStorageService.deleteFile(imageFeedback.getPath(), "feedback");
      }
      imageFeedbackRepository.deleteAll(imageFeedbackList);
      feedbackRepository.delete(feedback);
    }
    // Delete address
    List<AddressDetail> addressDetailList = addressDetailRepository.findAddressDetailsByUser(user);
    addressDetailRepository.deleteAll(addressDetailList);

    imageStorageService.deleteFile(user.getImages(), "user");
    userRepository.delete(user);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete user success!!!", null));
  }

  @Override
  public ResponseEntity<UserResponseDTO> getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
    UserResponseDTO userResponseDTO = mapper.userToUserResponseDTO(user);
    return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
  }

  @Override
  public ResponseEntity<ResponseObject> verifyUser(String verifyCode) {
    User getUser = userRepository.findUserByVerificationCode(verifyCode)
        .orElseThrow(() -> new ResourceNotFoundException("Verify code is incorrect"));
    getUser.setEnable(true);
    User user = userRepository.save(getUser);
    // Create cart by user
    Optional<Cart> getCart = cartRepository.findCartByUser(user);
    if (getCart.isPresent()){
      throw new ResourceNotFoundException("User verified");
    }
    Cart cart = new Cart();
    cart.setUser(user);
    this.cartRepository.save(cart);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Verify account success!!!"));
  }

  private void encodePassword(User user) {
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
  }

  private void sendVerificationEmail(User user, String siteUrl)
      throws MessagingException, UnsupportedEncodingException {
    String subject = "Please verify your registration";
    String senderName = "Mobile University Store";
    String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();
    String mailContent = "<p>Dear " + user.getName() + ",<p><br>"
        + "Please click the link below to verify your registration:<br>"
        + "<h3><a href = \"" + verifyUrl + "\">VERIFY</a></h3>"
        + "Thank you,<br>" + "Mobile University.";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message);

    messageHelper.setFrom("mobileuniversity@gmail.com", senderName);
    messageHelper.setTo(user.getEmail());
    messageHelper.setSubject(subject);
    messageHelper.setText(mailContent, true);
    mailSender.send(message);
  }

  @Override
  public ResponseEntity<Integer> getNumberOfUser() {
    int numberOfUser = userRepository.getNumberOfUser().orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return ResponseEntity.status(HttpStatus.OK).body(numberOfUser);
  }
}
