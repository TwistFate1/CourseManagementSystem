<template>
  <div>
    <el-row>
      <el-col :span="8">
        <label>课程选择：</label>
        <el-select id="clas" v-model="selectedClass" style="height:30px">
          <el-option v-for="clas in classList" :key="clas.classID" :value="clas.classID">
            {{ clas.courseName + ' ' + clas.classID }}
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="8">
        <label>学年选择：</label>
        <el-select id="clas" v-model="selectedClassYear" style="height:30px">
          <el-option v-for="clas in classList" :key="clas.classYear" :value="clas.classYear">
            {{ clas.classYear }}
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-button @click="queryStuName" type="primary">确认信息</el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-table :data="tableData" style="width: 100%" max-height="800" :row-class-name="tableColour">
        <el-table-column fixed prop="studentID" label="学生编号">
        </el-table-column>
        <el-table-column prop="studentName" label="学生姓名">
        </el-table-column>
        <el-table-column prop="grade" label="分数">
        </el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button @click.prevent="open(scope.row)" type="text" size="small">添加/修改成绩</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>
  
<script>


export default {
  data() {
    return {
      class: "",
      selectedClass: "",
      selectedClassYear: "",
      stuName: "",
      stuID: "",
      dialogVisible: false,
      classList: [],
      classyearList: [],
      tableData: [],
    };
  },

  created() {
    this.findClass();
  },

  methods: {
    // 查课
    findClass: function () {
      let form = { teacherId: sessionStorage.getItem("teacherId") }
      this.$axios.post("http://192.168.1.119:8080/teacherClass", form)
        .then((res) => {
          this.classList = res.data.result;
        })
    },
    // 查学生
    queryStuName: function () { 
      let form = {
        classId: this.selectedClass, classYear: this.selectedClassYear
      }
      this.$axios.post("http://192.168.1.119:8080/teacherScoreAndName", form)
        .then((res) => {
          this.tableData = res.data.result;
          this.$alert("查询成功");
        })
    },
    // 输入成绩
    open(row) {
      this.stuID = row.studentID;
      this.stuName = row.studentName;
      this.$prompt('请输入学号为' + this.stuID + '的' + this.stuName + '的成绩', '提示', {  //显示提示
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {      //错误捕捉
        const intValue = parseInt(value, 10);
        if (intValue < 0 || intValue > 100) {
          throw new Error('未成功！成绩输入不在0~100之间');
        }
        // 在此处调用跳转函数
        this.insertGrade(this.stuID, this.stuName, intValue, this.selectedClass, this.selectedClassYear);
        this.$message({
          type: 'success',
          message: '修改成功'
        });
      }).catch((error) => {
        this.$message({
          type: 'info',
          message: error.message || '取消输入'
        });
      });
    },
    // 插入成绩
    insertGrade(sid, sname, grade, cid, cyear) {
      let form = {
        studentId: sid, studentName: sname, grade: grade, classId: cid, classYear: cyear
      }
      this.$axios.post("http://192.168.1.119:8080/reviseGrade", form)
        .then((res) => {
          if (res.data.code * 1 == 200) {
            this.queryStuName();
            return true;
          } else {
            return false;
          }
        })
    },
    // 判断未填写或不及格的变色
    tableColour({ row, rowIndex }) {
      if (row.grade < 60) {
        return 'warning-row';
      }
      return '';
    }
  },
};
</script>
  
<style>
.el-table .warning-row {
  background: rgba(247, 125, 95, 0.457);
}
</style>
  