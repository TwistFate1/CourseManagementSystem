<template>
    <el-card class="box-card">
        <div class="text item">
            <el-descriptions class="margin-top" title="个人信息" :column="2" border>
                <template slot="extra">
                    <!-- 编辑个人信息 -->
                    <el-button type="primary" icon="el-icon-edit" @click="editorDialogVisible = true;">编辑</el-button>
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
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="editorDialogVisible = false">取 消</el-button>
                            <el-button @click="editorResetForm('editorRuleForm')">重置</el-button>
                            <el-button type="primary" @click="editorSubmitForm('editorRuleForm')">提交</el-button>
                        </div>
                    </el-dialog>
                </template>
                <!-- 个人信息展示 -->
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-user"></i>
                        学号
                    </template>
                    {{ studentInfo.studentID }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-postcard"></i>
                        姓名
                    </template>
                    {{ studentInfo.studentName }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-guide"></i>
                        性别
                    </template>
                    {{ studentInfo.studentGender }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-chat-line-round"></i>
                        导师
                    </template>
                    {{ studentInfo.teacherName }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-office-building"></i>
                        学院
                    </template>
                    {{ studentInfo.departmentName }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-location-outline"></i>
                        专业
                    </template>
                    {{ studentInfo.specialityName }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-phone-outline"></i>
                        手机号
                    </template>
                    {{ studentInfo.studentPhoneNumber }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-message"></i>
                        邮箱
                    </template>
                    {{ studentInfo.studentEmail }}
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
                        <el-form :model="changePasswordForm" :rules="rules" ref="passwordForm" label-width="80px">
                            <el-form-item label="旧密码" prop="oldPassword" :rules="rules.oldPassword">
                                <el-input v-model="changePasswordForm.oldPassword" type="password" autocomplete="off"
                                    @input="handleOldPasswordInput"></el-input>
                            </el-form-item>
                            <el-form-item label="密码" prop="password">
                                <el-input v-model="changePasswordForm.password" type="password" autocomplete="off"
                                    @input="handlePasswordInput"></el-input>
                            </el-form-item>
                            <el-form-item label="确认密码" prop="confirmPassword">
                                <el-input v-model="changePasswordForm.confirmPassword" type="password" autocomplete="off"
                                    @input="handleConfirmPasswordInput"></el-input>
                            </el-form-item>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="dialogFormVisible = false">取 消</el-button>
                            <el-button type="primary" @click="submitForm">提交</el-button>
                        </div>
                    </el-dialog>
                </el-descriptions-item>
            </el-descriptions>
            <br>
            <!-- 头像 -->
            <el-descriptions class="margin-top" title="头像大图" style="width: 235px;">
                <template slot="extra">
                    <!-- 显示可选头像界面 -->
                    <el-popover placement="right" width="570" trigger="click">
                        <el-descriptions class="margin-top" title="可选择头像" :column="4" direction="vertical">
                            <el-descriptions-item label="默认小海豚">
                                <el-image style="width: 120px;" :src="getFromAvatars(0)"></el-image>
                                <br>
                                <el-button icon="el-icon-check" :type="studentInfo.pictureID === 0 ? 'success' : ''" circle
                                    size="mini" @click="selectAvatar(0)"></el-button>
                            </el-descriptions-item>
                            <el-descriptions-item label="调皮女生">
                                <el-image style="width: 120px;" :src="getFromAvatars(1)"></el-image>
                                <br>
                                <el-button icon="el-icon-check" :type="studentInfo.pictureID === 1 ? 'success' : ''" circle
                                    size="mini" @click="selectAvatar(1)"></el-button>
                            </el-descriptions-item>
                            <el-descriptions-item label="羞涩女孩" :span="2">
                                <el-image style="width: 120px;" :src="getFromAvatars(2)"></el-image>
                                <br>
                                <el-button icon="el-icon-check" :type="studentInfo.pictureID === 2 ? 'success' : ''" circle
                                    size="mini" @click="selectAvatar(2)"></el-button>
                            </el-descriptions-item>
                            <el-descriptions-item label="风衣酷男">
                                <el-image style="width: 120px;" :src="getFromAvatars(3)"></el-image>
                                <br>
                                <el-button icon="el-icon-check" :type="studentInfo.pictureID === 3 ? 'success' : ''" circle
                                    size="mini" @click="selectAvatar(3)"></el-button>
                            </el-descriptions-item>
                            <el-descriptions-item label="QQ男">
                                <el-image style="width: 120px;" :src="getFromAvatars(4)"></el-image>
                                <br>
                                <el-button icon="el-icon-check" :type="studentInfo.pictureID === 4 ? 'success' : ''" circle
                                    size="mini" @click="selectAvatar(4)"></el-button>
                            </el-descriptions-item>
                            <el-descriptions-item label="自定义">
                                <el-image style="width: 120px; height: 120px;"
                                    :src="studentInfo.pictureID === 99 ? `http://192.168.1.119/images/${studentInfo.studentID}.jpg?${Math.random() * 10}` : ''">
                                    <div slot="error" class="image-slot">
                                        <i class="el-icon-picture-outline"></i>
                                    </div>
                                </el-image>
                                <br>
                                <el-button icon="el-icon-check" :type="studentInfo.pictureID === 99 ? 'success' : ''" circle
                                    size="mini" disabled></el-button>
                            </el-descriptions-item>
                            <!-- 自定义头像上传 -->
                            <el-descriptions-item label="上传自定义头像">
                                <el-upload class="avatar-uploader" action="http://192.168.1.119:8070/upload"
                                    :data=avatarName :show-file-list="false" :on-success="handleAvatarSuccess"
                                    :before-upload="beforeAvatarUpload" name="file">
                                    <img v-if="imageUrl" :src="imageUrl" class="avatar">
                                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                                </el-upload>
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
            // 学生个人信息
            studentInfo: {
                studentID: "无",
                studentPassword: "",
                studentName: "匿名",
                studentGender: "未知",
                studentPhoneNumber: "无",
                studentEmail: "无",
                pictureID: "0",
                specialityName: "无",
                departmentName: "无",
                teacherName: "无"
            },
            // 编辑信息对话框
            editorDialogVisible: false,
            editorRuleForm: {
                name: '',
                gender: '',
                phoneNumber: '',
                email: ''
            },
            specialityList: [],
            // 自定义头像名称
            avatarName: {},
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
                ]
            },
            // 修改密码对话框
            dialogFormVisible: false,
            // 修改密码
            changePasswordForm: {
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
            formLabelWidth: '40px',
            // 自定义头像地址
            imageUrl: ''
        }
    },

    created() {
        this.getStudentInfo()
    },

    mounted() {
        this.avatarName = {
            studentId: sessionStorage.getItem("studentId")
        }
    },

    methods: {
        // 获取学生个人信息
        getStudentInfo() {
            let Id = { studentId: sessionStorage.getItem("studentId") }
            this.$axios.post("http://192.168.1.119:8080/viewPersonalInformation", Id).then((res) => {
                this.studentInfo.studentID = res.data.result[0].studentID;
                this.studentInfo.studentPassword = res.data.result[0].studentPassword;
                this.studentInfo.studentName = res.data.result[0].studentName;
                this.studentInfo.studentGender = res.data.result[0].studentGender ? "男" : "女";
                this.studentInfo.studentPhoneNumber = res.data.result[0].studentPhoneNumber;
                this.studentInfo.studentEmail = res.data.result[0].studentEmail;
                this.studentInfo.departmentName = res.data.result[0].departmentName;
                this.studentInfo.specialityName = res.data.result[0].specialityName;
                this.studentInfo.pictureID = res.data.result[0].pictureID;
            })
            this.$axios.post("http://192.168.1.119:8080/findMentorInformation", Id).then((res1) => {
                if (res1.data.result[0] == null) {
                    this.studentInfo.teacherName = "无";
                }
                else {
                    this.studentInfo.teacherName = res1.data.result[0].teacherName;
                }
            })
        },
        // 个人信息修改重置
        editorResetForm(formName) {
            this.$refs[formName].resetFields();
        },
        // 编辑信息提交
        editorSubmitForm(formName) {
            this.$refs[formName].validate((infoValid) => {
                if (infoValid) {
                    let editorStudentForm = {
                        studentId: sessionStorage.getItem("studentId"),
                        studentName: this.editorRuleForm.name,
                        studentGender: this.editorRuleForm.gender,
                        studentPhoneNumber: this.editorRuleForm.phoneNumber,
                        studentEmail: this.editorRuleForm.email
                    }
                    this.$axios.post("http://192.168.1.119:8080/modifyStudentInformation", editorStudentForm).then((editorRes) => {
                        if (editorRes.data.code * 1 == 200) {
                            this.editorDialogVisible = false;
                            this.getStudentInfo();
                            this.$bus.$emit("updateBasicInfo", editorRes.data.result, this.editorRuleForm.name);
                            this.$message({
                                showClose: true,
                                message: '个人信息修改成功！',
                                type: 'success'
                            });
                        }
                        else {
                            this.$message({
                                showClose: true,
                                message: '个人信息修改失败，请重试！',
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
            if (this.studentInfo.studentPassword) {
                return true; // 密码已设置
            } else {
                return false; // 密码未设置
            }
        },
        // 修改密码
        validateOldPassword(rules, value, callback) {
            if (value !== this.studentInfo.studentPassword) {
                callback(new Error("旧密码不正确"));
            }
            else {
                callback();
            }
        },
        validateConfirmPassword(rules, value, callback) {
            if (value !== this.changePasswordForm.password) {
                callback(new Error("两次输入的密码不一致"));
            }
            else {
                callback();
            }
        },
        handleOldPasswordInput() {
            this.$refs.passwordForm.validateField("oldPassword");
        },
        handlePasswordInput() {
            this.$refs.passwordForm.validateField("password");
        },
        handleConfirmPasswordInput() {
            this.$refs.passwordForm.validateField("confirmPassword");
        },
        // 提交修改密码
        submitForm() {
            this.$refs.passwordForm.validate(valid => {
                if (valid) {
                    let newPassword = { studentId: sessionStorage.getItem("studentId"), studentPassword: this.changePasswordForm.password }
                    this.$axios.post("http://192.168.1.119:8080/changeAccountPassword", newPassword).then((res2) => {
                        if (res2.data.code * 1 == 200) {
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
            if (this.studentInfo.pictureID * 1 == 99) {
                return `http://192.168.1.119/images/${sessionStorage.getItem("studentId")}.jpg?${Math.random() * 10}`
            }
            return require(`/src/assets/avatar/${this.studentInfo.pictureID}.jpg`);
        },
        // 图片库获取头像
        getFromAvatars(number) {
            return require(`/src/assets/avatar/${number}.jpg`);
        },
        // 更改用户头像
        selectAvatar(pictureId) {
            let updatedAvatar = { studentId: this.studentInfo.studentID, pictureId: pictureId };
            this.$axios.post("http://192.168.1.119:8080/modifyProfilePicture", updatedAvatar).then((updatedAvatarRes) => {
                if (updatedAvatarRes.data.code * 1 == 200) {
                    this.studentInfo.pictureID = pictureId;
                    this.$bus.$emit("changepic", pictureId)
                    this.imageUrl = '';
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
        },
        // 上传自定义头像
        handleAvatarSuccess(uploadRes, file) {
            if (uploadRes.status === '1') return this.$message.error(uploadRes.message)
            this.$message({
                showClose: true,
                message: '头像上传成功！',
                type: 'success'
            });
            this.$bus.$emit("changepic",0);
            this.imageUrl = URL.createObjectURL(file.raw);
            this.selectAvatar(99);
        },
        // 头像上传前校验
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        }
    }
};
</script>
  
<style>
.box-card {
    height: 660px;
}

.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

/* 上传按钮 */
.avatar-uploader-icon {
    color: #ffffff;
    top: 50px;
    width: 100px;
    height: 100px;
    line-height: 100px;
}
</style>