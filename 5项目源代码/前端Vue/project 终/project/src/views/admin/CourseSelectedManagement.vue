<template>
  <div>
    <!--进度条-->
    <el-row>
      <el-col :span=24 :offset="2">
        <div>
          <el-steps :space="300" :active=activeStation finish-status="success">
            <el-step title="选择课程"></el-step>
            <el-step title="插入教学班"></el-step>
            <el-step title="选择教师"></el-step>
            <el-step title="选择授课时间"></el-step>
          </el-steps>
        </div>
      </el-col>
    </el-row>
    <!--开设教学班-->
    <el-tabs type="border-card" style="height: 600px;">
      <el-tab-pane label="设定教学班">
        <el-row :gutter="30">
          <br>
          <el-col :span=8>
            <label>学院选择: </label>
            <el-select id="major" v-model="selectedMajor" style="height:30px">
              <el-option v-for="major in majorList" :key="major.departmentId" :value="major.departmentId"
                :label="major.departmentName">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span=12><label>开课学期选择: </label>
            <el-select id="text" v-model="term" style="height:30px">
              <el-option label="大一上" value="1"></el-option>
              <el-option label="大一下" value="2"></el-option>
              <el-option label="大二上" value="3"></el-option>
              <el-option label="大二下" value="4"></el-option>
              <el-option label="大三上" value="5"></el-option>
              <el-option label="大三下" value="6"></el-option>
              <el-option label="大四上" value="7"></el-option>
              <el-option label="大四下" value="8"></el-option>
            </el-select>
            <el-button @click="handleClickthis()" type="primary" size="medium"
              style="position:relative;left:5px;">确认</el-button>
          </el-col>
        </el-row>
        <el-divider></el-divider>
        <el-row>
          <el-table :data="teacherList" max-height="400" border style="width: 100%" align="center">
            <el-table-column prop="teacherID" label="教师ID"></el-table-column>
            <el-table-column prop="teacherName" label="教师姓名"></el-table-column>
            <el-table-column prop="teacherIntroduction" label="教师简介"></el-table-column>
            <el-table-column prop="phoneNumber" label="教师电话"></el-table-column>
            <el-table-column label="选择教师">
              <template slot-scope="scope">
                <el-button @click="handleClick1(scope.row)" type="text" size="small">确认</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
      </el-tab-pane>
      <!--教学班排课-->
      <el-tab-pane label="排课时间">
        <el-row>
          <el-col :span="12">
            当前教学班号 {{ this.classList.classId ? `: ${this.classList.classId}` : ": 未选中" }}
          </el-col>
          <el-col>
            <el-select v-model="selectedClass" style="height: 30px">
              <el-option v-for="csl in classSelectList" :key="csl.classId" :value="csl.classId"
                :label="`${csl.courseName} ${csl.classId}`">
              </el-option>
            </el-select>
            <el-button @click="chooseClass" type="text" size="small" style="position:relative;left:5px;">选择教学班</el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span=10>
            周次选择:
            <el-select v-model="selectedNumber" style="height: 30px" @change="handleClick3">
              <el-option v-for="number in numbers" :key="number" :value="number" :label="number">
                {{ number }}
              </el-option>
            </el-select>
          </el-col>
          <el-col :span=10><label>教室选择：</label>
            <el-select v-model="selectedClassroom" style="height: 30px">
              <el-option v-for="item in classroomList" :key="item.classroomID" :value="item.classroomID"
                :label="item.classroomID">
                {{ item.classroomID }}
              </el-option>
            </el-select></el-col>
        </el-row>
        <el-row>
          <el-table :data="schedule" border row-class-name="" align="center">
            <el-table-column type="index" label="节次/日" width="120"></el-table-column>
            <el-table-column v-for="day in daysOfWeek" :key="day.id" :label="day.da">
              <template slot-scope="{ row }">
                <div :class="{ 'highlighted': isHighlighted(row, day.id) }" align="center">
                  <el-checkbox v-model="row[day.id]" :disabled="isHighlighted(row, day.id)"
                    @change="updateSchedule(row, day.id)" align="center">
                  </el-checkbox>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <br>
          <br>
          <br>
          <el-col :span="4">
            <el-button @click="handleClick2()" type="primary" size="medium">确认</el-button>
          </el-col>
          <el-col :span="12">
            <el-progress :percentage=percent :format="format"></el-progress>
          </el-col>
          <el-col :span="8">
            <el-button @click="handleClick3()" type="primary" size="medium">取消已选</el-button>
            <el-button @click="handleClick4()" type="primary" size="medium">查看冲突</el-button>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <div>
      <el-dialog title="创建教学班" :visible.sync="dialogFormVisible">
        <el-form>
          <el-form-item label="选择课程" :label-width="formLabelWidth">
            <el-select id="course" v-model="selectedCourse" placeholder="请选择课程">
              <el-option v-for="course in courseList" :key="course.courseId" :value="course.courseId"
                :label="course.courseName">
                {{ course.courseName }}
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班级人数" :label-width="formLabelWidth">
            <el-input v-model="people" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitClass()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Query',
  props: {
    msg: String
  },
  data() {
    return {
      activeStation: 0, //进度条
      term: "",        //课程学期
      //isdisabled: false,
      dialogFormVisible: false,
      classSelectList: [], //选择教学班
      majorList: [],
      classroomList: [],
      courseList: [],
      classReturn: [],
      teacherList: [],
      classList: [],
      selectedClass: "",
      selectedCourse: "",
      selectedClassroom: "",
      selectedMajor: "",
      selectedTeacher: "",
      //进度条的进度
      percent: 0,
      people: '',
      region: '',
      date1: '',
      date2: '',
      delivery: false,
      resource: '',
      desc: '',
      tid: "",  //教师编号
      formLabelWidth: '120px',
      type: [],
      tableData: [],
      multipleSelection: [],
      //以下是课表的组件
      schedule: [
        {
          index: 1
        },
        {
          index: 2
        },
        {
          index: 3
        },
        {
          index: 4
        },
        {
          index: 5
        }
      ], // 初始化课程表数据
      daysOfWeek: [{ da: 'Monday', id: 1 }, { da: 'Tuesday', id: 2 }, { da: 'Wednesday', id: 3 },
      { da: 'Thursday', id: 4 }, { da: 'Friday', id: 5 }, { da: 'Saturday', id: 6 }, { da: 'Sunday', id: 7 }],
      numOfTimeSlots: 5,
      selectedNumber: '',
      numbers: Array.from({ length: 20 }, (_, index) => index + 1),
      schList: [],
      //课表变色
      illList: []
    };
  },

  created() {
    // 在组件创建时调用fetchData方法，获取列表数据
    this.findmajors();
    this.queryClassroom();
    this.queryClass();
  },

  methods: {
    findmajors: function () {
      this.$axios.post("http://192.168.1.119:8080/registDepartment")
        .then((result) => {
          this.majorList = result.data.result;
        })
    },
    handleClickthis() {
      this.activeStation = 0;
      this.queryCourses();
      this.dialogFormVisible = true;
    },
    //插入课程
    submitClass() {
      let form1 = {
        courseId: this.selectedCourse, people: this.people
      }
      this.$axios
        .post("http://192.168.1.119:8080/insertAcademicClass", form1)
        .then((res) => {
          if (res.data.code * 1 == 200) {
            alert("成功");
            this.classList = res.data.result;
            this.dialogFormVisible = false;
            this.activeStation = 1;
            this.queryTeacher(this.selectedCourse);
          } else if (res.data.code * 1 == 201) {
            alert("出错");
          }
        })
    },
    handleClick1(row) {
      this.dialogVisible = true;
      this.tid = row.teacherID;
      this.insertTeaching();
    },
    //根据课程查找教师
    queryTeacher: function (cid) {
      let forms = null;
      forms = {
        courseId: cid, term: this.term
      }
      this.$axios
        .post("http://192.168.1.119:8080/queryTeachering", forms)
        .then((r) => {
          if (r.data.code * 1 == 200) {
            this.teacherList = r.data.result;
            this.activeStation = 2;
          } else {
            alert("出错");
          }
        })
    },
    format() {
      return this.percent === 100 ? '满' : `${this.percent}%`;//判断是否排课排满
    },
    queryCourses() {//查课
      let form3 = {
        departmentId: this.selectedMajor, term: this.term
      }
      this.$axios
        .post("http://192.168.1.119:8080/queryAcademicCourse", form3)
        .then((res) => {
          if (res.data.code * 1 == 200) {
            this.courseList = res.data.result;
          } else if (res.data.code * 1 == 201) {
            alert("出错");
          }
        })
    },
    //根据选择教师插入教学班
    insertTeaching: function () {
      let teachering = {
        teacherId: this.tid, classId: this.classList.classId, classYear: this.classList.classYear
      }
      this.$axios
        .post("http://192.168.1.119:8080/insertAcademicTeaching", teachering)
        .then((r) => {
          if (r.data.code * 1 == 200) {
            alert("成功");
            this.activeStation = 3; //进度条前进
            this.isdisabled = true;
            this.queryClassroom();
          } else if (r.data.code * 1 == 201) {
            alert("出错");
          }
        })
    },
    queryClassroom() {
      this.$axios
        .post("http://192.168.1.119:8080/queryAcademicClassroom")
        .then((res) => {
          this.classroomList = res.data.result;
        })
    },
    queryClass() {
      this.$axios
        .post("http://192.168.1.119:8080/queryAcdemicClass")
        .then((res) => {
          this.classSelectList = res.data.result;
        })
    },
    updateSchedule(rowId, colId) {
      const sch = {
        week: this.selectedNumber,
        day: colId,
        number: rowId.index,
        classYear: this.classList.classYear,
        classroomId: this.selectedClassroom,
        classId: this.classList.classId,
      };
      const index = this.schList.findIndex(item => (
        item.day === sch.day &&
        item.number === sch.number &&
        item.classYear === sch.classYear &&
        item.classroomId === sch.classroomId &&
        item.classId === sch.classId
      ));
      if (index !== -1) {
        this.schList.splice(index, 1);
      } else {
        this.schList.push(sch);
      }
    },
    handleClick2: async function () {
      let item = null;
      let scount = 0;  //多选成功数
      let failcount = 0  //多选失败数
      for (let i = 0; i < this.schList.length; i++) {   //对于选中课程依次读取
        item = this.schList[i]
        let res = await this.$axios.post("http://192.168.1.119:8080/insertAcademicCourseInfo", item)
        if (res.data.code * 1 == 200) {
          scount++;
          this.percent = res.data.result;
          this.handleClick4();
        } else if (res.data.code * 1 == 202) {
          failcount++;
          this.percent = res.data.result;
        }
        else {
          failcount++;
        }
      }
      if (failcount == 0 && scount != 0) {     //全部成功
        this.$message("插入成功！")
        this.schList = [];
        this.activeStation = 4;
      } else if (scount != 0) {
        this.$message("部分成功，请继续插入")
        this.schList = [];
        this.activeStation = 4;
      } else {
        this.$message("插入失败")
      }
      this.schList = [];
      this.activeStation = 4;
    },
    handleClick3: function () {
      this.schList = [];
      for (let i = 0; i < this.schedule.length; i++) {
        for (const day in this.schedule[i]) {
          if (typeof this.schedule[i][day] === 'boolean') {
            this.schedule[i][day] = false;
          }
        }
      }
    },
    // 是否变色，将周次天数结束与后端传来冲突节数一一比对
    isHighlighted(row, dayId) {
      let item = null;
      for (let i = 0; i < this.illList.length; i++) {
        item = this.illList[i];
        if (item.week === this.selectedNumber && item.day === dayId) {
          if (item.number === row.index) {
            return true;
          }
        }
      }
      return false;
    },
    // 变色查看
    handleClick4() {
      let pcourse = {
        classId: 1, courseId: this.classList.courseId,
        classYear: this.classList.classYear, classroomId: this.selectedClassroom
      }
      this.$axios.post("http://192.168.1.119:8080/queryAcademicIlligal", pcourse)
        .then((res) => {
          if (res.data.code * 1 == 200) {
            this.illList = res.data.result;
          }
          else {
            this.handleClick4()
          }
        })
    },
    chooseClass() {
      let item = null;
      for (let i = 0; i < this.classSelectList.length; i++) {
        item = this.classSelectList[i];
        if (item.classId == this.selectedClass) {
          this.classList = item;
          this.$message("选择成功！");
        }
      }
    }
  }
};
</script>
  
<style>
.highlighted {
  background-color: rgba(231, 78, 44, 0.817);
}

.el-row {
  margin-bottom: 20px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}
</style>
  