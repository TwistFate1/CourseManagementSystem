package com.system.controller;

import com.system.entity.Class;
import com.system.result.*;
import com.system.entity.*;

import com.system.result.Unlawful;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AcademicController {
    @Autowired
    private JdbcTemplate jdbc;
    //信息管理：教师信息和学生信息
    @PostMapping("/queryAcademicTeacher")
    public Result queryATeacher(@RequestBody Department d){
        Result r = new Result();
        try{
            String sql = "SELECT teacher.teacherID, department.departmentName, teacher.teacherPassword, teacher.teacherName, " +
                    "teacher.teacherGender, teacher.teacherIntroduction, teacher.phoneNumber, teacher.teacherEmail,teacher.pictureID " +
                    "FROM teacher, department " +
                    "WHERE teacher.departmentID = department.departmentID AND department.departmentName = ?";

            List<Map<String, Object>> t = jdbc.queryForList(sql, d.getDepartmentName());
            r.setCode(200);
            r.setResult(t);
        }catch (DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }
    @PostMapping("/registDepartment")
    public Result queryDep(){
        Result res =new Result();
        try{
            List<Department> dep = jdbc.query("select * from department where departmentID <> 0;",
                    new BeanPropertyRowMapper<>(Department.class));
            res.setCode(200);
            res.setResult(dep);
            return res;
        }catch (DataAccessException e){
            e.printStackTrace();
            res.setCode(201);
            res.setResult("false");
            return res;
        }
    }
    @PostMapping("/registTeachering")
    public Result queryTeacher(){
        Result res =new Result();
        try{
            List<Teacher> dep = jdbc.query("select * from teacher;",new BeanPropertyRowMapper<>(Teacher.class));
            res.setCode(200);
            res.setResult(dep);
            return res;
        }catch (DataAccessException e){
            e.printStackTrace();
            res.setCode(201);
            res.setResult("false");
            return res;
        }
    }
    //查看课程信息+查询某专业+要排课的学期的课程有哪些
    @PostMapping("/queryAcademicCourse")
    public Result queryACourse(@RequestBody Course ci){
        Result r = new Result();
        if(ci.getDepartmentId()==0){
            try{
                List<Course> c=jdbc.query("select * from course",
                        new BeanPropertyRowMapper<>(Course.class));
                r.setCode(200);
                r.setResult(c);
            }catch (DataAccessException e){
                e.printStackTrace();
                r.setCode(201);
                r.setResult("false"+e.getMessage());
            }

        }else{
            try{
                List<Course> c=jdbc.query("select * from course where term = ?and departmentID= ?;",
                        new BeanPropertyRowMapper<>(Course.class),ci.getTerm(),ci.getDepartmentId());
                r.setCode(200);
                r.setResult(c);
            }catch (DataAccessException e){
                e.printStackTrace();
                r.setCode(201);
                r.setResult("false"+e.getMessage());
            }
        }
        return r;

    }



    //插入课程+修改所属关系
    @PostMapping("/insertAcademicCourse")
    public Result insertAC(@RequestBody Course c){
        Result r =new Result();
        if(c.getCourseId()==0){
            try {
                //如果是插入新课程
                double num = jdbc.queryForObject("select count(courseID) from course;",Integer.class);
                jdbc.update("insert into course values (?,?,?,?,?,?,?)",
                        num+1,c.getDepartmentId(),c.getCourseName(),c.getHour(),c.getCredit(),c.getTerm(),c.getState());
                r.setCode(200);
                r.setResult(num+1);
            }catch (DataAccessException e){
                e.printStackTrace();
                r.setCode(201);
                r.setResult("false"+e.getMessage());
            }
        }else{
            try{
                //如果修改老课程
                jdbc.update("UPDATE course SET hour= ?,credit=?,term=?,state=? WHERE courseID=?;",
                        c.getHour(),c.getCredit(),c.getTerm(),c.getState(),c.getCourseId());
                r.setCode(200);
                r.setResult("true");
            }catch (DataAccessException e){
                e.printStackTrace();
                r.setCode(201);
                r.setResult("false"+e.getMessage());
            }
        }
        return r;
    }
    @PostMapping("/insertAcademicBelong")  //插入新课程一定要加belong！
    public Result insertABelong(@RequestBody Belong b){
        Result r =new Result();
        try{
           jdbc.update("insert into belong values (?,?)",b.getCourseId(),b.getSpecialityId());
            r.setCode(200);
            r.setResult("true");
        }catch (DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }
    //插入教学班级和教师授课表
    @PostMapping("/insertAcademicClass")
    public Result insertAClass(@RequestBody Class ci){
        Result r =new Result();
        try{
            Integer coursenum=jdbc.queryForObject("select term from course where courseID=?",Integer.class,ci.getCourseId());
            LocalDate currentDate = LocalDate.now();
            String presentYear = currentDate.getYear()+"";
            String term = (coursenum % 2 == 0) ? (presentYear + "春") : (presentYear + "秋") ;
            Integer classnum = jdbc.queryForObject("select count(classID) from class where classYear=?;", Integer.class,term);
            classnum =classnum+1;
            jdbc.update("insert into class values (?,?,?,?);",classnum,ci.getCourseId(),term,ci.getPeople());
            Class c = new Class();
            c.setClassId(classnum);c.setCourseId(coursenum);c.setClassYear(term);c.setPeople(ci.getPeople());
            r.setCode(200);
            r.setResult(c);
        }catch (DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }
    @PostMapping("/queryTeachering")
    public Result queryTeacherCourse(@RequestBody Course c){
        Result res =new Result();
        try{
            List<Map<String,Object>> t = jdbc.queryForList("select teacherID, teacherName,teacherIntroduction, phoneNumber from " +
                            "teacher, course where course.departmentID=teacher.departmentID and courseID=? and term=?;"
                    ,c.getCourseId(),c.getTerm());
            res.setCode(200);
            res.setResult(t);
            return res;
        }catch (DataAccessException e){
            e.printStackTrace();
            res.setCode(201);
            res.setResult("false");
            return res;
        }
    }
    @PostMapping("/insertAcademicTeaching") //改了这个函数！！！！！！！！！！1
    public Result insertATeaching(@RequestBody Teaching t){
        Result r =new Result();
        Integer num = jdbc.queryForObject("SELECT count(classID) from teaching where  classID=? AND classYear=?",
                Integer.class,t.getClassId(),t.getClassYear());
        if(num==0){
            try{
                jdbc.update("insert into teaching values (?,?,?)",t.getClassId(),t.getClassYear(),t.getTeacherId());
                r.setCode(200);
                r.setResult("true");
            }catch (DataAccessException e){
                e.printStackTrace();
                r.setCode(201);
                r.setResult("false"+e.getMessage());
            }
        }else{
            try{
                jdbc.update("update teaching set teacherID=? where classID=? and classYear=?",
                        t.getTeacherId(),t.getClassId(),t.getClassYear());
                r.setCode(200);
                r.setResult("true");
            }catch (DataAccessException e){
                e.printStackTrace();
                r.setCode(201);
                r.setResult("false"+e.getMessage());
            }
        }

        return r;
    }

    //排课
    //根据选定的教学班，返回专业内冲突的时间
    @PostMapping("/queryAcademicIlligal")
    public  Result queryAIlligal(@RequestBody Unlawful ci){
        Result r = new Result();
        try {
            String sql="SELECT w as week,d as day, n as number\n" +
                    "FROM\n" +
                    "    (SELECT DISTINCT courseinfo.week AS w, courseinfo.day AS d, courseinfo.number AS n\n" +
                    "    FROM teaching NATURAL JOIN class NATURAL JOIN courseinfo\n" +
                    "    WHERE teaching.teacherID IN (\n" +
                    "        SELECT teacherID\n" +
                    "        FROM teaching NATURAL JOIN class\n" +
                    "        WHERE courseID = ? AND classYear = ?\n" +
                    "    )) AS a  \n" +
                    "UNION ALL\n" +
                    "SELECT DISTINCT courseinfo.week AS w, courseinfo.day AS d, courseinfo.number AS n\n" +
                    "FROM courseinfo NATURAL JOIN class\n" +
                    "WHERE classYear = ? AND classroomID = ?;";
            List<Map<String,Object>> times = jdbc.queryForList(sql,ci.getCourseId(),ci.getClassYear(),ci.getClassYear(),ci.getClassroomId());
            r.setCode(200);
            r.setResult(times);  //可能为空，此时即为没有冲突
        }catch (DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }
    //可能出现的冲突：排课没排满，人数没排够，同专业开设课程不能时间冲突
    @PostMapping("/insertAcademicCourseInfo")
    public Result insertACourse(@RequestBody Courseinfo ci){
        Result r =new Result();
        Integer num = jdbc.queryForObject("select count(classID) from courseinfo where classID= ? and classYear = ?;",
                Integer.class,ci.getClassId(),ci.getClassYear());
        Integer tim = jdbc.queryForObject("select hour from course,class where course.courseID=class.courseID AND class.classID= ? and classYear = ?;",
                Integer.class,ci.getClassId(),ci.getClassYear());
        if(num<tim){
            try{
                jdbc.update("insert into courseinfo values (?,?,?,?,?,?);",
                        ci.getWeek(),ci.getDay(),ci.getNumber(),ci.getClassroomId(),ci.getClassId(),ci.getClassYear());
                num=num+1;
                if(num > tim){
                    num=100;
                }else{
                    num=(num*100) /(tim);
                }
                r.setCode(200);
                r.setResult(num);
            }catch (DataAccessException e){
                e.printStackTrace();
                r.setCode(201);
                r.setResult("false");
            }
        }
        else{
            r.setCode(202);
            r.setResult(100);
        }

            return r;


    }
    @PostMapping("/queryAcademicClassroom")
    public Result queryACR(){
        Result r=new Result();
        try{
            List<Map<String,Object>> cid=jdbc.queryForList("select classroomID from classroom");
            r.setCode(200);
            r.setResult(cid);
        }catch (DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }

    @PostMapping("/queryAcademicGrade")
    public Result queryAGrade(@RequestBody Class c){
        Result r=new Result();
        try{
            String sql="select avg(grade) as avg,classYear from study natural join class " +
                        "where class.courseID=?" +
                        "group by classYear";
            List<Map<String, Object>> grade = jdbc.queryForList(sql,c.getCourseId());
            List<Double> gradeValues = new ArrayList<>();
            for (Map<String, Object> row : grade) {
                for (Object value : row.values()) {
                    if (value instanceof Number) {
                        gradeValues.add(((Number) value).doubleValue());
                    }
                }
            }//将返回值转成double类型的列表
            r.setCode(200);
            r.setResult(grade);
        }catch (DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }

    @PostMapping("/queryAcademicInformation")
    public Result queryAInfor(@RequestBody Academic aid){
        Result r=new Result();
        try{
            aid= jdbc.queryForObject("select * from academic where academicID=?",
                    new BeanPropertyRowMapper<>(Academic.class),aid.getAcademicId());
            r.setCode(200);
            r.setResult(aid);
            return r;
        }catch (DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }

    @PostMapping("/queryAcademicGrade1")
    public Result queryAGrade1(@RequestBody Class c){
        Result r=new Result();
        try{
            String sql="select grade,classYear from study natural join class " +
                    "where class.courseID=? AND class.classYear=?";
            List<Map<String, Object>> grade = jdbc.queryForList(sql,c.getCourseId(),c.getClassYear());
            //将返回值转成double类型的列表
            r.setCode(200);
            r.setResult(grade);
        }catch (DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }

    @PostMapping("/queryAcdemicClass")
    public Result queryAClass(){
        Result r = new Result();
        LocalDate currentDate = LocalDate.now();
        int y = currentDate.getYear();
        int m = currentDate.getMonthValue();
        String prey;

        if (m >= 8) {
            prey = y + "秋";
        } else {
            prey = y + "春";
        }

        try{
            List<Map<String,Object>>  ci =jdbc.queryForList("select course.courseName,course.courseId,classId,classYear " +
                    "from class,course where classYear=? And class.courseID=course.courseID",prey);
            r.setCode(200);
            r.setResult(ci);
        }catch(DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }
    //新增，删除课程
    @PostMapping("/deleteAcademicClass")
    public Result deleteAcademicClass(@RequestBody Class c){
        Result r =new Result();
        try{
            jdbc.update("delete from courseinfo where classID=? AND classYear=?",c.getClassId(),c.getClassYear());
            jdbc.update("delete from teaching where classID=? AND classYear=?;",c.getClassId(),c.getClassYear());
            jdbc.update("update class set classYear=0 where classID=? AND classYear=?;",c.getClassId(),c.getClassYear());
            r.setCode(200);
            r.setResult("true");
        }catch(DataAccessException e){
            e.printStackTrace();
            r.setCode(201);
            r.setResult("false"+e.getMessage());
        }
        return r;
    }

}

