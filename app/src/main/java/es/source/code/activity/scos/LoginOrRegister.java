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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import es.source.code.model.scos.User;

public class LoginOrRegister extends AppCompatActivity {

    private ProgressBar rectangleProgressBar = null;
    //声明Sharedpreferenced对象
    private SharedPreferences sp;
    private Button buttontest;
    private Button buttonRegister;
    private EditText name;
    private EditText password;
    private String nameStr;
    private String passwordStr;
    private String result = null;
    private boolean loginOrRegister = false;//false表示登陆，true表示注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        rectangleProgressBar = (ProgressBar) findViewById(R.id.circleProgressBar);
        buttontest = (Button) findViewById(R.id.btn_login);
        buttonRegister = (Button) findViewById(R.id.btn_register);
        name = (EditText) findViewById(R.id.et_userName);
        password = (EditText) findViewById(R.id.et_password);

        //检验是否已经登陆
        sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        String userName = sp.getString("userName", "null");
        if (userName != "null") {
            buttonRegister.setVisibility(View.GONE);
            name.setText(sp.getString("userName", "null"));
        }

        //登陆按钮
        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrRegister = false;
                nameStr = name.getText().toString();
                passwordStr = password.getText().toString();
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
                new Thread(networkTask).start();
            }
        });

        //注册按钮
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrRegister = true;
                nameStr = name.getText().toString();
                passwordStr = password.getText().toString();
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
                // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                new Thread(networkTask).start();
            }
        });

        //返回按钮
        Button buttonback;
        buttonback = (Button) findViewById(R.id.btn_back);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginState = sp.getString("loginState", "0");
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("loginState", "0");
                edit.commit();
                Intent intent = new Intent();
                intent.setClass(LoginOrRegister.this, MainScreen.class);
                intent.putExtra("source", "Return");
                startActivity(intent);
            }
        });
    }

    public void validator(String flag){
        if (flag.equals("1")) {//符合，跳转至MainScreen
            //创建User
            User loginUser = new User();
            if(!loginOrRegister){//登陆
                loginUser.setOldUser(true);
                loginUser.setUserName(name.getText().toString());
                loginUser.setPassword(password.getText().toString());
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("userName",name.getText().toString());
                edit.putString("loginState","1");
                edit.commit();
                Toast.makeText(LoginOrRegister.this, "登陆成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(LoginOrRegister.this, MainScreen.class);
                intent.putExtra("source","LoginSuccess");
                intent.putExtra("user", loginUser);
                startActivity(intent);
            }else{//注册
                loginUser.setOldUser(false);
                loginUser.setUserName(name.getText().toString());
                loginUser.setPassword(password.getText().toString());
                //登录状态存储
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("userName", name.getText().toString());
                edit.putString("loginState", "1");
                edit.commit();
                Toast.makeText(LoginOrRegister.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(LoginOrRegister.this, MainScreen.class);
                intent.putExtra("source", "RegisterSuccess");
                intent.putExtra("user", loginUser);
                startActivity(intent);
            }

        } else {//不符合，提示“输入内容不符合规则”
            Toast.makeText(LoginOrRegister.this, "输入内容不符合规则", Toast.LENGTH_SHORT).show();
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            result = data.getString("value");
            //将result转换成json并拿取信息
            JSONObject json = null;
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            result = json.optString("RESULTCODE");
            Log.i("result", result);
            validator(result);
        }
    };
    Runnable networkTask = new Runnable() {
        //	创建一个StringBuffer
        StringBuffer sb = new StringBuffer();
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            StringBuilder response = new StringBuilder();
            //新建一个URL对象
            try{
                HttpURLConnection connection =  null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://172.16.64.112:8081/login");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.connect();

                    OutputStream outputStream = connection.getOutputStream();
                    //字符串数据的请求
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    String content = "name=" + nameStr + "&password=" + passwordStr;
                    dataOutputStream.writeBytes(content);
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    //此时获取的是字节流
                    InputStream in = connection.getInputStream();
                    //对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in)); //将字节流转化成字符流
                    String line;
                    while ((line = reader.readLine())!= null) {
                        response.append(line);
                    }
                } catch (Exception e ) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e ) {
                            e.printStackTrace();
                        }
                    }
                    if ( connection!= null) {
                        connection.disconnect();
                    }
                }
            } catch(Exception e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", response.toString());
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    //定义一个Handler
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            rectangleProgressBar.setVisibility(View.GONE);
            Thread.currentThread().interrupt();
        }
    };
}
