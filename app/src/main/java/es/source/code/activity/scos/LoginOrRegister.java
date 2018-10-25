package es.source.code.activity.scos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.source.code.model.scos.User;

public class LoginOrRegister extends AppCompatActivity {

    private Handler handler=null;
    private ProgressBar rectangleProgressBar = null;
    //声明Sharedpreferenced对象
    private SharedPreferences sp ;
    private Button buttontest;
    private Button buttonRegister;
    private EditText name;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        rectangleProgressBar = (ProgressBar)findViewById(R.id.circleProgressBar);
        buttontest = (Button) findViewById(R.id.btn_login);
        buttonRegister = (Button) findViewById(R.id.btn_register);
        name = (EditText)findViewById(R.id.et_userName);
        password = (EditText)findViewById(R.id.et_password);

        //检验是否已经登陆
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        String userName = sp.getString("userName","null");
        if(userName != "null"){
            buttonRegister.setVisibility(View.GONE);
            name =(EditText) findViewById (R.id.et_userName);
            name.setText(sp.getString("userName", "null"));
        }

        //登陆按钮
        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动登陆,进度条持续2秒钟后消失
                rectangleProgressBar.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message msg = new Message();
                            msg.what = 2000;
                            mHandler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //验证登录名和密码
                Log.d("CardView", "name: " + name.getText().toString() + "pass" + password.getText().toString());
                boolean isNameValid = isValid(name.getText().toString());
                boolean isPasswordValid = isValid(password.getText().toString());

                if(isNameValid && isPasswordValid){//符合，跳转至MainScreen
                    //创建User
                    User loginUser = new User();
                    loginUser.setOldUser(true);
                    loginUser.setUserName(name.getText().toString());
                    loginUser.setPassword(password.getText().toString());

                    //登录状态存储
                    //获取到edit对象
                    SharedPreferences.Editor edit = sp.edit();
                    //通过editor对象写入数据
                    edit.putString("userName",name.getText().toString());
                    edit.putString("loginState","1");
                    //提交数据存入到xml文件中
                    edit.commit();

                    Toast.makeText(LoginOrRegister.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    //跳转到一下页面
                    Intent intent = new Intent();
                    //启动主页面
                    intent.setClass(LoginOrRegister.this, MainScreen.class);
                    //传递参数值和类对象
                    intent.putExtra("source","LoginSuccess");
                    intent.putExtra("user", loginUser);
                    startActivity(intent);
                }else{//不符合，提示“输入内容不符合规则”
                    Toast.makeText(LoginOrRegister.this, "输入内容不符合规则", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //注册按钮
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name =(EditText) findViewById (R.id.et_userName);
                password =(EditText) findViewById (R.id.et_password);
                //启动登陆,进度条持续2秒钟后消失
                rectangleProgressBar.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message msg = new Message();
                            msg.what = 2000;
                            mHandler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //验证注册名和密码
                boolean isNameValid = isValid(name.getText().toString());
                boolean isPasswordValid = isValid(password.getText().toString());

                if(isNameValid && isPasswordValid){//符合，跳转至MainScreen
                    //创建User
                    User loginUser = new User();
                    loginUser.setOldUser(false);
                    loginUser.setUserName(name.getText().toString());
                    loginUser.setPassword(password.getText().toString());

                    //登录状态存储
                    //获取到edit对象
                    SharedPreferences.Editor edit = sp.edit();
                    //通过editor对象写入数据
                    edit.putString("userName",name.getText().toString());
                    edit.putString("loginState","1");
                    //提交数据存入到xml文件中
                    edit.commit();

                    Toast.makeText(LoginOrRegister.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //跳转到一下页面
                    Intent intent = new Intent();
                    //启动主页面
                    intent.setClass(LoginOrRegister.this, MainScreen.class);
                    //传递参数值和类对象
                    intent.putExtra("source","RegisterSuccess");
                    intent.putExtra("user", loginUser);
                    startActivity(intent);
                }else{//不符合，提示“输入内容不符合规则”
                    Toast.makeText(LoginOrRegister.this, "输入内容不符合规则", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //返回按钮
        Button buttonback;
        buttonback = (Button) findViewById(R.id.btn_back);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginState = sp.getString("loginState","0");
                //获取到edit对象
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("loginState","0");
                //提交数据存入到xml文件中
                edit.commit();

                //跳转到一下页面
                Intent intent = new Intent();
                //启动主页面
                intent.setClass(LoginOrRegister.this, MainScreen.class);
                //传递参数值
                intent.putExtra("source","Return");
                startActivity(intent);
            }
        });
    }
    Boolean isValid(String str){
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher a = p.matcher(str);
        boolean b = a.matches();
        return b;
    }

    //定义一个Handler
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            rectangleProgressBar.setVisibility(View.GONE);
            Thread.currentThread().interrupt();
        }
    };
}
