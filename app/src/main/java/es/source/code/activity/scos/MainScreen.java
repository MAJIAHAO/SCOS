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

public class MainScreen extends AppCompatActivity implements AdapterView.OnItemClickListener{
    //定义以及初始化数据
    private GridView gridView;
    private List<Map<String,Object>> dataList;
    private int[] icon={R.drawable.tab_order, R.drawable.tab_check, R.drawable.tab_login, R.drawable.tab_help};
    private String[] iconName={"点菜","查看订单","登陆/注册","系统帮助"};
    private SimpleAdapter adapter;

    public MainScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);

        //实例化gridView
        gridView=(GridView)findViewById(R.id.gridView);
        /*
        1.准备数据源； 2.新建适配器； 3.GridView加载适配器；4.GridView配置事件监听器
         */
        dataList=new ArrayList<>();
        adapter=new SimpleAdapter(this,getData(),R.layout.item,new String[]{"image","text"},
                new int[]{R.id.image,R.id.text});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        User user = null;
        /////////////////////////////////隐藏功能////////////////////////////////////
        /*LinearLayout line_order = (LinearLayout)findViewById(R.id.btn_order);
        LinearLayout line_check = (LinearLayout)findViewById(R.id.btn_check);*/
        String text = getIntent().getStringExtra("source");
        user =(User)getIntent().getSerializableExtra("user");

        if(text.equals("RegisterSuccess")){
            Toast.makeText(this,"欢迎您成为 SCOS 新用户",Toast.LENGTH_SHORT).show();
            /*line_order.setVisibility(View.VISIBLE);
            line_check.setVisibility(View.VISIBLE);*/
        }
        if(!text.equals("FromEntry") && !text.equals("LoginSuccess")){
            /*line_order.setVisibility(View.GONE);
            line_check.setVisibility(View.GONE);*/
        }


    }
    private List<Map<String,Object>> getData(){
        for(int i=0;i<icon.length;i++){
            Map<String,Object>map=new HashMap<>();
            map.put("image",icon[i]);
            map.put("text",iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"我是"+iconName[position],Toast.LENGTH_SHORT).show();
        Log.i("tag","我是"+iconName[position]);
    }
}
