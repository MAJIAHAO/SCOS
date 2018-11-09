package es.source.code.model.scos;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class FoodCollection implements Serializable {

    private List<Food> coldFoodList;
    private List<Food> hotFoodList;
    private List<Food> seaFoodList;
    private List<Food> drinkingList;

    public FoodCollection(List<Food> coldFoodList, List<Food> hotFoodList, List<Food> seaFoodList, List<Food>
            drinkingList) {
        this.coldFoodList = coldFoodList;
        this.hotFoodList = hotFoodList;
        this.seaFoodList = seaFoodList;
        this.drinkingList = drinkingList;
    }

    public List<Food> getColdFoodList() {
        return coldFoodList;
    }

    public void setColdFoodList(List<Food> coldFoodList) {
        this.coldFoodList = coldFoodList;
    }

    public List<Food> getHotFoodList() {
        return hotFoodList;
    }

    public void setHotFoodList(List<Food> hotFoodList) {
        this.hotFoodList = hotFoodList;
    }

    public List<Food> getSeaFoodList() {
        return seaFoodList;
    }

    public void setSeaFoodList(List<Food> seaFoodList) {
        this.seaFoodList = seaFoodList;
    }

    public List<Food> getDrinkingList() {
        return drinkingList;
    }

    public void setDrinkingList(List<Food> drinkingList) {
        this.drinkingList = drinkingList;
    }
}

