<template>
  <div>
    <el-carousel height="600px">
      <el-carousel-item v-for="item in [img1, img2]" :key="item">
        <img :src="item" alt="" style="width: 100%; height: 600px">
      </el-carousel-item>
    </el-carousel>
    <div style="height: 95px; background-color: #cc0000; display: flex; justify-content: center; color: #eee">
      <div style="width: 40%; display: flex; align-items: center">
        <div @click="goPage('/front/myIdentification')" class="quick-item" style="flex: 1; display: flex; align-items: center; justify-content: center">
          <el-icon size="30" color="#eee"><CirclePlus /></el-icon>
          <span style="margin-left: 5px; font-size: 20px; ">加入代取员</span>
        </div>
        <div @click="goPage('/front/addOrder')" class="quick-item" style="flex: 1; display: flex; align-items: center; justify-content: center">
          <el-icon size="30" color="#eee"><Bicycle /></el-icon>
          <span style="margin-left: 5px; font-size: 20px; ">立刻下单</span>
        </div>
        <div @click="goPage('/front/orderDetail')" class="quick-item" style="flex: 1; display: flex; align-items: center; justify-content: center">
          <el-icon size="30" color="#eee"><Search /></el-icon>
          <span style="margin-left: 5px; font-size: 20px; ">订单查询</span>
        </div>
        <div @click="goPage('/front/courierCommission')" class="quick-item" style="flex: 1; display: flex; align-items: center; justify-content: center">
          <el-icon size="30" color="#eee"><Tickets /></el-icon>
          <span style="margin-left: 5px; font-size: 20px; ">提现规则</span>
        </div>
      </div>
    </div>

    <div style="margin: 30px auto; width: 70%">
      <div>
       <div style="text-align: center; font-size: 26px; font-weight: bold; margin-bottom: 30px">代取服务</div>
       <div>
         <el-tabs v-model="data.active">
           <el-tab-pane label="快递代取" name="1">
             <el-row :gutter="10">
               <el-col :span="6" @click="goPage('/front/addOrder?expressType=' + item.id)" v-for="item in data.typeList.filter(v => v.attribute === '快递')" :key="item.id">
                 <el-card style="max-width: 480px">
                   <template #header>
                     <img src="@/assets/imgs/q1.png" alt="" style="width: 100%; height: 240px">
                   </template>
                   <div>
                     <div class="quick-item" style="display: flex; align-items: center; margin-bottom: 10px">
                       <div style="flex: 1; font-size: 20px; font-weight: bold">{{ item.name }}</div>
                       <el-icon size="18"><DArrowRight /></el-icon>
                     </div>
                     <div style="color: #666; text-align: justify">为您提供"快速、准时、稳定"的高品质、门到门的标准代取服务</div>
                   </div>
                 </el-card>
               </el-col>
             </el-row>
           </el-tab-pane>
           <el-tab-pane label="外卖代取" name="2">
             <el-row :gutter="10">
               <el-col :span="6" @click="goPage('/front/addOrder?expressType=' + item.id)" v-for="item in data.typeList.filter(v => v.attribute === '外卖')" :key="item.id">
                 <el-card style="max-width: 480px">
                   <template #header>
                     <img src="@/assets/imgs/q2.png" alt="" style="width: 100%; height: 240px">
                   </template>
                   <div>
                     <div class="quick-item" style="display: flex; align-items: center; margin-bottom: 10px">
                       <div style="flex: 1; font-size: 20px; font-weight: bold">{{ item.name }}</div>
                       <el-icon size="18"><DArrowRight /></el-icon>
                     </div>
                     <div style="color: #666; text-align: justify">外卖服务，省时省力，高效便捷</div>
                   </div>
                 </el-card>
               </el-col>
             </el-row>
           </el-tab-pane>
           <el-tab-pane label="医药代取" name="3">
             <el-row :gutter="10">
               <el-col :span="6" @click="goPage('/front/addOrder?expressType=' + item.id)" v-for="item in data.typeList.filter(v => v.attribute === '医药')" :key="item.id">
                 <el-card style="max-width: 480px">
                   <template #header>
                     <img src="@/assets/imgs/q3.png" alt="" style="width: 100%; height: 240px">
                   </template>
                   <div>
                     <div class="quick-item" style="display: flex; align-items: center; margin-bottom: 10px">
                       <div style="flex: 1; font-size: 20px; font-weight: bold">{{ item.name }}</div>
                       <el-icon size="18"><DArrowRight /></el-icon>
                     </div>
                     <div style="color: #666; text-align: justify">医药代取，贴心呵护您的健康，为您的生活保驾护航</div>
                   </div>
                 </el-card>
               </el-col>
             </el-row>
           </el-tab-pane>
         </el-tabs>
       </div>
      </div>

      <div style="margin: 50px 0">
        <div style="text-align: center; font-size: 26px; font-weight: bold; margin-bottom: 30px; position: relative">
          校园资讯
          <el-button @click="goPage('/front/information')" type="primary" plain size="small" style="position: absolute; right: 0; top: 10px; border-radius: 15px">查看全部</el-button>
        </div>
        <div @click="router.push('/front/informationDetail?id=' + item.id)" class="card item" style="padding: 20px; display: flex; grid-gap: 20px; align-items: center; margin-bottom: 5px; cursor: pointer" v-for="item in data.tableData" :key="item.id">
          <img style="width: 200px; height: 150px; border-radius: 5px" :src="item.cover" alt="">
          <div>
            <div class="title" style="font-size: 24px; margin-bottom: 15px">{{ item.title }}</div>
            <div v-html="item.simpleContent" class="line3 simpleContent" style="color: #666; text-align: justify; line-height: 20px; height: 60px; margin-bottom: 15px"></div>
            <div style="color: #666">
              <el-icon size="16" style="top: 2px"><Clock /></el-icon> <span style="margin-right: 20px">{{ item.time }}</span>
              <el-icon size="16" style="top: 2px"><View /></el-icon> <span>{{ item.viewCount }}</span>
            </div>
          </div>
        </div>

      </div>
    </div>


  </div>
</template>

<script setup>
import img1 from '@/assets/imgs/轮播图1.jpg'
import img2 from '@/assets/imgs/轮播图2.jpg'
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  active: '1',
  typeList: [],
  tableData: []
})

const goPage = (path) => {
  location.href = path
}

request.get('/expressType/selectAll').then(res => {
  data.typeList = res.data
})

const load = () => {
  request.get('/information/selectPage', {
    params: {
      pageNum: 1,
      pageSize: 3,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>

<style scoped>
.quick-item {
  height: 100%;
}
.quick-item:hover {
  cursor: pointer;
  background-color: #fff;
  color: #cc0000;
}
.quick-item:hover .el-icon{
  color: #cc0000;
}

:deep(.el-tabs__item) {
  font-size: 20px !important;
}
:deep(.el-tabs__item.is-active) {
  color: #cc0000;
}
:deep(.el-tabs__active-bar.is-top) {
  background-color: #cc0000 !important;
}
:deep(.el-tabs__item:hover) {
  color:  #cc0000;
}

.item:hover .title{
  color: red;
}
.item:hover .simpleContent {
  color: #333 !important;
}
</style>