<template>
  <div style="width: 60%; margin: 10px auto; min-height: 500px; padding: 20px" class="card">
    <div style=" margin-bottom: 20px; display: flex">
      <div style="flex: 1; font-size: 24px;">运单详情</div>
      <div>
        <el-input style="width: 240px; margin-right: 5px" v-model="data.orderNo" placeholder="请输入订单号" clearable></el-input>
        <el-button type="primary" plain @click="load">查询</el-button>
      </div>
    </div>
    <div style="padding: 20px" v-if="data.orders?.id">
      <div style="display: flex; align-items: flex-start; grid-gap: 20px; margin-bottom: 40px">
        <div>
          <div style="font-size: 24px; font-weight: 500; margin-bottom: 10px">订单号：{{ data.orders.orderNo }}</div>
          <div>
            <el-tag style="padding: 15px;" type="warning" v-if="data.orders.status === '待接单'">待接单</el-tag>
            <el-tag style="padding: 15px;" type="primary" v-if="data.orders.status === '运输中'">运输中</el-tag>
            <el-tag style="padding: 15px;" type="primary" v-if="data.orders.status === '已送达'">已送达</el-tag>
            <el-tag style="padding: 15px;" type="success" v-if="data.orders.status === '已签收'">已签收</el-tag>
          </div>
        </div>
        <div style="flex: 1; display: flex; grid-gap: 20px; align-items: center">
          <div style="flex: 1; text-align: right">
            <div style="font-size: 24px; margin-bottom: 10px">取件地址</div>
            <div style="color: #666">{{ data.orders.pickupAddress }}</div>
          </div>
          <img style="width: 150px; height: 30px" src="@/assets/imgs/箭头.png" alt="">
          <div style="flex: 1; text-align: right">
            <div style="font-size: 24px;  margin-bottom: 10px">收件地址</div>
            <div style="color: #666">{{ data.orders.shippingAddress }}</div>
          </div>
        </div>
      </div>

      <div style="margin-bottom: 40px">
        <div style="font-size: 24px; color: red; border-bottom: 1px solid #ddd; padding-bottom: 15px">路由详情</div>
        <div style="padding: 20px 0">
          <el-timeline>
            <el-timeline-item :timestamp="data.orders.createTime" v-if="data.orders.createTime" color="orange" icon="Promotion">
              <div style="font-size: 24px; color: #333">待接单 <span style="color: #666; font-size: 20px">您的代取订单已发布，正在等待代取员接取任务。</span></div>
            </el-timeline-item>
            <el-timeline-item :timestamp="data.orders.accessTime" v-if="data.orders.accessTime" color="#0bbd87" icon="Present">
              <div style="font-size: 24px; color: #333">运输中 <span style="color: #666; font-size: 20px">您的代取任务已由{{ data.orders.courierName }}师傅接取,很快将送达。</span></div>
            </el-timeline-item>
            <el-timeline-item :timestamp="data.orders.deliverTime" v-if="data.orders.deliverTime" color="#1890ff" icon="Handbag">
              <div style="font-size: 24px; color: #333">已送达 <span style="color: #666; font-size: 20px">订单已送达，等待您的签收。</span></div>
            </el-timeline-item>
            <el-timeline-item :timestamp="data.orders.signTime" v-if="data.orders.signTime" color="green" icon="PriceTag">
              <div style="font-size: 24px; color: #333">已签收 <span style="color: #666; font-size: 20px">您已确认签收。</span></div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>

      <div style="font-size: 16px" v-if="data.orders.courierId">
        <div style="font-size: 24px; color: red; border-bottom: 1px solid #ddd; padding-bottom: 15px; margin-bottom: 20px">代取员信息</div>
        <div>
          <div style="display: flex; align-items: center; margin-bottom: 10px">
            <img :src="data.orders.courierAvatar" alt="" style="width: 30px; height: 30px; border-radius: 50%">
            <span style="margin-left: 10px">{{ data.orders.courierName }}</span>
          </div>
          <div style="margin-bottom: 10px">联系方式：{{ data.orders.courierPhone }}</div>
          <div>评分:
            <span v-if="data.orders.courierRate"><el-rate v-model="data.orders.courierRate" allow-half  disabled show-score score-template="{value} 星" text-color="#ff9900" /></span>
            <span v-else style="color: #666">暂无评分</span>
          </div>
        </div>

      </div>
    </div>
    <div v-else style="padding: 20px; color: #666; font-size: 24px; text-align: center">未查询单任何订单记录</div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import { MoreFilled, Present, Handbag, PriceTag } from "@element-plus/icons-vue"

const data = reactive({
  id: router.currentRoute.value.query.id,
  orderNo: null,
  orders: {}
})

const load = () => {
  if (data.orderNo) {
    request.get('/orders/selectByOrderNo/' + data.orderNo).then(res => {
      data.orders = res.data
    })
  } else {
    if (data.id) {
      request.get('/orders/selectById/' + data.id).then(res => {
        data.orders = res.data
      })
    }
  }
}
load()
</script>

<style scoped>

</style>