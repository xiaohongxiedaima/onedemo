package com.xiaohongxiedaima.demo.algorithm.dnf.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liusheng on 17-9-28.
 */
public class AdGroup {
    private String id;
    private String title;
    @SerializedName("plan_id")
    private String planId;
    /** 0:暂停,1:有效,2:未开始,3:已下线,-1:删除 */
    @SerializedName("plan_status")
    private Integer planStatus;
    /** 1:pc 2:移动 */
    @SerializedName("ad_type")
    private List<Integer> adType;
    @SerializedName("price_model")
    private String priceModel;
    private Float price;
    private List<String> region;
    @SerializedName("site_black_list")
    private List<String> siteBlackList;
    @SerializedName("site_white_list")
    private List<String> siteWhiteList;
    private List<String> keyword;
    /** 七个数组分表代表周一至之日,0-23代表24个小时 */
     private List<List<Integer>> schedule;
    /** 1-99次,0为无限制 */
     @SerializedName("show_per_user")
    private Integer showPerUser;
    private List<List<String>> crowd;
    @SerializedName("ad_zone_id")
    private List<String> adZoneId;
    private Integer weight;
    /** -1:删除,0:暂停,1:有效 */
     private Integer status;
    @SerializedName("content_tag")
    private List<String> contentTag;
    @SerializedName("equipment_type")
    private List<List<String>> equipmentType;

    public AdGroup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Integer getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public List<Integer> getAdType() {
        return adType;
    }

    public void setAdType(List<Integer> adType) {
        this.adType = adType;
    }

    public String getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(String priceModel) {
        this.priceModel = priceModel;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public List<String> getRegion() {
        return region;
    }

    public void setRegion(List<String> region) {
        this.region = region;
    }

    public List<String> getSiteBlackList() {
        return siteBlackList;
    }

    public void setSiteBlackList(List<String> siteBlackList) {
        this.siteBlackList = siteBlackList;
    }

    public List<String> getSiteWhiteList() {
        return siteWhiteList;
    }

    public void setSiteWhiteList(List<String> siteWhiteList) {
        this.siteWhiteList = siteWhiteList;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    public List<List<Integer>> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<List<Integer>> schedule) {
        this.schedule = schedule;
    }

    public Integer getShowPerUser() {
        return showPerUser;
    }

    public void setShowPerUser(Integer showPerUser) {
        this.showPerUser = showPerUser;
    }

    public List<List<String>> getCrowd() {
        return crowd;
    }

    public void setCrowd(List<List<String>> crowd) {
        this.crowd = crowd;
    }

    public List<String> getAdZoneId() {
        return adZoneId;
    }

    public void setAdZoneId(List<String> adZoneId) {
        this.adZoneId = adZoneId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getContentTag() {
        return contentTag;
    }

    public void setContentTag(List<String> contentTag) {
        this.contentTag = contentTag;
    }

    public List<List<String>> getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(List<List<String>> equipmentType) {
        this.equipmentType = equipmentType;
    }
}
