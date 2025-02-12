# 学生端接口使用说明

## 说明
- `@param`代表参数，只需要传给后端POST请求内容（顺序无所谓，注意大小写）
- `@return`代表返回值，紧跟结果(需要修改或者结果有排序要求提出即可)
- 传回结果**仅供参考**，数据会**更新变化**
- 所有失败结果均返回`code: 201`和`对应错误信息`

## 查看教务信息
```java
@return res
{
	"code": 200,
	"result": {
		"academicId": 0,
		"academicPassword": null,
		"academicName": "教务处",
		"contactInformation": "023-65103029",
		"address": "沙坪坝区沙正街174号",
		"pictureId": 0
	}
}
测试样例
网址：192.168.1.119:8080/viewAcademicInformation
POST请求
```

## 按照学生ID查询学生信息
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"departmentID": 31,
			"specialityID": 1,
			"studentID": 20210001,
			"teacherID": 30000001,
			"studentPassword": "11111111",
			"studentName": "李白",
			"studentGender": true,
			"studentPhoneNumber": 13468871022,
			"studentEmail": "1472580369@qq.com",
			"pictureID": 4,
			"specialityName": "计算机科学与技术",
			"departmentName": "计算机学院"
		}
	]
}
测试样例
网址：192.168.1.119:8080/viewPersonalInformation
POST请求
{
    "studentId": 20210001
}
```

## 查找导师信息
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"teacherID": 30000001,
			"departmentID": 31,
			"teacherPassword": "11111111",
			"teacherName": "赵三",
			"teacherGender": true,
			"teacherIntroduction": "研究大数据技术",
			"phoneNumber": 11088888887,
			"teacherEmail": "jiao_code_02@qq.com",
			"pictureID": 5,
			"studentID": 20210001
		}
	]
}
测试样例
网址：192.168.1.119:8080/findMentorInformation
POST请求
{
    "studentId": 20230001
}
```

## 按照学生ID修改学生信息（除主键、导师、头像和密码）
```java
@param stu 请求体
@return res
{
	"code": 200,
	"result": "计算机科学与技术"
}
测试样例
网址：192.168.1.119:8080/modifyStudentInformation
POST请求
{
    "studentName": "李白",
    "studentGender": 1,
    "studentPhoneNumber": "13468871022",
    "studentEmail": "1472580369@qq.com",
    "studentId": 20210001
}
```

## 教务修改学生信息
```java
@param stu 请求体
@return res
{
	"code": 200,
	"result": "修改成功"
}
测试样例
网址：192.168.1.119:8080/reviseStudentInformation
POST请求：
{
    "specialityId": 1,
    "studentName": "李白",
    "studentGender": 1,
    "studentPhoneNumber": "13468871022",
    "studentEmail": "1472580369@qq.com",
    "studentId": 20210001,
    "studentPassword": 11111111
}
```

## 修改学生头像
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": "修改头像成功"
}
测试样例
网址：192.168.1.119:8080/modifyProfilePicture
POST请求
{
  "pictureId": 1,
  "studentId": 20210001
}
```

## 修改学生账号密码
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": "修改密码成功"
}
测试样例
网址：192.168.1.119:8080/changeAccountPassword
POST请求
{
    "studentId": 20210001,
    "studentPassword": 11111111
}
```

## 修改学生头像
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": "修改头像成功"
}
测试样例
网址：192.168.1.119:8080/modifyProfilePicture
POST请求
{
  "pictureId": 1,
  "studentId": 20210001
}
```

## 修改学生账号密码
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": "修改密码成功"
}
测试样例
网址：192.168.1.119:8080/changeAccountPassword
POST请求
{
    "studentId": 20210001,
    "studentPassword": 11111111
}
```

