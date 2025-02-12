<template>
  <div>
    <!-- 卡片视图 -->
    <el-card class="container">
      <!-- 搜索 添加 -->
      <el-row :gutter="20">
        <el-col :span="6" class="fixed-component">
          <span>请选择学院：</span>
          <el-select style="position:absolute; left: 100px ;width:200px" v-model="selectedDepartment" placeholder="请选择">
            <el-option v-for="department in departmentList" :key="department.departmentID"
              :label="department.departmentName" :value="department.departmentID">
            </el-option>
          </el-select>
          <el-button id="search" type="primary" icon="el-icon-search" style="position:absolute; left: 320px;top:6px"
            @click="searchStudentDe">按照学院搜索</el-button>
        </el-col>
        <el-col :span="6" class="fixed-component1">
          <label>请选择专业：</label>
          <el-select style="position:absolute; left: 100px ;width:200px" v-model="selectedMajor" placeholder="请选择">
            <el-option v-for="major in specialityList" :key="major.specialityID" :label="major.specialityName"
              :value="major.specialityID">
            </el-option>
          </el-select>
          <el-button id="search" type="primary" icon="el-icon-search" style="position:absolute; left: 320px;top:6px"
            @click="searchStudentMa">按照专业搜索</el-button>
        </el-col>
      </el-row>
      <!-- 学生列表区域 -->
      <el-table :data="studentList" stripe style="width: 100%" height="550" size="small" id="studentTable">
        <!-- stripe: 斑马条纹 border：边框-->
        <el-table-column width=100 prop="studentID" label="学生学号"></el-table-column>
        <el-table-column width=100 prop="studentName" label="学生姓名"></el-table-column>
        <el-table-column width=110 prop="studentPassword" label="学生密码"></el-table-column>
        <el-table-column width=190 prop="departmentName" label="学生学院"></el-table-column>
        <el-table-column width=190 prop="specialityName" label="学生专业"></el-table-column>
        <el-table-column width=80 prop="studentGender" label="学生性别">
          <template slot-scope="scope">
            {{ scope.row.studentGender == false ? "女" : "男" }}
          </template>
        </el-table-column>
        <el-table-column width=180 prop="studentPhoneNumber" label="学生电话"></el-table-column>
        <el-table-column width=200 prop="studentEmail" label="学生邮箱"></el-table-column>
        <el-table-column width=200 label="操作">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="small" circle
              @click="showEditDialog(scope.row.studentID)"></el-button>
            <el-button type="danger" icon="el-icon-delete" size="small" circle @click="removeById(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 修改学生的对话框 -->
    <el-dialog title="修改学生信息" :visible.sync="editDialogVisible" width="30%" @close="editDialogClosed">
      <!-- 内容主体 -->
      <el-form :model="editForm" ref="editFormRef" :rules="editFormRules" label-width="70px">
        <el-form-item label="学生姓名">
          <el-input v-model="editForm.studentName"></el-input>
        </el-form-item>
        <el-form-item label="学生密码" prop="studentPassword">
          <el-input v-model="editForm.studentPassword"></el-input>
        </el-form-item>
        <el-form-item label="学生学院" prop="departmentName">
          <el-select style="position:relative;right:2px" v-model="selectedDepartment1" placeholder="请选择">
            <el-option v-for="departmentEdit in departmentList" :key="departmentEdit.departmentID"
              :label="departmentEdit.departmentName" :value="departmentEdit.departmentID">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学生专业" prop="specialityName">
          <el-select style="position:relative;right:2px" v-model="selectedSpeciality1" placeholder="请选择">
            <el-option v-for="specialityEdit in specialityList1" :key="specialityEdit.specialityID"
              :label="specialityEdit.specialityName" :value="specialityEdit.specialityID">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学生性别" prop="studentGender">
          <el-radio v-model="editForm.studentGender" :label="false">女</el-radio>
          <el-radio v-model="editForm.studentGender" :label="true">男</el-radio>
        </el-form-item>
        <el-form-item label="学生电话" prop="studentPhoneNumber">
          <el-input v-model="editForm.studentPhoneNumber"></el-input>
        </el-form-item>
        <el-form-item label="学生邮箱" prop="studentEmail">
          <el-input v-model="editForm.studentEmail"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editUser">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
  
