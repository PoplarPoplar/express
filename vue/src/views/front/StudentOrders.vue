<template>
  <div class="card" style="margin: 10px auto; padding: 20px; width: 80%; min-height: 500px">
    <div style="margin-bottom: 5px">
      <el-input v-model="data.orderNo" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入订单号查询"></el-input>
      <el-select v-model="data.expressType" style="width: 240px; margin-right: 10px" placeholder="请选择代取快递类型">
        <el-option :label="item.name" :value="item.id" v-for="item in data.typeList" :key="item.id"></el-option>
      </el-select>
      <el-select v-model="data.status" style="width: 240px; margin-right: 10px" placeholder="请选择订单状态">
        <el-option value="已取消"></el-option>
        <el-option value="待支付"></el-option>
        <el-option value="待接单"></el-option>
        <el-option value="运输中"></el-option>
        <el-option value="已送达"></el-option>
        <el-option value="已签收"></el-option>
      </el-select>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData">
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
        <el-table-column prop="orderNo" label="订单号" width="140"></el-table-column>
        <el-table-column prop="courierName" label="代取员"></el-table-column>
        <el-table-column prop="studentName" label="发布学生"></el-table-column>
        <el-table-column prop="typeName" label="代取物品类型" width="140"></el-table-column>
        <el-table-column prop="price" label="订单价格"></el-table-column>
        <el-table-column prop="pickupAddress" label="取件地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="shippingAddress" label="收件地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="pickupCode" label="取件码截图" width="140">
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
        <el-table-column label="操作" width="200" fixed="right">
          <template v-slot="scope">
            <el-button plain type="success" @click="changeStatus(scope.row, '已签收')" v-if="scope.row.status === '已送达'">签收</el-button>
            <el-button plain type="success" @click="handleEdit(scope.row)" v-if="scope.row.status === '已签收'">评价</el-button>
            <el-button plain type="primary" @click="changeStatus(scope.row, '待接单')" v-if="scope.row.status === '待支付'">支付</el-button>
            <el-button plain type="danger" @click="changeStatus(scope.row, '已取消')" v-if="scope.row.status === '待支付'">取消</el-button>
            <el-button plain type="primary" @click="router.push('/front/orderDetail?id=' + scope.row.id)"
                       v-if="scope.row.status !== '待支付' && scope.row.status !== '已取消'">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin-top: 15px" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="订单评价" v-model="data.formVisible" width="30%" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="90px" style="padding: 20px">
        <el-form-item prop="feedback" label="评价内容">
          <el-input type="textarea" :rows="3" v-model="data.form.feedback" placeholder="请输入评价内容"></el-input>
        </el-form-item>
        <el-form-item prop="rate" label="评分">
          <el-rate v-model="data.form.rate" allow-half show-score score-template="{value} 星" ext-color="#ff9900" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>

import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";
import router from "@/router/index.js";

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  orderNo: null,
  status: null,
  expressType: null,
  ids: [],
  typeList: [],
  rules: {
    feedback: [
      { required: true, message: '请输入评价内容', trigger: 'blur' }
    ],
    rate: [
      { required: true, message: '请打分', trigger: 'change' }
    ]
  }
})

const formRef = ref()

request.get('/expressType/selectAll').then(res => {
  data.typeList = res.data
})

const load = () => {
  request.get('/orders/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      orderNo: data.orderNo,
      status: data.status,
      expressType: data.expressType
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

// 改变订单状态
const changeStatus = (row, status) => {
  ElMessageBox.confirm('您确认操作吗？', '操作确认', { type: 'warning' }).then(res => {
    let formData = JSON.parse(JSON.stringify(row))
    formData.status = status
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

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
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
  data.status = null
  data.expressType = null
  load()
}

load()
</script>