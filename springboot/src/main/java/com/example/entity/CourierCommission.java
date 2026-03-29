package com.example.entity;

/**
 * 代取员等级信息
 */
public class CourierCommission {
    /** 主键 */
    private Integer id;
    /** 等级名称 */
    private String name;
    /** 提成比例 */
    private Double commission;
    /** 说明 */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
