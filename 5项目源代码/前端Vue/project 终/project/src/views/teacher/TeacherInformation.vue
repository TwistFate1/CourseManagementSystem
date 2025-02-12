<template>
  <el-card class="box-card">
    <div class="text item">
      <el-descriptions class="margin-top" title="个人信息" :column="2" border>
        <template slot="extra">
          <!-- 编辑个人信息 -->
          <el-button type="primary" icon="el-icon-edit" @click="findDepartment()">编辑</el-button>
          <el-dialog title="个人信息编辑" :visible.sync="editorDialogVisible" style="width: 850px;">
            <el-form :model="editorRuleForm" :rules="editorRules" ref="editorRuleForm" label-width="80px"
              class="demo-editorRuleForm" style="width: 350px">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="editorRuleForm.name"></el-input>
              </el-form-item>
              <el-form-item label="性别" prop="gender" style="text-align: center;">
                <el-radio-group v-model="editorRuleForm.gender">
                  <el-radio label="1">男</el-radio>
                  <el-radio label="0">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="手机" prop="phoneNumber">
                <el-input v-model="editorRuleForm.phoneNumber"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="editorRuleForm.email"></el-input>
              </el-form-item>
              <el-form-item label="简介" prop="introduction">
                <el-input type="textarea" v-model="editorRuleForm.introduction"></el-input>
              </el-form-item>
              <el-form-item label="学院" prop="department">
                <el-select id="department" v-model="editorRuleForm.department" placeholder="请选择学院" style="width: 270px;">
                  <el-option v-for="department in departmentList" :key="department.departmentId"
                    :value="department.departmentId" :label="department.departmentName">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="editorDialogVisible = false">取 消</el-button>
              <el-button @click="editorResetForm('editorRuleForm')">重置</el-button>
              <el-button type="primary" @click="editorTeacherInfoSubmitForm('editorRuleForm')">提交</el-button>
            </div>
          </el-dialog>
        </template>
        <!-- 个人信息展示 -->
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            工号
          </template>
          {{ teacherInfo.teacherId }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-postcard"></i>
            姓名
          </template>
          {{ teacherInfo.teacherName }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-guide"></i>
            性别
          </template>
          {{ teacherInfo.teacherGender }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            学院
          </template>
          {{ teacherInfo.departmentName }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-phone-outline"></i>
            手机号
          </template>
          {{ teacherInfo.teacherPhoneNumber }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
          {{ teacherInfo.teacherEmail }}
        </el-descriptions-item>
        <el-descriptions-item :span="2">
          <template slot="label">
            <i class="el-icon-postcard"></i>
            简介
          </template>
          {{ teacherInfo.teacherIntroduction }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-key"></i>
            密码
          </template>
          <span v-if="isPasswordSet()">已设置&nbsp;&nbsp;</span>
          <span v-else>未设置&nbsp;&nbsp;</span>
          <!-- 修改密码 -->
          <el-button type="text" @click="dialogFormVisible = true">修改密码</el-button>
          <el-dialog title="修改密码" :visible.sync="dialogFormVisible">
            <el-form :model="updatePasswordForm" :rules="rules" ref="teacherPasswordForm" label-width="80px">
              <el-form-item label="旧密码" prop="oldPassword" :rules="rules.oldPassword">
                <el-input v-model="updatePasswordForm.oldPassword" type="password" autocomplete="off"
                  @input="handleOldPasswordInput"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="updatePasswordForm.password" type="password" autocomplete="off"
                  @input="handlePasswordInput"></el-input>
              </el-form-item>
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="updatePasswordForm.confirmPassword" type="password" autocomplete="off"
                  @input="handleConfirmPasswordInput"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="submitTeacherForm">提交</el-button>
            </div>
          </el-dialog>
        </el-descriptions-item>
      </el-descriptions>
      <br>
      <!-- 头像 -->
      <el-descriptions class="margin-top" title="头像大图" style="width: 235px;">
        <template slot="extra">
          <el-popover placement="right" width="500" trigger="click">
            <el-descriptions class="margin-top" title="可选择头像" :column="3" direction="vertical">
              <el-descriptions-item label="默认小海豚">
                <el-image style="width: 120px;" :src="getFromAvatars(0)"></el-image>
                <br>
                <el-button icon="el-icon-check" :type="teacherInfo.pictureId === 0 ? 'success' : ''" circle size="mini"
                  @click="selectAvatar(0)"></el-button>
              </el-descriptions-item>
              <el-descriptions-item label="型男">
                <el-image style="width: 120px;" :src="getFromAvatars(5)"></el-image>
                <br>
                <el-button icon="el-icon-check" :type="teacherInfo.pictureId === 5 ? 'success' : ''" circle size="mini"
                  @click="selectAvatar(5)"></el-button>
              </el-descriptions-item>
              <el-descriptions-item label="络腮大叔" :span="2">
                <el-image style="width: 120px;" :src="getFromAvatars(6)"></el-image>
                <br>
                <el-button icon="el-icon-check" :type="teacherInfo.pictureId === 6 ? 'success' : ''" circle size="mini"
                  @click="selectAvatar(6)"></el-button>
              </el-descriptions-item>
              <el-descriptions-item label="发夹女生">
                <el-image style="width: 120px;" :src="getFromAvatars(7)"></el-image>
                <br>
                <el-button icon="el-icon-check" :type="teacherInfo.pictureId === 7 ? 'success' : ''" circle size="mini"
                  @click="selectAvatar(7)"></el-button>
              </el-descriptions-item>
              <el-descriptions-item label="害羞女">
                <el-image style="width: 120px;" :src="getFromAvatars(8)"></el-image>
                <br>
                <el-button icon="el-icon-check" :type="teacherInfo.pictureId === 8 ? 'success' : ''" circle size="mini"
                  @click="selectAvatar(8)"></el-button>
              </el-descriptions-item>
            </el-descriptions>
            <el-button slot="reference" type="text"><i class="el-icon-check"></i> 更改头像</el-button>
          </el-popover>
        </template>
      </el-descriptions>
      <div class="demo-basic--circle">
        <span class="usedAvatar"><el-avatar shape="square" :size="220" :src="getAvatarUrl()"></el-avatar></span>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      teacherInfo: {
        teacherId: "无",
        teacherPassword: "",
        teacherName: "匿名",
        teacherGender: "未知",
        teacherPhoneNumber: "无",
        teacherEmail: "无",
        pictureId: "0",
        departmentName: "无",
        teacherName: "无",
        teacherIntroduction: "无"
      },
      // 编辑信息对话框
      editorDialogVisible: false,
      editorRuleForm: {
        name: '',
        gender: '',
        phoneNumber: '',
        email: '',
        department: '',
        introduction: ''
      },
      departmentList: [],
      editorRules: {
        name: [
          { required: true, message: '请输入您的姓名', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择您的性别', trigger: 'change' }
        ],
        phoneNumber: [
          { required: true, message: '请输入您的手机号' }
        ],
        email: [
          { required: true, message: '请输入您的邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        introduction: [
          { required: true, message: '请填写您的简介', trigger: 'blur' }
        ],
        department: [
          { required: true, message: '请选择您的学院', trigger: 'change' }
        ]
      },
      // 修改密码对话框
      dialogFormVisible: false,
      updatePasswordForm: {
        oldPassword: "",
        password: "",
        confirmPassword: ""
      },
      rules: {
        oldPassword: [
          { required: true, message: "请输入旧密码", trigger: "blur" },
          { validator: this.validateOldPassword, trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 8, max: 20, message: "密码长度为8-20个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "请再次输入密码", trigger: "blur" },
          { validator: this.validateConfirmPassword, trigger: "blur" }
        ]
      },
      formLabelWidth: '40px'
    }
  },

  created() {
    this.getTeacherInfo()
  },

  methods: {
    // 获取教师个人信息
    getTeacherInfo() {
      let Id = { teacherId: sessionStorage.getItem("teacherId") }
      this.$axios.post("http://192.168.1.119:8080/teacherInfo", Id).then((res) => {
        this.teacherInfo.teacherId = res.data.result[0].teacherID;
        this.teacherInfo.teacherPassword = res.data.result[0].teacherPassword;
        this.teacherInfo.teacherName = res.data.result[0].teacherName;
        this.teacherInfo.teacherGender = res.data.result[0].teacherGender ? "男" : "女";
        this.teacherInfo.teacherPhoneNumber = res.data.result[0].phoneNumber;
        this.teacherInfo.teacherEmail = res.data.result[0].teacherEmail;
        this.teacherInfo.departmentName = res.data.result[0].departmentName;
        this.teacherInfo.teacherIntroduction = res.data.result[0].teacherIntroduction;
        this.teacherInfo.pictureId = res.data.result[0].pictureID;
      })
    },
    // 获取学院信息
    findDepartment() {
      this.editorDialogVisible = true;
      this.$axios.post("http://192.168.1.119:8080/registDepartment")
        .then((departmentResp) => {
          this.departmentList = departmentResp.data.result;
        }).catch((err) => {
          console.log(err)
        })
    },
    // 个人信息修改重置
    editorResetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // 编辑信息提交
    editorTeacherInfoSubmitForm(formName) {
      this.$refs[formName].validate((infoValid) => {
        if (infoValid) {
          let editorTeacherForm = {
            teacherId: sessionStorage.getItem("teacherId"),
            departmentId: this.editorRuleForm.department,
            teacherName: this.editorRuleForm.name,
            teacherGender: this.editorRuleForm.gender,
            phoneNumber: this.editorRuleForm.phoneNumber,
            teacherEmail: this.editorRuleForm.email,
            teacherIntroduction: this.editorRuleForm.introduction
          }
          this.$axios.post("http://192.168.1.119:8080/updateTeacher", editorTeacherForm).then((editorRes) => {
            if (editorRes.data.code * 1 == 200) {
              this.editorDialogVisible = false;
              this.getTeacherInfo();
              this.$bus.$emit("updateTeacherBasicInfo", editorRes.data.result, this.editorRuleForm.name);
              this.$message({
                showClose: true,
                message: '个人信息编辑成功！',
                type: 'success'
              });
            }
            else {
              this.$message({
                showClose: true,
                message: '个人信息编辑失败，请重试！',
                type: 'error'
              });
            }
          })
        } else {
          this.$message({
            showClose: true,
            message: '请正确输入！',
            type: 'error'
          });
        }
      });
    },
    // 判断用户密码字段是否为空
    isPasswordSet() {
      if (this.teacherInfo.teacherPassword) {
        return true; // 密码已设置
      } else {
        return false; // 密码未设置
      }
    },
    // 修改密码
    validateOldPassword(rules, value, callback) {
      if (value !== this.teacherInfo.teacherPassword) {
        callback(new Error("旧密码不正确"));
      }
      else {
        callback();
      }
    },
    // 判断两次密码输入是否一致
    validateConfirmPassword(rules, value, callback) {
      if (value !== this.updatePasswordForm.password) {
        callback(new Error("两次输入的密码不一致"));
      }
      else {
        callback();
      }
    },
    // 检测旧密码
    handleOldPasswordInput() {
      this.$refs.teacherPasswordForm.validateField("oldPassword");
    },
    // 检测新密码
    handlePasswordInput() {
      this.$refs.teacherPasswordForm.validateField("password");
    },
    // 检测确认密码
    handleConfirmPasswordInput() {
      this.$refs.teacherPasswordForm.validateField("confirmPassword");
    },
    // 提交修改密码
    submitTeacherForm() {
      this.$refs.teacherPasswordForm.validate(valid => {
        if (valid) {
          let newPassword = { teacherId: sessionStorage.getItem("teacherId"), teacherPassword: this.updatePasswordForm.password }
          this.$axios.post("http://192.168.1.119:8080/updateTeacherPassword", newPassword).then((res2) => {
            if (res2.data.code * 1 == 200) {
              this.getTeacherInfo()
              this.dialogFormVisible = false;
              this.$message({
                showClose: true,
                message: '密码修改成功！',
                type: 'success'
              });
            }
            else {
              this.$message({
                showClose: true,
                message: '修改失败，请重试！',
                type: 'error'
              });
            }
          })
        }
        else {
          this.$message({
            showClose: true,
            message: '请正确输入！',
            type: 'error'
          });
        }
      });
    },
    // 获得用户头像
    getAvatarUrl() {
      return require(`/src/assets/avatar/${this.teacherInfo.pictureId}.jpg`);
    },
    // 图片库获取头像
    getFromAvatars(number) {
      return require(`/src/assets/avatar/${number}.jpg`);
    },
    // 更改用户头像
    selectAvatar(pictureId) {
      let updatedAvatar = { teacherId: this.teacherInfo.teacherId, pictureId: pictureId };
      this.$axios.post("http://192.168.1.119:8080/updateTeacherPicture", updatedAvatar).then((updatedAvatarRes) => {
        if (updatedAvatarRes.data.code * 1 == 200) {
          this.teacherInfo.pictureId = pictureId;
          this.$bus.$emit("changeTeacherPic", pictureId)
          this.$notify({
            title: '成功',
            message: '图片更改成功！',
            type: 'success',
            offset: 100
          });
        }
        else {
          this.$notify({
            title: '错误',
            message: '图片更改失败！',
            offset: 100
          });
        }
      })

    }
  }
};
</script>

<style>
.box-card {
  height: 660px;
}
</style>