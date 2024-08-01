package com.example.etas.dtos;

public class BookingRequest {
  private String sourceLocation;
  private String dateTimeOfJourney;
  private Long employeeId;

  // Getters and setters

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
}
