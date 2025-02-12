<template>
  <el-card class="card-container">
    <div class="container">
      <div class="demo-input-suffix">
        <el-tooltip content="输入格式示例：2" placement="top" effect="light">
          <el-input placeholder="请输入查询学期" v-model="term" clearable maxlength="1">
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
        </el-tooltip>
        <div @click="handleButtonClick">
          <el-button plain>查询</el-button>
        </div>
      </div>
      <div class="content-container">
        <div class="score-list rounded-box">
          <table>
            <thead>
              <tr>
                <th class="vertical-line">科目</th>
                <th class="vertical-line">成绩</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(subject, index) in subjects" :key="index" :class="rowClass(index)">
                <td class="vertical-line">{{ subject }}</td>
                <td class="vertical-line">{{ scores[index] }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="chart-container rounded-box">
          <div id="lineDemo" class="echart" ref="chart"></div>
          <div class="grade-text-container">
            <p>总绩点(四分制):{{ totalgpa4[0] }}</p>
            <p>总绩点(五分制):{{ totalgpa5[0] }}</p>
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>
  
<script>
import axios from "axios";
export default {
  name: "index",
  data() {
    return {
      term: "",
      myChart: null,
      subjects: [], //科目
      scores: [], //成绩
      gpas4: [], //四分制绩点
      terms5: [], //学期数
      gpas5: [], //五分制绩点
      totalgpa4: [], //四分制总绩点
      totalgpa5: [], //五分制总绩点
      isEvenRow: true,
      option: {
        title: {
          text: "学期绩点折线图",
          top: "5%",
          left: "center",
        },
        length: {
          data: [],
        },
        xAxis: [
          {
            type: "category",
            data: [], // 横坐标为年份
          },
        ],
        yAxis: {
          type: "value",
          min: 0, // 纵坐标最小值
          max: 5.0, // 纵坐标最大值
        },
        tooltip: {
          trigger: "axis",
        },
        series: [{
          name: "四分制",
          type: "line",
          data: [], // 纵坐标为绩点4，gpas4传入
          itemStyle: {
            color: "#1890ff", // 设置折线1的颜色为深蓝色
          },
        },
        {
          name: "五分制",
          type: "line",
          data: [], // 纵坐标为绩点5，gpas5传入
          itemStyle: {
            color: "#40a9ff", // 设置折线2的颜色为浅蓝色
          },
        },
        ],
      },
    };
  },

  computed: {
    rowClass() {
      return (index) => (this.isEvenRow ? "even-row" : "odd-row");
    },
  },

  //初始化echarts,更新图表数据
  mounted() {
    let chartDom = document.getElementById("lineDemo");
    let myChart = this.$echarts.init(chartDom);
    this.myChart = myChart;
    if (myChart) {
      // 如果已经存在实例，则直接使用现有实例进行操作
      myChart.setOption(this.option);
    } else {
      // 如果不存在实例，则进行初始化
      myChart = this.$echarts.init(chartDom);
      myChart.setOption(this.option);
    }
  },

  methods: {
    //获取后端数据
    handleButtonClick() {
      this.getData();
      this.getData1();
      this.getData2();
      this.getData3();
      this.getData4();
    },
    getData() {
      var data = {
        studentId: sessionStorage.getItem("studentId"),
        term: this.term,
      };
      axios.post("http://192.168.1.119:8080/queryGrades", data).then((res) => {
        if (res.data.code * 1 == 200) {
          this.$message({
            showClose: true,
            message: '查询成功！',
            type: 'success'
          });
          this.subjects = res.data.result.map((item) => item.courseName);
          this.scores = res.data.result.map((item) => {
            if (item.grade === null) {
              return "无成绩";
            } else {
              return item.grade;
            }
          });
        } else if (res.data.code * 1 == 201) {
          this.$message({
            showClose: true,
            message: '查询失败！',
            type: 'error'
          });
        }
      });
    },
    getData1() {
      var data = {
        studentId: sessionStorage.getItem("studentId"),
      };
      axios
        .post("http://192.168.1.119:8080/calculateGPA_5", data)
        .then((res) => {
          this.terms5 = res.data.result.map((item) => {
            //将对应学期转化
            switch (item.term) {
              case 1:
                return "大一上";
              case 2:
                return "大一下";
              case 3:
                return "大二上";
              case 4:
                return "大二下";
              case 5:
                return "大三上";
              case 6:
                return "大三下";
              case 7:
                return "大四上";
              case 8:
                return "大四下";
              default:
                return "";
            }
          });
          this.gpas5 = res.data.result.map((item) => item.gpa.toFixed(2));
          this.option.series[1].data = this.gpas5;
          this.option.xAxis[0].data = this.terms5;
          this.myChart.setOption(this.option);
        });
    },
    getData2() {
      var data = {
        studentId: sessionStorage.getItem("studentId"),
      };
      axios
        .post("http://192.168.1.119:8080/calculateTotalGPA_5", data)
        .then((res) => {
          this.totalgpa5 = res.data.result.map((item) => item.gpa.toFixed(2));
        });
    },
    getData3() {
      var data = {
        studentId: sessionStorage.getItem("studentId"),
      };
      axios
        .post("http://192.168.1.119:8080/calculateGPA_4", data)
        .then((res) => {
          this.gpas4 = res.data.result.map((item) => item.gpa.toFixed(2));
          this.option.series[0].data = this.gpas4;
          this.myChart.setOption(this.option);
        });
    },
    getData4() {
      var data = {
        studentId: sessionStorage.getItem("studentId"),
      };
      axios
        .post("http://192.168.1.119:8080/calculateTotalGPA_4", data)
        .then((res) => {
          this.totalgpa4 = res.data.result.map((item) => item.gpa.toFixed(2));
        });
    },
  },
};
</script>
    
<style scoped>
.container {
  display: flex;
  flex-direction: column;
}

.demo-input-suffix {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  width: 25%;
}

.content-container {
  display: flex;
}

.score-list {
  width: 60%;
  margin-right: 20px;
}

.chart-container {
  width: 40%;
  height: 300px;
}

.echart {
  width: 100%;
  height: 100%;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.vertical-line {
  border-right: 1px solid #ddd;
  /* 添加竖线 */
}

.rounded-box {
  border-radius: 10px;
  /* 添加圆角框 */
  border: 1px solid #ddd;
  padding: 10px;
}

.grade-text-container {
  background-color: #b3c5e0bf;
  /* 设置背景色 */
  border-radius: 10px;
  /* 设置圆角框的圆角半径 */
  padding: 5px;
  /* 设置内边距 */
  margin-top: 30px;
  text-align: center;
  width: 100%;
  height: 30%;
}

.card-container {
  height: 100%;
}

.even-row {
  background-color: #fff;
}

.odd-row {
  background-color: #e6e9ef;
}
</style>