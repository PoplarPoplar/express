package com.example.mapper;

import com.example.entity.ExpressType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExpressTypeMapper {

    int insert(ExpressType expressType);

    void updateById(ExpressType expressType);

    void deleteById(Integer id);

    @Select("select * from `express_type` where id = #{id}")
    ExpressType selectById(Integer id);

    List<ExpressType> selectAll(ExpressType expressType);

}
