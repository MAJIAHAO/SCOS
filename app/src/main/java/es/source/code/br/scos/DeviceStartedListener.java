package es.source.code.br.scos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import es.source.code.service.scos.UpdateService;

public class DeviceStartedListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Toast.makeText(context, "广播我已经启动服务了", Toast.LENGTH_SHORT).show();
            Intent service = new Intent(context,UpdateService.class);
            context.startService(service);
        }
    }
}
