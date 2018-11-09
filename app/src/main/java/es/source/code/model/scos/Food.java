package es.source.code.model.scos;

import java.io.Serializable;

public class Food implements Serializable {
    private int id;//图片id
    private String name;//名字
    private int price;//价格
    private String remark;//备注
    private int isOrdered = 0;//0:未点，1：已点
    private int remian = 10;//剩余量
    private int x;
    private int y;

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

    public int getRemian() {
        return remian;
    }

    public void setRemian(int remian) {
        this.remian = remian;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", remark='" + remark + '\'' +
                ", isOrdered=" + isOrdered +
                ", remian=" + remian +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
