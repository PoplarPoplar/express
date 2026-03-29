<template>
  <div style="width: 50%; margin: 10px auto; min-height: 500px">
    <div v-if="data.total">
      <div class="card" v-for="item in data.tableData" :key="item.id" style="padding: 20px; margin-bottom: 10px; display: flex; align-items: center; grid-gap: 20px">
        <el-image :src="item.pickupCode" :preview-src-list="[item.pickupCode]" alt="" style="width: 240px; height: 150px"></el-image>
        <div style="flex: 1">
          <div style="margin-bottom: 5px; font-size: 20px; font-weight: bold">{{ item.typeName }} <span style="color: red; margin-left: 20px">￥{{ item.price }}</span></div>
          <div style="margin-bottom: 5px"><span style="color: #666; ">快递简介：</span> <sapn style="color: #333">{{ item.descr }}</sapn></div>
          <div style="display: flex; margin-bottom: 5px">
            <div style="margin-bottom: 5px; flex: 1"><span style="color: #666;">取件地址：</span> <sapn style="color: #333">{{ item.pickupAddress }}</sapn></div>
            <div style="margin-bottom: 5px; flex: 1"><span style="color: #666;">收件地址：</span> <sapn style="color: #333">{{ item.shippingAddress }}</sapn></div>
          </div>
          <div style="display: flex; margin-bottom: 5px">
            <div style="margin-bottom: 5px; flex: 1"><span style="color: #666;">收件人：</span> <sapn style="color: #333">{{ item.customerName }}</sapn></div>
            <div style="margin-bottom: 5px; flex: 1"><span style="color: #666;">联系方式：</span> <sapn style="color: #333">{{ item.phone }}</sapn></div>
          </div>
          <div style="text-align: right">
            <el-button type="primary" @click="deliverOrder(item)">已送达</el-button>
          </div>
        </div>
      </div>
      <div style="margin-top: 15px" >
        <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
      </div>
    </div>

    <div v-else class="card" style="padding: 20px; text-align: center; color: #666; font-size: 20px">暂无可接取的订单...</div>


  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
})

const load = () => {
  request.get('/orders/selectCourierPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      status: '运输中',
      courierId: data.user.id
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

const deliverOrder = (row) => {
  ElMessageBox.confirm('您确认订单已送达吗？', '操作确认', { type: 'warning' }).then(res => {
    let formData = JSON.parse(JSON.stringify(row))
    formData.status = '已送达'
    request.put('/orders/update', formData).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}
</script>

<style scoped>

</style>