<script>
export default {
  data() {
    return {
      studentList: [],
      departmentList: [],
      specialityList1: [],
      specialityList: [],
      selectedDepartment: "",
      selectedDepartment1: "",
      selectedSpeciality1: "",
      selectedMajor: "",
      // 添加学生对话框
      addDialogVisible: false,
      // 修改学生
      editDialogVisible: false,
      editForm: {
        departmentName: "",
        departmentId: "",
        specialityId: "",
        specialityName: "",
        studentID: "",
        studentName: "",
        studentPassword: "",
        studentGender: "",
        studentPhoneNumber: "",
        studentEmail: "",
      },
      // 编辑学生表单验证
      editFormRules: {
        studentName: [
          { required: true, message: "请输入学生姓名", trigger: "blur" },
        ],
      },
    };
  },

  watch: {
    selectedDepartment(newDepartment) {
      this.specialityList = '';
      let form = {
        departmentId: newDepartment
      }
      //这里不能用this应该改成箭头函数,因为每个函数都有自己的上下文,箭头函数表示上面的this
      this.$axios
        .post("http://192.168.1.119:8080/provideCollegeMajors", form)
        .then((ress) => {
          if (ress.data.code * 1 == 200) {
            if (ress.data.result == "")
              alert("查询失败,请重新选择");
            // 弹窗提示注册成功
            else {
              this.specialityList = ress.data.result;
            }
          } else if (ress.data.code * 1 == 201) {
            alert("查询失败");
          }
        });
    },
    selectedDepartment1(newDepartment1) {
      this.specialityList1 = '';
      let form = {
        departmentId: newDepartment1
      }
      //这里不能用this应该改成箭头函数,因为每个函数都有自己的上下文,箭头函数表示上面的this
      this.$axios
        .post("http://192.168.1.119:8080/provideCollegeMajors", form)
        .then((ress1) => {
          if (ress1.data.code * 1 == 200) {
            if (ress1.data.result == "")
              alert("查询失败,请重新选择");
            // 弹窗提示注册成功
            else {
              this.specialityList1 = ress1.data.result;
            }
          } else if (ress1.data.code * 1 == 201) {
            alert("查询失败");
          }
        });
    }
  },

  created() {
    this.getDepartments();
    this.getStudentList();
  },

  methods: {
    getStudentList: function () {
      this.$axios.post("http://192.168.1.119:8080/queryAllStudents")
        .then((rest) => {
          this.studentList = rest.data.result;
        });
    },
    getDepartments: function () {
      this.$axios.post("http://192.168.1.119:8080/allDepartment")
        .then((res) => {
          this.departmentList = res.data.result;
        })
    },
    // 监听 添加学生对话框的关闭事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },
    // 删除学生
    async removeById(row) {
      let removeId = { studentId: row.studentID }
      const confirmResult = await this.$confirm("确定修改该学生学籍为无学籍?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).catch((err) => err);
      // 点击确定 返回值为：confirm
      // 点击取消 返回值为： cancel
      if (confirmResult !== "confirm") {
        return this.$message.info("已取消操作");
      }
      const { data: res } = await this.$axios.post("http://192.168.1.119:8080/dropoutAndSuspension", removeId);
      if (res.code !== 200) return this.$message.error("修改学生状态失败！");
      this.$message.success("该学生已无学籍！");
      this.getStudentList();
    },
    // 编辑学生信息
    async showEditDialog(id) {
      this.editDialogVisible = true;
      let editShow = { studentId: id }
      const { data: res } = await this.$axios.post("http://192.168.1.119:8080/viewPersonalInformation", editShow);
      if (res.code !== 200) {
        return this.$message.error("查询老师信息失败！");
      }
      //这里的返回值是个数组,因此要用result[0]来表示
      this.editForm = res.result[0];
      this.selectedDepartment1 = this.editForm.departmentID;
      this.selectedSpeciality1 = this.editForm.specialityID;
    },
    // 监听修改学生对话框的关闭事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },
    // 修改学生信息
    editUser() {
      // 提交请求前，表单预验证
      this.$refs.editFormRef.validate(async (valid) => {
        if (this.editForm.studentGender == true)
          this.editForm.studentGender = "1";
        else this.editForm.studentGender = "0";
        let editForms = {
          studentName: this.editForm.studentName, studentGender: this.editForm.studentGender,
          studentPassword: this.editForm.studentPassword, studentEmail: this.editForm.studentEmail, studentPhoneNumber: this.editForm.studentPhoneNumber,
          studentId: this.editForm.studentID, specialityId: this.selectedSpeciality1
        }
        if (!valid) return;
        const { data: res } = await this.$axios.post("http://192.168.1.119:8080/reviseStudentInformation", editForms);
        if (res.code !== 200) {
          this.$message.error("更新学生信息失败！");
        }
        // 隐藏添加教师对话框
        this.editDialogVisible = false;
        this.$message.success("更新学生信息成功！");
        this.getStudentList();
      });
    },
    //通过院系查询
    searchStudentDe() {
      // 发送注册(post)请求
      let form = {
        departmentId: this.selectedDepartment
      }
      //这里不能用this应该改成箭头函数,因为每个函数都有自己的上下文,箭头函数表示上面的this
      this.$axios
        .post("http://192.168.1.119:8080/queryCollegeStudents", form)
        .then((resp) => {
          if (resp.data.code * 1 == 200) {
            if (resp.data.result == "")
              alert("查询失败，无相关学院学生，请重新选择");
            // 弹窗提示注册成功
            else {
              alert("查询成功");
              this.studentList = resp.data.result;
            }
          } else if (resp.data.code * 1 == 201) {
            alert("查询失败");
          }
        });
    },
    //通过专业查询
    searchStudentMa() {
      // 发送注册(post)请求
      let form = {
        specialityId: this.selectedMajor
      }
      //这里不能用this应该改成箭头函数,因为每个函数都有自己的上下文,箭头函数表示上面的this
      this.$axios
        .post("http://192.168.1.119:8080/querySpecialityStudents", form)
        .then((resp) => {
          if (resp.data.code * 1 == 200) {
            if (resp.data.result == "")
              alert("查询失败，无相关学院学生，请重新选择");
            // 弹窗提示注册成功
            else {
              alert("查询成功");
              this.studentList = resp.data.result;
            }
          } else if (resp.data.code * 1 == 201) {
            alert("查询失败");
          }
        });
    }
  },
};
</script>

<style>
.el-select {
  position: relative;
  right: 9px;
  top: 5px;
}

.fixed-component {
  position: absolute;
  /* 设置组件为绝对定位 */
  top: 10px;
  /* 设置组件距离容器顶部的距离 */
  left: 5px;
  /* 设置组件距离容器左侧的距离 */
  width: 200px;
  /* 设置组件的宽度 */
  height: 100px;
  /* 设置组件的高度 */
  padding: 10px;
}

.fixed-component1 {
  position: absolute;
  /* 设置组件为绝对定位 */
  top: 10px;
  /* 设置组件距离容器顶部的距离 */
  left: 500px;
  /* 设置组件距离容器左侧的距离 */
  width: 200px;
  /* 设置组件的宽度 */
  height: 100px;
  /* 设置组件的高度 */
  padding: 10px;
}

.container {
  position: relative;
  /* 设置容器为相对定位 */
  width: 100%;
  height: 100%;
}

#studentTable {
  position: relative;
  top: 50px;
}
</style>
  