package es.source.code.adapter.scos;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import es.source.code.activity.scos.FoodOrderView;
import es.source.code.activity.scos.FoodView;
import es.source.code.activity.scos.R;
import es.source.code.model.scos.Food;

public class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.MyViewHolder>  {
    private Context context;
    private LayoutInflater li = null;
    private LayoutInflater mLayoutInflater;
    private int location;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public TextView priceTextView;
        public ImageView imageView;
        public Button btnOrder;

        public MyViewHolder(View v) {
            super(v);

            mCardView = v.findViewById(R.id.card_view);
            mTextView = v.findViewById(R.id.tv_text);
            priceTextView = v.findViewById(R.id.tv_blah);
            imageView = v.findViewById(R.id.iv_image);
            btnOrder = v.findViewById(R.id.btn_order);
            btnOrder.setText("退点");
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FoodOrderAdapter(int location,Context context) {
        this.location = location;
        //this.mLayoutInflater = mActivity.getLayoutInflater();
        this.context = context;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public FoodOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FoodOrderAdapter.MyViewHolder vh = new FoodOrderAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FoodOrderAdapter.MyViewHolder holder, final int position) {

        if(location == 0){//已经下单页面
            if(position > FoodView.orderedNum)
                return;
            int x = FoodView.correctHasOrder.get(position).getX();
            int y = FoodView.correctHasOrder.get(position).getY();
            Log.d("CardView", "x: " + x + ",  y:" + y);
            holder.mTextView.setText(FoodView.foodList[x][y].getName());
            holder.priceTextView.setText(FoodView.foodList[x][y].getPrice() + "元");
            holder.imageView.setImageResource(FoodView.foodList[x][y].getId());

        }else{//没有下单页面
            if(position > FoodView.notOrderedNum)
                return;
            int x = FoodView.correctNotOrder.get(position).getX();
            int y = FoodView.correctNotOrder.get(position).getY();
            Log.d("CardView", "x: " + x + ",  y:" + y);
            holder.btnOrder.setVisibility(View.GONE);
            holder.mTextView.setText(FoodView.foodList[x][y].getName());
            holder.priceTextView.setText(FoodView.foodList[x][y].getPrice() + "元");
            holder.imageView.setImageResource(FoodView.foodList[x][y].getId());

        }
        //点击卡片的其他区域，进入到详细页面
        /*holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = FoodView.foodList[location][position].getName();
                String price = FoodView.foodList[location][position].getPrice() + "元";
                Intent mIntent=new Intent(context, FoodDetail.class);
                mIntent.putExtra("x",location);
                mIntent.putExtra("y",position);
                context.startActivity(mIntent);
                //Log.d("CardView", "CardView Clicked: " + name);
            }
        });*/

        //点餐按钮
        /*holder.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = view.findViewById(R.id.btn_order);
                if(btn.getText().equals("点餐")){
                    btn.setText("取消");
                    btn.setBackgroundColor(Color.GRAY);
                    FoodView.foodList[location][position].setIsOrdered(1);
                    Toast.makeText(context, "点餐成功", Toast.LENGTH_SHORT).show();
                }else{
                    btn.setText("点餐");
                    btn.setBackgroundColor(Color.parseColor("#03A9F4"));
                    FoodView.foodList[location][position].setIsOrdered(0);
                    Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if(location == 0) {
            return FoodView.orderedNum;
        }
        else {
            return FoodView.notOrderedNum;
        }
    }
}

