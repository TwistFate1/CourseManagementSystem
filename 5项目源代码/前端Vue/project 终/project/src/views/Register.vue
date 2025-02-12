<template>
    <div id="background">
        <div id="contain">
            <el-button type="text" icon="el-icon-back" @click.prevent="backForm('loginForm')">返回</el-button>
            <h3>注册Register</h3>
            <div class="form">
                <label>入学年份：</label><input type="text" v-model.trim="id"><br />
            </div>
            <div class="form">
                <label>学生姓名：</label><input type="text" v-model.trim="name"><br />
            </div>
            <div class="form">
                <label>性别：</label>
                <el-radio-group v-model="gender">
                    <el-radio label="1">男</el-radio>
                    <el-radio label="0">女</el-radio>
                </el-radio-group>
            </div>
            <div class="form">
                <label>密码：</label><input type="password" v-model.trim="password"><br />
            </div>
            <div class="form">
                <label>确认密码：</label><input type="password" v-model.trim="confirmPassword"><br />
            </div>
            <div>
                <p v-if="password == '' || confirmPassword == ''"></p>
                <p v-else-if="password == confirmPassword" style="color: green;">密码和确认密码一致。</p>
                <p v-else style="color: red;">密码和确认密码不一致。</p>
            </div>
            <div class="form">
                <label>邮箱：</label><input type="email" v-model.trim="mail"><br />
            </div>
            <div class="form">
                <label>专业选择:</label>
                <select id="major" v-model="selectedMajor" style="height:30px;width:210px">
                    <option v-for="major in majorList" :key="major.specialityId" :value="major.specialityId">
                        {{ major.specialityName }}
                    </option>
                </select>
            </div>
            <div>
                <button class="button" @click.prevent="handlefinish">提交</button>
                <button class="button" @click="resetForm">重置</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Register',
    props: {
        msg: String
    },
    data() {
        return {
            id: "",
            name: "",
            gender: "",
            password: "",
            confirmPassword: "",
            mail: "",
            selectedMajor: "",
            majorList: [],
        };
    },

    created() {
        // 在组件创建时调用fetchData方法，获取列表数据
        this.findmajors();
    },

    methods: {
        findmajors: function () {
            this.$axios.post("http://192.168.1.119:8080/registSpeciality")
                .then((result) => {
                    this.majorList = result.data.result;
                })
        },
        backForm() {
            this.$router.push('/');
        },
        resetForm() {
            this.id = "";
            this.name = "";
            this.gender = "";
            this.password = "";
            this.confirmPassword = "";
            this.mail = "";
            this.selectedMajor = "";
            this.findmajors();
        },
        //点击完成按钮触发handlefinish
        handlefinish: function () {
            const that = this;
            if (this.id === '') {
                alert("入学年份不能为空");
            }
            else if (localStorage['name'] === this.name) {
                alert("学生姓名已存在");
            }
            else if (this.name === '') {
                alert("学生姓名不能为空");
            }
            else if (this.gender === '') {
                alert("学生性别不能为空");
            }
            else if (this.password.length < 8 || this.password.length >= 20) {
                alert("密码长度必须大于等于8位且小于20位");
            }
            else if (this.password !== this.confirmPassword) {
                alert("密码输入不一致，请重新输入");
            }
            else if (this.mail === '') {
                alert("学生邮箱不能为空");
            }
            else if (this.selectedMajor === '') {
                alert("专业不能为空");
            }
            else {//将新用户信息存储到localStorage
                localStorage.setItem('id', this.id);
                localStorage.setItem('name', this.name);
                localStorage.setItem('gender', this.gender);
                localStorage.setItem('password', this.password);
                localStorage.setItem('mail', this.mail);
                localStorage.setItem('major', this.major);
                //alert("注册成功");
                let form = {
                    studentId: this.id, studentName: this.name, studentGender: this.gender, studentPassword: this.password,
                    studentEmail: this.mail, specialityId: this.selectedMajor
                }
                // 发送注册(post)请求
                this.$axios
                    .post("http://192.168.1.119:8080/studentRegister", form)
                    .then(function (resp) {
                        if (resp.data.code * 1 == 200) {
                            // 弹窗提示注册成功
                            alert("注册成功，您的用户账号为" + resp.data.result);
                            // 注册成功后直接跳转到登录页
                            that.$router.push('/');
                        } else if (resp.data.code * 1 == 201) {
                            alert("用户名已注册，请更换");
                        }
                    });
            }
        }
    }
};
</script>

<style scoped>
#background {
    width: 100%;
    height: 100%;
    background: url("../assets/背景-冰雪.JPG");
    background-size: 100% 100%;
    position: fixed;
    top: 0;
    left: 0;
}

#contain {
    width: 400px;
    height: 520px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    /*background: #fffdfd;*/
    background-color: rgba(255, 255, 255, 0.9);
    text-align: center;
    border-radius: 20px;
}

#contain h2 {
    color: black;
}

#major {
    margin-left: 10px;
    width: 200px;
    height: 20px;
}

.form {
    color: black;
    margin-left: 10%;
    margin-top: 20px;
    font-size: 15px;
    text-align: left;
}

option {
    margin-left: 10px;
    width: 200px;
    height: 30px;
    size: 50px;
}

label {
    float: left;
    width: 5em;
    margin-right: 1em;
    text-align: right;
}

input,
textarea {
    margin-left: 10px;
    padding: 4px;
    border: solid 1px #000000;
    outline: 0;
    font: normal 13px/100% Verdana, Tahoma, sans-serif;
    width: 200px;
    height: 20px;
    background: #ffffff;
}

input:hover,
textarea:hover,
input:focus,
textarea:focus {
    border-color: #70d8f1;
}

.el-button {
    position: relative;
    right: 150px;
    top: 5px;
}

.button {
    position: relative;
    right: 8px;
    top: 3px;
}

.button {
    /* 文字颜色 */
    color: #0099CC;
    /* 清除背景色 */
    background: transparent;
    /* 边框样式、颜色、宽度 */
    border: 2px solid #0099CC;
    /* 给边框添加圆角 */
    border-radius: 6px;
    /* 字母转大写 */
    border: none;
    color: white;
    padding: 7px 32px;
    text-align: center;
    display: inline-block;
    font-size: 15px;
    margin: 8px 2px;
    -webkit-transition-duration: 0.4s;
    /* Safari */
    transition-duration: 0.4s;
    cursor: pointer;
    text-decoration: none;
    text-transform: uppercase;
    justify-content: center;
}

.button {
    background-color: white;
    color: black;
    border: 2px solid #4682B4;
}

/* 悬停样式 */
.button:hover {
    background-color: #4682B4;
    color: white;
}
</style>