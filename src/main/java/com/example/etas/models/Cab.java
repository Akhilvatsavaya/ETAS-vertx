package com.example.etas.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cab {

  @JsonProperty("cab_id")
  private Long cabId;

  @JsonProperty("registration_number")
  private String registrationNumber;

  @JsonProperty("driver_id")
  private Long driverId;

  @JsonProperty("cab_status")
  private String cabStatus;

  private String comments;
  private int vacancy;

  public Long getCabId() {
    return cabId;
  }

  public void setCabId(Long cabId) {
    this.cabId = cabId;
  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public Long getDriverId() {
    return driverId;
  }

  public void setDriverId(Long driverId) {
    this.driverId = driverId;
  }

  public String getCabStatus() {
    return cabStatus;
  }

  public void setCabStatus(String cabStatus) {
    this.cabStatus = cabStatus;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public int getVacancy() {
    return vacancy;
  }

  public void setVacancy(int vacancy) {
    this.vacancy = vacancy;
  }
}
