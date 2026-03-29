package com.example.entity;

/**
 * 校园资讯
 */
public class Information {
    /** ID */
    private Integer id;
    /** 资讯标题 */
    private String title;
    /** 资讯内容 */
    private String content;
    /** 资讯封面 */
    private String cover;
    /** 发布时间 */
    private String time;
    /** 浏览量 */
    private Integer viewCount;
    private String simpleContent;

    public String getSimpleContent() {
        return simpleContent;
    }

    public void setSimpleContent(String simpleContent) {
        this.simpleContent = simpleContent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}