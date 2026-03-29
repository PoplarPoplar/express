<template>
  <div>
   <div style="display: flex; grid-gap: 10px; margin-bottom: 10px">
     <div style="flex: 1; padding: 20px; display: flex; align-items: center" class="card">
       <div style="flex: 1; text-align: center">
         <img src="@/assets/imgs/1.png" alt="" style="width: 80px; height: 80px">
       </div>
       <div style="flex: 1; font-size: 20px">
         <div style="margin-bottom: 10px">代取订单数</div>
         <div style="font-weight: bold">{{ data.count.orderNum }}（件）</div>
       </div>
     </div>

     <div style="flex: 1; padding: 20px; display: flex; align-items: center" class="card">
       <div style="flex: 1; text-align: center">
         <img src="@/assets/imgs/2.png" alt="" style="width: 80px; height: 80px">
       </div>
       <div style="flex: 1; font-size: 20px">
         <div style="margin-bottom: 10px">代取订单金额</div>
         <div style="font-weight: bold">{{ data.count.orderTotal }}（元）</div>
       </div>
     </div>

     <div style="flex: 1; padding: 20px; display: flex; align-items: center" class="card">
       <div style="flex: 1; text-align: center">
         <img src="@/assets/imgs/3.png" alt="" style="width: 80px; height: 80px">
       </div>
       <div style="flex: 1; font-size: 20px">
         <div style="margin-bottom: 10px">提现金额</div>
         <div style="font-weight: bold">{{ data.count.cashTotal }}（元）</div>
       </div>
     </div>

     <div style="flex: 1; padding: 20px; display: flex; align-items: center" class="card">
       <div style="flex: 1; text-align: center">
         <img src="@/assets/imgs/4.png" alt="" style="width: 80px; height: 80px">
       </div>
       <div style="flex: 1; font-size: 20px">
         <div style="margin-bottom: 10px">代取员总数</div>
         <div style="font-weight: bold">{{ data.count.courierCount }}（人）</div>
       </div>
     </div>

     <div style="flex: 1; padding: 20px; display: flex; align-items: center" class="card">
       <div style="flex: 1; text-align: center">
         <img src="@/assets/imgs/5.png" alt="" style="width: 80px; height: 80px">
       </div>
       <div style="flex: 1; font-size: 20px">
         <div style="margin-bottom: 10px">学生总数</div>
         <div style="font-weight: bold">{{ data.count.studentCount }}（人）</div>
       </div>
     </div>
   </div>

    <!--    折线图-->
    <div class="card" style="padding: 20px; height: 400px" id="line"></div>

    <div style="margin-top: 10px; margin-bottom: 50px">
      <el-row :gutter="10">
       <el-col :span="12">
      <!--    饼图     -->
         <div id="pie" style="height: 400px; padding: 20px" class="card"></div>
       </el-col>

        <el-col :span="12">
          <!--    柱状图     -->
          <div id="bar" style="height: 400px; padding: 20px" class="card"></div>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script setup>

import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from 'echarts'

const lineOption = {
  title: {
    text: '近一周代取订单金额趋势图',
    subtext: '折线图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'line',
      smooth: true
    },
  ]
}

const pieOption = {
  title: {
    text: '不同等级代取员的分布',
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      center: ['50%', '60%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}

const barOption = {
  title: {
    text: '不同类型的的代取订单数量',
    subtext: '柱状图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      interval: 0,
      rotate: 45
    }
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar',
      smooth: true,
      itemStyle: {
        normal: {
          color: function(params) { // 根据索引返回对应的颜色
            let colorList = ['#ffaa2e','#32C5E9','#fa4c4c','#08b448','#FFDB5C','#ff9f7f','#fb7293','#E062AE','#E690D1','#e7bcf3']
            return colorList[params.dataIndex];
          }
        }
      },
    }
  ]
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  count: {}
})

const loadCount = () => {
  request.get('/count').then(res => {
    if (res.code === '200') {
      data.count = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadCount()

onMounted(() => {
  // 请求数据  初始化图表
  // 折线图
  let lineDom = document.getElementById('line');
  let lineChart = echarts.init(lineDom);
  request.get('/line').then(res => {
    lineOption.xAxis.data = res.data.dateStrList
    lineOption.series[0].data = res.data.totalList
    lineChart.setOption(lineOption)
  })

  // 饼图
  let pieDom = document.getElementById('pie');
  let pieChart = echarts.init(pieDom);
  request.get('/pie').then(res => {
    pieOption.series[0].data = res.data
    pieChart.setOption(pieOption)
  })

  // 柱状图
  let barDom = document.getElementById('bar');
  let barChart = echarts.init(barDom);
  request.get('/bar').then(res => {
    barOption.xAxis.data = res.data.typeNameList
    barOption.series[0].data = res.data.countList
    barChart.setOption(barOption)
  })

})
</script>