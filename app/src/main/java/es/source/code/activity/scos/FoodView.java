package es.source.code.activity.scos;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import es.source.code.fargment.scos.BlankFragment;
import es.source.code.model.scos.Food;
import es.source.code.model.scos.Position;
import es.source.code.model.scos.User;


public class FoodView extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> list;
    private MyAdapter adapter;
    public User user;
    private String[] titles = {"热菜", "冷菜", "海鲜", "酒水"};
    public static Food[][] foodList = new Food[4][4];
    public static int[][] img = new int[4][4];
    public static int orderedNum = 0;
    public static int notOrderedNum = 16;
    public static int orderedPrice = 0;
    public static int notOrderedPrice = 620;
    public static HashMap<Integer, Position> hasOrder = new HashMap<Integer, Position>();
    public static HashMap<Integer, Position> notOrder = new HashMap<Integer, Position>();
    public static HashMap<Integer, Position> correctHasOrder = new HashMap<Integer, Position>();
    public static HashMap<Integer, Position> correctNotOrder = new HashMap<Integer, Position>();
    //public static User mainUser;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view);

        user = (User) getIntent().getSerializableExtra("user");
        Log.d("CardView", "用户: " + user);

        //实例化
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        //初始化img数组id
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                //img[i][j] = getImageId(i, j );
                foodList[i][j] = new Food();
                foodList[i][j].setId(getImageId(i, j));
            }
        }
        //初始化foodList
        foodList[0][0].setName("红烧排骨");
        foodList[0][0].setPrice(35);
        foodList[0][1].setName("红烧狮子头");
        foodList[0][1].setPrice(40);
        foodList[0][2].setName("虎皮青椒");
        foodList[0][2].setPrice(25);
        foodList[0][3].setName("地三鲜");
        foodList[0][3].setPrice(25);

        foodList[1][0].setName("拍黄瓜");
        foodList[1][0].setPrice(15);
        foodList[1][1].setName("冷吃牛肉");
        foodList[1][1].setPrice(40);
        foodList[1][2].setName("凉拌牛蹄筋");
        foodList[1][2].setPrice(25);
        foodList[1][3].setName("凉拌花生");
        foodList[1][3].setPrice(15);

        foodList[2][0].setName("红烧鲫鱼");
        foodList[2][0].setPrice(45);
        foodList[2][1].setName("油闷大虾");
        foodList[2][1].setPrice(40);
        foodList[2][2].setName("香辣蟹");
        foodList[2][2].setPrice(55);
        foodList[2][3].setName("龙虾两吃");
        foodList[2][3].setPrice(75);

        foodList[3][0].setName("红酒");
        foodList[3][0].setPrice(65);
        foodList[3][1].setName("清酒");
        foodList[3][1].setPrice(40);
        foodList[3][2].setName("鸡尾酒");
        foodList[3][2].setPrice(55);
        foodList[3][3].setName("沙冰");
        foodList[3][3].setPrice(25);

        //List初始化
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                Integer key = i * 4 + j;
                Position pos = new Position(i, j);
                notOrder.put(key, pos);
            }
        }

        //页面，数据源
        list = new ArrayList<>();
        list.add(new BlankFragment(0,FoodView.this));
        list.add(new BlankFragment(1,FoodView.this));
        list.add(new BlankFragment(2,FoodView.this));
        list.add(new BlankFragment(3,FoodView.this));
        //ViewPager的适配器
        adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //绑定
        tabLayout.setupWithViewPager(viewPager);
        //可滑动
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 主标题
        toolbar.setTitle("点餐");
        //设置toolbar
        setSupportActionBar(toolbar);

    }

    //toolBar的控制器
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //重新排序
        FoodView.orderMap();
        switch (item.getItemId()){
            case R.id.menu_1:
                Toast.makeText(this, "已点菜品", Toast.LENGTH_SHORT).show();
                //跳转到一下页面
                Intent intent = new Intent();
                intent.setClass(FoodView.this, FoodOrderView.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            case R.id.menu_2:
                Toast.makeText(this, "查看订单", Toast.LENGTH_SHORT).show();
                //跳转到一下页面
                Intent intent1 = new Intent();
                intent1.setClass(FoodView.this, FoodOrderView.class);
                intent1.putExtra("user", user);
                startActivity(intent1);
                break;
            case R.id.menu_3:
                Toast.makeText(this, "呼叫服务", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
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


    //获取资源ID
    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getImageId(int x, int y){
       // return 1;
        if(x == 0){
            if(y == 0){
                return getResources().getIdentifier("re1", "drawable", getPackageName());
            }else if(y == 1) {
                return getResources().getIdentifier("re2", "drawable", getPackageName());
            }else if(y == 2){
                return getResources().getIdentifier("re3", "drawable", getPackageName());
            }else{
                return getResources().getIdentifier("re4", "drawable", getPackageName());
            }
        }else if(x == 1){
            if(y == 0){
                return getResources().getIdentifier("leng1", "drawable", getPackageName());
            }else if(y == 1) {
                return getResources().getIdentifier("leng2", "drawable", getPackageName());
            }else if(y == 2){
                return getResources().getIdentifier("leng3", "drawable", getPackageName());
            }else{
                return getResources().getIdentifier("leng4", "drawable", getPackageName());
            }
        }else if(x == 2){
            if(y == 0){
                return getResources().getIdentifier("hai1", "drawable", getPackageName());
            }else if(y == 1) {
                return getResources().getIdentifier("hai2", "drawable", getPackageName());
            }else if(y == 2){
                return getResources().getIdentifier("hai3", "drawable", getPackageName());
            }else{
                return getResources().getIdentifier("hai4", "drawable", getPackageName());
            }
        }else {
            if(y == 0){
                return getResources().getIdentifier("jiu1", "drawable", getPackageName());
            }else if(y == 1) {
                return getResources().getIdentifier("jiu2", "drawable", getPackageName());
            }else if(y == 2){
                return getResources().getIdentifier("jiu3", "drawable", getPackageName());
            }else{
                return getResources().getIdentifier("jiu4", "drawable", getPackageName());
            }
        }
    }
    public static void orderMap(){
        int i = 0, j = 0;
        for(Integer key:hasOrder.keySet())
        {
            correctHasOrder.put(i, hasOrder.get(key));
            i++;
        }
        for(Integer key:notOrder.keySet())
        {
            correctNotOrder.put(j, notOrder.get(key));
            j++;
        }
    }
}
