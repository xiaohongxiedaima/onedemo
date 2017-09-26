package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.List;

/**
 * Created by liusheng on 17-9-21.
 */
public class Conjunction {

    private List<Assignment> assignmentList;

    public Conjunction() {
    }

    public Conjunction(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conjunction that = (Conjunction) o;

        return assignmentList.equals(that.assignmentList);
    }

    @Override
    public int hashCode() {
        return assignmentList.hashCode();
    }
}
