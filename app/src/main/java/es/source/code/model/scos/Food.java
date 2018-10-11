package es.source.code.model.scos;

import java.io.Serializable;

public class Food  implements Serializable {
    private int id;//图片id
    private String name;//名字
    private int price;//价格
    private String remark;//备注
    private int isOrdered = 0;//0:未点，1：已点

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(int isOrdered) {
        this.isOrdered = isOrdered;
    }
}
