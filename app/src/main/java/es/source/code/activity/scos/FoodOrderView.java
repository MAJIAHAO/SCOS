package es.source.code.activity.scos;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ProgressBar;
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
    public Button btnOrder;
    private ProgressBar progerss_nomal ;

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
        //    弹出要给ProgressDialog
        progerss_nomal = (ProgressBar)findViewById(R.id.pb);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //标签选中之后执行的方法
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.d("CardView", "当前的：" + tab.getPosition());
                if(tab.getPosition() == 0){
                    orderNum.setText("菜品总数：" + FoodView.orderedNum + "盘");
                    orderPrice.setText("菜单总价：" + FoodView.orderedPrice + "元");
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
                progerss_nomal.setVisibility(View.VISIBLE);
                new ProgressTask().execute("本次结账:" + FoodView.orderedPrice);
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(6000);
                            Message msg = new Message();
                            msg.what = 6000;
                            mHandler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //if(user.getOldUser())
                 //   Toast.makeText(FoodOrderView.this, "您好，老顾客，本次你可享受 7 折优惠", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //定义一个Handler
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            btnSubmit.setBackgroundColor(Color.GRAY);
            progerss_nomal.setVisibility(View.GONE);
            Thread.currentThread().interrupt();
        }
    };
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

    class ProgressTask extends AsyncTask<String ,Integer,String>{
        @Override
        protected String doInBackground(String... arg0) {
            //处理后台任务，在后台线程执行  不能再此 有操作UI的操作
            progerss_nomal.setProgress(0); // 进度条复位
            int i = 0;
            for (i = 0; i <= 100; i++) {
                publishProgress(i); // 将会调用onProgressUpdate方法
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return arg0[0];
        }
        @Override
        protected void onPostExecute(String result) {//执行完成后
            Toast.makeText(FoodOrderView.this,result , Toast.LENGTH_SHORT).show();
            progerss_nomal.setProgress(0);
        }
        @Override
        protected void onPreExecute() {//在 doInBackground(Params...)之前被调用，在ui线程执行
            progerss_nomal.setProgress(0); // 进度条复位
            //Toast.makeText(FoodOrderView.this,"执行完毕后输出 helloworld" , Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progerss_nomal.setProgress(values[0]);
        }

    }
}
