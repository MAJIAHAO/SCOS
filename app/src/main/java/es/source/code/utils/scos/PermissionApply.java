package es.source.code.utils.scos;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import java.util.HashMap;

/**
 * Created by yangjh on 2017/3/10.
 * 权限申请的入口类，负责申请对应的权限以及权限申请成功时候后的处理
 * 这里有一种情况目前没有处理，就是比如：
 *  先申请电话权限，然后在还没得到结果时又申请SD卡权限，此时会导致检查权限申请状况时，电话权限检查不到，所以不要这样申请
 */
public class PermissionApply {

    private PermissionUtil permissionUtil;
    private Activity mActivity;
    HashMap<Integer,OnPermissResponse> permissionResponses = new HashMap<Integer,OnPermissResponse>();

    public PermissionApply(Activity activity){
        mActivity = activity;
        permissionUtil = new PermissionUtil();
    }

    /**
     * 请求电话权限
     */
    public void requestPhonePermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_CALL_PHONE,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_CALL_PHONE}, REQUEST_CALL_PHONE,mActivity)){
            callBack(REQUEST_CALL_PHONE);
        }
    }

    /**
     * 请求SD卡权限
     */
    public void requestSDCardPermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_WRITE_EXTERNAL_STORAGE,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE,mActivity)){
            callBack(REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 请求相机权限
     */
    public void requestCameraPermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_CAMERA,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_CAMERA}, REQUEST_CAMERA,mActivity)){
            callBack(REQUEST_CAMERA);
        }
    }

    /**
     * 请求联系人权限
     */
    public void requestContactsPermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_CONTACTS,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_CONTACTS}, REQUEST_CONTACTS,mActivity)){
            callBack(REQUEST_CONTACTS);
        }
    }

    /**
     * 请求日历权限
     */
    public void requestCalendarPermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_CALENDAR,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_CALENDAR}, REQUEST_CALENDAR,mActivity)){
            callBack(REQUEST_CALENDAR);
        }
    }

    /**
     * 请求地理(GPS)权限
     */
    public void requestLocationPermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_LOCATION,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_LOCATION}, REQUEST_LOCATION,mActivity)){
            callBack(REQUEST_LOCATION);
        }
    }

    /**
     * 请求麦克风权限
     */
    public void requestMicrophonePermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_MICROPHONE,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_MICROPHONE}, REQUEST_MICROPHONE,mActivity)){
            callBack(REQUEST_MICROPHONE);
        }
    }

    /**
     * 请求传感器权限
     */
    public void requestSensorsPermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_SENSORS,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_SENSORS}, REQUEST_SENSORS,mActivity)){
            callBack(REQUEST_SENSORS);
        }
    }

    /**
     * 请求短信权限
     */
    public void requestSmsPermission(OnPermissResponse permissionRes){
        permissionResponses.put(REQUEST_SMS,permissionRes);
        if(permissionUtil.requestPermission(new String[]{PERMISSION_SMS}, REQUEST_SMS,mActivity)){
            callBack(REQUEST_SMS);
        }
    }


    /**
     * 接收权限请求的结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        boolean success = permissionUtil.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(!success){//没有成功就打开设置页面
            showTipsDialog();
        }else{
            callBack(requestCode);
        }
    }

    private void callBack(int requestCode){
        OnPermissResponse response = permissionResponses.get(requestCode);
        if(null != response){
            response.onPermissionSuccess();
        }
    }

    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivity(intent);
    }

    public interface OnPermissResponse{
        public abstract void onPermissionSuccess();
    }

    //请求电话方面的权限
    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    private static final int REQUEST_CALL_PHONE = 0x0001;

    //SD卡读取写入权限
    private static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0x0002;

    //相机
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final int REQUEST_CAMERA = 0x0003;

    //读取联系人
    private static final String PERMISSION_CONTACTS = Manifest.permission.READ_CONTACTS;
    private static final int REQUEST_CONTACTS = 0x0004;

    //日历
    private static final String PERMISSION_CALENDAR = Manifest.permission.READ_CALENDAR;
    private static final int REQUEST_CALENDAR = 0x0005;

    //GPS
    private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int REQUEST_LOCATION = 0x0006;

    //麦克风
    private static final String PERMISSION_MICROPHONE = Manifest.permission.RECORD_AUDIO;
    private static final int REQUEST_MICROPHONE = 0x0007;

    //传感器
    private static final String PERMISSION_SENSORS = Manifest.permission.BODY_SENSORS;
    private static final int REQUEST_SENSORS = 0x0008;

    //短信
    private static final String PERMISSION_SMS = Manifest.permission.READ_SMS;
    private static final int REQUEST_SMS = 0x0009;

}
