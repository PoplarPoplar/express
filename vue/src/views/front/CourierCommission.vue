<template>
  <div class="card" style="margin: 10px auto; width: 60%">
    <el-table stripe :data="data.tableData">
      <el-table-column prop="name" label="等级名称"></el-table-column>
      <el-table-column prop="commission" label="提成比例"></el-table-column>
      <el-table-column prop="remark" label="说明"></el-table-column>
    </el-table>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";


const data = reactive({
  tableData: [],
})

const load = () => {
  request.get('/courierCommission/selectAll').then(res => {
    if (res.code === '200') {
      data.tableData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>