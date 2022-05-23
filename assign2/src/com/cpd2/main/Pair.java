package com.cpd2.main;

import java.io.Serializable;

public class Pair<L,R> implements Serializable{
    private L l;
    private R r;

    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }

    public L getL(){ return l; }

    public R getR(){ return r; }

    public void setL(L l){ this.l = l; }

    public void setR(R r){ this.r = r; }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof Pair)) {
            return false;
        }

        Pair<L,R> x = (Pair<L,R>) o;
        
        return (this.l.equals(x.getL()) && this.r.equals(x.getR()));
    }

}
