package com.example.entity;

/**
 * 学生地址
 */
public class Address {
    /** 主键 */
    private Integer id;
    /** 所属学生id */
    private Integer studentId;
    /** 地址 */
    private String address;
    /** 手机号 */
    private String phone;
    /** 姓名 */
    private String name;
    /** 是否为默认地址 */
    private String asDefault;
    private String studentName;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsDefault() {
        return asDefault;
    }

    public void setAsDefault(String asDefault) {
        this.asDefault = asDefault;
    }
}