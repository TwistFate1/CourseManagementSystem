<template>
    <el-container class="adminHome">
        <!-- 侧边栏 -->
        <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
            <!-- 管理员基本信息 -->
            <el-row>
                <el-col>
                    <!-- 头像 -->
                    <div class="adminAvatar" style="text-align: center; width: 200px; ">
                        <br>
                        <div class="block"><el-avatar :size="120"
                                :src="require(`/src/assets/avatar/admin.jpg`)"></el-avatar></div>
                    </div>
                    <!-- 基本信息 -->
                    <div style="text-align: center;">
                        <div style="line-height: 1.5;"><strong>{{ adminInfo.adminName }}</strong></div>
                        <div style="line-height: 1.5;"><strong>{{ adminInfo.adminId }}</strong></div>
                        <br>
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <el-col>
                    <el-menu class="adminMenu" @select="select" router :default-openeds="['infoManagement']">
                        <el-submenu index="infoManagement">
                            <template slot="title"><i class="el-icon-document"></i>信息管理</template>
                            <el-menu-item-group>
                                <el-menu-item index="teacherManagement">教师信息管理</el-menu-item>
                                <el-menu-item index="studentManagement">学生信息管理</el-menu-item>
                            </el-menu-item-group>
                        </el-submenu>
                        <el-menu-item index="courseManagement">
                            <i class="el-icon-notebook-1"></i>
                            <span slot="title">课程管理</span>
                        </el-menu-item>
                        <el-menu-item index="courseSelectedManagement">
                            <i class="el-icon-date"></i>
                            <span slot="title">排课管理</span>
                        </el-menu-item>
                        <el-menu-item index="gradeManagement">
                            <i class="el-icon-data-line"></i>
                            <span slot="title">成绩管理</span>
                        </el-menu-item>
                    </el-menu>
                </el-col>
            </el-row>
        </el-aside>

        <el-container>
            <!-- 顶栏 -->
            <el-header style="text-align: center; color: rgb(92, 89, 89); height: 50px; line-height: 50px;">
                <!-- 根据用户类型进行修改 -->
                <strong>管理员端</strong>
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
            adminInfo: {
                adminId: "无",
                adminName: "匿名",
            }
        }
    },

    created() {
        this.getAdminInfo()
    },

    methods: {
        // 确认退出，返回登录界面
        confirmLogout() {
            sessionStorage.clear();
            this.$router.push('/');
        },
        getAdminInfo() {
            let adminInfo2 = { academicId: sessionStorage.getItem("adminId") }
            this.$axios.post("http://192.168.1.119:8080/queryAcademicInformation", adminInfo2).then((adminRes) => {
                this.adminInfo.adminId = adminRes.data.result.academicId;
                this.adminInfo.adminName = adminRes.data.result.academicName;
            })
        },
        select(index) {
            this.$router.push("/admin/" + index);
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
.adminMenu {
    width: 197px;
}

/* 设置退出按钮样式 */
.exitButton {
    position: absolute;
    right: 10px;
    top: 8px;
}
</style>