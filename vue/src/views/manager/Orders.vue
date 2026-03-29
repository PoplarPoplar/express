<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.orderNo" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入订单号查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="expand">
          <template #default="scope">
            <div style="padding: 10px">
              <div style="font-size: 16px; font-weight: bold; margin-bottom: 10px">订单详细信息</div>
              <el-descriptions border>
                <el-descriptions-item label="订单号">{{ scope.row.orderNo }}</el-descriptions-item>
                <el-descriptions-item label="代取员">{{ scope.row.courierName }}</el-descriptions-item>
                <el-descriptions-item label="发布学生">{{ scope.row.studentName }}</el-descriptions-item>
                <el-descriptions-item label="代取物品类型">{{ scope.row.typeName }}</el-descriptions-item>
                <el-descriptions-item label="订单价格">{{ scope.row.price }}</el-descriptions-item>
                <el-descriptions-item label="小费">{{ scope.row.tip }}</el-descriptions-item>
                <el-descriptions-item label="代取订单简介">{{ scope.row.descr }}</el-descriptions-item>
                <el-descriptions-item label="取件地址">{{ scope.row.pickupAddress }}</el-descriptions-item>
                <el-descriptions-item label="收件地址">{{ scope.row.shippingAddress }}</el-descriptions-item>
                <el-descriptions-item label="收件人名称">{{ scope.row.customerName }}</el-descriptions-item>
                <el-descriptions-item label="收件人手机号">{{ scope.row.phone }}</el-descriptions-item>
                <el-descriptions-item label="取件码截图">
                  <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.pickupCode" :preview-src-list="[scope.row.pickupCode]" preview-teleported></el-image>
                </el-descriptions-item>
                <el-descriptions-item label="订单创建时间">{{ scope.row.createTime }}</el-descriptions-item>
                <el-descriptions-item label="订单接取时间">{{ scope.row.accessTime }}</el-descriptions-item>
                <el-descriptions-item label="订单送达时间">{{ scope.row.deliverTime }}</el-descriptions-item>
                <el-descriptions-item label="订单签收时间">{{ scope.row.signTime }}</el-descriptions-item>
                <el-descriptions-item label="评分">
                  <el-rate v-model="scope.row.rate" allow-half  disabled show-score score-template="{value} 星" text-color="#ff9900" />
                </el-descriptions-item>
                <el-descriptions-item label="反馈信息">{{ scope.row.feedback }}</el-descriptions-item>
                <el-descriptions-item label="代取状态">{{ scope.row.status }}</el-descriptions-item>
              </el-descriptions>
            </div>
          </template>
        </el-table-column>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderNo" label="订单号"></el-table-column>
        <el-table-column prop="courierName" label="代取员"></el-table-column>
        <el-table-column prop="studentName" label="发布学生"></el-table-column>
        <el-table-column prop="typeName" label="代取物品类型"></el-table-column>
        <el-table-column prop="price" label="订单价格"></el-table-column>
        <el-table-column prop="pickupAddress" label="取件地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="shippingAddress" label="收件地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="pickupCode" label="取件码截图">
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.pickupCode" :preview-src-list="[scope.row.pickupCode]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="代取状态">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
            <el-tag type="info" v-if="scope.row.status === '待支付'">待支付</el-tag>
            <el-tag type="warning" v-if="scope.row.status === '待接单'">待接单</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '运输中'">运输中</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '已送达'">已送达</el-tag>
            <el-tag type="success" v-if="scope.row.status === '已签收'">已签收</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";


const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  orderNo: null,
  ids: []
})

const load = () => {
  request.get('/orders/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      orderNo: data.orderNo
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
const handleAdd = () => {
  data.form = { price: 1 }
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  request.post('/orders/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/orders/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/orders/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/orders/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const reset = () => {
  data.orderNo = null
  load()
}

load()
</script>