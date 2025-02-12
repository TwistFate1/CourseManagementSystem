<template>
  <div>
    <!-- 卡片视图 -->
    <el-card>
      <!-- 搜索 添加 -->
      <el-row :gutter="20">
        <el-col :span="6">
          <span>请选择学院：</span>
          <el-select id="dn" v-model="selectedDepartment" placeholder="请选择">
            <el-option v-for="department in departmentList" :key="department.departmentName"
              :label="department.departmentName" :value="department.departmentName">
            </el-option>
          </el-select>
        </el-col>
        <el-button id="search" type="primary" icon="el-icon-search" style="position: fixed; right: 230px; top: 100px"
          @click="searchTeacher">搜索</el-button>
        <el-col :span="4">
          <el-button id="addButton" type="primary" style="position: fixed; right: 70px; top: 100px"
            @click="addDialogVisible = true">添加教师</el-button>
        </el-col>
      </el-row>
      <!-- 教师列表区域 -->
      <el-table :data="teacherList" stripe style="width: 100%;top:10px" height="520" size="small" id="teacherTable">
        <!-- stripe: 斑马条纹 border：边框-->
        <el-table-column width=190 prop="teacherID" label="教师工号"></el-table-column>
        <el-table-column width=150 prop="teacherName" label="教师姓名"></el-table-column>
        <el-table-column width=180 prop="teacherPassword" label="教师密码"></el-table-column>
        <el-table-column width=190 prop="departmentName" label="教师学院"></el-table-column>
        <el-table-column width=80 prop="teacherGender" label="教师性别">
          <template slot-scope="scope">
            {{ scope.row.teacherGender == false ? "女" : "男" }}
          </template>
        </el-table-column>
        <el-table-column width=200 prop="phoneNumber" label="教师电话"></el-table-column>
        <el-table-column width=200 prop="teacherEmail" label="教师邮箱"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" size="mini" circle
              @click="showEditDialog(scope.row.teacherID)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 添加教师的对话框 -->
    <el-dialog title="添加教师" :visible.sync="addDialogVisible" width="30%" @close="addDialogClosed">
      <!-- 内容主体 -->
      <el-form :model="addForm" ref="addFormRef" :rules="addFormRules" label-width="100px">
        <el-form-item label="教师姓名" prop="teacherName">
          <el-input v-model="addForm.teacherName"></el-input>
        </el-form-item>
        <el-form-item label="教师密码" prop="teacherPassword">
          <el-input v-model="addForm.teacherPassword"></el-input>
        </el-form-item>
        <el-form-item label="教师学院" prop="selectedDepartment1">
          <el-select id="deSelect" v-model="addForm.selectedDepartment1" placeholder="请选择">
            <el-option v-for="departmentAdd in departmentList" :key="departmentAdd.departmentID"
              :label="departmentAdd.departmentName" :value="departmentAdd.departmentID">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="教师性别" prop="teacherGender">
          <el-radio v-model="addForm.teacherGender" label="0">女</el-radio>
          <el-radio v-model="addForm.teacherGender" label="1">男</el-radio>
        </el-form-item>
        <el-form-item label="教师电话" prop="phoneNumber">
          <el-input v-model="addForm.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item label="教师邮箱" prop="teacherEmail">
          <el-input v-model="addForm.teacherEmail"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="add">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 修改教师的对话框 -->
    <el-dialog title="修改教师信息" :visible.sync="editDialogVisible" width="30%" @close="editDialogClosed">
      <!-- 内容主体 -->
      <el-form :model="editForm" ref="editFormRef" :rules="editFormRules" label-width="70px">
        <el-form-item label="教师名称">
          <el-input v-model="editForm.teacherName"></el-input>
        </el-form-item>
        <el-form-item label="教师密码" prop="teacherPassword">
          <el-input v-model="editForm.teacherPassword"></el-input>
        </el-form-item>
        <el-form-item label="教师学院" prop="departmentName">
          <el-select id="dn" v-model="selectedDepartment2" placeholder="请选择">
            <el-option v-for="departmentEdit in departmentList" :key="departmentEdit.departmentID"
              :label="departmentEdit.departmentName" :value="departmentEdit.departmentID">
            </el-option>
          </el-select>
        </el-form-item>
        <!-- 加了冒号之后就能够绑定，这里就是一个判断的属性，不加冒号的时候是字符串 -->
        <el-form-item label="教师性别" prop="teacherGender">
          <el-radio v-model="editForm.teacherGender" :label="false">女</el-radio>
          <el-radio v-model="editForm.teacherGender" :label="true">男</el-radio>
        </el-form-item>
        <el-form-item label="教师电话" prop="phoneNumber">
          <el-input v-model="editForm.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item label="教师邮箱" prop="teacherEmail">
          <el-input v-model="editForm.teacherEmail"></el-input>
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
      teacherList: [],
      teacherNewList: [],
      teacherListSelect: [],
      departmentList: [],
      selectedDepartment: "",
      selectedDepartment1: "",
      selectedDepartment2: "",
      savedGender: "",
      // 添加教师对话框
      addDialogVisible: false,
      // 教师添加
      addForm: {
        teacherName: "",
        teacherPassword: "",
        selectedDepartment1: "",
        teacherGender: "",
        phoneNumber: "",
        teacherEmail: "",
      },
      // 教师添加表单验证规则
      addFormRules: {
        teacherName: [
          { required: true, message: "请输入教师姓名", trigger: "blur" },
          {
            min: 2,
            max: 50,
            message: "教师名称的长度在2～50个字",
            trigger: "blur",
          },
        ],
        teacherPassword: [
          { required: true, message: "请输入教师密码", trigger: "blur" },
          {
            min: 8,
            max: 20,
            message: "教师密码的长度在8～20个字",
            trigger: "blur",
          },
        ],
        selectedDepartment1: [
          { required: true, message: "请选择教师学院", trigger: "blur" },
        ],
        teacherGender: [
          { required: true, message: "请选择教师性别", trigger: "blur" },
        ],
      },
      // 修改教师
      editDialogVisible: false,
      editForm: {
        teacherGender: "",
        teacherPassword: "",
        departmentName: "",
        departmentId: "",
        teacherName: "",
        teacherID: "",
        phoneNumber: "",
        teacherEmail: "",
      },
      // 编辑教师表单验证
      editFormRules: {
        teacherName: [
          { required: true, message: "请输入教师姓名", trigger: "blur" },
        ],
      },
    };
  },

  created() {
    this.getDepartments();
    this.getTeacherList();
  },

  methods: {
    getTeacherList: function () {
      this.$axios.post("http://192.168.1.119:8080/allTeacherInfo")
        .then((rest) => {
          this.teacherList = rest.data.result;
        });
    },
    getDepartments: function () {
      this.$axios.post("http://192.168.1.119:8080/allDepartment")
        .then((res) => {
          this.departmentList = res.data.result;
        })
    },
    // 监听 添加教师对话框的关闭事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },
    // 添加教师
    add() {
      // 提交请求前，表单预验证
      this.$refs.addFormRef.validate(async (valid) => {
        let addFormq = {
          departmentId: this.addForm.selectedDepartment1, teacherName: this.addForm.teacherName, teacherGender: this.addForm.teacherGender,
          teacherPassword: this.addForm.teacherPassword, teacherEmail: this.addForm.teacherEmail, phoneNumber: this.addForm.phoneNumber,
        }
        // 表单预校验失败
        if (!valid) return;
        const { data: res } = await this.$axios.post("http://192.168.1.119:8080/teacherRegister", addFormq);
        if (res.code !== 200) {
          this.$message.error("添加教师失败！");
          return;
        }
        this.$message.success("添加教师成功！");
        // 隐藏添加教师对话框
        this.addDialogVisible = false;
        this.getTeacherList();
      });
    },
    // 编辑教师信息
    async showEditDialog(id) {
      this.editDialogVisible = true;
      let editShow = { teacherId: id }
      const { data: res } = await this.$axios.post("http://192.168.1.119:8080/teacherInfo", editShow);
      if (res.code !== 200) {
        return this.$message.error("查询教师信息失败！");
      }
      //这里的返回值是个数组,因此要用result[0]来表示
      this.editForm = res.result[0];
      this.selectedDepartment2 = this.editForm.departmentID;
    },
    // 监听修改教师对话框的关闭事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },
    // 修改教师信息
    editUser() {
      // 提交请求前，表单预验证
      this.$refs.editFormRef.validate(async (valid) => {
        if (this.editForm.teacherGender == true)
          this.editForm.teacherGender = "1";
        else this.editForm.teacherGender = "0";
        let editFormq = {
          departmentId: this.selectedDepartment2, teacherName: this.editForm.teacherName, teacherGender: this.editForm.teacherGender,
          teacherPassword: this.editForm.teacherPassword, teacherEmail: this.editForm.teacherEmail, phoneNumber: this.editForm.phoneNumber,
          teacherId: this.editForm.teacherID
        }
        if (!valid) return;
        const { data: res } = await this.$axios.post("http://192.168.1.119:8080/updateInfo", editFormq);
        if (res.code !== 200) {
          this.$message.error("更新教师信息失败！");
        } else {
          // 隐藏添加教师对话框
          this.editDialogVisible = false;
          this.$message.success("更新教师信息成功！");
        }
        this.getTeacherList();
      });
    },
    //查询教师
    searchTeacher() {
      // 发送注册(post)请求
      let form = {
        departmentName: this.selectedDepartment
      }
      //这里不能用this应该改成箭头函数,因为每个函数都有自己的上下文,箭头函数表示上面的this
      this.$axios
        .post("http://192.168.1.119:8080/queryAcademicTeacher", form)
        .then((resp) => {
          if (resp.data.code * 1 == 200) {
            if (resp.data.result == "")
              alert("查询失败，无相关学院老师，请重新选择");
            // 弹窗提示注册成功
            else {
              alert("查询成功");
              this.teacherList = resp.data.result;
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

#dn {
  position: relative;
  left: 10px;
}

#teacherTable {
  position: relative;
  top: 10px;
}
</style>
  