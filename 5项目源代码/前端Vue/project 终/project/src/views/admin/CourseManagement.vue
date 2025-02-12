<template>
  <el-card class="box-card">
    <div>
      <el-row>
        <el-col :span="8">
          <label>学院选择: </label>
        </el-col>
        <el-col :span="8">
          <label>学年选择: </label>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-select id="major" v-model="selectedMajor" style="height:30px">
            <el-option v-for="major in majorList" :key="major.departmentId" :value="major.departmentId"
              :label="major.departmentName">
            </el-option>
          </el-select></el-col>
        <el-col :span="8">
          <el-tooltip class="item" effect="dark" content="输入示例：2" placement="bottom-start">
            <el-input type="text" v-model.trim="year"></el-input>
          </el-tooltip>
        </el-col>
      </el-row>
      <div style="align">
        <el-row>
          <el-col :span="12"><el-button @click="queryclass" type="primary">查课确认</el-button></el-col>
          <el-col :span="12"><el-button @click="handle()" type="primary">插入课程</el-button></el-col>
        </el-row>
      </div>
      <br>
      <br>
      <!--表格部分-->
      <el-table :data="tableData" height="400" border style="width: 100%" :cell-style="{ 'text-align': 'center' }">
        <el-table-column prop="courseId" label="课程编号" align="center">
        </el-table-column>
        <el-table-column prop="departmentId" label="学院编号" align="center">
        </el-table-column>
        <el-table-column prop="courseName" label="课程名" align="center">
        </el-table-column>
        <el-table-column prop="hour" label="学时" align="center">
        </el-table-column>
        <el-table-column prop="credit" label="学分" align="center">
        </el-table-column>
        <el-table-column prop="term" label="学期" align="center">
        </el-table-column>
        <el-table-column prop="state" label="开放状态" align="center">
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">修改课程</el-button>
            <el-button @click="handleClick1(scope.row)" type="text" size="small">添加开设专业</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--插入开设专业-->
      <div>
        <el-dialog :visible.sync="dialogVisible2">
          <el-form>
            <el-form-item :data="speList" label="开课专业">
              <template>
                <el-select v-model="selectedSpe" style="height: 30px;">
                  <el-option v-for="spe in speList" :key="spe.specialityId" :label="spe.specialityName"
                    :value="spe.specialityId"></el-option>
                </el-select>
              </template>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="uploadBelong1" type="primary">确认提交</el-button>
          </div>
        </el-dialog>
        <!--修改课程-->
        <el-dialog :visible.sync="dialogVisible">
          <el-form>
            <el-form-item label="学时">
              <el-input v-model="hour" type="text"></el-input>
            </el-form-item>
            <el-form-item label="学分">
              <el-input v-model="credit" type="text"></el-input>
            </el-form-item>
            <el-form-item label="学期">
              <el-select v-model="term">
                <el-option label="大一上" value="1"></el-option>
                <el-option label="大一下" value="2"></el-option>
                <el-option label="大二上" value="3"></el-option>
                <el-option label="大二下" value="4"></el-option>
                <el-option label="大三上" value="5"></el-option>
                <el-option label="大三下" value="6"></el-option>
                <el-option label="大四上" value="7"></el-option>
                <el-option label="大四下" value="8"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="开课状态">
              <el-select v-model="state">
                <el-option label="0" value="0"></el-option>
                <el-option label="1" value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="submitForm" type="primary">确认提交</el-button>
          </div>
        </el-dialog>
        <!--插入新课-->
        <el-dialog :visible.sync="dialogVisible1">
          <el-form>
            <el-form-item :data="majorList" label="开设专业">
              <template>
                <el-select v-model="selectedMajor" style="height: 30px;">
                  <el-option v-for="major in majorList" :key="major.departmentId" :label="major.departmentName"
                    :value="major.departmentId"></el-option>
                </el-select>
              </template>
            </el-form-item>
            <el-form-item :data="speList" label="开课专业">
              <template>
                <el-select v-model="selectedSpe" style="height: 30px;">
                  <el-option v-for="spe in speList" :key="spe.specialityId" :label="spe.specialityName"
                    :value="spe.specialityId"></el-option>
                </el-select>
              </template>
            </el-form-item>
            <el-form-item label="学期">
              <el-select v-model="term">
                <el-option label="大一上" value="1"></el-option>
                <el-option label="大一下" value="2"></el-option>
                <el-option label="大二上" value="3"></el-option>
                <el-option label="大二下" value="4"></el-option>
                <el-option label="大三上" value="5"></el-option>
                <el-option label="大三下" value="6"></el-option>
                <el-option label="大四上" value="7"></el-option>
                <el-option label="大四下" value="8"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="开课状态">
              <el-select v-model="state">
                <el-option label="0" value="0"></el-option>
                <el-option label="1" value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="课程名">
              <el-input v-model="courseName" type="text"></el-input>
            </el-form-item>
            <el-form-item label="学时">
              <el-input v-model="hour" type="text"></el-input>
            </el-form-item>
            <el-form-item label="学分">
              <el-input v-model="credit" type="text"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="submitNewForm" type="primary">确认提交</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'Query',
  props: {
    msg: String
  },

  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogVisible1: false,
      dialogVisible2: false,
      courseId: "",
      departmentId: "",
      courseName: "",
      hour: "",
      credit: "",
      term: "",
      state: "",
      store: "",
      selectedMajor: "",
      selectedSpe: "",
      year: "",
      coid: "",
      cid: [],
      majorList: [],
      speList: []
    }
  },

  created() {
    // 在组件创建时调用fetchData方法，获取列表数据
    this.findmajors();
  },

  methods: {
    // 查询学院信息+专业信息
    findmajors: function () {
      this.$axios.post("http://192.168.1.119:8080/registDepartment")
        .then((result) => {
          this.majorList = result.data.result;
        })
      this.$axios.post("http://192.168.1.119:8080/registSpeciality")
        .then((result) => {
          this.speList = result.data.result;
        })
    },
    // 请求查询课程信息
    queryclass: function () {
      localStorage.setItem('major.departmentId', this.major);
      localStorage.setItem('year', this.year);
      let form = {
        departmentId: this.selectedMajor, term: this.year
      }
      this.$axios
        .post("http://192.168.1.119:8080/queryAcademicCourse", form)
        .then((res) => {
          if (res.data.code * 1 == 200) {
            this.tableData = res.data.result
          } else if (res.data.code * 1 == 201) {
            alert("出错");
          }
        })
    },
    // 修改课程，赋值
    handleClick(row) {
      this.dialogVisible = true;
      this.courseId = row.courseId;
      this.departmentId = row.departmentId;
      this.courseName = row.courseName;
    },
    // 更改修改课程信息窗口可见状态
    handle: function () {
      this.dialogVisible1 = true;
    },
    // 修改课程提交
    submitForm: function () {
      let form = {
        courseId: this.courseId, departmentId: this.departmentId, courseName: this.courseName,
        hour: this.hour, credit: this.credit, term: this.term, state: this.state
      }
      this.$axios
        .post("http://192.168.1.119:8080/insertAcademicCourse", form)
        .then((res) => {
          if (res.data.code * 1 == 200) {
            alert("成功");
            this.dialogVisible = false;
          } else if (res.data.code * 1 == 201) {
            alert("出错");
          }
        })
    },
    // 插入新课
    submitNewForm: function () {
      let form = {
        courseId: 0, departmentId: this.selectedMajor, courseName: this.courseName,
        hour: this.hour, credit: this.credit, term: this.term, state: this.state
      }
      this.$axios
        .post("http://192.168.1.119:8080/insertAcademicCourse", form)
        .then((res) => {
          if (res.data.code * 1 == 200) {
            this.uploadBelong(res.data.result);
          } else if (res.data.code * 1 == 201) {
            alert("出错");
          }
        })
    },
    // 增加开设课程专业（插入时）
    uploadBelong: function (cid) {
      let form1 = null;
      form1 = {
        specialityId: this.selectedSpe, courseId: cid //区别在使用不同存储变量
      }
      this.$axios
        .post("http://192.168.1.119:8080/insertAcademicBelong", form1)
        .then((rs) => {
          if (rs.data.code * 1 == 200) {
            alert("成功");
            this.dialogVisible1 = false;
          } else {
            alert("出错");
          }
        })
    },
    // 增加课程开设专业（修改已有）
    uploadBelong1: function () {
      let form1 = null;
      form1 = {
        specialityId: this.selectedSpe, courseId: this.courseId
      }
      this.$axios
        .post("http://192.168.1.119:8080/insertAcademicBelong", form1)
        .then((rs) => {
          if (rs.data.code * 1 == 200) {
            alert("成功");
            this.dialogVisible1 = false;
          } else {
            alert("出错");
          }
        })
    },
    // 修改可见状态
    handleClick1(row) {
      this.dialogVisible2 = true;
      this.courseId = row.courseId;
    }
  },
};
</script>
  
<style>
.align {
  display: flex;
  justify-content: flex-end;
}

.el-select {
  position: relative;
  left: 2px;
}
</style>
  