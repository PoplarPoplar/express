package com.example.entity;

/**
 * 代取订单信息
 */
public class Orders {
    /** ID */
    private Integer id;
    /** 订单号 */
    private String orderNo;
    /** 代取员id */
    private Integer courierId;
    /** 发布学生id */
    private Integer studentId;
    /** 代取物品类型id */
    private Integer expressType;
    /** 订单价格 */
    private Integer price;
    /** 小费 */
    private Integer tip;
    /** 代取订单简介 */
    private String descr;
    /** 取件地址 */
    private String pickupAddress;
    /** 收件地址 */
    private String shippingAddress;
    /** 收件人名称 */
    private String customerName;
    /** 收件人手机号 */
    private String phone;
    /** 取件码截图 */
    private String pickupCode;
    /** 订单创建时间 */
    private String createTime;
    /** 订单接取时间 */
    private String accessTime;
    /** 订单送达时间 */
    private String deliverTime;
    /** 订单签收时间 */
    private String signTime;
    /** 评分 */
    private Double rate;
    /** 反馈信息 */
    private String feedback;
    /** 代取状态 */
    private String status;
    private String courierName;
    private String studentName;
    private String typeName;
    private String courierAvatar;
    private String courierPhone;
    private Double courierRate = 0D;

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public Double getCourierRate() {
        return courierRate;
    }

    public void setCourierRate(Double courierRate) {
        this.courierRate = courierRate;
    }

    public String getCourierAvatar() {
        return courierAvatar;
    }

    public void setCourierAvatar(String courierAvatar) {
        this.courierAvatar = courierAvatar;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getExpressType() {
        return expressType;
    }

    public void setExpressType(Integer expressType) {
        this.expressType = expressType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTip() {
        return tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
