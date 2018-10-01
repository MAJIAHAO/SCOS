package es.source.code.activity.scos;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {
    private LinearLayout llChat, llFriends, llContacts, llSettings;
    private ImageView ivChat, ivFriends, ivContacts, ivSettings, ivCurrent;
    private TextView tvChat, tvFriends, tvContacts, tvSettings, tvCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);

        LinearLayout line_order = (LinearLayout)findViewById(R.id.btn_order);
        LinearLayout line_check = (LinearLayout)findViewById(R.id.btn_check);
        String text = getIntent().getStringExtra("source");
        /*TextView param = (TextView)findViewById(R.id.tvChat);
        param.setText(text);*/
        if(!text.equals("FromEntry") && !text.equals("LoginSuccess")){
            line_order.setVisibility(View.GONE);
            line_check.setVisibility(View.GONE);
        }
    }
}
