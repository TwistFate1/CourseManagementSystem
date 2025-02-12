<template>
  <el-card>
    <div class="container">
      <div class="top-bar">
        <el-tooltip content="输入示例：2" placement="top" effect="light">
          <el-input placeholder="请输入教学班号" v-model="classId" clearable maxlength="3" minlength="1">
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
        </el-tooltip>
        <div class="space"></div>
        <el-tooltip content="输入示例：2021秋" placement="top" effect="light">
          <el-input placeholder="请输入开设年份" v-model="classYear" clearable maxlength="5" minlength="1">
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
          </el-input>
        </el-tooltip>
        <div class="space"></div>
        <div @click="getData">
          <el-button size="small" class="custom-button">查询</el-button>
        </div>
      </div>
      <div class="chart-and-words rounded-box">
        <div class="echart" id="lineDemo" ref="chart"></div>
        <div id="word" class="words">
          <p class="bold-text">平均分：{{ averageScore }}</p>
          <p class="bold-text">最高分：{{ highestScore }}</p>
          <p class="bold-text">最低分：{{ lowestScore }}</p>
          <p class="bold-text">合格率：{{ passRate }}%</p>
          <p class="bold-text">优秀率：{{ excellentRate }}%</p>
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
      classId: "",
      classYear: "",
      myChart: null,
      scores: [], // 学生的成绩
      option: {
        color: ["#003399", "#0055A7", "#0077CC", "#0099FF", "#66B3FF"],
        title: {
          text: "学生成绩分布饼状图",
          top: "5%",
          left: "center",
        },
        tooltip: {
          trigger: "item",
        }, //鼠标悬停
        legend: {
          orient: "vertical",
          left: "left",
          top: "20%",
          data: ["90以上", "80到89", "70到79", "60到69", "60以下"],
        }, //竖列图标
        series: [{
          name: "该分数段人数",
          type: "pie",
          radius: "55%",
          center: ["60%", "50%"],
          data: [],
          itemStyle: {
            normal: {
              label: {
                show: true,
                formatter: "{b} ",
              },
              labelLine: { show: true },
            },
          },
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
        },
        ],
      },
    };
  },

  computed: {
    //平均分
    averageScore() {
      const sum = this.scores.reduce((total, score) => total + score, 0);
      const rate = sum / this.scores.length;
      return rate.toFixed(2);
    },
    //最高分
    highestScore() {
      return Math.max(...this.scores);
    },
    //最低分
    lowestScore() {
      return Math.min(...this.scores);
    },
    //合格率
    passRate() {
      const passCount = this.scores.filter((score) => score >= 60).length;
      const rate = (passCount / this.scores.length) * 100;
      return rate.toFixed(2);
    },
    //优秀率
    excellentRate() {
      const excellentCount = this.scores.filter((score) => score >= 90).length;
      const rate = (excellentCount / this.scores.length) * 100;
      return rate.toFixed(2);
    },
  },

  //初始化echarts,更新图表数据
  mounted() {
    let chartDom = document.getElementById("lineDemo");
    let myChart = this.$echarts.init(chartDom);
    this.myChart = myChart;
    if (myChart) {
      // 如果已经存在实例，则直接使用现有实例进行操作
      this.updateChartData();
      myChart.setOption(this.option);
    } else {
      // 如果不存在实例，则进行初始化
      myChart = this.$echarts.init(chartDom);
      this.updateChartData();
      myChart.setOption(this.option);
    }
  },

  watch: {
    scores: {
      handler() {
        this.updateChartData();
      },
      deep: true,
    },
  },

  // 获取后端数据
  methods: {
    getData() {
      var data = {
        classId: this.classId,
        classYear: this.classYear,
      };
      axios.post("http://192.168.1.119:8080/teacherScore", data).then((res) => {
        if (res.data.code * 1 == 200) {
          this.$message({
            showClose: true,
            message: '查询成功！',
            type: 'success'
          });
          this.scores = res.data.result.map((item) => item.grade);
          this.updateChartData();
          this.myChart.setOption(this.option);
        } else if (res.data.code * 1 == 201) {
          this.$message({
            showClose: true,
            message: '查询失败！',
            type: 'error'
          });
        }
      });
    },
    //成绩分段计数
    updateChartData() {
      const count = [0, 0, 0, 0, 0];
      this.scores.forEach((score) => {
        if (score >= 90) {
          count[0]++;
        } else if (score >= 80) {
          count[1]++;
        } else if (score >= 70) {
          count[2]++;
        } else if (score >= 60) {
          count[3]++;
        } else {
          count[4]++;
        }
      });
      const newData = [
        { value: count[0], name: "90以上" },
        { value: count[1], name: "80到89" },
        { value: count[2], name: "70到79" },
        { value: count[3], name: "60到69" },
        { value: count[4], name: "60以下" },
      ];
      this.option.series[0].data = newData;
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  width: 600px;
  margin-top: 20px;
}

.space {
  width: 20px;
  /* 设置间距的宽度 */
}

.chart-and-words {
  display: flex;
  justify-content: space-between;
  width: 900px;
  margin-top: 40px;
  margin-left: 160px;
}

.words {
  color: #4682b4;
  width: 300px;
  height: 100px;
  margin-top: 80px;
  margin-right: 0px;
  display: inline-block;
}

.echart {
  width: 500px;
  height: 460px;
  margin-top: 0px;
  margin-left: 10px;
  display: inline-block;
}

.custom-button {
  font-size: 16px;
  padding: 10px 20px;
}

.rounded-box {
  border: 1px solid #a4abb1;
  border-radius: 10px;
  padding: 10px;
  margin-left: -20px;
  margin-right: -10px;
}

.bold-text {
  font-weight: bold;
}
</style>
