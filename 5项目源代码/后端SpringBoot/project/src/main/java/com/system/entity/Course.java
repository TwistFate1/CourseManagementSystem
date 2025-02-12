package com.system.entity;


public class Course {

  private double courseId;
  private long departmentId;
  private String courseName;
  private long hour;
  private double credit;
  private double term;
  private long state;


  public double getCourseId() {
    return courseId;
  }

  public void setCourseId(double courseId) {
    this.courseId = courseId;
  }


  public long getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(long departmentId) {
    this.departmentId = departmentId;
  }


  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }


  public long getHour() {
    return hour;
  }

  public void setHour(long hour) {
    this.hour = hour;
  }


  public double getCredit() {
    return credit;
  }

  public void setCredit(double credit) {
    this.credit = credit;
  }


  public double getTerm() {
    return term;
  }

  public void setTerm(double term) {
    this.term = term;
  }


  public long getState() {
    return state;
  }

  public void setState(long state) {
    this.state = state;
  }

}
