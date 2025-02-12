<template>
    <el-container class="teacherHome">
        <!-- 侧边栏 -->
        <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
            <!-- 教师基本信息 -->
            <el-row class="basicInfo">
                <el-col>
                    <!-- 头像 -->
                    <div class="teacherAvatar" style="text-align: center; width: 200px;">
                        <br>
                        <div class="block"><el-avatar :size="120" :src="getAvatarUrl()"></el-avatar></div>
                    </div>
                    <!-- 基本信息 -->
                    <div style="text-align: center;">
                        <div style="line-height: 1.5;"><strong>{{ teacherInfo.teacherName }}</strong></div>
                        <div style="line-height: 1.5;"><strong>{{ teacherInfo.teacherId }}</strong></div>
                        <div style="line-height: 1.5;"><strong>{{ teacherInfo.departmentName }}</strong></div>
                        <br>
                    </div>
                    <div style="position: absolute;top: 690px;left: 39px;font-size: 12px; text-align: center;">
                        <div><strong>教务处</strong></div>
                        <div>023-65103029</div>
                        <div>沙坪坝区沙正街174号</div>
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-menu class="teacherMenu" @select="select" router :default-openeds="['studentGrade']">
                        <el-menu-item index="teacherInformation">
                            <i class="el-icon-s-custom"></i>
                            <span slot="title">教师信息</span>
                        </el-menu-item>
                        <el-menu-item index="teacherCourses">
                            <i class="el-icon-date"></i>
                            <span slot="title">教师课表</span>
                        </el-menu-item>
                        <el-submenu index="studentGrade">
                            <template slot="title"><i class="el-icon-document-checked"></i>成绩管理</template>
                            <el-menu-item-group>
                                <el-menu-item index="gradeView">学生成绩查询</el-menu-item>
                                <el-menu-item index="gradeModification">学生成绩修改</el-menu-item>
                            </el-menu-item-group>
                        </el-submenu>
                    </el-menu>
                </el-col>
            </el-row>
        </el-aside>
        <el-container>
            <!-- 顶栏 -->
            <el-header style="text-align: center; color: rgb(92, 89, 89); height: 50px; line-height: 50px;">
                <!-- 根据用户类型进行修改 -->
                <strong>教师端</strong>
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
            teacherInfo: {
                teacherId: "无工号",
                teacherName: "匿名",
                departmentName: "无学院",
                pictureId: "0"
            }
        }
    },

    created() {
        this.$bus.$on("changeTeacherPic", (picId) => {
            this.teacherInfo.pictureId = picId
        })
        this.$bus.$on("updateTeacherBasicInfo", (department, teacherName) => {
            this.teacherInfo.departmentName = department;
            this.teacherInfo.teacherName = teacherName;
        })
        this.getTeacherInfo()
    },
    
    beforeDestroy() {
        this.$bus.$off("changeTeacherPic")
        this.$bus.$off("updateTeacherBasicInfo")
    },

    methods: {
        // 确认退出，返回登录界面
        confirmLogout() {
            sessionStorage.clear();
            this.$router.push('/');
        },
        getTeacherInfo() {
            let teacherInfo2 = { teacherId: sessionStorage.getItem("teacherId") }
            this.$axios.post("http://192.168.1.119:8080/teacherInfo", teacherInfo2).then((res) => {
                this.teacherInfo.teacherId = res.data.result[0].teacherID;
                this.teacherInfo.teacherName = res.data.result[0].teacherName;
                this.teacherInfo.departmentName = res.data.result[0].departmentName;
                this.teacherInfo.pictureId = res.data.result[0].pictureID;
            })
        },
        // 获得用户头像
        getAvatarUrl() {
            return require(`/src/assets/avatar/${this.teacherInfo.pictureId}.jpg`);
        },
        select(index) {
            this.$router.push("/teacher/" + index);
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
.teacherMenu {
    width: 197px;
}

/* 设置退出按钮样式 */
.exitButton {
    position: absolute;
    right: 10px;
    top: 8px;
}
</style>