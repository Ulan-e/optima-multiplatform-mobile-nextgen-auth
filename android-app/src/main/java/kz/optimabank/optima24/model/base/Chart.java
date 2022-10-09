package kz.optimabank.optima24.model.base;


import java.io.Serializable;


public class Chart implements Serializable{

    public String name;
    public String curr;
    public float percent;
    public float amount;
    public int viewId;
    public int IDcategory;
    public Category category;

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Chart(String name, float percent, float amount, int viewId, String curr, int IDcategory, Category category){
        this.name = name;
        this.percent = percent;
        this.amount = amount;
        this.viewId = viewId;
        this.curr = curr;
        this.IDcategory = IDcategory;
        this.category = category;
    }

    public Chart(String name, float percent, float amount, int viewId, String curr, Category category){
        this.name = name;
        this.percent = percent;
        this.amount = amount;
        this.viewId = viewId;
        this.curr = curr;
        this.category = category;
    }

    public Chart(int viewId){
        this.viewId = viewId;
    }
}