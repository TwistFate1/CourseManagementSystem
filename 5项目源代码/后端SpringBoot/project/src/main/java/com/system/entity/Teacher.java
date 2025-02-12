package com.system.entity;


public class Teacher {

  private double teacherId;
  private long departmentId;
  private String teacherPassword;
  private String teacherName;
  private long teacherGender;
  private String teacherIntroduction;
  private double phoneNumber;
  private String teacherEmail;
  private double pictureId;


  public double getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(double teacherId) {
    this.teacherId = teacherId;
  }


  public long getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(long departmentId) {
    this.departmentId = departmentId;
  }


  public String getTeacherPassword() {
    return teacherPassword;
  }

  public void setTeacherPassword(String teacherPassword) {
    this.teacherPassword = teacherPassword;
  }


  public String getTeacherName() {
    return teacherName;
  }

  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }


  public long getTeacherGender() {
    return teacherGender;
  }

  public void setTeacherGender(long teacherGender) {
    this.teacherGender = teacherGender;
  }


  public String getTeacherIntroduction() {
    return teacherIntroduction;
  }

  public void setTeacherIntroduction(String teacherIntroduction) {
    this.teacherIntroduction = teacherIntroduction;
  }


  public double getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(double phoneNumber) {
    this.phoneNumber = phoneNumber;
  }


  public String getTeacherEmail() {
    return teacherEmail;
  }

  public void setTeacherEmail(String teacherEmail) {
    this.teacherEmail = teacherEmail;
  }


  public double getPictureId() {
    return pictureId;
  }

  public void setPictureId(double pictureId) {
    this.pictureId = pictureId;
  }

}
