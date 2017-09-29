package com.xiaohongxiedaima.demo.algorithm.dnf.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liusheng on 17-9-28.
 */
public class AdPlan {
    private String id;
    private String title;
    private Integer starttime;
    private Integer endtime;
    private Float budget;
    /** -1:删除,0:暂停,1:有效,2:未开始,3:已下线*/
    private Integer status;
    /** 0:已消费完,1:正常 */
     @SerializedName("today_status")
    private Integer todayStatus;

    public AdPlan() {
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

    public Integer getStarttime() {
        return starttime;
    }

    public void setStarttime(Integer starttime) {
        this.starttime = starttime;
    }

    public Integer getEndtime() {
        return endtime;
    }

    public void setEndtime(Integer endtime) {
        this.endtime = endtime;
    }

    public Float getBudget() {
        return budget;
    }

    public void setBudget(Float budget) {
        this.budget = budget;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTodayStatus() {
        return todayStatus;
    }

    public void setTodayStatus(Integer todayStatus) {
        this.todayStatus = todayStatus;
    }
}
