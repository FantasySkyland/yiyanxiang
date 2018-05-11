package com.example.administrator.italker.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.administrator.common.ui.Activity;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.fragments.ActiveFragment;
import com.example.administrator.italker.ui.fragments.ContactFragment;
import com.example.administrator.italker.ui.fragments.GroupFragment;
import com.example.administrator.italker.ui.util.App;
import com.example.administrator.italker.ui.util.DbHelper;
import com.example.administrator.italker.ui.util.NavHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {
    private String  strrecv="";
    // 从蓝牙未连接到蓝牙已成功连接中的状态变化 用 BluetoothChatService 的状态进行判断
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // send Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int LOCAL_BLUETOOTH_DEVICE_STATE = 3;
    private TextView tvTest;
    // 用于获取蓝牙设备名字的键
    public static final String DEVICE_NAME = "device_name";
    // 用于获取蓝牙状态改变的键
    public static final String TOAST = "toast";
    // Name of the connected device
    private String mConnectedDEviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    public static StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter;
    private BluetoothAdapter mBLuetoothAdapter = null;
    // Member object for the chat services
    // 将所有蓝牙的操作封装到 BluetoothChatService 中，降低类之间的耦合
    public static BluetoothChatService mChatService = null;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;

    @BindView(R.id.navigation_bottom)
    BottomNavigationView mNavigationBottom;

    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.im_search)
    ImageView mImSearch;
    @BindView(R.id.bt_action)
    FloatActionButton mBtAction;
    @BindView(R.id.content_layout)
    FrameLayout mLayContainer;
    @BindView(R.id.temp)
    TextView temp;

    public static DbHelper mDbHelper  = DbHelper.getInstance(App.getInstance(),"MedicineBox.db",null,1);
    public static SQLiteDatabase mSQLiteDatabase =mDbHelper.getWritableDatabase();
    private NavHelper mNavHelper;
    private int serachType = 1;
    private Intent notifiIntent;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    //    @Override
    protected void initData() {
        super.initData();
        notifiIntent = getIntent();
        //从底部导航栏中接管menu,进行手动触发第一次点击
        Menu menu = mNavigationBottom.getMenu();
        //触发首次选中Home
        menu.performIdentifierAction(R.id.action_home, 0);

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                0);
        // requestQueue = MyApplication.getsRequestQueue();
        // 获取本地的蓝牙适配器，开始蓝牙的基本任务，例如发现蓝牙设备，查看已
        // 配对的蓝牙设备，监听其它设备等功能，是连接蓝牙的基础
        mBLuetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //如果没有获取到证明，设备不支持蓝牙
        if (mBLuetoothAdapter == null) {
            Toast.makeText(this, "蓝牙设备不存在", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra("type",2);
                    PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder b = new NotificationCompat.Builder(MainActivity.this,"chat");

                    b.setAutoCancel(false)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.yaoxiang)
                            .setContentTitle("健康小常识")
                            .setContentText("健康知识")
                            .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                            .setContentIntent(contentIntent)
                            .setContentInfo("Info");
                    NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, b.build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.content_layout,
                getSupportFragmentManager(), this);

        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group)).
                add(R.id.action_contact,new NavHelper.Tab<>(ContactFragment.class,R.string.title_contact));


        Glide.with(this).
                load(R.drawable.bg_src_morning).
                centerCrop().
                into(new ViewTarget<View, GlideDrawable>(mAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });

        mNavigationBottom.setOnNavigationItemSelectedListener(this);

        if (notifiIntent != null && notifiIntent.getIntExtra("type",0) ==2){
            mNavHelper.performClickMenu(R.id.action_contact);
        }
    }


    @OnClick({R.id.im_search, R.id.bt_action})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_search:
                if (serachType == 0){
                    Intent intent = new Intent(MainActivity.this,QueryActivity.class);
                    startActivity(intent);

                    //sendMessage("*cmd-track-1!");

                }else {
                    Intent intent = new Intent(this,DeviceListActivity.class);
                    ensureDiscoverable();
                    startActivityForResult(intent,REQUEST_CONNECT_DEVICE_SECURE);
                }

                break;
            case R.id.bt_action:
                Intent intent2 = new Intent(this,AddActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 底部导航栏点击事件触发时调用
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //转接事件到工具类中

        return mNavHelper.performClickMenu(item.getItemId());

    }


    /**
     * 处理后回调的方法
     *
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中获取title的资源id
        //mTxtTitle.setText(newTab.extra);

        //对浮动按钮进行隐藏与显示的动画
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
            serachType = 1;
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                mBtAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
                serachType = 0;
            } else {
                transY = Ui.dipToPx(getResources(), 76);
                serachType = 1;
            }


        }
        //开始动画
        mBtAction.animate().
                rotation(rotation).
                translationY(transY).
                setInterpolator(new AnticipateOvershootInterpolator()).
                setDuration(480).start();

    }

    private static final String TAG = "MainActivity";

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == 1) {
                    connectDevice(data, true);
                }
                break;
//            case REQUEST_CONNECT_DEVICE_INSECURE:
//                // When DeviceListActivity returns with a device to connect
//                if (resultCode == Activity.RESULT_OK) {
//                    connectDevice(data, false);
//                }
//                break;
            case LOCAL_BLUETOOTH_DEVICE_STATE:
                // 用户开启蓝牙后
                if (resultCode == android.app.Activity.RESULT_OK) {
                    setupChat();
                } else {
                    // 提示用户没有开启蓝牙
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case -1:
                Toast.makeText(this, "resultCode=" + resultCode, Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectDevice(Intent data, boolean secure) {
        // 得到连接设备的 mac 地址
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // 得到蓝牙对象
        BluetoothDevice device = mBLuetoothAdapter.getRemoteDevice(address);
        // 连接蓝牙
        mChatService.connect(device, secure);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!mBLuetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, LOCAL_BLUETOOTH_DEVICE_STATE);
        } else {
            if (mChatService == null) setupChat();
        }
    }
    private void setupChat() {
        // 初始化蓝牙服务，建立蓝牙连接,
        // mHandler 的作用是得到从 BluetoothChatService 发来的消息，并作出处理
        mChatService = new BluetoothChatService(this, mHandler);
        // 初始化输出 message 的缓冲区
        mOutStringBuffer = new StringBuffer("");
    }

    //  开启蓝牙设备 300s
    private void ensureDiscoverable() {

        if (mBLuetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    public static void sendMessage(String message) {
        // 只有在已经连接的状态才能正常通信
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            //Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            System.out.println("----no bluetooth-" + R.string.not_connected);
            return;
        }
        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mChatService.write(send);
            // 置空EditText
            mOutStringBuffer.setLength(0);
            System.out.println("----mainactivity--sendmessage ok");
        }
    }

    // onResume() 方法开始执行时，用户才可以与之交互
    @Override
    protected void onResume() {
        super.onResume();
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatService != null) mChatService.stop();
        Log.e(TAG, "--- ON DESTROY ---");
    }

    private boolean flagstart=false;
    // 处理 BluetoothChatService 发来的消息
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                // 蓝牙的状态改变
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            // 设置标题栏的显示状态和连接设备的名字
                            // getString() 用于替换本地化的字符串中一部分
                            // 在 string.xml 下定义命名空间后，可以用于替换。 xliff 的使用
                            //setStatus(getString(R.string.title_connected_to, mConnectedDEviceName));
                            // 将 adapter 中的数据清空 准备开始新的会话
                            //mConversationArrayAdapter.clear();
                            mTxtTitle.setText(getString(R.string.title_connected_to, mConnectedDEviceName));
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            mTxtTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            mTxtTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                // 注意以下的 case 的判断全都是基于与其它蓝牙设备已经连接
                // MESSAGE_STATE_CHANGE 的状态为 BluetoothChatService.STATE_CONNECTED
                // 收到蓝牙发送写入的信息(自己发送的信息)
                case MESSAGE_WRITE:
                    // 得到的 msg.obj 的对象可以直接复用
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("ME say: " + writeMessage);
                    break;
                // 收到蓝牙收到的信息(其它蓝牙设备发送的信息)
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    System.out.println("----mainactivity readBuf--" + Arrays.toString(readBuf));
                    String readMessage = new String(readBuf);
                    temp.setText(readMessage + "°");
                    //Toast.makeText(MainActivity.this, readMessage, Toast.LENGTH_SHORT).show();
                    if(!flagstart&&readMessage.equals("*"))
                    {
                        strrecv="";
                        flagstart=true;
                        break;
                    }
                    if (flagstart) {
                        if (!readMessage.equals("!")) {
                            strrecv += readMessage;
                            temp.setText("药箱温度"+strrecv + "°");
                        }
                        else {
                            flagstart = false;
                            temp.setText("药箱温度"+strrecv + "°");
                            System.out.println("----mainactivity read--" + strrecv);
                            //String[] strlist = strrecv.split("-");
                            //System.out.println("----mainactivity split--" + strlist[0]+"-" + strlist[1]+"-" + strlist[2]);
                                    break;}
                            }



                    //mConversationArrayAdapter.add(mConnectedDEviceName +" say: " + readMessage);
                            /*
                            待处理
                            msgrecv += readMessage + "\n";
                            tvdata.setText(msgrecv);
                            tvdata.invalidate();*/
                    /*温湿度信息	*sta-temp-33.5!
                    烟雾报警	*urgency-smoke-1!
                    */
                    /*if(!flagstart&&readMessage.equals("*"))
                    {
                        strrecv="";
                        flagstart=true;
                    }
                    while (flagstart)
                    {
                        if(!readMessage.equals("!"))
                            strrecv+=readMessage;
                        else
                        {
                            flagstart=false;
                            System.out.println("----mainactivity read--"+strrecv);
                            String[] strlist = strrecv.split("-");
                            System.out.println("----mainactivity read--"+strlist);

                            switch (curposition) {
                                case 0:
                                    fragment1.showdata(strrecv);
                                    break;
                                case 1:
                                    fragment2.showdata(strrecv);
                                    break;
                                case 2:
                                    fragment3.showdata(strrecv);
                                    break;
                                case 3:
                                    fragment4.showdata(strrecv);
                                    break;
                                case 4:
                                    fragment5.showdata(strrecv);
                                    break;
                            }

                        }


                    }*/

                    break;
                // 保存连接到的蓝牙的名字
                case MESSAGE_DEVICE_NAME:
                    mConnectedDEviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "连接到 " + mConnectedDEviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                // 蓝牙的状态改变通知用户
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}
