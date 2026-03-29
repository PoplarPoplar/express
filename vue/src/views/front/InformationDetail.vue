<template>
  <div class="card" style="width: 60%; margin: 10px auto; padding: 20px">
    <div style="font-size: 24px; text-align: center; margin-bottom: 20px">{{ data.information.title }}</div>
    <div style="color: #666; margin-bottom: 30px; text-align: center">
      <el-icon size="16" style="top: 2px"><Clock /></el-icon> <span style="margin-right: 20px">{{ data.information.time }}</span>
      <el-icon size="16" style="top: 2px"><View /></el-icon> <span>{{ data.information.viewCount }}</span>
    </div>
    <div style="padding: 0 20px" v-html="data.information.content"></div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id: router.currentRoute.value.query.id,
  information: {}
})

request.put('/information/updateViewCount/' + data.id).then(res => {
  if (res.code === '200') {
    request.get('/information/selectById/' + data.id).then(res1 => {
      data.information = res1.data

      window.scrollTo({ top: 0 })  // 页面滚动到最顶部
    })
  }
})

</script>

<style>

</style>