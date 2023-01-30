package com.example.webecom.services;

import com.example.webecom.dto.request.BillRequestDTO;
import com.example.webecom.dto.request.PaymentRequestDTO;
import com.example.webecom.dto.response.ResponseObject;
import com.mservice.models.PaymentResponse;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
  ResponseEntity<ResponseObject> createPaymentPaypal(PaymentRequestDTO paypalRequestDTO, String cancelUrl, String successUrl) throws PayPalRESTException;
  ResponseEntity<?> successPaypal(String paymentId, String payerId, String token) throws PayPalRESTException;
  ResponseEntity<PaymentResponse> createPaymentMomo(PaymentRequestDTO paymentRequestDTO, String returnUrl) throws Exception;
  ResponseEntity<?> successMomo(Long billId, int resultCode);
}