## 提供学院列表
```java
@return res
{
	"code": 200,
	"result": [
		{
			"departmentID": 1,
			"departmentName": "本科生院"
		},
		{
			"departmentID": 2,
			"departmentName": "公共管理学院"
		},
		{
			"departmentID": 3,
			"departmentName": "经济与工商管理学院"
		},
		{
			"departmentID": 4,
			"departmentName": "新闻学院"
		},
		{
			"departmentID": 5,
			"departmentName": "法学院"
		},
		{
			"departmentID": 6,
			"departmentName": "马克思主义学院"
		},
		{
			"departmentID": 7,
			"departmentName": "艺术学院"
		},
		{
			"departmentID": 8,
			"departmentName": "外国语学院"
		},
		{
			"departmentID": 9,
			"departmentName": "体育学院"
		},
		{
			"departmentID": 10,
			"departmentName": "美视电影学院"
		},
		{
			"departmentID": 11,
			"departmentName": "博雅学院"
		},
		{
			"departmentID": 12,
			"departmentName": "人文社会科学高等研究院"
		},
		{
			"departmentID": 13,
			"departmentName": "数学与统计学院"
		},
		{
			"departmentID": 14,
			"departmentName": "物理学院"
		},
		{
			"departmentID": 15,
			"departmentName": "化学化工学院"
		},
		{
			"departmentID": 16,
			"departmentName": "生命科学学院"
		},
		{
			"departmentID": 17,
			"departmentName": "机械工程学院"
		},
		{
			"departmentID": 18,
			"departmentName": "电气工程学院"
		},
		{
			"departmentID": 19,
			"departmentName": "能源与动力工程学院"
		},
		{
			"departmentID": 20,
			"departmentName": "资源与安全学院"
		},
		{
			"departmentID": 21,
			"departmentName": "材料科学与工程学院"
		},
		{
			"departmentID": 22,
			"departmentName": "航空航天学院"
		},
		{
			"departmentID": 23,
			"departmentName": "汽车工程学院"
		},
		{
			"departmentID": 24,
			"departmentName": "重庆大学-辛辛那提大学联合学院"
		},
		{
			"departmentID": 25,
			"departmentName": "建筑城规学院"
		},
		{
			"departmentID": 26,
			"departmentName": "土木工程学院"
		},
		{
			"departmentID": 27,
			"departmentName": "环境与生态学院"
		},
		{
			"departmentID": 28,
			"departmentName": "管理科学与房地产学院"
		},
		{
			"departmentID": 29,
			"departmentName": "光电工程学院"
		},
		{
			"departmentID": 30,
			"departmentName": "微电子与通信工程学院"
		},
		{
			"departmentID": 31,
			"departmentName": "计算机学院"
		},
		{
			"departmentID": 32,
			"departmentName": "自动化学院"
		},
		{
			"departmentID": 33,
			"departmentName": "大数据与软件学院"
		},
		{
			"departmentID": 34,
			"departmentName": "药学院"
		},
		{
			"departmentID": 35,
			"departmentName": "生物工程学院"
		}
	]
}
测试样例
网址：192.168.1.119:8080/provideCollegeList
POST请求
```

## 根据学院ID查询专业
```java
@param department 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"specialityID": 1,
			"specialityName": "计算机科学与技术"
		},
		{
			"specialityID": 2,
			"specialityName": "信息安全"
		},
		{
			"specialityID": 3,
			"specialityName": "物联网"
		}
	]
}
测试样例
网址：192.168.1.119:8080/provideCollegeMajors
POST请求
{
  "departmentId": 31
}
```

