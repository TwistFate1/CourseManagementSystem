/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2023/7/8 16:05:55                            */
/*==============================================================*/


drop table if exists Academic;

drop table if exists Belong;

drop table if exists Class;

drop table if exists Classroom;

drop table if exists Course;

drop table if exists CourseInfo;

drop table if exists Department;

drop table if exists Speciality;

drop table if exists Student;

drop table if exists Study;

drop table if exists Teacher;

drop table if exists Teaching;

/*==============================================================*/
/* Table: Academic                                              */
/*==============================================================*/
create table Academic
(
   academicID           numeric(8,0) not null,
   academicPassword     varchar(20) not null,
   academicName         varchar(12) not null,
   contactInformation   varchar(20),
   address              varchar(20),
   pictureID            numeric(2,0) not null,
   primary key (academicID)
);

/*==============================================================*/
/* Table: Belong                                                */
/*==============================================================*/
create table Belong
(
   courseID             numeric(8,0) not null,
   specialityID         int not null,
   primary key (courseID, specialityID)
);

/*==============================================================*/
/* Table: Class                                                 */
/*==============================================================*/
create table Class
(
   classID              numeric(3,0) not null,
   courseID             numeric(8,0) not null,
   classYear            varchar(5) not null,
   people               numeric(8,0) not null,
   primary key (classID, classYear)
);

/*==============================================================*/
/* Table: Classroom                                             */
/*==============================================================*/
create table Classroom
(
   classroomID          varchar(8) not null,
   primary key (classroomID)
);

/*==============================================================*/
/* Table: Course                                                */
/*==============================================================*/
create table Course
(
   courseID             numeric(8,0) not null,
   departmentID         int not null,
   courseName           varchar(20) not null,
   hour                 int not null,
   credit               float(1) not null,
   term                 numeric(1,0) not null,
   state                bool not null,
   primary key (courseID)
);

/*==============================================================*/
/* Table: CourseInfo                                            */
/*==============================================================*/
create table CourseInfo
(
   week                 numeric(2,0) not null,
   day                  numeric(1,0) not null,
   number               numeric(2,0) not null,
   classroomID          varchar(8) not null,
   classID              numeric(3,0) not null,
   classYear            varchar(5) not null,
   primary key (week, day, number, classID, classroomID, classYear)
);

/*==============================================================*/
/* Table: Department                                            */
/*==============================================================*/
create table Department
(
   departmentID         int not null auto_increment,
   departmentName       varchar(20) not null,
   primary key (departmentID)
);

/*==============================================================*/
/* Table: Speciality                                            */
/*==============================================================*/
create table Speciality
(
   specialityID         int not null auto_increment,
   departmentID         int not null,
   specialityName       varchar(20) not null,
   primary key (specialityID)
);

/*==============================================================*/
/* Table: Student                                               */
/*==============================================================*/
create table Student
(
   studentID            numeric(8,0) not null,
   specialityID         int not null,
   teacherID            numeric(8,0),
   studentPassword      varchar(20) not null,
   studentName          varchar(12) not null,
   studentGender        bool not null,
   studentPhoneNumber   numeric(11,0),
   studentEmail         varchar(32),
   pictureID            numeric(2,0) not null,
   primary key (studentID)
);

/*==============================================================*/
/* Table: Study                                                 */
/*==============================================================*/
create table Study
(
   classID              numeric(3,0) not null,
   classYear            varchar(5) not null,
   studentID            numeric(8,0) not null,
   grade                int,
   primary key (classID, classYear, studentID)
);

/*==============================================================*/
/* Table: Teacher                                               */
/*==============================================================*/
create table Teacher
(
   teacherID            numeric(8,0) not null,
   departmentID         int not null,
   teacherPassword      varchar(20) not null,
   teacherName          varchar(12) not null,
   teacherGender        bool not null,
   teacherIntroduction  varchar(200),
   phoneNumber          numeric(11,0),
   teacherEmail         varchar(32),
   pictureID            numeric(2,0) not null,
   primary key (teacherID)
);

/*==============================================================*/
/* Table: Teaching                                              */
/*==============================================================*/
create table Teaching
(
   classID              numeric(3,0) not null,
   classYear            varchar(5) not null,
   teacherID            numeric(8,0) not null,
   primary key (classID, classYear, teacherID)
);

alter table Belong add constraint FK_Reflect foreign key (specialityID)
      references Speciality (specialityID) on delete restrict on update restrict;

alter table Belong add constraint FK_relative foreign key (courseID)
      references Course (courseID) on delete restrict on update restrict;

alter table Class add constraint FK_Mapping foreign key (courseID)
      references Course (courseID) on delete restrict on update restrict;

alter table Course add constraint FK_Develop foreign key (departmentID)
      references Department (departmentID) on delete restrict on update restrict;

alter table CourseInfo add constraint FK_Attend foreign key (classroomID)
      references Classroom (classroomID) on delete restrict on update restrict;

alter table CourseInfo add constraint FK_SpecificInfo foreign key (classID, classYear)
      references Class (classID, classYear) on delete restrict on update restrict;

alter table Speciality add constraint FK_Establish foreign key (departmentID)
      references Department (departmentID) on delete restrict on update restrict;

alter table Student add constraint FK_Corresponding foreign key (specialityID)
      references Speciality (specialityID) on delete restrict on update restrict;

alter table Student add constraint FK_Mentor foreign key (teacherID)
      references Teacher (teacherID) on delete restrict on update restrict;

alter table Study add constraint FK_Learn foreign key (classID, classYear)
      references Class (classID, classYear) on delete restrict on update restrict;

alter table Study add constraint FK_Select foreign key (studentID)
      references Student (studentID) on delete restrict on update restrict;

alter table Teacher add constraint FK_Hire foreign key (departmentID)
      references Department (departmentID) on delete restrict on update restrict;

alter table Teaching add constraint FK_Instruct foreign key (teacherID)
      references Teacher (teacherID) on delete restrict on update restrict;

alter table Teaching add constraint FK_Relationship foreign key (classID, classYear)
      references Class (classID, classYear) on delete restrict on update restrict;

