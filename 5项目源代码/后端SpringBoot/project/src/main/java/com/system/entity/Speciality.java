package com.system.entity;


public class Speciality {

  private long specialityId;
  private long departmentId;
  private String specialityName;


  public long getSpecialityId() {
    return specialityId;
  }

  public void setSpecialityId(long specialityId) {
    this.specialityId = specialityId;
  }


  public long getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(long departmentId) {
    this.departmentId = departmentId;
  }


  public String getSpecialityName() {
    return specialityName;
  }

  public void setSpecialityName(String specialityName) {
    this.specialityName = specialityName;
  }

}
