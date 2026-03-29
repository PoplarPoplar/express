package com.example.mapper;

import com.example.entity.CourierCommission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourierCommissionMapper {

    int insert(CourierCommission courierCommission);

    void updateById(CourierCommission courierCommission);

    void deleteById(Integer id);

    @Select("select * from `courier_commission` where id = #{id}")
    CourierCommission selectById(Integer id);

    List<CourierCommission> selectAll(CourierCommission courierCommission);

}
