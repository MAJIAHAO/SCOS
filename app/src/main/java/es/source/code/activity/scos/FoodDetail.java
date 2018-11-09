package es.source.code.activity.scos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import es.source.code.model.scos.Food;
import es.source.code.model.scos.Position;

import static es.source.code.activity.scos.R.drawable.re1;

public class FoodDetail extends AppCompatActivity {
    public TextView nameTextView;
    public TextView priceTextView;
    public ImageView imageView;
    public Button btnOrder;
    private GestureDetector mGestureDetector;
    int x;
    int y;
    int from;
    private Food newFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        nameTextView = findViewById(R.id.tv_food_name);
        priceTextView = findViewById(R.id.tv_food_price);
        imageView = findViewById(R.id.iv_food);
        btnOrder = findViewById(R.id.btn_order);

        from =  getIntent().getIntExtra("from", 0);
        x = getIntent().getIntExtra("x", 0);
        y = getIntent().getIntExtra("y", 0);
        newFood = (Food) getIntent().getSerializableExtra("food");

        if(FoodView.foodList[x][y].getIsOrdered() == 0){//未点
            btnOrder.setText("点餐");
            btnOrder.setBackgroundColor(Color.parseColor("#03A9F4"));
        }else{//已经点了
            btnOrder.setText("取消");
            btnOrder.setBackgroundColor(Color.GRAY);
        }
        //滑动操作
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                if(Math.abs(e1.getRawY() - e2.getRawY())>100){
                    return true;
                }
                if(Math.abs(velocityX)<150){
                    return true;
                }
                if((e1.getRawX() - e2.getRawX()) >200){// 表示 向右滑动表示下一页
                    next(null);
                    return true;
                }

                if((e2.getRawX() - e1.getRawX()) >200){  //向左滑动 表示 上一页
                    pre(null);
                    return true;//消费掉当前事件  不让当前事件继续向下传递
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer key = x * 4 + y;
                Position pos = new Position(x, y);
                if(FoodView.foodList[x][y].getIsOrdered() == 0){//未点
                    btnOrder.setText("点餐");
                    btnOrder.setBackgroundColor(Color.parseColor("#03A9F4"));
                    FoodView.foodList[x][y].setIsOrdered(1);
                    FoodView.orderedNum++;
                    FoodView.notOrderedNum--;
                    FoodView.orderedPrice += FoodView.foodList[x][y].getPrice();
                    FoodView.notOrderedPrice -= FoodView.foodList[x][y].getPrice();
                    FoodView.notOrder.remove(key);
                    FoodView.hasOrder.put(key, pos);
                    Toast.makeText(FoodDetail.this, "取消成功", Toast.LENGTH_SHORT).show();
                }else{//已经点了
                    btnOrder.setText("取消");
                    btnOrder.setBackgroundColor(Color.GRAY);
                    FoodView.foodList[x][y].setIsOrdered(0);
                    FoodView.orderedNum++;
                    FoodView.notOrderedNum--;
                    FoodView.orderedPrice += FoodView.foodList[x][y].getPrice();
                    FoodView.notOrderedPrice -= FoodView.foodList[x][y].getPrice();
                    FoodView.notOrder.remove(key);
                    FoodView.hasOrder.put(key, pos);
                    Toast.makeText(FoodDetail.this, "点餐成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(from == 0){
            nameTextView.setText(FoodView.foodList[x][y].getName());
            priceTextView.setText(FoodView.foodList[x][y].getPrice() + "元");
            imageView.setImageResource(FoodView.foodList[x][y].getId());
        }else{
            nameTextView.setText(newFood.getName());
            priceTextView.setText(newFood.getPrice() + "元");
            imageView.setImageResource(newFood.getId());
        }
    }

    public  void next(View view){
        if(x == 3 && y == 3){
            Toast.makeText(FoodDetail.this, "最后了", Toast.LENGTH_SHORT).show();
            return;
        }
        getNext();
        nameTextView.setText(FoodView.foodList[x][y].getName());
        priceTextView.setText(FoodView.foodList[x][y].getPrice() + "元");
        imageView.setImageResource(FoodView.foodList[x][y].getId());
        Toast.makeText(FoodDetail.this, "netx", Toast.LENGTH_SHORT).show();
    }
    public void pre(View view){
        if(x == 0 && y == 0){
            Toast.makeText(FoodDetail.this, "最前了", Toast.LENGTH_SHORT).show();
            return;
        }
        getPre();
        nameTextView.setText(FoodView.foodList[x][y].getName());
        priceTextView.setText(FoodView.foodList[x][y].getPrice() + "元");
        imageView.setImageResource(FoodView.foodList[x][y].getId());
    }

    //重写activity的触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //2.让手势识别器生效
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    public void getNext(){
        int sum = x * 4 + y;
        sum++;
        x = sum / 4;
        y = sum % 4;
    }
    public void getPre(){
        int sum = x * 4 + y;
        sum--;
        x = sum / 4;
        y = sum % 4;
    }
}
