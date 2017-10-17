package com.xiaohongxiedaima.demo.algorithm.dnf.index.query;

import com.xiaohongxiedaima.demo.algorithm.dnf.index.Operator;
import com.xiaohongxiedaima.demo.algorithm.dnf.index.term.AbstractTerm;

/**
 * Created by liusheng on 17-10-17.
 */
public class TermQuery {

    private AbstractTerm term;

    private Operator operator;

    public TermQuery(AbstractTerm term, Operator operator) {
        this.term = term;
        this.operator = operator;
    }

    public AbstractTerm getTerm() {
        return term;
    }

    public void setTerm(AbstractTerm term) {
        this.term = term;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}
