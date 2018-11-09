package es.source.code.service.scos;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import es.source.code.activity.scos.FoodDetail;
import es.source.code.activity.scos.R;
import es.source.code.model.scos.Food;
import es.source.code.utils.scos.MessageEvent;

import static org.greenrobot.eventbus.EventBus.TAG;

public class UpdateService extends IntentService {

    public UpdateService() {
        super("UpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        new Thread(networkTask).start();

    }


    Runnable networkTask = new Runnable() {
        //	创建一个StringBuffer
        StringBuffer sb = new StringBuffer();
        InputStream in = null;
        @Override
        public void run() {
            // 在这里进行 http request.网络请求相关操作
            StringBuilder response = new StringBuilder();
            //新建一个URL对象
            try{
                HttpURLConnection connection =  null;
                BufferedReader reader = null;

                for(int i = 0; i < 1; i++){
                    try {
                        URL url = new URL("http://172.16.64.112:8081/getfood");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        //connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.connect();
                        //此时获取的是字节流
                        in = connection.getInputStream();
                        //对获取到的输入流进行读取
                        reader = new BufferedReader(new InputStreamReader(in)); //将字节流转化成字符流
                        String line;
                        while ((line = reader.readLine())!= null) {
                            response.append(line);
                        }

                       /* {
                            try {
                                long s = System.currentTimeMillis();
                                //一系列的初始化
                                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder builder = null;
                                builder = factory.newDocumentBuilder(); //获得Document对象
                                Document document = builder.parse(in);
                                for(int q = 0; q < 10000; q++){
                                    //获得student的List
                                    NodeList studentList = document.getElementsByTagName("food");
                                    //遍历student标签
                                    for (int k = 0; k < studentList.getLength(); k++) {
                                        //获得student标签
                                        Node node_student = studentList.item(i);
                                        //获得student标签里面的标签
                                        NodeList childNodes = node_student.getChildNodes();
                                        //新建student对象
                                        Food food = new Food();
                                        //遍历student标签里面的标签
                                        for (int j = 0; j < childNodes.getLength(); j++) {
                                            //获得name和nickName标签
                                            Node childNode = childNodes.item(j);
                                            //判断是name还是nickName
                                            if ("name".equals(childNode.getNodeName())) {
                                                String name = childNode.getTextContent();
                                                //Log.i("name", name);
                                            } else if ("num".equals(childNode.getNodeName())) {
                                                String num = childNode.getTextContent();
                                                //Log.i("num", num);
                                            } else if ("price".equals(childNode.getNodeName())) {
                                                String price = childNode.getTextContent();
                                               // Log.i("price", price);
                                            } else if ("type".equals(childNode.getNodeName())) {
                                                String type = childNode.getTextContent();
                                                //Log.i("type", type);
                                            }
                                        }
                                    }
                                }

                                long e = System.currentTimeMillis();
                                Log.i("xml解析10000次的时间",e - s + "ms");
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    */
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
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String foodStr = data.getString("value");
            //将result转换成json并拿取信息
            JSONObject json = null;
            JSONObject food = null;
            try {
                json = new JSONObject(foodStr);
                food = json.getJSONObject("food");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String name = food.optString("name");
            String num = food.optString("num");
            String price = food.optString("price");
            String type = food.optString("type");
            //创建通知栏管理工具
            NotificationManager notificationManager = (NotificationManager) getSystemService
                    (NOTIFICATION_SERVICE);
            //实例化通知栏构造器
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(UpdateService.this);
            //设置跳转页面
            Food newFood = new Food();
            newFood.setName(name);
            newFood.setPrice(Integer.parseInt(price));
            newFood.setId(getResources().getIdentifier("ftq", "drawable", getPackageName()));

            Intent mIntent = new Intent(UpdateService.this,FoodDetail.class);
            mIntent.putExtra("from",1);
            mIntent.putExtra("food",newFood);
            //传递数据想要成功，需要设置这里的flag参数
            mIntent.setFlags(mIntent.FLAG_ACTIVITY_SINGLE_TOP | mIntent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //设置标题
            String note = "菜品：" + name + "  价格：" + price + " 类型：" + type + " 菜品数量：" + num;
            mBuilder.setContentTitle("新品上架：")
                    //设置内容
                    .setContentText(note)
                    //设置大图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ftq))
                    //设置小图标
                    .setSmallIcon(R.drawable.ftq)
                    //设置通知时间
                    .setWhen(System.currentTimeMillis())
                    //首次进入时显示效果
                    .setTicker("我是测试内容")
                    //点击跳转详细页面
                    .setContentIntent(pendingIntent)
                    //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                    .setDefaults(Notification.DEFAULT_SOUND);
            //发送通知请求
            notificationManager.notify(10, mBuilder.build());
            Log.i("通知：", "新品上架");
        }
    };
}
