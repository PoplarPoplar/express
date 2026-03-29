package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.ExpressType;
import com.example.mapper.ExpressTypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class ExpressTypeService {

    @Resource
    private ExpressTypeMapper expressTypeMapper;

    public void add(ExpressType expressType) {
        expressTypeMapper.insert(expressType);
    }

    public void updateById(ExpressType expressType) {
        expressTypeMapper.updateById(expressType);
    }

    public void deleteById(Integer id) {
        expressTypeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            expressTypeMapper.deleteById(id);
        }
    }

    public ExpressType selectById(Integer id) {
        return expressTypeMapper.selectById(id);
    }

    public List<ExpressType> selectAll(ExpressType expressType) {
        return expressTypeMapper.selectAll(expressType);
    }

    public PageInfo<ExpressType> selectPage(ExpressType expressType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExpressType> list = expressTypeMapper.selectAll(expressType);
        return PageInfo.of(list);
    }

}
