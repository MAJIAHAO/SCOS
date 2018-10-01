package es.source.code.activity.scos;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SCOSEntry extends AppCompatActivity{

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

        /*TextView param = (TextView) findViewById(R.id.mjh_title);
        String text = getIntent().getStringExtra("source");
        param.setText(text);*/

        //1 初始化  手势识别器
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                // e1: 第一次按下的位置   e2   当手离开屏幕 时的位置  velocityX  沿x 轴的速度  velocityY： 沿Y轴方向的速度
                //判断竖直方向移动的大小
                if(Math.abs(e1.getRawY() - e2.getRawY())>100){
                    //Toast.makeText(getApplicationContext(), "动作不合法", 0).show();
                    return true;
                }
                if(Math.abs(velocityX)<150){
                    //Toast.makeText(getApplicationContext(), "移动的太慢", 0).show();
                    return true;
                }

                if((e1.getRawX() - e2.getRawX()) >200){// 表示 向右滑动表示下一页
                    //显示下一页
                    next(null);
                    return true;
                }

                if((e2.getRawX() - e1.getRawX()) >200){  //向左滑动 表示 上一页
                    //显示上一页
                    pre(null);
                    return true;//消费掉当前事件  不让当前事件继续向下传递
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }
    /**
     * 下一个页面
     * @param view
     */
    public  void next(View view){
        //显示message："next"
        Toast.makeText(SCOSEntry.this, "next", Toast.LENGTH_SHORT).show();
    }
    /**
     * 上一个页面
     * @param view
     */
    public void pre(View view){
        //显示message："pre"
        Toast.makeText(SCOSEntry.this, "pre", Toast.LENGTH_SHORT).show();
        //跳转到一下页面
        Intent intent = new Intent();
        //启动主页面
        intent.setClass(SCOSEntry.this, MainScreen.class);
        //传递参数值
        intent.putExtra("source","FromEntry");
        startActivity(intent);
    }

    //重写activity的触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //2.让手势识别器生效
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
