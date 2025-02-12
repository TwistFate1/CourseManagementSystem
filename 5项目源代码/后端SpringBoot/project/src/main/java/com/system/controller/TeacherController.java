package com.system.controller;


import com.system.entity.Study;
import com.system.entity.Studywithstudentname;
import com.system.entity.Teacher;
import com.system.entity.Teacherwithdeparment;
import com.system.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController//说明是返回数据的httpApi接口(SpringMvc中叫做控制器)
@CrossOrigin(origins = "*")//允许跨域
public class TeacherController {
    @Autowired
    private JdbcTemplate jdbc;

    //1.教师信息
    //输入：教师工号teacherId，输出：teacher和department表的所有属性
    //网址：http://localhost:8080/teacherInfo
    @PostMapping("/teacherInfo")
    public Result teacherInfo(@RequestBody Teacher teacher) {
        Result res = new Result();
        try {
            String sql = "SELECT * FROM teacher JOIN department ON " +
                    "teacher.departmentID = department.departmentID WHERE teacher.teacherID = ?";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql, teacher.getTeacherId());
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //2.修改信息
    //输入：teacher表的所有内容
    //网址：http://localhost:8080/updateInfo
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestBody Teacher teacher) {
        Result res = new Result();
        String sqlUpdate = "UPDATE teacher SET departmentId = ?, teacherPassword = ?, teacherName = ?, teacherGender = ?, " +
                "teacherIntroduction = ?, phoneNumber = ?, teacherEmail = ?, pictureId = ? WHERE teacherId = ?";
        try {
            jdbc.update(sqlUpdate, teacher.getDepartmentId(), teacher.getTeacherPassword(),
                    teacher.getTeacherName(), teacher.getTeacherGender(), teacher.getTeacherIntroduction(),
                    teacher.getPhoneNumber(), teacher.getTeacherEmail(), teacher.getPictureId(), teacher.getTeacherId());
            res.setCode(200); //成功
            res.setResult("更新成功");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("更新失败");
            return res;
        }
    }



