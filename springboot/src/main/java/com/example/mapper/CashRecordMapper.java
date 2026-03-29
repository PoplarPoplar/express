package com.example.mapper;

import com.example.entity.CashRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CashRecordMapper {

    int insert(CashRecord cashRecord);

    void updateById(CashRecord cashRecord);

    void deleteById(Integer id);

    @Select("select * from `cash_record` where id = #{id}")
    CashRecord selectById(Integer id);

    List<CashRecord> selectAll(CashRecord cashRecord);

    @Select("select * from `cash_record` where courier_id = #{courierId} and status = '待审核'")
    CashRecord selectByCourierId(Integer courierId);

}