## 查询所有学生
```java
@return res
{
	"code": 200,
	"result": [
		{
			"departmentID": 31,
			"specialityID": 1,
			"studentID": 20210001,
			"teacherID": 30000001,
			"studentPassword": "11111111",
			"studentName": "李白",
			"studentGender": true,
			"studentPhoneNumber": 13468871022,
			"studentEmail": "1472580369@qq.com",
			"pictureID": 1,
			"specialityName": "计算机科学与技术",
			"departmentName": "计算机学院"
		},
		{
			"departmentID": 31,
			"specialityID": 2,
			"studentID": 20210002,
			"teacherID": null,
			"studentPassword": "87654321",
			"studentName": "小米",
			"studentGender": true,
			"studentPhoneNumber": 13899999999,
			"studentEmail": "xiaomi@qq.com",
			"pictureID": 0,
			"specialityName": "信息安全",
			"departmentName": "计算机学院"
		},
		{
			"departmentID": 31,
			"specialityID": 3,
			"studentID": 20210003,
			"teacherID": 30000005,
			"studentPassword": "12345678",
			"studentName": "微软",
			"studentGender": true,
			"studentPhoneNumber": 13612341234,
			"studentEmail": "microsoft@gmail.com",
			"pictureID": 0,
			"specialityName": "物联网",
			"departmentName": "计算机学院"
		},
		{
			"departmentID": 6,
			"specialityID": 4,
			"studentID": 20210004,
			"teacherID": 30000007,
			"studentPassword": "12345678",
			"studentName": "婵娟",
			"studentGender": false,
			"studentPhoneNumber": 18011112222,
			"studentEmail": "moonlight@gmail.com",
			"pictureID": 0,
			"specialityName": "马克思主义基本原理",
			"departmentName": "马克思主义学院"
		},
		{
			"departmentID": 6,
			"specialityID": 5,
			"studentID": 20210005,
			"teacherID": null,
			"studentPassword": "12345678",
			"studentName": "宇宙",
			"studentGender": true,
			"studentPhoneNumber": 16510011001,
			"studentEmail": "earthlove@qq.com",
			"pictureID": 0,
			"specialityName": "马克思主义发展史",
			"departmentName": "马克思主义学院"
		},
		{
			"departmentID": 6,
			"specialityID": 6,
			"studentID": 20210006,
			"teacherID": null,
			"studentPassword": "12345678",
			"studentName": "婉玉",
			"studentGender": false,
			"studentPhoneNumber": 13400001111,
			"studentEmail": "loveyou@gmail.com",
			"pictureID": 0,
			"specialityName": "思想政治教育",
			"departmentName": "马克思主义学院"
		},
		{
			"departmentID": 8,
			"specialityID": 7,
			"studentID": 20210007,
			"teacherID": null,
			"studentPassword": "12345678",
			"studentName": "余幕",
			"studentGender": true,
			"studentPhoneNumber": 13266669999,
			"studentEmail": "parislike@gmail.com",
			"pictureID": 0,
			"specialityName": "综合英语",
			"departmentName": "外国语学院"
		},
		{
			"departmentID": 8,
			"specialityID": 8,
			"studentID": 20210008,
			"teacherID": null,
			"studentPassword": "12345678",
			"studentName": "顾盼",
			"studentGender": true,
			"studentPhoneNumber": 13899775533,
			"studentEmail": "1472580369@gmail.com",
			"pictureID": 0,
			"specialityName": "英语视听",
			"departmentName": "外国语学院"
		},
		{
			"departmentID": 8,
			"specialityID": 9,
			"studentID": 20210009,
			"teacherID": 30000010,
			"studentPassword": "12345678",
			"studentName": "舒瑜",
			"studentGender": false,
			"studentPhoneNumber": 13206962233,
			"studentEmail": "translator@qq.com",
			"pictureID": 0,
			"specialityName": "翻译理论与实践",
			"departmentName": "外国语学院"
		},
		{
			"departmentID": 13,
			"specialityID": 10,
			"studentID": 20210010,
			"teacherID": null,
			"studentPassword": "12345678",
			"studentName": "宋璟",
			"studentGender": true,
			"studentPhoneNumber": 13567891234,
			"studentEmail": "dragonmath@qq.com",
			"pictureID": 0,
			"specialityName": "数学与应用数学",
			"departmentName": "数学与统计学院"
		},
		{
			"departmentID": 13,
			"specialityID": 10,
			"studentID": 20230001,
			"teacherID": null,
			"studentPassword": "jyf20021010",
			"studentName": "xiaojiao",
			"studentGender": true,
			"studentPhoneNumber": null,
			"studentEmail": "yfan.jiao@stu.cqu.edu.cn",
			"pictureID": 0,
			"specialityName": "数学与应用数学",
			"departmentName": "数学与统计学院"
		},
		{
			"departmentID": 13,
			"specialityID": 11,
			"studentID": 20210011,
			"teacherID": 30000004,
			"studentPassword": "12345678",
			"studentName": "XP",
			"studentGender": true,
			"studentPhoneNumber": 17932235445,
			"studentEmail": "calculate@qq.com",
			"pictureID": 0,
			"specialityName": "信息与计算科学",
			"departmentName": "数学与统计学院"
		},
		{
			"departmentID": 13,
			"specialityID": 12,
			"studentID": 20210012,
			"teacherID": 30000009,
			"studentPassword": "12345678",
			"studentName": "莫珩",
			"studentGender": true,
			"studentPhoneNumber": 15977880532,
			"studentEmail": "mathwork@qq.com",
			"pictureID": 0,
			"specialityName": "统计学",
			"departmentName": "数学与统计学院"
		}
	]
}
测试样例
网址：192.168.1.119:8080/queryAllStudents
POST请求
```

