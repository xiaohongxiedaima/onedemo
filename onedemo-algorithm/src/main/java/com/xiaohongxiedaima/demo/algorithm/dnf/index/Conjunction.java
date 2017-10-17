package com.xiaohongxiedaima.demo.algorithm.dnf.index;


import com.xiaohongxiedaima.demo.algorithm.dnf.index.assignment.AbstractAssignment;

import java.util.Set;

/**
 * Created by liusheng on 17-9-21.
 */
public class Conjunction {
    private Set<AbstractAssignment> assignments;
    private Integer eqCount = 0;
    private Integer neCount = 0;

    public Conjunction(Set<AbstractAssignment> assignments) {
        this.assignments = assignments;
        for (AbstractAssignment assignment : assignments) {
            if (assignment.getOperator().equals(Operator.EQ)) {
                eqCount = this.eqCount + 1;
            } else {
                neCount = this.neCount + 1;
            }
        }
    }

    public Set<AbstractAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<AbstractAssignment> assignments) {
        this.assignments = assignments;
    }

    public Integer getEqCount() {
        return eqCount;
    }

    public void setEqCount(Integer eqCount) {
        this.eqCount = eqCount;
    }

    public Integer getNeCount() {
        return neCount;
    }

    public void setNeCount(Integer neCount) {
        this.neCount = neCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conjunction that = (Conjunction) o;

        if (this.eqCount != that.eqCount) return false;

        if (this.neCount != that.neCount) return false;

        if (this.assignments.size() != that.getAssignments().size()) return false;

        for (AbstractAssignment assignment : this.assignments) {
            if (!that.getAssignments().contains(assignment)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 31 * eqCount.hashCode() + eqCount.hashCode();
        for (AbstractAssignment assignment : assignments) {
            result = 31 * result + assignment.hashCode();
        }
        return result;
    }
}
