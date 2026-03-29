package com.example.controller;

import com.example.common.Result;
import com.example.entity.Identification;
import com.example.service.IdentificationService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/identification")
public class IdentificationController {

    @Resource
    private IdentificationService identificationService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Identification identification) {
        identificationService.add(identification);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Identification identification) {
        identificationService.updateById(identification);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        identificationService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        identificationService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Identification identification = identificationService.selectById(id);
        return Result.success(identification);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Identification identification) {
        List<Identification> list = identificationService.selectAll(identification);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Identification identification,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Identification> pageInfo = identificationService.selectPage(identification, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