## 查找学院所有学生
```java
@param department 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"specialityID": 1,
			"departmentID": 31,
			"departmentName": "计算机学院",
			"specialityName": "计算机科学与技术",
			"studentID": 20210001,
			"teacherID": 30000001,
			"studentPassword": "11111111",
			"studentName": "李白",
			"studentGender": true,
			"studentPhoneNumber": 13468871022,
			"studentEmail": "1472580369@qq.com",
			"pictureID": 1
		},
		{
			"specialityID": 1,
			"departmentID": 31,
			"departmentName": "计算机学院",
			"specialityName": "计算机科学与技术",
			"studentID": 20210010,
			"teacherID": null,
			"studentPassword": "12345678",
			"studentName": "宋璟",
			"studentGender": true,
			"studentPhoneNumber": 13567891234,
			"studentEmail": "dragonmath@qq.com",
			"pictureID": 0
		},
		{
			"specialityID": 2,
			"departmentID": 31,
			"departmentName": "计算机学院",
			"specialityName": "信息安全",
			"studentID": 20210002,
			"teacherID": null,
			"studentPassword": "87654321",
			"studentName": "小米",
			"studentGender": true,
			"studentPhoneNumber": 13899999999,
			"studentEmail": "xiaomi@qq.com",
			"pictureID": 0
		},
		{
			"specialityID": 3,
			"departmentID": 31,
			"departmentName": "计算机学院",
			"specialityName": "物联网",
			"studentID": 20210003,
			"teacherID": 30000005,
			"studentPassword": "12345678",
			"studentName": "微软",
			"studentGender": true,
			"studentPhoneNumber": 13612341234,
			"studentEmail": "microsoft@gmail.com",
			"pictureID": 0
		}
	]
}
测试样例
网址：192.168.1.119:8080/queryCollegeStudents
POST请求
{
  "departmentId": 31
}
```

## 查找专业所有学生
```java
@param speciality 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"departmentID": 31,
			"specialityID": 1,
			"studentID": 20210001,
			"teacherID": 30000001,
			"studentPassword": "11111111",
			"studentName": "李白",
			"studentGender": true,
			"studentPhoneNumber": 13468871022,
			"studentEmail": "1472580369@qq.com",
			"pictureID": 1,
			"specialityName": "计算机科学与技术",
			"departmentName": "计算机学院"
		},
		{
			"departmentID": 31,
			"specialityID": 1,
			"studentID": 20210010,
			"teacherID": null,
			"studentPassword": "12345678",
			"studentName": "宋璟",
			"studentGender": true,
			"studentPhoneNumber": 13567891234,
			"studentEmail": "dragonmath@qq.com",
			"pictureID": 0,
			"specialityName": "计算机科学与技术",
			"departmentName": "计算机学院"
		}
	]
}
测试样例
网址：192.168.1.119:8080/querySpecialityStudents
POST请求
{
    "specialityId": 1
}
```

