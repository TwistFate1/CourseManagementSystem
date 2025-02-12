<template>
    <!-- 卡片视图 -->
    <el-card>
        <!-- 搜索 添加 -->
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
            <div><strong>当前学期：</strong>{{ formInline.classYear }}</div>
        </el-form>
        <!-- 课程列表区域 -->
        <el-table :data="classList" stripe style="width: 100%" height="550" size="medium" id="courseTable">
            <!-- stripe: 斑马条纹
          border：边框-->
            <el-table-column width=100 prop="courseID" label="课程号"></el-table-column>
            <el-table-column width=100 prop="classID" label="教学班号"></el-table-column>
            <el-table-column width=190 prop="courseName" label="课程名"></el-table-column>
            <el-table-column width=150 prop="teacherName" label="课程教师"></el-table-column>
            <el-table-column width=190 prop="classroomID" label="开设教室"></el-table-column>
            <el-table-column width=110 prop="departmentName" label="开设学院"></el-table-column>
            <el-table-column width=110 prop="hour" label="开设学时"></el-table-column>
            <el-table-column width=110 prop="credit" label="开设学分"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="danger" icon="el-icon-delete" size="small" circle
                        @click="removeById(scope.row)"></el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-card>
</template>
    
<script>
export default {
    data() {
        return {
            courseID: "",
            classID: "",
            courseName: "",
            teacherName: "",
            classroomID: "",
            departmentName: "",
            hour: "",
            credit: "",
            formInline: {
                id: '',
                classYear: ''
            },
            classList: [],
        };
    },

    created() {
        this.getTimeAndId();
    },

    methods: {
        getTimeAndId: function () {
            var data = {
                studentId: sessionStorage.getItem("studentId"),
            };
            this.formInline.id = data.studentId;
            //axios是一个异步的执行,在执行rest回答的同时执行外面的调用,如果函数调用在外面的话,参数调用应该为空
            this.$axios.post("http://192.168.1.119:8080/getTime")
                .then((rest) => {
                    this.formInline.classYear = rest.data;
                    this.getCourse(this.formInline);
                });
        },
        getCourse(course) {
            var data = {
                studentId: sessionStorage.getItem("studentId"),
            };
            let form = {
                studentId: data.studentId,
                classYear: course.classYear
            };
            this.$axios
                .post("http://192.168.1.119:8080/stuCourse", form)
                .then((resc) => {
                    this.classList = resc.data.result;
                });
        },
        // 退课
        async removeById(row) {
            var data = {
                studentId: sessionStorage.getItem("studentId"),
            };
            let courseForm = { classId: row.classID, studentId: data.studentId, classYear: this.formInline.classYear }
            let coForm = { id: data.studentId, classYear: this.formInline.classYear }
            const confirmResult = await this.$confirm("确定退课?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).catch((err) => err);
            // 点击确定 返回值为：confirm
            // 点击取消 返回值为： cancel
            if (confirmResult !== "confirm") {
                return this.$message.info("已取消退课");
            }
            const { data: res } = await this.$axios.post("http://192.168.1.119:8080/dropCourse", courseForm);
            if (res.code !== 200) return this.$message.error("退课失败！");
            this.$message.success("退课成功！");
            this.getCourse(coForm)
        },
    },
};
</script>
  
    
<style>
.el-select {
    position: relative;
    right: 9px;
    top: 5px;
}

#courseTable {
    position: relative;
    top: 10px;
}
</style>
    