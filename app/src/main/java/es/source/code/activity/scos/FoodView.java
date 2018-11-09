package es.source.code.activity.scos;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import es.source.code.fargment.scos.BlankFragment;
import es.source.code.model.scos.Food;
import es.source.code.model.scos.Position;
import es.source.code.model.scos.User;
import es.source.code.service.scos.ServerObserverService;
import es.source.code.service.scos.UpdateService;
import es.source.code.utils.scos.MessageEvent;


public class FoodView extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> list;
    private MyAdapter adapter;
    public User user;
    Messenger cMessenger;
    Messenger sMessenger;
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
    private Messenger serverMsger;
    private Message msg = new Message();

    private Handler sMessageHandler = new SMessageHandler();
    boolean[] fragmentsUpdateFlag = { false, false, false, false };
    @Override
    protected void onStart() {
        super.onStart();
        //设置服务
        Intent in = new Intent();
        in.setClass(FoodView.this, ServerObserverService.class);
        bindService(in, connection, BIND_AUTO_CREATE);
    }

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
        viewPager.setOffscreenPageLimit(4);
        //初始化img数组id
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
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
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Integer key = i * 4 + j;
                Position pos = new Position(i, j);
                notOrder.put(key, pos);
            }
        }

        //页面，数据源
        list = new ArrayList<>();
        list.add(new BlankFragment(0, FoodView.this));
        list.add(new BlankFragment(1, FoodView.this));
        list.add(new BlankFragment(2, FoodView.this));
        list.add(new BlankFragment(3, FoodView.this));
        //ViewPager的适配器
        adapter = new MyAdapter(getSupportFragmentManager());
        //viewPager.setLayoutManager(new LinearLayoutManager(this));
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

        MenuItem item = (MenuItem) findViewById(R.id.menu_4);
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("mess", "连接建立");
            serverMsger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("mess", "连接断开");
        }
    };

    //toolBar的控制器
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //重新排序
        FoodView.orderMap();
        switch (item.getItemId()) {
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
            case R.id.menu_4:
                String name = (String) item.getTitle();
                if (name.equals("启动实时更新")) {
                    Toast.makeText(this, "启动实时更新", Toast.LENGTH_SHORT).show();
                    item.setTitle("取消实时更新");
                    msg.what = 1;
                    msg.replyTo = clientMessenger;
                    try {
                        serverMsger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "取消实时更新", Toast.LENGTH_SHORT).show();
                    item.setTitle("启动实时更新");
                    msg.what = 0;
                    try {
                        serverMsger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            break;case R.id.menu_5:
                Intent in = new Intent(this, UpdateService.class);
                startService(in);
                Toast.makeText(this, "模拟新品上架", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    class MyAdapter extends FragmentStatePagerAdapter {

        FragmentManager fm = null;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = list.get(position % list.size());
            Log.i("测试信息：", "getItem:position=" + position + ",fragment:"
                    + fragment.getClass().getName() + ",fragment.tag="
                    + fragment.getTag());
            return fragment;
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //得到缓存的fragment
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            //得到tag，这点很重要
            String fragmentTag = fragment.getTag();
            if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {
                //如果这个fragment需要更新
                FragmentTransaction ft = fm.beginTransaction();
                //移除旧的fragment
                ft.remove(fragment);
                //换成新的fragment
                fragment = list.get(position % list.size());
                //添加新fragment时必须用前面获得的tag，这点很重要
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();
                //复位更新标志
                fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
            }
            return fragment;
        }
        public void setFragments() {
            if(list != null){
                FragmentTransaction ft = fm.beginTransaction();
                for(Fragment f:list){
                    ft.remove(f);
                }
                ft.commit();
                ft=null;
                fm.executePendingTransactions();
            }
            notifyDataSetChanged();
        }

        public void refreshItem(int x, int y){
            //得到缓存的fragment
            BlankFragment fragment = (BlankFragment) list.get(x);
            fragment.refreshItem(y);
        }
    }


    //获取资源ID
    public int getImageId(int x, int y) {
        // return 1;
        if (x == 0) {
            if (y == 0) {
                return getResources().getIdentifier("re1", "drawable", getPackageName());
            } else if (y == 1) {
                return getResources().getIdentifier("re2", "drawable", getPackageName());
            } else if (y == 2) {
                return getResources().getIdentifier("re3", "drawable", getPackageName());
            } else {
                return getResources().getIdentifier("re4", "drawable", getPackageName());
            }
        } else if (x == 1) {
            if (y == 0) {
                return getResources().getIdentifier("leng1", "drawable", getPackageName());
            } else if (y == 1) {
                return getResources().getIdentifier("leng2", "drawable", getPackageName());
            } else if (y == 2) {
                return getResources().getIdentifier("leng3", "drawable", getPackageName());
            } else {
                return getResources().getIdentifier("leng4", "drawable", getPackageName());
            }
        } else if (x == 2) {
            if (y == 0) {
                return getResources().getIdentifier("hai1", "drawable", getPackageName());
            } else if (y == 1) {
                return getResources().getIdentifier("hai2", "drawable", getPackageName());
            } else if (y == 2) {
                return getResources().getIdentifier("hai3", "drawable", getPackageName());
            } else {
                return getResources().getIdentifier("hai4", "drawable", getPackageName());
            }
        } else {
            if (y == 0) {
                return getResources().getIdentifier("jiu1", "drawable", getPackageName());
            } else if (y == 1) {
                return getResources().getIdentifier("jiu2", "drawable", getPackageName());
            } else if (y == 2) {
                return getResources().getIdentifier("jiu3", "drawable", getPackageName());
            } else {
                return getResources().getIdentifier("jiu4", "drawable", getPackageName());
            }
        }
    }

    public static void orderMap() {
        int i = 0, j = 0;
        for (Integer key : hasOrder.keySet()) {
            correctHasOrder.put(i, hasOrder.get(key));
            i++;
        }
        for (Integer key : notOrder.keySet()) {
            correctNotOrder.put(j, notOrder.get(key));
            j++;
        }
    }

    private Messenger clientMessenger = new Messenger(new SMessageHandler());

    //定义一个Handler
    private class SMessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    Bundle data = msg.getData();
                    if (data != null) {
                        String str = data.getString("msg");
                        Food food = (Food) data.getSerializable("food");
                        int x = food.getX();
                        int y = food.getY();
                        int remain = food.getRemian();
                        FoodView.foodList[x][y].setRemian(remain);
                        Log.i("DemoLog", "客户端收到Service的消息: " + food);
                        adapter.refreshItem(x,y);
                    }
                    break;
                case 0:
                    break;
            }
        }
    }
}
