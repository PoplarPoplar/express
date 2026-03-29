package com.example.entity;

/**
 * 提现申请
 */
public class CashRecord {
    /** 主键 */
    private Integer id;
    /** 代取员id */
    private Integer courierId;
    /** 收款码照片 */
    private String img;
    /** 申请状态 */
    private String status;
    /** 提现金额 */
    private Double money;
    /** 申请时间 */
    private String applyTime;
    /** 审批反馈 */
    private String feedback;
    private String courierName;

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}