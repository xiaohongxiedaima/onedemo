package com.xiaohongxiedaima.demo.algorithm.dnf.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liusheng on 17-9-28.
 */
public class AdUnit {
    private String id;
    @SerializedName("plan_id")
    private String planId;
    @SerializedName("group_id")
    private String groupId;
    /**
     * -1:删除,0:暂停 1:有效
     */
    private Integer status;
    /**
     * 广告类型:1:文字,2:图片,3:FLASH,4:桌面图标,5:文字+图标,6:应用程序,7:JS广告,8:微信公众号
     */
    private Integer type;
    private String title;
    private Integer width;
    private Integer height;
    private Integer weight;

    public AdUnit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
