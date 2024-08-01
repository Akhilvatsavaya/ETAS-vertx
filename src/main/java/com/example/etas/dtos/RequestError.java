package com.example.etas.dtos;

public class RequestError {
  private String errorCode;

  public RequestError(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
