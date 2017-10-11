package com.xiaohongxiedaima.demo.algorithm.dnf;

import java.util.Set;

/**
 * Created by liusheng on 17-9-21.
 */
public class Conjunction {
    private Set<AbstractAssignment> assignments;

    public Conjunction(Set<AbstractAssignment> assignments) {
        this.assignments = assignments;
    }

    public Set<AbstractAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<AbstractAssignment> assignments) {
        this.assignments = assignments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conjunction that = (Conjunction) o;

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
        return assignments.hashCode();
    }
}
