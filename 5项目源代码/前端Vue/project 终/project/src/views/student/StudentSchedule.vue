<template>
  <div>
    <h1 class="term">当前学期：{{ term }}</h1>
    <!-- 下拉选择框 -->
    <div class="el-dropdown-link">
      <el-select v-model="selectedWeek" placeholder="请选择周次">
        <el-option v-for="week in weeks" :key="week" :value="week">{{
          week
        }}</el-option>
      </el-select>
      <!-- 查询按钮 -->
      <el-button id="search" type="primary" icon="el-icon-search"
        style="margin-top: 5px; margin-right: -210px; position: inherit" @click="searchCourse">
        查询
      </el-button>
    </div>
    <!-- 学生课表整体 -->
    <div class="studentCourse">
      <!-- 星期表头 -->
      <div class="table_head">
        <div class="day">周一</div>
        <div class="day">周二</div>
        <div class="day">周三</div>
        <div class="day">周四</div>
        <div class="day">周五</div>
        <div class="day">周六</div>
        <div class="day">周日</div>
      </div>
      <div class="table_main">
        <!-- 节数表头 -->
        <div class="table_aside">
          <div class="time">1</div>
          <div class="time">2</div>
          <div class="time">3</div>
          <div class="time">4</div>
          <div class="time">5</div>
        </div>
        <div class="table_content">
          <!-- 循环星期得列 -->
          <div v-for="day in daysOfWeek" :key="day" class="class_column">
            <!-- 循环节数得行 -->
            <div v-for="number in numbers" :key="number" class="class_row">
              <div v-for="course in courseData" :key="course.index">
                <div v-if="
                  // 判断此课程此时段是否有课
                  hasCourse(day, number) &&
                  course.day == day &&
                  course.number == number &&
                  hasCourseFlag" class="course_info" @click="showCourseDetails(course)">
                  <h3 style="font-size: 18px; margin-top: 7px">
                    {{ course.courseName }}
                  </h3>
                  <div class="smallInfo">
                    <p>任课教师：{{ course.teacherName }}</p>
                    <p>上课教室：{{ course.classroomID }}</p>
                  </div>
                </div>
                <!-- 弹出对话框显示课程详情 -->
                <el-dialog :visible.sync="dialogVisible" title="课程详情" @close="closeDialog" class="centered-dialog">
                  <hr />
                  <h3>{{ selectedCourse.courseName }}</h3>
                  <p>
                    上课时段：周{{ selectedCourse.day }}第{{ selectedCourse.number }}节
                  </p>
                  <p>任课教师：{{ selectedCourse.teacherName }}</p>
                  <p>班级编号：{{ selectedCourse.classID }}</p>
                  <p>
                    上课时间：{{
                      getCourseTime(selectedCourse.number).startTime
                    }}~{{ getCourseTime(selectedCourse.number).endTime }}
                  </p>
                  <p>上课周次：第{{ selectedWeek }}周</p>
                  <p>上课教室：{{ selectedCourse.classroomID }}</p>
                </el-dialog>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 每个节数对应的上课时间
const courseTimeMap = {
  1: { startTime: "8:30", endTime: "10:10" },
  2: { startTime: "10:30", endTime: "12:10" },
  3: { startTime: "14:25", endTime: "16:05" },
  4: { startTime: "16:25", endTime: "18:05" },
  5: { startTime: "19:00", endTime: "20:40" },
};
export default {
  data() {
    return {
      dialogVisible: false, //是否弹出对话框
      selectedCourse: "", //选择要弹出对话框的课程
      term: "", //当前学期
      selectedWeek: "", // 选择的周次
      hasCourseFlag: true, //某周次有没有课的标志
      weeks: [
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
      ], // 周次列表
      daysOfWeek: [1, 2, 3, 4, 5, 6, 7], //星期列表
      numbers: [1, 2, 3, 4, 5], // 节数列表
      courseData: [], //返回的所选周次的所有课程
    };
  },

  created() {
    // 获取当前学期
    this.getTime();
  },

  methods: {
    getTime() {
      let form = {};
      this.$axios
        .post("http://192.168.1.119:8080/getTime", form)
        .then((resp) => {
          this.term = resp.data;
        });
    },
    // 根据课程节数得到上课时间
    getCourseTime: function (number) {
      if (number in courseTimeMap) {
        return courseTimeMap[number];
      } else {
        return { startTime: "", endTime: "" };
      }
    },
    // 弹出对话框
    showCourseDetails(course) {
      this.selectedCourse = course;
      this.dialogVisible = true;
    },
    // 关闭对话框
    closeDialog() {
      this.dialogVisible = false;
    },
    // 判断该时间段是否有课程
    hasCourse(day, number) {
      return this.courseData.some(
        (course) => course.day === day && course.number === number
      );
    },
    // 查询所选周次的课程
    searchCourse() {
      let form = {
        // 从sessionStorage中取出studentId
        studentId: sessionStorage.getItem("studentId"),
        week: this.selectedWeek,
      };
      this.$axios
        .post("http://192.168.1.119:8080/queryCurriculum", form)
        .then((resp) => {
          if (resp.data.code * 1 == 200) {
            if (resp.data.result == "") {
              // 返回课程为空
              this.hasCourseFlag = false;
              alert("此周次无课");
            } else {
              this.courseData = resp.data.result;
              alert("查询成功");
            }
          } else if (resp.data.code * 1 == 201) {
            alert("查询失败");
          }
        });
    },
  },
};
</script>

<style scoped>
/* 选择周次下拉菜单 */
.el-dropdown-link {
  position: absolute;
  margin-top: -38px;
  margin-left: 2cqmin;
  cursor: pointer;
  color: #409eff;
  width: 140px;
}

/* 当前学期标题 */
.term {
  margin-left: 520px;
  float: top;
  margin-top: -13px;
  color: grey;
}

/* 整个课表 */
.studentCourse {
  margin-top: 70px;
}

/* 星期表头 */
.table_head {
  padding: 15px;
  background-color: #fff;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
  display: flex;
  margin-left: 60px;
  margin-top: 30px;
}

/* 星期 */
.day {
  width: 40%;
  text-align: center;
  font-size: 20px;
}

/* 课表主体部分 */
.table_main {
  height: 835px;
  display: flex;
}

/* 节数表头 */
.table_aside {
  width: 40px;
  height: 815px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  padding: 10px;
  margin-right: 20px;
}

/* 节数 */
.time {
  margin-top: 15px;
  line-height: 150px;
  text-align: center;
  font-size: 1.5rem;
}

/* 大边框 */
.table_content {
  background-color: #fff;
  border-radius: 10px;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  display: flex;
  flex-wrap: nowrap;
  width: 100%;
}

/* 每列 */
.class_column {
  flex: 0 0 14.3%;
  height: 166.5px;
  width: 14.5%;
  padding: 2px;
  box-sizing: border-box;
}

/* 每行 */
.class_row {
  height: 100%;
  border-radius: 10px;
  padding: 18px;
  box-sizing: border-box;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}

/* 课程信息 */
.course_info {
  width: 120px;
  word-wrap: break-word;
  overflow: hidden;
  white-space: wrap;
  text-overflow: ellipsis;
  color: grey;
  font-size: 18px;
  font-weight: bold;
  min-height: 65px;
}

.smallInfo {
  margin-top: 7px;
  padding: 0px;
  font-size: 12px;
  font-weight: normal;
}

/* 对话框 */
.centered-dialog {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>