<template>
  <el-card class="main-card">
    <div><strong>学生选课 | 当前学期：</strong>{{ currentClassYear }}</div>
    <br>
    <!-- 可选课程 -->
    <el-table :data="courseBasicData" height="320" style="width: 60%" row-key="courseID" border>
      <el-table-column prop="courseName" label="课程名称">
      </el-table-column>
      <el-table-column prop="courseID" label="课程代码">
      </el-table-column>
      <el-table-column prop="credit" label="学分">
      </el-table-column>
      <el-table-column prop="selectionStatus" label="标记">
      </el-table-column>
      <el-table-column prop="operation" label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small"
            :disabled="scope.row.selectionStatus === '已选'">查看具体信息</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 教学班以及选课 -->
    <span><strong>课程具体信息 | </strong></span>
    <span><i class="el-icon-bell" style="color: rgb(85, 147, 199);"></i>请选择 <strong>{{ clickCourseName }}</strong> 的教学班
    </span>
    <span><el-button @click="clearView()" type="text" size="medium"><i
          class="el-icon-delete"></i>清空教学班课程时间预览</el-button></span>
    <el-table :data="courseAllData" height="230" style="width: 100%" row-key="classID" border>
      <el-table-column prop="courseName" label="课程名称">
      </el-table-column>
      <el-table-column prop="courseId" label="课程代码">
      </el-table-column>
      <el-table-column prop="hour" label="学时">
      </el-table-column>
      <el-table-column prop="classID" label="教学班号">
      </el-table-column>
      <el-table-column prop="teacherName" label="上课教师">
      </el-table-column>
      <el-table-column prop="capacity" label="已选/容量">
      </el-table-column>
      <el-table-column prop="operation" label="操作">
        <template slot-scope="scope">
          <el-button @click="viewTime(scope.row)" type="text" size="small">查看冲突情况</el-button>
          <el-button @click="selectClass(scope.row)" type="primary" size="small"
            :disabled="scope.row.selected == scope.row.people">选课</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 右侧课表 -->
    <el-card class="course-card">
      <div style="text-align: center;"><strong>选课预览</strong></div>
      <br>
      <el-table :data="myCourse" border height="288" style="width: 330px;">
        <el-table-column prop="courseNumber" label="  " width="40">
        </el-table-column>
        <el-table-column v-for="day in weekDays" :key="day.id" :label="day.name" width="40">
          <template slot-scope="{ row }">
            <div :class="highlightClass(row, day.id)">&nbsp;</div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </el-card>
</template>

