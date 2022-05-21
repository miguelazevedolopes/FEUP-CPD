package com.cpd2.main;

public class Pair<L,R> {
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

    public boolean isEqual(Pair<Integer, Integer> x)
    {
        return (this.l == x.getL() && this.r == x.getR());
    }
}
