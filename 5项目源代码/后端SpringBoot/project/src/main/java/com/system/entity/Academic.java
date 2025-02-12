package com.system.entity;


public class Academic {

  private double academicId;
  private String academicPassword;
  private String academicName;
  private String contactInformation;
  private String address;
  private double pictureId;


  public double getAcademicId() {
    return academicId;
  }

  public void setAcademicId(double academicId) {
    this.academicId = academicId;
  }


  public String getAcademicPassword() {
    return academicPassword;
  }

  public void setAcademicPassword(String academicPassword) {
    this.academicPassword = academicPassword;
  }


  public String getAcademicName() {
    return academicName;
  }

  public void setAcademicName(String academicName) {
    this.academicName = academicName;
  }


  public String getContactInformation() {
    return contactInformation;
  }

  public void setContactInformation(String contactInformation) {
    this.contactInformation = contactInformation;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public double getPictureId() {
    return pictureId;
  }

  public void setPictureId(double pictureId) {
    this.pictureId = pictureId;
  }

}
