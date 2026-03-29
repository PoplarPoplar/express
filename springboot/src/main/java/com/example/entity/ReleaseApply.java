package com.example.entity;

/**
 * 代取员解封申请
 */
public class ReleaseApply {
    /** ID */
    private Integer id;
    /** 代取员id */
    private Integer courierId;
    /** 图片 */
    private String img;
    /** 申请理由 */
    private String reason;
    /** 申请时间 */
    private String time;
    /** 申请状态 */
    private String status;
    /** 回复 */
    private String reply;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
