package com.example.mapper;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrdersMapper {

    int insert(Orders orders);

    void updateById(Orders orders);

    void deleteById(Integer id);

    Orders selectById(Integer id);

    List<Orders> selectAll(Orders orders);

    Orders selectByOrderNo(String orderNo);

    @Select("select count(*) from orders where courier_id = #{courierId}")
    Integer selectCountByCourierId(Integer courierId);

}
