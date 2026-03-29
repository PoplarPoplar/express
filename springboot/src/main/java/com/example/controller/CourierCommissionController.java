package com.example.controller;

import com.example.common.Result;
import com.example.entity.CourierCommission;
import com.example.service.CourierCommissionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/courierCommission")
public class CourierCommissionController {

    @Resource
    private CourierCommissionService courierCommissionService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody CourierCommission courierCommission) {
        courierCommissionService.add(courierCommission);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody CourierCommission courierCommission) {
        courierCommissionService.updateById(courierCommission);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        courierCommissionService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        courierCommissionService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        CourierCommission courierCommission = courierCommissionService.selectById(id);
        return Result.success(courierCommission);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(CourierCommission courierCommission) {
        List<CourierCommission> list = courierCommissionService.selectAll(courierCommission);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(CourierCommission courierCommission,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<CourierCommission> pageInfo = courierCommissionService.selectPage(courierCommission, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
