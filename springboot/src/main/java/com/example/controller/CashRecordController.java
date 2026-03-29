package com.example.controller;

import com.example.common.Result;
import com.example.entity.CashRecord;
import com.example.service.CashRecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/cashRecord")
public class CashRecordController {

    @Resource
    private CashRecordService cashRecordService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody CashRecord cashRecord) {
        cashRecordService.add(cashRecord);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody CashRecord cashRecord) {
        cashRecordService.updateById(cashRecord);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        cashRecordService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        cashRecordService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        CashRecord cashRecord = cashRecordService.selectById(id);
        return Result.success(cashRecord);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(CashRecord cashRecord) {
        List<CashRecord> list = cashRecordService.selectAll(cashRecord);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(CashRecord cashRecord,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<CashRecord> pageInfo = cashRecordService.selectPage(cashRecord, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