## 学生所属专业学院教师信息
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"departmentID": 31,
			"teacherID": 30000002,
			"teacherPassword": "12345678",
			"teacherName": "刘能",
			"teacherGender": true,
			"teacherIntroduction": "研究量子计算机",
			"phoneNumber": 13430003000,
			"teacherEmail": "1744736200@qq.com",
			"pictureID": 0,
			"departmentName": "计算机学院"
		},
		{
			"departmentID": 31,
			"teacherID": 30000005,
			"teacherPassword": "12345678",
			"teacherName": "哈喽",
			"teacherGender": true,
			"teacherIntroduction": "研究量子信息",
			"phoneNumber": 13322332234,
			"teacherEmail": "111@qq.com",
			"pictureID": 0,
			"departmentName": "计算机学院"
		}
	]
}
测试样例
网址：192.168.1.119:8080/queryTeacherInformation
POST请求
{
    "studentId": "20210001"
}
```

## 更新导师信息
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": "导师选择成功"
}
网址：192.168.1.119:8080/updateGuidanceInformation
POST请求
{
    "studentId": 20210001,
    "teacherId": 30000001
}
```

## 根据学生ID计算当前学期（类内使用）
```java
@param s 请求体
@return term
提取学号前四位和当前年月进行计算，涉及当前学期
```

## 根据学生查询课程安排
```java
@param student 请求体
@return res 该学期学生可选的选课内容
{
	"code": 200,
	"result": [
		{
			"classYear": "2023春",
			"courseName": "大学英语扩展",
			"courseID": 14,
			"credit": 2,
			"selectionStatus": "已选"
		},
		{
			"classYear": "2023春",
			"courseName": "计算机组成原理",
			"courseID": 15,
			"credit": 3,
			"selectionStatus": "已选"
		},
		{
			"classYear": "2023春",
			"courseName": "数学模型",
			"courseID": 16,
			"credit": 2,
			"selectionStatus": "未选"
		},
		{
			"classYear": "2023春",
			"courseName": "习近平思想",
			"courseID": 17,
			"credit": 3,
			"selectionStatus": "未选"
		},
		{
			"classYear": "2023春",
			"courseName": "机器学习",
			"courseID": 18,
			"credit": 3,
			"selectionStatus": "已选"
		}
	]
}
测试样例
网址：192.168.1.119:8080/queryCourseSchedule
POST请求
{
    "studentId": 20210001
}
```

## 查询课程具体信息
```java
@param c 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"courseName": "计算机组成原理",
			"courseId": 15,
			"hour": 45,
			"classID": 2,
			"teacherName": "哈喽",
			"selected": 0,
			"people": 40
		}
	]
}
测试样例
网址：192.168.1.119:8080/querySpecificInformation
POST请求
{
  "courseId": 15,
  "classYear": "2023春"
}
```

 ## 选择修读课程
```java
@param study 请求体
@return res
{
	"code": 200,
	"result": "选课成功"
}
测试样例
网址：192.168.1.119:8080/selectCourse
POST请求
{
  "studentId": 20210001,
  "classId": 1,
  "classYear": "2023秋"
}
```

## 查询冲突结果
```java
@param study 请求体
@return res
{
	"code": 201,
	"result": [
		{
			"day": 1,
			"number": 1
		},
		{
			"day": 4,
			"number": 3
		}
	]
}
测试样例
网址：192.168.1.119:8080/queryConflictResult
POST请求
{
  "studentId": 20210001,
  "classId": 1,
  "classYear": "2023春"
}
```

