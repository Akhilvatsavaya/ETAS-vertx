package com.example.etas.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Booking {

  @JsonProperty("booking_id")
  private Long bookingId;

  @JsonProperty("request_id")
  private Long requestId;

  @JsonProperty("driver_id")
  private Long driverId;

  @JsonProperty("cab_id")
  private Long cabId;

  @JsonProperty("source_location")
  private String sourceLocation;

  @JsonProperty("date_time_of_journey")
  private String dateTimeOfJourney;

  @JsonProperty("request_creation_date")
  private String requestCreationDate;

  @JsonProperty("request_generator")
  private long requestGenerator;

  @JsonProperty("employee_id")
  private Long employeeId;

  @JsonProperty("request_status")
  private String requestStatus;

  @JsonProperty("booking_status")
  private String bookingStatus;

  private String comments;


  public Long getBookingId() {
    return bookingId;
  }

  public void setBookingId(Long bookingId) {
    this.bookingId = bookingId;
  }

  public String getSourceLocation() {
    return sourceLocation;
  }

  public void setSourceLocation(String sourceLocation) {
    this.sourceLocation = sourceLocation;
  }

  public String getDateTimeOfJourney() {
    return dateTimeOfJourney;
  }

  public void setDateTimeOfJourney(String dateTimeOfJourney) {
    this.dateTimeOfJourney = dateTimeOfJourney;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
  }

  public String getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(String requestStatus) {
    this.requestStatus = requestStatus;
  }

  public Long getRequestId() {
    return requestId;
  }

  public void setRequestId(Long requestId) {
    this.requestId = requestId;
  }

  public String getBookingStatus() {
    return bookingStatus;
  }

  public void setBookingStatus(String bookingStatus) {
    this.bookingStatus = bookingStatus;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Long getDriverId() {
    return driverId;
  }

  public void setDriverId(Long driverId) {
    this.driverId = driverId;
  }

  public Long getCabId() {
    return cabId;
  }

  public void setCabId(Long cabId) {
    this.cabId = cabId;
  }

  public String getRequestCreationDate() {
    return requestCreationDate;
  }

  public void setRequestCreationDate(String requestCreationDate) {
    this.requestCreationDate = requestCreationDate;
  }

  public Long getRequestGenerator() {
    return requestGenerator;
  }

  public void setRequestGenerator(Long requestGenerator) {
    this.requestGenerator = requestGenerator;
  }
}
