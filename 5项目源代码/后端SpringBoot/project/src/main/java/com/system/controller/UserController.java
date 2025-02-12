package com.system.controller;

import com.system.entity.*;
import com.system.result.ChooseLogin;
import com.system.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.Class;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private JdbcTemplate jdbc;

    //用户注册控制
    //教务注册
    @PostMapping("/academicRegister")
    public Result doRegister(@RequestBody Academic ac){
        Result res =new Result();
        try {
            jdbc.update("insert into academic values (?,?,?,?,?,?);",
                    ac.getAcademicId(),ac.getAcademicPassword(),ac.getAcademicName(),ac.getContactInformation(),ac.getAddress(),ac.getPictureId());
            res.setCode(202);
            res.setResult("true");
            return res;
        }catch (DataAccessException e){
            e.printStackTrace();
            res.setCode(201);
            res.setResult("false");
            return res;
        }
    }
    private static final int teachernum = 30000001;
    @PostMapping("/teacherRegister")
    public Result doTeacherRegister(@RequestBody Teacher t){
        Result res =new Result();
        try {
            int tnum = jdbc.queryForObject("select count(teacherID) from teacher;", Integer.class);
            tnum = tnum+teachernum;
            jdbc.update("insert into teacher(teacherID,departmentID,teacherPassword,teacherName,teacherGender,teacherIntroduction,phoneNumber,teacherEmail,pictureID) values (?,?,?,?,?,?,?,?,?);",
                    tnum,t.getDepartmentId(),t.getTeacherPassword(),t.getTeacherName(),t.getTeacherGender(),
                    t.getTeacherIntroduction(),t.getPhoneNumber(),t.getTeacherEmail(),t.getPictureId());
            res.setCode(200);
            res.setResult(tnum);
            return res;
        }catch (DataAccessException e){
            e.printStackTrace();
            res.setCode(201);
            res.setResult("false");
            return res;
        }
    }
    @PostMapping("/studentRegister")
    public Result doStudentRegister(@RequestBody Student s){
        Result res =new Result();
        try {
            int snum = jdbc.queryForObject("select count(studentID) from student where studentID>? && " +
                    "studentID<?;", Integer.class, s.getStudentId()*10000, (s.getStudentId()+1)*10000);
            snum=snum+1;
            jdbc.update("insert into student values (?,?,null,?,?,?,null,?,?);",
                    snum+s.getStudentId()*10000,s.getSpecialityId(),s.getStudentPassword(),s.getStudentName(),
                        s.getStudentGender(),s.getStudentEmail(),s.getPictureId());
            res.setCode(200);
            res.setResult(snum+s.getStudentId()*10000);
            return res;
        }catch (DataAccessException e){
            e.printStackTrace();
            res.setCode(201);
            res.setResult("false");
            return res;
        }
    }

    @PostMapping("/registSpeciality")
    public Result queryDep(){
        Result res =new Result();
        try{
            List<Speciality> dep = jdbc.query("select * from speciality where specialityID <> 0;",new BeanPropertyRowMapper<>(Speciality.class));
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

    @PostMapping("/doLogin")
    public Result doLogin(@RequestBody ChooseLogin cl){
        Result res = new Result();
        Integer type = cl.getLoginType();
        String id = cl.getId();
        String pw = cl.getPassword();
        char i = id.charAt(0);
        if(i=='1' & type==0 | i=='2' & type==1|i=='3' & type==2){
            try{
                switch (type){
                    case 0:{
                        Academic ac = tryLogin(Academic.class, id, pw);
                        res.setCode(200);
                        res.setResult(ac);
                        break;
                    }
                    case 1:{
                        Student s = tryLogin(Student.class, id, pw);
                        res.setCode(200);
                        res.setResult(s);
                        break;
                    }
                    case 2:{
                        Teacher t = tryLogin(Teacher.class, id, pw);
                        res.setCode(200);
                        res.setResult(t);
                        break;
                    }
                    default:{
                        res.setCode(201);
                        res.setResult("false");
                    }
                }
                return res;
            }catch (DataAccessException e) {
                switch (type){
                    case 0:{
                        int c=getCount(Academic.class,id);
                        System.out.println(c);
                        if(c==0){
                            res.setCode(202);
                            res.setResult("账号不存在");
                            return res;
                        }else{
                            res.setCode(203);
                            res.setResult("密码错误");
                            return res;
                        }
                    }
                    case 1:{
                        int c=getCount(Student.class,id);
                        if(c==0){
                            res.setCode(202);
                            res.setResult("账号不存在");
                            return res;
                        }else{
                            res.setCode(203);
                            res.setResult("密码错误");
                            return res;
                        }
                    }
                    case 2:{
                        int c=getCount(Teacher.class,id);
                        if(c==0){
                            res.setCode(202);
                            res.setResult("账号不存在");
                            return res;
                        }else{
                            res.setCode(203);
                            res.setResult("密码错误");
                            return res;
                        }
                    }
                    default:{
                        res.setCode(201);
                        res.setResult("false");
                        return res;
                    }
                }
            }
        }else{

            res.setCode(201);
            res.setResult("用户类型与用户账号不匹配");
            return res;
        }

    }

    public <T> T tryLogin(Class<T> c, String id, String pw){
        T result = jdbc.queryForObject("SELECT * FROM " +
                        "(SELECT "  + c.getSimpleName().toLowerCase() + "ID a," + c.getSimpleName().toLowerCase() + "Password b FROM "+c.getSimpleName()+ ") as total" +
                        " WHERE a=? AND b=?;",
                new BeanPropertyRowMapper<>(c), id, pw);
        return result;
    }

    public int getCount(Class<?> c, String id) {
        String tableName = c.getSimpleName();
        String idColumnName = tableName.toLowerCase() + "ID";
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + idColumnName + "=?";
        return jdbc.queryForObject(sql, Integer.class, id);
    }
}