<template>
  <div class="card" style="margin: 10px auto; padding: 20px; width: 80%; min-height: 500px">
    <div style="margin-bottom: 20px; font-size: 20px">提现记录</div>
    <el-table stripe :data="data.tableData">
      <el-table-column prop="courierName" label="代取员名称"></el-table-column>
      <el-table-column prop="img" label="收款码照片">
        <template #default="scope">
          <el-image style="width: 50px; height: 50px; border-radius: 5px" :src="scope.row.img"
                    :preview-src-list="[scope.row.img]" preview-teleported></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="money" label="提现金额"></el-table-column>
      <el-table-column prop="applyTime" label="申请时间"></el-table-column>
      <el-table-column prop="status" label="申请状态">
        <template v-slot="scope">
          <el-tag type="warning" v-if="scope.row.status === '待审核'">待审核</el-tag>
          <el-tag type="success" v-if="scope.row.status === '通过'">通过</el-tag>
          <el-tag type="danger" v-if="scope.row.status === '拒绝'">拒绝</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="feedback" label="审批反馈"></el-table-column>
    </el-table>

    <div style="margin-top: 15px" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize"
                     v-model:current-page="data.pageNum" :total="data.total"/>
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
  courierName: null,
  ids: []
})

const load = () => {
  request.get('/cashRecord/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      courierName: data.courierName
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
  data.form = {price: 1}
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.form.status = '通过'
  data.formVisible = true
}
const add = () => {
  request.post('/cashRecord/add', data.form).then(res => {
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
  request.put('/cashRecord/update', data.form).then(res => {
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
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', {type: 'warning'}).then(res => {
    request.delete('/cashRecord/delete/' + id).then(res => {
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
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', {type: 'warning'}).then(res => {
    request.delete("/cashRecord/delete/batch", {data: data.ids}).then(res => {
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
  data.courierName = null
  load()
}

load()
</script>