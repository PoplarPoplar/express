package com.example.controller;

import com.example.common.Result;
import com.example.entity.ExpressType;
import com.example.service.ExpressTypeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/expressType")
public class ExpressTypeController {

    @Resource
    private ExpressTypeService expressTypeService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ExpressType expressType) {
        expressTypeService.add(expressType);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody ExpressType expressType) {
        expressTypeService.updateById(expressType);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        expressTypeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        expressTypeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ExpressType expressType = expressTypeService.selectById(id);
        return Result.success(expressType);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ExpressType expressType) {
        List<ExpressType> list = expressTypeService.selectAll(expressType);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ExpressType expressType,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ExpressType> pageInfo = expressTypeService.selectPage(expressType, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
