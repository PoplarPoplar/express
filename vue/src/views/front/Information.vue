<template>
  <div style="width: 60%; margin: 10px auto; min-height: 500px">
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

    <div style="margin-top: 20px" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
})

const load = () => {
  request.get('/information/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>

<style scoped>
.item:hover .title{
  color: red;
}
.item:hover .simpleContent {
  color: #333 !important;
}
</style>