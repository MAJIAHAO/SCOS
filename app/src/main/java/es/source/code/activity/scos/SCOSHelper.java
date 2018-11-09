package es.source.code.activity.scos;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.model.scos.MailSenderInfo;
import es.source.code.utils.scos.MessageEvent;
import es.source.code.utils.scos.PermissionApply;
import es.source.code.utils.scos.SimpleMailSender;

import static org.greenrobot.eventbus.EventBus.TAG;


public class SCOSHelper extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //定义以及初始化数据
    private GridView gridView;
    private int[] icon = {R.drawable.protocol, R.drawable.system, R.drawable.phone, R.drawable.message, R.drawable.mail};
    private String[] iconName = {"用户使用协议", "关于系统", "电话人工帮助", "短信帮助", "邮件帮助"};
    private SimpleAdapter adapter;
    private List<Map<String, Object>> dataList;
    private String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
    private String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    private MyServiceReceiver mReceiver01;
    private PermissionApply apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helper);

        // 注册订阅者
        EventBus.getDefault().register(this);

        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS} , -1);
        //实例化gridView
        gridView = (GridView) findViewById(R.id.gv_function);
        /*
        1.准备数据源； 2.新建适配器； 3.GridView加载适配器；4.GridView配置事件监听器
         */
        dataList = new ArrayList<>();
        adapter = new SimpleAdapter(this, getData(), R.layout.item, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        //注册短信广播事件
        IntentFilter mFilter01;
        mFilter01 = new IntentFilter(SMS_SEND_ACTIOIN);
        mReceiver01 = new MyServiceReceiver();
        registerReceiver(mReceiver01, mFilter01);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* 取消注册自定义Receiver */
        if (mReceiver01 != null) {
            unregisterReceiver(mReceiver01);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {//用户使用协议

        } else if (position == 1) {//关于系统

        } else if (position == 2) {//电话人工帮助
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "5554"));
            startActivity(intent);
        } else if (position == 3) {//短信帮助
            SmsManager smsManager = SmsManager.getDefault();
            /* 创建自定义Action常数的Intent(给PendingIntent参数之用) */
            Intent itSend = new Intent(SMS_SEND_ACTIOIN);
            Intent itDeliver = new Intent(SMS_DELIVERED_ACTION);
            /* deliveryIntent参数为送达后接受的广播信息PendingIntent */
            PendingIntent mDeliverPI = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), itDeliver, PendingIntent.FLAG_UPDATE_CURRENT);
            /* sentIntent参数为传送后接受的广播信息PendingIntent */
            PendingIntent mSendPI = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), itSend, PendingIntent.FLAG_UPDATE_CURRENT);
            smsManager.sendTextMessage("5554", null, "test scos helper", mSendPI, mDeliverPI);
            Toast.makeText(SCOSHelper.this, "求助短信发送成功", Toast.LENGTH_SHORT).show();
        } else {//邮件帮助
            sendEmail();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN )
    public void onMessageEvent(MessageEvent event) {
        Log.i(TAG, "接收到EventBus " + event.getMessage());
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            Bundle data = msg.getData();
            boolean bool = data.getBoolean("value");
            if(bool)
                Toast.makeText(SCOSHelper.this, "求助邮件已发送成功", Toast.LENGTH_SHORT).show();
        }
    };

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }

    private void sendEmail() {
        new Thread() {
            @Override
            public void run() {
                MailSenderInfo mailInfo = new MailSenderInfo();
                mailInfo.setMailServerHost("smtp.qq.com");
                mailInfo.setMailServerPort("25");
                mailInfo.setValidate(true);
                mailInfo.setUserName("279695176@qq.com");  //你的邮箱地址
                mailInfo.setPassword("szjjqdxtcyfycabb");//您的邮箱密码
                mailInfo.setFromAddress("279695176@qq.com");//和上面username的邮箱地址一致
                mailInfo.setToAddress("15990062427@163.com");
                mailInfo.setSubject("hello world");
                mailInfo.setContent("hello world");

                SimpleMailSender sms = new SimpleMailSender();
                boolean b = sms.sendTextMail(mailInfo);//发送文体格式,返回是否发送成功的boolean类型\
                Log.e("MainActivity", "MainActivity sendEmail()" + b);
                if(b){
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putBoolean("value", b);
                    msg.setData(data);
                    mHandler.sendMessage(msg);
                    // 发布事件
                    EventBus.getDefault().post(new MessageEvent("Hello EventBus!"));
                }
            }
        }.start();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        apply.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

//短信监听器
class MyServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        try {
            /* Android.content.BroadcastReceiver.getResultCode()方法 */
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    /* 发送短信成功 */
                    Log.d("lmn", "----发送短信成功---------------------------");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    /* 发送短信失败 */
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                case SmsManager.RESULT_ERROR_NULL_PDU:
                default:
                    Log.d("lmn", "----发送短信失败---------------------------");
                    break;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}