    //3.教学课表
    //输入：教师工号teacherId，输出：课名，课程编号，教学班号，周此，星期，节数，教室
    //网址：http://localhost:8080/teacherCourse
    @PostMapping("/teacherCourse")
    public Result teacherSchedule(@RequestBody Teacher teacher) {
        Result res = new Result();
        try {
            String sql = "SELECT courseName,courseID,classID,week,day,number,classroomID " +
                    "FROM teacher NATURAL JOIN teaching NATURAL JOIN class NATURAL JOIN courseinfo NATURAL JOIN course " +
                    "WHERE teacherID=?";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql, teacher.getTeacherId());
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //4.查看成绩
    //输入：教学班号，年份   输出：对应班的学生成绩
    //网址：http://localhost:8080/teacherScore
    @PostMapping("/teacherScore")
    public Result teacherScore(@RequestBody Study study) {
        Result res = new Result();
        try {
            String sql = "SELECT studentID,grade FROM study WHERE classID=? && classYear=?";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql,study.getClassId(),study.getClassYear());
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //5.修改成绩(新)
    //输入：学生编号，学生姓名，教学班号，年份，成绩
    //网址：http://localhost:8080/reviseGrade
    @PostMapping("/reviseGrade")
    //网址：http://localhost:8080/reviseGrade
    public Result reviseGrade(@RequestBody Studywithstudentname studywithstudentname)
    {
        Result r = new Result();
        String sql = "SELECT studentName FROM student WHERE studentID=?;";
        List<Map<String, Object>> queryResult0 = jdbc.queryForList(sql,studywithstudentname.getStudentId());  //根据学生ID返回学生姓名
        String temStudentName = (String) queryResult0.get(0).get("studentName");
        if (temStudentName.equals(studywithstudentname.getStudentName()))
        {
            try {
                //String sqlInsert = "INSERT INTO study VALUES (?,?,?,?)";  //插入语句
                String sqlUP = "Update  study set grade=? where classId=? And classYear=? AND studentId=?";
                jdbc.update(sqlUP,studywithstudentname.getGrade(),studywithstudentname.getClassId(),
                        studywithstudentname.getClassYear(),studywithstudentname.getStudentId());
                r.setCode(200);
                r.setResult("学生ID和学生姓名匹配成功，成绩插入成功");
                return r;
            } catch (DataAccessException e) {
                r.setCode(201);
                r.setResult("学生ID和学生姓名匹配成功，成绩插入失败"+e.getMessage());
                return r;

            }

        }else {
            r.setCode(201);
            r.setResult("学生ID和学生姓名匹配成功，成绩插入失败");
            return r;
        }
    }
    //修改成绩(旧)
/*

//输入：学生编号，学生姓名，教学班号，年份，成绩
@PostMapping("/reviseGrade")
//网址：http://localhost:8080/reviseGrade
public String reviseGrade(Integer studentId, String studentName, Integer classId,String classYear,Integer grade)
{
    String sql = "SELECT studentName FROM student WHERE studentID=?;";
    List<Map<String, Object>> queryResult0 = jdbc.queryForList(sql,studentId);  //根据学生ID返回学生姓名
    String temStudentName = (String) queryResult0.get(0).get("studentName");
    if (temStudentName.equals(studentName))
    {
        try {
            String sqlInsert = "INSERT INTO study VALUES (?,?,?,?)";  //插入语句
            jdbc.update(sqlInsert,classId,classYear,studentId,grade);
            return ("学生ID和学生姓名匹配成功，成绩插入成功");
        } catch (DataAccessException e) {
            return ("学生ID和学生姓名匹配成功，成绩插入失败"+e.getMessage());
        }

    }else {
        return ("学生ID和学生姓名匹配失败");
    }
}
    */

    //6.教师信息全部展示
    //输入为空，输出：teacherID，teacherName，teacherPassword，departmentName，teacherGender，phoneNumber，teacherEmail
    //网址：http://localhost:8080/allTeacherInfo
    @PostMapping ("/allTeacherInfo")
    public Result allTeacherInfo() {
        Result res = new Result();
        try {
            String sql = "SELECT teacherID,teacherName,teacherPassword,departmentName," +
                    "teacherGender,phoneNumber,teacherEmail FROM teacher NATURAL JOIN department";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql);
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }


    //7.所有专业
    //输出：所有专业ID和专业name
    //网址：http://localhost:8080/allDepartment
    @PostMapping ("/allDepartment")
    public Result allDepartment() {
        Result res = new Result();
        try {
            String sql = "SELECT * FROM department";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql);
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //8.修改密码
    //输入：teacherId，teacherPassword
    //网址：http://localhost:8080/updateTeacherPassword
    @PostMapping("/updateTeacherPassword")
    public Result updateTeacherPassword(@RequestBody Teacher teacher) {
        Result res = new Result();
        try {
            String sql = "UPDATE teacher SET teacherPassword=? WHERE teacherId = ?";
            jdbc.update(sql,teacher.getTeacherPassword(),teacher.getTeacherId());
            res.setCode(200); //成功
            res.setResult("密码修改成功");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //9.修改头像
    //输入：teacherId，pictureID
    //网址：http://localhost:8080/updateTeacherPicture
    @PostMapping("/updateTeacherPicture")
    public Result updateTeacherPicture(@RequestBody Teacher teacher) {
        Result res = new Result();
        try {
            String sql = "UPDATE teacher SET pictureID=? WHERE teacherID = ?";
            jdbc.update(sql,teacher.getPictureId(),teacher.getTeacherId());
            res.setCode(200); //成功
            res.setResult("头像修改成功");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //10.修改除了密码，头像的所有信息
    //输入：除了密码，头像的所有信息，  输出：DepartmentName
    //网址：http://localhost:8080/updateTeacher
    @PostMapping("/updateTeacher")
    public Result updateTeacher(@RequestBody Teacherwithdeparment teacher) {
        Result res = new Result();
        try {
            String sql = "UPDATE teacher SET departmentId = ?,teacherName = ?, teacherGender = ?,teacherIntroduction = ?, " +
                    "phoneNumber = ?, teacherEmail = ? WHERE teacherID = ?";
            jdbc.update(sql, teacher.getDepartmentId(), teacher.getTeacherName(), teacher.getTeacherGender(), teacher.getTeacherIntroduction(),
                    teacher.getPhoneNumber(), teacher.getTeacherEmail(), teacher.getTeacherId());
            String sql0 = "SELECT departmentName FROM department WHERE departmentId = ?;";
            teacher.setDepartmentName(jdbc.queryForObject(sql0, String.class, teacher.getDepartmentId()));
            res.setCode(200); //成功
            res.setResult(teacher.getDepartmentName());
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //11.已选课程
    //输入：学生ID，当前学期   输出：courseId departmentId courseName courseHour courseCredit teacherName classId classroomId
    //网址：http://localhost:8080/stuCourse
    /*@PostMapping("/stuCourse")
    public Result stuCourse(@RequestBody Study study) {
        Result res = new Result();
        try {
            String sql = "SELECT DISTINCT classID,courseID,course.departmentID,department.departmentName,courseName,`hour`,credit,teacherName,classroomID " +
                    "FROM study NATURAL JOIN class NATURAL JOIN course NATURAL JOIN courseinfo NATURAL JOIN teaching " +
                    "JOIN teacher ON teacher.teacherID=teaching.teacherID JOIN department ON course.departmentID=department.departmentID  " +
                    "WHERE studentID=?  && classYear=?";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql,study.getStudentId(),study.getClassYear());
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }*/

    @PostMapping("/stuCourse")
    public Result stuCourse(@RequestBody Study study) {
        Result res = new Result();
        try {
            String sql = "SELECT DISTINCT classID, courseID, course.departmentID, department.departmentName, courseName, `hour`, credit, teacherName, classroomID " +
                    "FROM study NATURAL JOIN class NATURAL JOIN course NATURAL JOIN courseinfo NATURAL JOIN teaching " +
                    "JOIN teacher ON teacher.teacherID = teaching.teacherID JOIN department ON course.departmentID = department.departmentID  " +
                    "WHERE studentID = ? AND classYear = ?";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql, study.getStudentId(), study.getClassYear());

            // 合并classID和classYear相同的classroomID
            List<Map<String, Object>> mergedResult = mergeClassroomIDs(queryResult);
            List<Map<String, Object>> deduplicatedResult = deduplicateResult(mergedResult);

            // 设置结果并返回
            res.setCode(200);
            res.setResult(deduplicatedResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); // 失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //去重
    private static List<Map<String, Object>> deduplicateResult(List<Map<String, Object>> mergedResult) {
        Set<Map<String, Object>> uniqueSet = new HashSet<>(mergedResult);
        return new ArrayList<>(uniqueSet);
    }
    //将classID和classYear相同的classroomID合并
    private List<Map<String, Object>> mergeClassroomIDs(List<Map<String, Object>> queryResult) {
        Map<String, List<String>> classIDMap = new HashMap<>();

        // 遍历查询结果，将classID和classYear相同的classroomID合并到列表中
        for (Map<String, Object> row : queryResult) {
            String classID = row.get("classID").toString();
            String classroomID = row.get("classroomID").toString();

            // 将classID和classroomID添加到对应的列表中
            if (classIDMap.containsKey(classID)) {
                List<String> classroomIDs = classIDMap.get(classID);
                classroomIDs.add(classroomID);
            } else {
                List<String> classroomIDs = new ArrayList<>();
                classroomIDs.add(classroomID);
                classIDMap.put(classID, classroomIDs);
            }
        }

        // 构建合并后的结果列表
        List<Map<String, Object>> mergedResult = new ArrayList<>();
        for (Map<String, Object> row : queryResult) {
            String classID = row.get("classID").toString();
            List<String> classroomIDs = classIDMap.get(classID);
            String mergedClassroomIDs = String.join(",", classroomIDs);

            Map<String, Object> mergedRow = new HashMap<>(row);
            mergedRow.put("classroomID", mergedClassroomIDs);
            mergedResult.add(mergedRow);
        }

        return mergedResult;
    }

    //12.自动得到学期
    //输出：年份+春/秋
    //网址：http://localhost:8080/getTime
    @PostMapping("/getTime")
    public static String getCurrentSemester() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();

        if (month >= 8) {
            return year + "秋";
        } else {
            return year + "春";
        }
    }

    //13.教师当前学期每周课表
    //输入：教师工号teacherId、周次，输出：courseName,classID,day,number,classroomID
    //网址：http://localhost:8080/weekTeacherCourse
    @PostMapping("/weekTeacherCourse")
    public Result weekTeacherCourse(@RequestBody Teacherwithdeparment teacher) {
        Result res = new Result();
        try {
            String sql = "SELECT courseName,classID,day,number,classroomID " +
                    "FROM teacher NATURAL JOIN teaching NATURAL JOIN class NATURAL JOIN courseinfo NATURAL JOIN course " +
                    "WHERE teacherID=? && week=? && classYear=?";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql, teacher.getTeacherId(),teacher.getWeek(),getCurrentSemester());
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    //新加端口：
    @PostMapping("/teacherClass")
    //网址：http://localhost:8080/teacherCourse
    public Result teacherqueryclass(@RequestBody Teacher teacher) {
        Result res = new Result();
        try {
            String sql = "SELECT distinct courseName,courseID,classID,classYear " +
                    "FROM teacher NATURAL JOIN teaching NATURAL JOIN class NATURAL JOIN  course " +
                    "WHERE teacherID=?";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql, teacher.getTeacherId());
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }

    @PostMapping("/teacherScoreAndName")
    //网址：http://localhost:8080/teacherScore
    public Result teacherScoreAndNam(@RequestBody Study study) {
        Result res = new Result();
        try {
            String sql = "SELECT studentID,grade,studentName FROM study NATURAL JOIN student WHERE classID=? && classYear=?";
            List<Map<String, Object>> queryResult = jdbc.queryForList(sql,study.getClassId(),study.getClassYear());
            res.setCode(200); //成功
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201); //失败
            res.setResult("出现异常" + e.getMessage());
            return res;
        }
    }
}
