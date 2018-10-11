package es.source.code.activity.scos;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.source.code.fargment.scos.FoodOrderFragment;
import es.source.code.model.scos.User;


public class FoodOrderView extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> list;
    private FoodOrderAdapter adapter;
    private String[] titles = {"已下菜单", "未下菜单"};

    public static TextView orderNum;
    public static TextView orderPrice;
    public static Button btnSubmit;
    public User user;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_order_view);

        user = (User) getIntent().getSerializableExtra("user");
        Log.d("CardView", "用户: " + user);

        //实例化
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        orderNum = findViewById(R.id.order_num);
        orderPrice = findViewById(R.id.order_price);
        btnSubmit = findViewById(R.id.btn_submit);

        orderNum.setText(FoodView.orderedNum + "盘");
        orderPrice.setText(FoodView.orderedPrice + "元");
        btnSubmit.setText("提交订单");

        //页面，数据源
        list = new ArrayList<>();
        list.add(new FoodOrderFragment(0, FoodOrderView.this));
        list.add(new FoodOrderFragment(1, FoodOrderView.this));

        //ViewPager的适配器
        adapter = new FoodOrderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //绑定
        tabLayout.setupWithViewPager(viewPager);
        //可滑动
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //标签选中之后执行的方法
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.d("CardView", "当前的：" + tab.getPosition());
                if(tab.getPosition() == 0){
                    orderNum.setText(FoodView.orderedNum + "盘");
                    orderPrice.setText(FoodView.orderedPrice + "元");
                    btnSubmit.setText("结账");
                }else{
                    orderNum.setText(FoodView.notOrderedNum + "盘");
                    orderPrice.setText(FoodView.notOrderedPrice + "元");
                    btnSubmit.setText("提交订单");
                    //btnSubmit.setBackgroundColor(Color.GREEN);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        //toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 主标题
        toolbar.setTitle("查看订单");
        //设置toolbar
        setSupportActionBar(toolbar);
        //监听结账按钮
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getOldUser())
                    Toast.makeText(FoodOrderView.this, "您好，老顾客，本次你可享受 7 折优惠", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class FoodOrderAdapter extends FragmentPagerAdapter {

        public FoodOrderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

}
