package com.system.controller;

import com.system.entity.*;
import com.system.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController//说明是返回数据的httpApi接口(SpringMvc中叫做控制器)
@CrossOrigin(origins = "*")//允许跨域
public class StudentController {
    @Autowired
    public JdbcTemplate jdbc;

    /**
     * 查看教务信息
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/viewAcademicInformation
     * POST请求
     */
    @PostMapping("/viewAcademicInformation")
    public Result viewAcademicInformation() {
        Result res = new Result();
        try {
            // 默认教务只有一个账号
            Academic ac = jdbc.queryForObject("select academicName, contactInformation, address from academic " +
                            "where academicID=10611001", new BeanPropertyRowMapper<>(Academic.class));
            res.setCode(200);
            res.setResult(ac);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 按照学生ID查询学生信息
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/viewPersonalInformation
     * POST请求
     * {
     *     "studentId": 20230001
     * }
     */
    @PostMapping("/viewPersonalInformation")
    public Result viewPersonalInformation(@RequestBody Student student) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from student natural join " +
                    "speciality natural join department where studentID=?", student.getStudentId());
            res.setCode(200);
            res.setResult(queryResult);
            // 一定登陆成功，必然注册过信息
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 查找导师信息
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/findMentorInformation
     * POST请求
     * {
     *     "studentId": 20230001
     * }
     */
    @PostMapping("/findMentorInformation")
    public Result findMentorInformation(@RequestBody Student student) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from teacher natural join " +
                    "(select studentID, teacherID from student where studentID=?) as a;", student.getStudentId());
            res.setCode(200);
            res.setResult(queryResult);
            // 没有导师返回空
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 按照学生ID修改学生信息（除主键、导师、头像和密码）
     * @param stu 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/modifyStudentInformation
     * POST请求
     * {
     *     "studentName": "李白",
     *     "studentGender": 1,
     *     "studentPhoneNumber": "13468871022",
     *     "studentEmail": "1472580369@qq.com",
     *     "studentId": 20030001
     * }
     */
    @PostMapping("/modifyStudentInformation")
    public Result modifyStudentInformation(@RequestBody Student stu) {
        Result res = new Result();
        try {
            jdbc.update("update student set studentName=?, studentGender=?, " +
                            "studentPhoneNumber=?, studentEmail=? where studentID=?", stu.getStudentName(),
                    stu.getStudentGender(), stu.getStudentPhoneNumber(), stu.getStudentEmail(), stu.getStudentId());
            Speciality speciality = jdbc.queryForObject("select specialityName from speciality natural " +
                            "join student where studentID=?;", new BeanPropertyRowMapper<>(Speciality.class),
                    stu.getStudentId());
            res.setCode(200);
            assert speciality != null;
            res.setResult(speciality.getSpecialityName());
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 教务修改学生信息
     * @param stu 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/reviseStudentInformation
     * POST请求：
     * {
     *     "specialityId": 1,
     *     "studentName": "李白",
     *     "studentGender": 1,
     *     "studentPhoneNumber": "13468871022",
     *     "studentEmail": "1472580369@qq.com",
     *     "studentId": 20210001,
     *     "studentPassword": 11111111
     * }
     */
    @PostMapping("/reviseStudentInformation")
    public Result reviseStudentInformation(@RequestBody Student stu) {
        Result res = new Result();
        try {
            jdbc.update("update student set specialityID=?, studentName=?, studentPassword=?, studentGender=?, " +
                            "studentPhoneNumber=?, studentEmail=? where studentID=?", stu.getSpecialityId(),
                    stu.getStudentName(), stu.getStudentPassword(), stu.getStudentGender(),
                    stu.getStudentPhoneNumber(), stu.getStudentEmail(), stu.getStudentId());
            res.setCode(200);
            res.setResult("修改成功");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 修改学生头像
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/modifyProfilePicture
     * POST请求
     * {
     *   "pictureId": 1,
     *   "studentId": 20210001
     * }
     */
    @PostMapping("/modifyProfilePicture")
    public Result modifyProfilePicture(@RequestBody Student student) {
        Result res = new Result();
        try {
            jdbc.update("update student set pictureID=? where studentID=?", student.getPictureId(),
                    student.getStudentId());
            res.setCode(200);
            res.setResult("修改头像成功");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 修改学生账号密码
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/changeAccountPassword
     * POST请求
     * {
     *     "studentId": 20230001,
     *     "studentPassword": 11111111
     * }
     */
    @PostMapping("/changeAccountPassword")
    public Result changeAccountPassword(@RequestBody Student student) {
        Result res = new Result();
        try {
            jdbc.update("update student set studentPassword=? where studentID=?", student.getStudentPassword(),
                    student.getStudentId());
            res.setCode(200);
            res.setResult("修改密码成功");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 提供学院列表
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/provideCollegeList
     * POST请求
     */
    @PostMapping("/provideCollegeList")
    public Result provideCollegeList() {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from department");
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 根据学院ID查询专业
     * @param department 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/provideCollegeMajors
     * POST请求
     * {
     *   "departmentId": 31
     * }
     */
    @PostMapping("/provideCollegeMajors")
    public Result provideCollegeMajors(@RequestBody Department department) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select specialityID, specialityName " +
                    "from speciality where departmentID=?", department.getDepartmentId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 查询所有学生
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/queryAllStudents
     * POST请求
     */
    @PostMapping("/queryAllStudents")
    public Result queryAllStudents(){
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from student natural " +
                    "join speciality natural join department");
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 查找学院所有学生
     * @param department 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/queryCollegeStudents
     * POST请求
     * {
     *   "departmentId": 31
     * }
     */
    @PostMapping("queryCollegeStudents")
    public Result queryCollegeStudents(@RequestBody Department department) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from department natural " +
                    "join speciality natural join student where departmentID=?", department.getDepartmentId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 查找专业所有学生
     * @param speciality 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/querySpecialityStudents
     * POST请求
     * {
     *     "specialityId": 1
     * }
     */
    @PostMapping("/querySpecialityStudents")
    public Result querySpecialityStudents(@RequestBody Speciality speciality) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from student natural " +
                    "join speciality natural join department where specialityID=?;", speciality.getSpecialityId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 学生所属专业学院教师信息
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/queryTeacherInformation
     * POST请求
     * {
     *     "studentId": "20220001"
     * }
     */
    @PostMapping("/queryTeacherInformation")
    public Result queryTeacherInformation(@RequestBody Student student) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from teacher natural " +
                    "join (select departmentID, departmentName from department natural join speciality " +
                    "natural join (select specialityID from student where studentID=?) as a) as b;",
                    student.getStudentId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 更新导师信息
     * @param student 请求体
     * @return res
     * 网址：192.168.1.119:8080/updateGuidanceInformation
     * POST请求
     * {
     *     "studentId": 20230001,
     *     "teacherId": 30000001
     * }
     */
    @PostMapping("/updateGuidanceInformation")
    public Result updateGuidanceInformation(@RequestBody Student student) {
        Result res = new Result();
        try {
            jdbc.update("update student set teacherID=? where studentID=?", student.getTeacherId(),
                    student.getStudentId());
            res.setCode(200);
            res.setResult("导师选择成功");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 根据学生ID计算当前学期
     * @param s 请求体
     * @return term
     */
    public int calculateTerm(double s) {
        // 得到整数位，也就是ID部分
        int integerPart = (int) s;
        // 得到前四位，代表入学年份
        int enrollmentYear = integerPart / 10000;
        // 获取当前年份
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        // 得到开设学龄，学期term
        int age = year - enrollmentYear;
        // 判断学期数
        switch (age){
            case 0:
                if(month < 8){
                    return 0;
                }
                else{ return 1; }
            case 1:
                if(month < 8){ return 2; }
                else{ return 3; }
            case 2:
                if(month < 8){ return 4; }
                else{ return 5; }
            case 3:
                if(month < 8){ return 6; }
                else{ return 7; }
            case 4:
                if(month < 8){ return 8; }
                else{
                    return 9;
                }
            default:
                return 0;
        }
    }

    /**
     * 根据学生查询课程安排
     * @param student 请求体
     * @return res 该学期学生可选的选课内容
     * 测试样例
     * 网址：192.168.1.119:8080/queryCourseSchedule
     * POST请求
     * {
     *     "studentId": 20210001
     * }
     */
    @PostMapping("/queryCourseSchedule")
    public Result queryCourseSchedule(@RequestBody Student student) {
        Result res = new Result();
        int term = calculateTerm(student.getStudentId());
        switch (term) {
            case 0:
                res.setCode(201);
                res.setResult("未入学");
                return res;
            case 9:
                res.setCode(201);
                res.setResult("已毕业");
                return res;
            default:
                break;
        }
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select distinct classYear, " +
                            "courseName, f.courseID, credit, IF(s.studentID IS NULL, '未选', '已选') " +
                            "AS selectionStatus from teaching natural join teacher natural join " +
                            "(select * from class natural join (select * from (select * from (select " +
                            "courseID, courseName, hour, credit, term from course where state=1) as b " +
                            "natural join (select specialityID, specialityName, courseID from speciality " +
                            "natural join belong) as a) as c natural join (select studentID, specialityID " +
                            "from student where studentID=?) as d ) as e) as f left join (select courseID, " +
                            "studentID from study natural join class natural join course where studentID=?) " +
                            "as s on f.courseID = s.courseID AND f.studentID = s.studentID where term=?",
                    student.getStudentId(), student.getStudentId(), term);
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 查询课程具体信息
     * @param c 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/querySpecificInformation
     * POST请求
     * {
     *   "courseId": 14,
     *   "classYear": "2023春"
     * }
     */
    @PostMapping("/querySpecificInformation")
    public Result querySpecificInformation(@RequestBody ClassWithCourse c) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select courseName, courseId, hour, " +
                    "classID, teacherName, selected, people from (select class.classYear, class.classID, " +
                    "coalesce(count(studentID), 0) as selected, people from class left join study on " +
                    "study.classYear = class.classYear AND study.classID = class.classID  where " +
                    "class.classYear=? group by classYear, classID) as a natural join class natural join " +
                    "course natural join teaching natural join teacher where state=1 && courseID=?;",
                    c.getClassYear(), c.getCourseId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 选择修读课程
     * @param study 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/selectCourse
     * POST请求
     * {
     *   "studentId": 20210001,
     *   "classId": 1,
     *   "classYear": "2023秋"
     * }
     */
    @PostMapping("/selectCourse")
    public Result selectCourse(@RequestBody Study study) {
        Result res = new Result();
        try {
            // 查找修读课程中的具体信息
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from (select week, day, " +
                    "number from study natural join class natural join courseinfo where studentID=?) as a " +
                    "natural join (select week, day, number from class natural join courseinfo where classID=? " +
                    "&& classYear=?) as b;", study.getStudentId(), study.getClassId(), study.getClassYear());
            if(queryResult.size() == 0) {
                // 没有冲突
                jdbc.update("insert into study values (?, ?, ?, null)", study.getClassId(),
                        study.getClassYear(), study.getStudentId());
                res.setCode(200);
                res.setResult("选课成功");
            }
            else {
                res.setCode(201);
                res.setResult("选课冲突");
            }
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 查询冲突结果
     * @param study 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/queryConflictResult
     * POST请求
     * {
     *   "studentId": 20210001
     *   "classId": 1,
     *   "classYear": "2023春"
     * }
     */
    @PostMapping("/queryConflictResult")
    public Result queryConflictResult(@RequestBody Study study) {
        Result res = new Result();
        try {
            // 查找修读课程中的具体信息
            List<Map<String, Object>> queryResult = jdbc.queryForList("select * from (select week, day, " +
                    "number from study natural join class natural join courseinfo where studentID=?) as a " +
                    "natural join (select week, day, number from class natural join courseinfo where classID=? " +
                    "&& classYear=?) as b;", study.getStudentId(), study.getClassId(), study.getClassYear());
            if(queryResult.size() == 0) {
                res.setCode(200);
            }
            else {
                res.setCode(201);
            }
            // 查找修读课程中的具体信息
            queryResult = jdbc.queryForList("select distinct day, number from " +
                            "class natural join courseinfo where classID=? && classYear=?;", study.getClassId(),
                    study.getClassYear());
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 查看已选课程
     * @param student 请求体
     * @return res 忽略周次，返回星期和节数（去重）
     * 测试样例
     * 网址：192.168.1.119:8080/viewSelectedCourses
     * POST请求
     * {
     *     "studentId": "20220001"
     * }
     */
    @PostMapping("/viewSelectedCourses")
    public Result viewSelectedCourses(@RequestBody Student student) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select distinct day, number from " +
                    "study natural join class natural join courseinfo where studentID=?;", student.getStudentId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 当前学期课表查询
     * @param s 请求体
     * @return
     * 测试样例
     * 网址：192.168.1.119:8080/queryCurriculum
     * POST请求
     * {
     *   "studentId": 20210001,
     *   "week": 1
     * }
     */
    @PostMapping("/queryCurriculum")
    public Result queryCurriculum(@RequestBody  StudentWithCourseinfo s) {
        Result res = new Result();
        // 自动计算学期数，选课时已经确定修读
        // 可能有多个老师，前端实现如何？
        int term = calculateTerm(s.getStudentId());
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select teacherName, day, number, " +
                    "classID, classroomID, courseName from study natural join class natural join " +
                    "courseinfo natural join course natural join (select teacherName, classYear, classID " +
                    "from teacher natural join teaching) as a where studentID=? && state=1 && term=? && week=?",
                    s.getStudentId(), term, s.getWeek());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 退课（提供已选课程）
     * @param study 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/dropCourse
     * POST请求
     * {
     *     "classId": 1,
     *     "classYear": "2022秋",
     *     "studentId": "20220003"
     * }
     */
    @PostMapping("/dropCourse")
    public Result dropCourse(@RequestBody Study study) {
        Result res = new Result();
        try {
            // 提供可选列表，得到参数后点击修改对应参数
            jdbc.update("delete from study where classID=? && classYear=? && studentID=?", study.getClassId(),
                    study.getClassYear(), study.getStudentId());
            res.setCode(200);
            res.setResult("退课成功");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 学生成绩查询
     * @param s 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/queryGrades
     * POST请求
     * {
     *   "studentId": 20210001,
     *   "term": 1
     * }
     */
    @PostMapping("/queryGrades")
    public Result queryGrades(@RequestBody StudentWithCourse s) {
        Result res = new Result();
        try {
            // 提供可选列表，得到参数后点击修改对应参数
            List<Map<String, Object>> queryResult = jdbc.queryForList("select courseName, grade from study " +
                            "natural join class natural join course where studentID=? && term=?;",
                    s.getStudentId(), s.getTerm());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 退学修改
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/dropoutAndSuspension
     * POST请求
     * {
     *   "studentId": 20210000
     * }
     */
    @PostMapping("/dropoutAndSuspension")
    public Result dropoutAndSuspension(@RequestBody Student student) {
        Result res = new Result();
        try {
            // 退学将其专业置于退学修学状态
            jdbc.update("update student set specialityID=0 where studentID=?;", student.getStudentId());
            res.setCode(200);
            res.setResult("已退学");
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 各学期绩点计算（五分制）
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/calculateGPA_5
     * POST请求
     * {
     *   "studentId": 20210001
     * }
     */
    @PostMapping("/calculateGPA_5")
    public Result calculateGPA_5(@RequestBody Student student) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select term, " +
                    "sum( credit * ( grade/ 10 - 5)) / sum(credit) as gpa from study natural join " +
                    "class natural join course where studentID=? and grade is not null group by term;",
                    student.getStudentId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 各学期绩点计算（四分制）
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/calculateGPA_4
     * POST请求
     * {
     *   "studentId": 20210001
     * }
     */
    @PostMapping("/calculateGPA_4")
    public Result calculateGPA_4(@RequestBody Student student) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select sum((case when grade>=90 then 4.0 " +
                            "when grade<50 then 0.0 else grade / 10 - 5 end) * course.credit) / sum(credit) as gpa " +
                            "from study natural join class natural join course where studentID=? and grade is not " +
                            "null group by term;", student.getStudentId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 总绩点计算（五分制）
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/calculateTotalGPA_5
     * POST请求
     * {
     *   "studentId": 20210001
     * }
     */
    @PostMapping("calculateTotalGPA_5")
    public Result calculateTotalGPA_5(@RequestBody Student student) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select " +
                    "sum(credit*(grade/10-5))/sum(credit) as gpa from study natural join class natural join course " +
                    "where studentID=? and grade is not null ", student.getStudentId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }

    /**
     * 总绩点计算（四分制）
     * @param student 请求体
     * @return res
     * 测试样例
     * 网址：192.168.1.119:8080/calculateTotalGPA_4
     * POST请求
     * {
     *   "studentId": 20210001
     * }
     */
    @PostMapping("calculateTotalGPA_4")
    public Result calculateTotalGPA_4(@RequestBody Student student) {
        Result res = new Result();
        try {
            List<Map<String, Object>> queryResult = jdbc.queryForList("select sum((case when grade>=90 " +
                    "then 4.0 when grade<50 then 0.0 else grade / 10 - 5 end) * course.credit) / sum(credit) " +
                    "as gpa from study natural join class natural join course where studentID=? and grade is not null;",
                    student.getStudentId());
            res.setCode(200);
            res.setResult(queryResult);
            return res;
        } catch (DataAccessException e) {
            e.printStackTrace();
            res.setCode(201);
            res.setResult(e.getMessage());
            return res;
        }
    }
}
