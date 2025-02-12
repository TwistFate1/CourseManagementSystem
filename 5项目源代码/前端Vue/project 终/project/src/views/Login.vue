<template>
  <div class="login-container">
    <el-card class="login-card">
      <div slot="header" class="clearfix">
        <span style="font-size: 23px; font-family: 'Microsoft YaHei'">课程管理系统 | CMS</span>
        <span style="float: right; font-size: 23px; font-family: 'Microsoft YaHei'"><i class="el-icon-s-home"
            style="font-size: 25px;"></i>登录</span>
      </div>
      <div class="item">
        <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-width="100px" class="demo-loginForm">
          <el-form-item label="用户账号" prop="username">
            <el-input v-model="loginForm.username" style="width: 320px;" placeholder="请输入用户账号"
              prefix-icon="el-icon-user"></el-input>
          </el-form-item>
          <el-form-item label="用户密码" prop="password">
            <el-input v-model="loginForm.password" style="width: 320px;" placeholder="请输入密码" show-password
              prefix-icon="el-icon-key"></el-input>
          </el-form-item>
          <el-form-item label="用户类型" prop="type" size="1">
            <el-radio-group v-model="loginForm.type">
              <el-radio :label="1" size="medium" style="margin-right: 30px;">学生</el-radio>
              <el-radio :label="2" size="medium" style="margin-right: 30px;">教师</el-radio>
              <el-radio :label="0" size="medium" style="margin-right: 30px;">管理员</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="medium" @click="submitForm('loginForm')">登录</el-button>
            <el-button type="success" size="medium" @click="registForm('loginForm')">注册</el-button>
            <el-button type="danger" size="medium" @click="resetForm('loginForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        type: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户账号', trigger: 'blur' },
          { min: 8, max: 8, message: '账号长度为8个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入用户密码', trigger: 'blur' },
          { min: 8, max: 20, message: '密码长度为8到20个字符', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择用户类型', trigger: 'change' }
        ]
      }
    };
  },
  
  methods: {
    submitForm(formName) {
      const that = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let form = { id: that.loginForm.username, password: that.loginForm.password, loginType: that.loginForm.type }
          this.$axios.post("http://192.168.1.119:8080/doLogin", form).then(function (resp) {
            // 登录验证通过
            if (resp.data.code * 1 == 200) {
              // 设置消息提示框
              that.$message({
                showClose: true,
                message: '登陆成功，欢迎!',
                type: 'success'
              });
              sessionStorage.clear();
              // 根据用户类型进行页面跳转
              // 教务（管理员）
              if (that.loginForm.type * 1 == 0) {
                // 存储登录数据
                sessionStorage.setItem("token", 'true')
                sessionStorage.setItem("adminId", that.loginForm.username)
                sessionStorage.setItem("type", that.loginForm.type)
                // 跳转至admin端课程管理系统
                that.$router.push('/admin');
              }
              // 学生
              else if (that.loginForm.type * 1 == 1) {
                // 存储登录数据
                sessionStorage.setItem("token", 'true')
                sessionStorage.setItem("studentId", that.loginForm.username)
                sessionStorage.setItem("type", that.loginForm.type)
                // 跳转至学生端课程管理系统
                that.$router.push('/student');
              }
              // 教师
              else {
                // 存储登录数据
                sessionStorage.setItem("token", 'true')
                sessionStorage.setItem("teacherId", that.loginForm.username)
                sessionStorage.setItem("type", that.loginForm.type)
                // 跳转至教师端课程管理系统
                that.$router.push('/teacher');
              }
            }
            // 登录失败：用户类型与用户账号不匹配
            else if (resp.data.code * 1 == 201) {
              that.$message({
                showClose: true,
                message: '登录失败，用户类型与用户账号不匹配！',
                type: 'error'
              });
            }
            // 登录失败：找不到用户账号
            else if (resp.data.code * 1 == 202) {
              that.$message({
                showClose: true,
                message: '登录失败，找不到该用户！',
                type: 'error'
              });
            }
            // 登录失败：用户密码错误
            else if (resp.data.code * 1 == 203) {
              that.$message({
                showClose: true,
                message: '登录失败，用户密码错误！',
                type: 'error'
              });
            }
          })
        } else {
          that.$message({
            showClose: true,
            message: '请正确输入！',
            type: 'error'
          });
          return false;
        }
      });

    },
    // 注册按钮
    registForm() {
      this.$router.push('/register');
    },
    // 重置按钮
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style>
/* 设置登录信息布局 */
.item {
  margin-bottom: 18px;
  display: flex;
  flex-direction: column;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
  clear: both;
}

/* 设置登录卡片的大小 */
.login-card {
  width: 480px;
  height: 350px;
  border-radius: 20px;
  /* 圆角边框半径 */
  background-color: rgba(255, 255, 255, 0.965);
}

.login-container {
  display: flex;
  /* 水平居中 */
  justify-content: center;
  /* 垂直居中 */
  align-items: center;
  /* 设置高度，让卡片垂直居中 */
  height: 100vh;
  /* 设置背景 */
  background: url("../assets/背景-冰雪.JPG");
  background-size: cover;
  opacity: 0.9;
}
</style>