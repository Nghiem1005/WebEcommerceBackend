package com.example.webecom.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{

  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}
