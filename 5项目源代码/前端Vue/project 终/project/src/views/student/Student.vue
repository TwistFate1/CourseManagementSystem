<template>
    <el-container class="studentHome">
        <!-- 侧边栏 -->
        <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
            <!-- 学生基本信息 -->
            <el-row>
                <el-col :span="24">
                    <!-- 头像 -->
                    <div class="studentAvatar" style="text-align: center; width: 200px; ">
                        <br>
                        <div class="block"><el-avatar :size="120" :src="getAvatarUrl()"></el-avatar></div>
                    </div>
                    <!-- 基本信息 -->
                    <div style="text-align: center; width: 200px;">
                        <div style="line-height: 1.5;"><strong>{{ studentInfo.studentName }}</strong></div>
                        <div style="line-height: 1.5;"><strong>{{ studentInfo.studentId }}</strong></div>
                        <div style="line-height: 1.5;"><strong>{{ studentInfo.specialityName }}</strong></div>
                        <br>
                    </div>
                    <div style="position: absolute;top: 680px;left: 39px;font-size: 12px; text-align: center;">
                        <div><strong>教务处</strong></div>
                        <div>023-65103029</div>
                        <div>沙坪坝区沙正街174号</div>
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <el-col>
                    <el-menu class="studentMenu" @select="select" router :default-openeds="['studentCourse']">
                        <el-menu-item index="studentSchedule">
                            <i class="el-icon-date"></i>
                            <span slot="title">学生课表</span>
                        </el-menu-item>
                        <el-submenu index="studentCourse">
                            <template slot="title"><i class="el-icon-circle-check"></i>选课管理</template>
                            <el-menu-item-group>
                                <el-menu-item index="courseSelection">选课</el-menu-item>
                                <el-menu-item index="selectedCourses">已选课程</el-menu-item>
                            </el-menu-item-group>
                        </el-submenu>
                        <el-menu-item index="studentGrade">
                            <i class="el-icon-document"></i>
                            <span slot="title">成绩查询</span>
                        </el-menu-item>
                        <el-menu-item index="studentInformation">
                            <i class="el-icon-s-custom"></i>
                            <span slot="title">个人信息</span>
                        </el-menu-item>
                    </el-menu>
                </el-col>
            </el-row>
        </el-aside>
        <el-container>
            <!-- 顶栏 -->
            <el-header style="text-align: center; color: rgb(92, 89, 89); height: 50px; line-height: 50px;">
                <!-- 根据用户类型进行修改 -->
                <strong>学生端</strong>
                <template>
                    <el-popconfirm title="确定退出登录吗？" @confirm="confirmLogout()">
                        <el-button type="info" plain size="medium" class="exitButton" slot="reference"><span
                                style="color: rgb(64, 63, 63);">退出</span></el-button>
                    </el-popconfirm>
                </template>
            </el-header>
            <!-- 主要区域 -->
            <el-main>
                <transition>
                    <router-view class="router"></router-view>
                </transition>
            </el-main>
        </el-container>
    </el-container>
</template>
  
<script>
export default {
    data() {
        return {
            studentInfo: {
                studentId: "无学号",
                studentName: "匿名",
                specialityName: "无专业",
                pictureId: "0"
            }
        }
    },

    created() {
        this.$bus.$on("changepic", (picid) => {
            this.studentInfo.pictureId = picid
            this.getAvatarUrl();
        })
        this.$bus.$on("updateBasicInfo", (speciality, name) => {
            this.studentInfo.specialityName = speciality;
            this.studentInfo.studentName = name;
        })
        this.getStudentInfo()
    },

    beforeDestroy() {
        this.$bus.$off("changepic")
        this.$bus.$off("updateBasicInfo")
    },

    methods: {
        // 确认退出，返回登录界面
        confirmLogout() {
            sessionStorage.clear();
            this.$router.push('/');
        },
        // 获取用户信息
        getStudentInfo() {
            let studentInfo2 = { studentId: sessionStorage.getItem("studentId") }
            this.$axios.post("http://192.168.1.119:8080/viewPersonalInformation", studentInfo2).then((res) => {
                this.studentInfo.studentId = res.data.result[0].studentID;
                this.studentInfo.studentName = res.data.result[0].studentName;
                this.studentInfo.specialityName = res.data.result[0].specialityName;
                this.studentInfo.pictureId = res.data.result[0].pictureID;
            })
        },
        // 获得用户头像
        getAvatarUrl() {
            if (this.studentInfo.pictureId * 1 == 99) {
                return `http://192.168.1.119/images/${sessionStorage.getItem("studentId")}.jpg?${Math.random() * 1}`
            }
            return require(`/src/assets/avatar/${this.studentInfo.pictureId}.jpg`);
        },
        // 菜单选择
        select(index) {
            this.$router.push("/student/" + index);
        }
    }
};
</script>
  
<style>
/* 整体 */
.el-container {
    padding: 0;
    margin: 0;
    height: 100vh;
}

/* 顶部 */
.el-header {
    background-color: #9dc0ef;
    color: #333;
}

/* 侧边栏 */
.el-aside {
    color: #333;
    height: 100vh;
}

/* 设置菜单样式 */
.studentMenu {
    width: 197px;
}

/* 设置退出按钮样式 */
.exitButton {
    position: absolute;
    right: 10px;
    top: 8px;
}
</style>