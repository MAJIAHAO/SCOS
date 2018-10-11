package es.source.code.activity.scos;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.model.scos.User;

public class MainScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //定义以及初始化数据
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private int[] icon = {R.drawable.tab_order, R.drawable.tab_check, R.drawable.tab_login, R.drawable.tab_help};
    private int[] iconNotLogin = {R.drawable.tab_login, R.drawable.tab_help};
    private String[] iconName = {"点菜", "查看订单", "登陆/注册", "系统帮助"};
    private String[] iconNameNotLogin = {"登陆/注册", "系统帮助"};
    private SimpleAdapter adapter;
    private static int logined = 0;
    private static User user = null;

    public MainScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);

        User u = (User) getIntent().getSerializableExtra("user");
        if(u != null)
            user = u;
        Log.d("CardView", "用户: " + user);

        //实例化gridView
        gridView = (GridView) findViewById(R.id.gridView);
        /*
        1.准备数据源； 2.新建适配器； 3.GridView加载适配器；4.GridView配置事件监听器
         */
        dataList = new ArrayList<>();
        adapter = new SimpleAdapter(this, getData(), R.layout.item, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);


        String text = getIntent().getStringExtra("source");

        Object item = adapter.getItem(0);
        long itemId = adapter.getItemId(0);
        Log.d("CardView", "结果: " + item + ", id:" + itemId);

        if (text != null) {
            if (text.equals("RegisterSuccess")) {
                Toast.makeText(this, "欢迎您成为 SCOS 新用户", Toast.LENGTH_SHORT).show();
            }
            if (!text.equals("FromEntry") && !text.equals("LoginSuccess") && !text.equals("RegisterSuccess")) {
                refresh();
            }else{//登陆成功
                logined = 1;
                refresh();
            }
        }
    }

    private List<Map<String, Object>> getData() {
        if (logined == 1)
            for (int i = 0; i < icon.length; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("image", icon[i]);
                map.put("text", iconName[i]);
                dataList.add(map);
            }
        else
            for (int i = 0; i < iconNotLogin.length; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("image", iconNotLogin[i]);
                map.put("text", iconNameNotLogin[i]);
                dataList.add(map);
            }
        return dataList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this,"我是"+iconName[position],Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        if(logined == 0){
            if (position == 0) {//登陆注册
                intent.setClass(MainScreen.this, LoginOrRegister.class);
                startActivity(intent);
            } else {//系统帮助

            }
        }else{
            if (position == 0) {//点菜系统
                intent.setClass(MainScreen.this, FoodView.class);
                intent.putExtra("user", user);
                intent.putExtra("test", "test");
                startActivity(intent);
            } else if (position == 1) {//订单查看
                intent.setClass(MainScreen.this, FoodOrderView.class);
                intent.putExtra("user", user);
                startActivity(intent);
            } else if (position == 2) {//登陆注册
                intent.setClass(MainScreen.this, LoginOrRegister.class);
                startActivity(intent);
            } else {//系统帮助

            }
        }
    }
    /**
     * 刷新
     */
    private void refresh() {
        finish();
        Intent intent = new Intent(MainScreen.this, MainScreen.class);
        startActivity(intent);
    }
}
