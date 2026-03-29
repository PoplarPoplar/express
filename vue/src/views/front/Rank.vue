<template>
  <div style="width: 60%; margin: 10px auto">
    <div style="display: flex; grid-gap: 20px">
      <div class="card" style="flex: 1; padding: 20px">
        <div style="display: flex; grid-gap: 10px; justify-content: center; align-items: center; margin-bottom: 30px">
          <img src="@/assets/imgs/rateRank.png" alt="" style="width: 50px">
          <div style="font-size: 20px; font-weight: bold">十佳榜单（评分）</div>
        </div>
        <div style="display: flex; grid-gap: 10px; padding: 10px 0; border-bottom: 1px solid #eee" v-for="(item, index) in data.rateCourierList" :key="item.id">
          <img :src="item.avatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
          <div style="flex: 1; height: 50px">
            <div>{{ item.name }}</div>
            <div style="display: flex; align-items: center">评分： <el-rate v-if="item.rate" v-model="item.rate" allow-half  disabled show-score score-template="{value} 星" text-color="#ff9900" /></div>
          </div>
          <div style="font-size: 16px; color: red" v-if="index === 0">Top {{ index + 1 }}</div>
          <div style="font-size: 16px; color: orange" v-else-if="index === 1">Top {{ index + 1 }}</div>
          <div style="font-size: 16px; color: saddlebrown"  v-else-if="index === 2">Top {{ index + 1 }}</div>
          <div style="font-size: 16px;" v-else>Top {{ index + 1 }}</div>
        </div>
      </div>

      <div class="card" style="flex: 1; padding: 20px">
        <div style="display: flex; grid-gap: 10px; justify-content: center; align-items: center; margin-bottom: 30px">
          <img src="@/assets/imgs/numRank.png" alt="" style="width: 50px">
          <div style="font-size: 20px; font-weight: bold">奋斗之星（接单数）</div>
        </div>
        <div style="display: flex; grid-gap: 10px; padding: 10px 0; border-bottom: 1px solid #eee" v-for="(item, index) in data.numCourierList" :key="item.id">
          <img :src="item.avatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
          <div style="flex: 1; height: 50px">
            <div style="margin-bottom: 10px">{{ item.name }}</div>
            <div style="display: flex; align-items: center">接单数： {{ item.orderNum }}（单）</div>
          </div>
          <div style="font-size: 16px; color: red" v-if="index === 0">Top {{ index + 1 }}</div>
          <div style="font-size: 16px; color: orange" v-else-if="index === 1">Top {{ index + 1 }}</div>
          <div style="font-size: 16px; color: saddlebrown"  v-else-if="index === 2">Top {{ index + 1 }}</div>
          <div style="font-size: 16px;" v-else>Top {{ index + 1 }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";

const data = reactive({
  rateCourierList: [],
  numCourierList: []
})

const load = () => {
  request.get('/student/selectRateRank').then(res => {
    data.rateCourierList = res.data
  })

  request.get('/student/selectNumRank').then(res => {
    data.numCourierList = res.data
  })
}
load()
</script>

<style scoped>

</style>