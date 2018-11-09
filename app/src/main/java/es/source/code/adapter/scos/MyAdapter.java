package es.source.code.adapter.scos;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import es.source.code.activity.scos.FoodDetail;
import es.source.code.activity.scos.FoodView;
import es.source.code.activity.scos.R;
import es.source.code.model.scos.Food;
import es.source.code.model.scos.Position;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater li = null;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private int location;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView priceTextView;
        public ImageView imageView;
        public Button btnOrder;
        public TextView left;

        public MyViewHolder(View v) {
            super(v);

            mCardView = v.findViewById(R.id.card_view);
            mTextView = v.findViewById(R.id.tv_text);
            priceTextView = v.findViewById(R.id.tv_blah);
            imageView = v.findViewById(R.id.iv_image);
            btnOrder = v.findViewById(R.id.btn_order);
            left = v.findViewById(R.id.remain);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(int location,Context context) {
        this.location = location;
        //this.mLayoutInflater = mActivity.getLayoutInflater();
        this.context = context;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mTextView.setText(FoodView.foodList[location][position].getName());
        holder.priceTextView.setText(FoodView.foodList[location][position].getPrice() + "元");
        holder.imageView.setImageResource(FoodView.foodList[location][position].getId());
        holder.left.setText("还剩：" + FoodView.foodList[location][position].getRemian() + "盘");
        //Log.d("CardView", "position: " + position + ",  location:" + location);

        //点击卡片的其他区域，进入到详细页面
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = FoodView.foodList[location][position].getName();
                String price = FoodView.foodList[location][position].getPrice() + "元";
                Intent mIntent=new Intent(context, FoodDetail.class);
                mIntent.putExtra("from",0);
                mIntent.putExtra("x",location);
                mIntent.putExtra("y",position);
                context.startActivity(mIntent);
                //Log.d("CardView", "CardView Clicked: " + name);
            }
        });

        //点餐按钮
        holder.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = view.findViewById(R.id.btn_order);
                int x = location;
                int y = position;
                Integer key = x * 4 + y;
                Position pos = new Position(x, y);

                if(btn.getText().equals("点餐")){
                    btn.setText("取消");
                    btn.setBackgroundColor(Color.GRAY);
                    FoodView.foodList[location][position].setIsOrdered(1);
                    FoodView.orderedNum++;
                    FoodView.notOrderedNum--;
                    FoodView.orderedPrice += FoodView.foodList[location][position].getPrice();
                    FoodView.notOrderedPrice -= FoodView.foodList[location][position].getPrice();
                    FoodView.notOrder.remove(key);
                    FoodView.hasOrder.put(key, pos);

                    Toast.makeText(context, "点餐成功", Toast.LENGTH_SHORT).show();
                }else{
                    btn.setText("点餐");
                    btn.setBackgroundColor(Color.parseColor("#03A9F4"));
                    FoodView.foodList[location][position].setIsOrdered(0);
                    FoodView.orderedNum--;
                    FoodView.notOrderedNum++;
                    FoodView.orderedPrice -= FoodView.foodList[location][position].getPrice();
                    FoodView.notOrderedPrice += FoodView.foodList[location][position].getPrice();
                    FoodView.hasOrder.remove(key);
                    FoodView.notOrder.put(key, pos);

                    Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return FoodView.foodList.length;
    }

    public void refreshItem(int position){
        notifyItemChanged(position);
    }
}
