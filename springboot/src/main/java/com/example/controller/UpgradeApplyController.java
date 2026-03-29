package com.example.controller;

import com.example.common.Result;
import com.example.entity.UpgradeApply;
import com.example.service.UpgradeApplyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/upgradeApply")
public class UpgradeApplyController {

    @Resource
    private UpgradeApplyService upgradeApplyService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody UpgradeApply upgradeApply) {
        upgradeApplyService.add(upgradeApply);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody UpgradeApply upgradeApply) {
        upgradeApplyService.updateById(upgradeApply);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        upgradeApplyService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        upgradeApplyService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        UpgradeApply upgradeApply = upgradeApplyService.selectById(id);
        return Result.success(upgradeApply);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(UpgradeApply upgradeApply) {
        List<UpgradeApply> list = upgradeApplyService.selectAll(upgradeApply);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(UpgradeApply upgradeApply,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<UpgradeApply> pageInfo = upgradeApplyService.selectPage(upgradeApply, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
