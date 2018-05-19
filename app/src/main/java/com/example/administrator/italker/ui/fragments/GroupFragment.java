package com.example.administrator.italker.ui.fragments;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.common.ui.Fragment;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.socketclient.SocketClient;
import com.example.administrator.italker.ui.socketclient.helper.SocketClientAddress;
import com.example.administrator.italker.ui.socketclient.helper.SocketClientDelegate;
import com.example.administrator.italker.ui.socketclient.helper.SocketPacketHelper;
import com.example.administrator.italker.ui.socketclient.helper.SocketResponsePacket;
import com.example.administrator.italker.ui.socketclient.util.CharsetUtil;

import butterknife.BindView;


public class GroupFragment extends Fragment {
    String content;
    @BindView(R.id.press)
    TextView mPress;
    @BindView(R.id.shake)
    TextView mShake;
    @BindView(R.id.voice)
    TextView mVoice;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data[] = content.split("|");
            if (msg.what == 1) {
                mPress.setText("压力: " + data[0]);
                mShake.setText("微振动: " + data[1]);
                mVoice.setText("声音: " + data[2]);
            }
        }
    };


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void sendRequestWithHttpURLConnection() {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SocketClient socketClient = new SocketClient(new SocketClientAddress());

                socketClient.getAddress().setRemoteIP("192.168.4.1"); // 远程端IP地址
                socketClient.getAddress().setRemotePort("9000"); // 远程端端口号
                socketClient.setCharsetName(CharsetUtil.UTF_8);
                socketClient.getSocketPacketHelper().setReadStrategy(SocketPacketHelper.ReadStrategy.AutoReadToTrailer);
                socketClient.getSocketPacketHelper().setReceiveTrailerData(new byte[]{10});
                socketClient.getSocketPacketHelper().setSendTrailerData(new byte[]{13, 10});
                socketClient.registerSocketClientDelegate(new SocketClientDelegate() {
                    @Override
                    public void onConnected(SocketClient client) {

                    }

                    @Override
                    public void onDisconnected(SocketClient client) {
                        socketClient.connect();
                        //todo 重连失败怎么办
                    }

                    @Override
                    public void onResponse(final SocketClient client, @NonNull SocketResponsePacket responsePacket) {
                        byte[] data = responsePacket.getData(); // 获取接收的byte数组，不为null

                        String message = responsePacket.getMessage();
                        content = message;// 获取按默认设置的编码转化的String，可能为null
                        if (data == null || message == null) {
                            content = new String(data);
                            //Log.i(Consts.LOG_TAG,data==null?"data null":"message null");

                            return;
                        }

                        Message msg = new Message();

                        msg.obj = message;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });

                socketClient.connect();

            }

        }).start();
    }

}
