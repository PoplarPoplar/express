package com.example.controller;

import com.example.common.Result;
import com.example.entity.ReleaseApply;
import com.example.service.ReleaseApplyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/releaseApply")
public class ReleaseApplyController {

    @Resource
    private ReleaseApplyService releaseApplyService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ReleaseApply releaseApply) {
        releaseApplyService.add(releaseApply);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody ReleaseApply releaseApply) {
        releaseApplyService.updateById(releaseApply);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        releaseApplyService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        releaseApplyService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ReleaseApply releaseApply = releaseApplyService.selectById(id);
        return Result.success(releaseApply);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ReleaseApply releaseApply) {
        List<ReleaseApply> list = releaseApplyService.selectAll(releaseApply);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ReleaseApply releaseApply,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ReleaseApply> pageInfo = releaseApplyService.selectPage(releaseApply, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
