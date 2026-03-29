package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.CourierCommission;
import com.example.mapper.CourierCommissionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class CourierCommissionService {

    @Resource
    private CourierCommissionMapper courierCommissionMapper;

    public void add(CourierCommission courierCommission) {
        courierCommissionMapper.insert(courierCommission);
    }

    public void updateById(CourierCommission courierCommission) {
        courierCommissionMapper.updateById(courierCommission);
    }

    public void deleteById(Integer id) {
        courierCommissionMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            courierCommissionMapper.deleteById(id);
        }
    }

    public CourierCommission selectById(Integer id) {
        return courierCommissionMapper.selectById(id);
    }

    public List<CourierCommission> selectAll(CourierCommission courierCommission) {
        return courierCommissionMapper.selectAll(courierCommission);
    }

    public PageInfo<CourierCommission> selectPage(CourierCommission courierCommission, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CourierCommission> list = courierCommissionMapper.selectAll(courierCommission);
        return PageInfo.of(list);
    }

}