<script>
export default {
  created() {
    // 获取本学期可选课程信息
    this.getCourses();
    // 获得初始课表情况
    this.getCourseTable();
  },

  data() {
    return {
      // 课程基础信息
      courseBasicData: [],
      // 课表初始信息
      initialCourseTable: [],
      // 该教学班课表信息
      thisCourseTable: [],
      // 课程冲突情况（200为无冲突）
      conflictResult: '200',
      // 查看某课程教学班
      clickCourseName: '未知课程',
      // 课程具体信息
      courseAllData: [],
      // 当前学期
      currentClassYear: '',
      // 课表表头
      weekDays: [
        { name: '一', id: 1 }, { name: '二', id: 2 }, { name: '三', id: 3 },
        { name: '四', id: 4 }, { name: '五', id: 5 }, { name: '六', id: 6 }, { name: '七', id: 7 }
      ],
      myCourse: [
        { courseNumber: '1' },
        { courseNumber: '2' },
        { courseNumber: '3' },
        { courseNumber: '4' },
        { courseNumber: '5' }
      ]
    };
  },

  methods: {
    // 获取本学期可选课程信息
    getCourses() {
      let getCoursesId = { studentId: sessionStorage.getItem("studentId") }
      this.$axios.post("http://192.168.1.119:8080/queryCourseSchedule", getCoursesId)
        .then((getCoursesRes) => {
          this.courseBasicData = getCoursesRes.data.result;
          this.currentClassYear = getCoursesRes.data.result[0].classYear;
        })
    },
    // 获得初始课表情况
    getCourseTable() {
      let getCourseTableId = { studentId: sessionStorage.getItem("studentId") }
      this.$axios.post("http://192.168.1.119:8080/viewSelectedCourses", getCourseTableId)
        .then((getCourseTableRes) => {
          this.initialCourseTable = getCourseTableRes.data.result;
        })
    },
    // 查看具体课程信息
    handleClick(row) {
      this.clearView();
      let getClassInfo = { classYear: this.currentClassYear, courseId: row.courseID }
      this.$axios.post("http://192.168.1.119:8080/querySpecificInformation", getClassInfo)
        .then((getClassInfoRes) => {
          if (getClassInfoRes.data.result[0] == null) {
            this.$message({
              showClose: true,
              message: '该课无教学班！',
              type: 'error'
            });
          }
          else {
            if (getClassInfoRes.data.code * 1 == 200) {
              this.clickCourseName = row.courseName;
              this.courseAllData = getClassInfoRes.data.result;
              for (let i = 0; i < this.courseAllData.length; i++) {
                this.$set(this.courseAllData[i], 'capacity', '');
                this.courseAllData[i].capacity = `${getClassInfoRes.data.result[i].selected}/${getClassInfoRes.data.result[i].people}`;
              }
            }
            else {
              this.$message({
                showClose: true,
                message: '错误！',
                type: 'error'
              });
            }
          }
        })
    },
    // 查看课程冲突情况
    viewTime(row) {
      // 判断冲突情况
      let classTimeInfo0 = { studentId: sessionStorage.getItem("studentId"), classId: row.classID, classYear: this.currentClassYear }
      this.$axios.post("http://192.168.1.119:8080/queryConflictResult", classTimeInfo0)
        .then((classTimeResp) => {
          this.thisCourseTable = classTimeResp.data.result;
          this.conflictResult = classTimeResp.data.code;
          if (classTimeResp.data.code * 1 == 200) {
            this.getCourses();
            this.$message({
              showClose: true,
              message: '无课程冲突！',
              type: 'success'
            });
          }
          else {
            this.$message({
              showClose: true,
              message: '课程冲突！请选择其他教学班！',
              type: 'error'
            });
          }
        })
    },
    // 选择课程教学班
    selectClass(row) {
      let selectClassInfo = { studentId: sessionStorage.getItem("studentId"), classId: row.classID, classYear: this.currentClassYear }
      this.$axios.post("http://192.168.1.119:8080/selectCourse", selectClassInfo)
        .then((selectClassRes) => {
          if (selectClassRes.data.code * 1 == 200) {
            this.getCourses();
            this.getCourseTable();
            this.courseAllData = [];
            this.thisCourseTable = [];
            this.conflictResult = '200';
            this.clickCourseName = '未知课程';
            this.$message({
              showClose: true,
              message: '选课成功！',
              type: 'success'
            });
          }
          else {
            this.$message({
              showClose: true,
              message: '选课失败！请检查课程冲突！',
              type: 'error'
            });
          }
        })
    },
    // 清空教学班选课预览
    clearView() {
      this.thisCourseTable = [];
      this.courseAllData = [];
      this.clickCourseName = '未知课程';
      this.getCourseTable();
    },
    // 判断单元格颜色
    highlightClass(row, dayIndex) {
      // 未查看课程冲突情况 / 该课程无教学班
      if (this.thisCourseTable[0] == null) {
        for (let i = 0; i < this.initialCourseTable.length; i++) {
          if (this.initialCourseTable[i].day === dayIndex && this.initialCourseTable[i].number == row.courseNumber) {
            return 'blue-cell';
          }
        }
        return '';
      }
      // 查看课程冲突
      else {
        // 无课程冲突（有课的变绿）
        if (this.conflictResult * 1 == 200) {
          // 先判断教学班：无冲突情况下可能出现课表时间重叠，优先显示教学班情况
          // 教学班有课，变绿
          for (let i = 0; i < this.thisCourseTable.length; i++) {
            if (this.thisCourseTable[i].day === dayIndex && this.thisCourseTable[i].number == row.courseNumber) {
              return 'green-cell';
            }
          }
          // 课表有课，变蓝
          for (let i = 0; i < this.initialCourseTable.length; i++) {
            if (this.initialCourseTable[i].day === dayIndex && this.initialCourseTable[i].number == row.courseNumber) {
              return 'blue-cell';
            }
          }
          return '';
        }
        // 有课程冲突（冲突的变红）
        else {
          for (let i = 0; i < this.initialCourseTable.length; i++) {
            // 课表有课，判断教学班有没有课
            if (this.initialCourseTable[i].day === dayIndex && this.initialCourseTable[i].number == row.courseNumber) {
              for (let j = 0; j < this.thisCourseTable.length; j++) {
                // 课程冲突，标记红色
                if (this.initialCourseTable[i].day === this.thisCourseTable[j].day &&
                  this.initialCourseTable[i].number === this.thisCourseTable[j].number) {
                  return 'red-cell';
                }
              }
              // 无课程冲突，标记课表有课
              return 'blue-cell';
            }
          }
          // 课表无课，若教学班中有该课，变绿
          for (let i = 0; i < this.thisCourseTable.length; i++) {
            if (this.thisCourseTable[i].day === dayIndex && this.thisCourseTable[i].number == row.courseNumber) {
              return 'green-cell';
            }
          }
          // 课表和教学班都无课
          return '';
        }
      }
    }
  }
};
</script>

<style>
.main-card {
  position: relative;
  height: 660px;
}

.selectTable {
  width: 500px;
}

.course-card {
  position: absolute;
  top: 30px;
  right: 20px;
  height: 370px;
  width: 361px;
}

/* 原课表有课 */
.blue-cell {
  background-color: rgb(212, 224, 233);
}

/* 课程冲突 */
.red-cell {
  background-color: rgb(244, 150, 131);
}

/* 教学班有课 */
.green-cell {
  background-color: rgb(88, 234, 117);
}
</style>
