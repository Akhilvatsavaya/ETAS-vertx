package com.example.etas.dtos;

public class RequestSuccess {
  private Long requestId;

  public RequestSuccess(Long requestId) {
    this.requestId = requestId;
  }

  public Long getRequestId() {
    return requestId;
  }

  public void setRequestId(Long requestId) {
    this.requestId = requestId;
  }
}