## 查看已选课程
```java
@param student 请求体
@return res 忽略周次，返回星期和节数（去重）
{
	"code": 200,
	"result": [
		{
			"day": 1,
			"number": 1
		},
		{
			"day": 3,
			"number": 1
		},
		{
			"day": 5,
			"number": 1
		},
		{
			"day": 1,
			"number": 2
		},
		{
			"day": 5,
			"number": 2
		},
		{
			"day": 2,
			"number": 1
		},
		{
			"day": 1,
			"number": 3
		},
		{
			"day": 4,
			"number": 3
		},
		{
			"day": 5,
			"number": 5
		}
	]
}
测试样例
网址：192.168.1.119:8080/viewSelectedCourses
POST请求
{
    "studentId": "20210001"
}
```

## 当前学期课表查询
```java
@param s 请求体
@return
{
	"code": 200,
	"result": [
		{
			"teacherName": "刘能",
			"day": 5,
			"number": 5,
			"classID": 5,
			"classroomID": "D1201",
			"courseName": "机器学习"
		},
		{
			"teacherName": "说米",
			"day": 4,
			"number": 3,
			"classID": 4,
			"classroomID": "D1102",
			"courseName": "习近平思想"
		},
		{
			"teacherName": "哈喽",
			"day": 2,
			"number": 1,
			"classID": 2,
			"classroomID": "D1101",
			"courseName": "计算机组成原理"
		},
		{
			"teacherName": "小好",
			"day": 3,
			"number": 1,
			"classID": 3,
			"classroomID": "D1101",
			"courseName": "数学模型"
		},
		{
			"teacherName": "嗷呜",
			"day": 1,
			"number": 1,
			"classID": 1,
			"classroomID": "D1101",
			"courseName": "大学英语扩展"
		}
	]
}
测试样例
网址：192.168.1.119:8080/queryCurriculum
POST请求
{
  "studentId": 20210001,
  "week": 1
}
```

## 退课（提供已选课程）
```java
@param study 请求体
@return res
{
	"code": 200,
	"result": "退课成功"
}
测试样例
网址：192.168.1.119:8080/dropCourse
POST请求
{
    "classId": 1,
    "classYear": "2021秋",
    "studentId": "20210003"
}
```

## 学生成绩查询
```java
@param s 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"courseName": "高等数学上",
			"grade": 95
		},
		{
			"courseName": "近现代史",
			"grade": 85
		}
	]
}
测试样例
网址：192.168.1.119:8080/queryGrades
POST请求
{
  "studentId": 20210001,
  "term": 1
}
```

## 退学修改
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": "已退学"
}
测试样例
网址：192.168.1.119:8080/dropoutAndSuspension
POST请求
{
  "studentId": 20210000	
}
```

## 各学期绩点计算（五分制）
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"term": 1,
			"gpa": 4.1
		},
		{
			"term": 4,
			"gpa": 4.261538461538462
		}
	]
}
测试样例
网址：192.168.1.119:8080/calculateGPA_5
POST请求
{
  "studentId": 20210001
}
```

## 各学期绩点计算（四分制）
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"gpa": 3.8
		},
		{
			"gpa": 4
		}
	]
}
测试样例
网址：192.168.1.119:8080/calculateGPA_4
POST请求
{
  "studentId": 20210001
}
```

## 总绩点计算（五分制）
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"gpa": 4.216666666666667
		}
	]
}
测试样例
网址：192.168.1.119:8080/calculateTotalGPA_5
POST请求
{
  "studentId": 20210001
}
```

## 总绩点计算（四分制）
```java
@param student 请求体
@return res
{
	"code": 200,
	"result": [
		{
			"gpa": 3.857142857142857
		}
	]
}
测试样例
网址：192.168.1.119:8080/calculateTotalGPA_4
POST请求
{
  "studentId": 20210001
}
```