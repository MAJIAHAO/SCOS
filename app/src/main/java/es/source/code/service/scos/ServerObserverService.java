package es.source.code.service.scos;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import es.source.code.model.scos.Food;

public class ServerObserverService extends Service {
    boolean flag = true;
    private int num = 0;
    private boolean run = false;
    Context mContext;
    private Handler cMessageHandler = new CMessageHandler();
    public Messenger sMessenger = new Messenger(cMessageHandler);
    private Messenger mActivityMessenger;
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(30, 60, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    Thread a = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //模拟接收剩余库存
    public void simulateRemain() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("测试：", "simulateStart");
        a = new Thread(networkTask);
        a.start();//开启线程
    }

    public void simulateStop() {
        Log.e("测试：", "simulateStop");
        a.interrupt();
    }

    //定义一个Handler
    private class CMessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivityMessenger = msg.replyTo;
            switch (msg.what) {
                case 1:
                    simulateRemain();
                    break;
                case 0:
                    simulateStop();
                    break;
            }
        }
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            Food food = new Food();
            Random rand = new Random();
            int x = (int) (rand.nextInt(4));
            int y = (int) (rand.nextInt(4));
            int r = (int) (rand.nextInt(100));
            food.setX(x);
            food.setY(y);
            food.setRemian(r);
            Log.v("thread", food.toString());

            if (mActivityMessenger != null) {
                Log.i("DemoLog", "ServerObserverService向客户端回信");
                Message msgToClient = Message.obtain();
                msgToClient.what = 10;
                //可以通过Bundle发送跨进程的信息
                Bundle bundle = new Bundle();
                bundle.putString("msg", "你好，客户端，我是ServerObserverService");
                bundle.putSerializable("food", food);
                msgToClient.setData(bundle);
                try {
                    mActivityMessenger.send(msgToClient);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e("DemoLog", "ServerObserverService向客户端发送信息失败: " + e.getMessage());
                }
            }
        }
    };
}