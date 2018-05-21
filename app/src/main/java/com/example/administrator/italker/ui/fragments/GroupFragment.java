package com.example.administrator.italker.ui.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.common.ui.Fragment;
import com.example.administrator.italker.R;
import com.example.administrator.italker.ui.socketclient.SocketClient;
import com.example.administrator.italker.ui.socketclient.helper.SocketClientAddress;
import com.example.administrator.italker.ui.socketclient.helper.SocketClientDelegate;
import com.example.administrator.italker.ui.socketclient.helper.SocketPacketHelper;
import com.example.administrator.italker.ui.socketclient.helper.SocketResponsePacket;
import com.example.administrator.italker.ui.socketclient.util.CharsetUtil;
import com.example.administrator.italker.ui.util.ApiService;
import com.example.administrator.italker.ui.util.NetUtil;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupFragment extends Fragment {
    String content;
    @BindView(R.id.press)
    TextView mPress;
    @BindView(R.id.shake)
    TextView mShake;
    @BindView(R.id.voice)
    TextView mVoice;
    @BindView(R.id.send)
    Button send;

    private String p = "1";
    private String sh= "1";
    private String v= "1";

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data[] = content.split("\\|");
            if (msg.what == 1) {
                p = data[0];
                sh = data[1];
                v = data[2];
                mPress.setText("压力: " + data[0]);
                mShake.setText("微振动: " + data[1]);
                mVoice.setText("声音: " + data[2]);
            }
        }
    };
    private SocketClient mSocketClient;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mSocketClient != null && !mSocketClient.isConnected()){
            mSocketClient.connect();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetUtil.createServcie(getActivity(), ApiService.class).sendData(87678,
                        p,sh,v,String.valueOf("13342276961")).enqueue(new Callback<ResponseBody>() {


                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity(),"发送成功",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(),"发送失败",Toast.LENGTH_SHORT).show();

                    }


                });
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        sendRequestWithHttpURLConnection();
    }

    private void sendRequestWithHttpURLConnection() {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSocketClient = new SocketClient(new SocketClientAddress());

                mSocketClient.getAddress().setRemoteIP("192.168.4.1"); // 远程端IP地址
                mSocketClient.getAddress().setRemotePort("9000"); // 远程端端口号
                mSocketClient.setCharsetName(CharsetUtil.UTF_8);
                mSocketClient.getSocketPacketHelper().setReadStrategy(SocketPacketHelper.ReadStrategy.AutoReadToTrailer);
                mSocketClient.getSocketPacketHelper().setReceiveTrailerData(new byte[]{10});
                mSocketClient.getSocketPacketHelper().setSendTrailerData(new byte[]{13, 10});
                mSocketClient.registerSocketClientDelegate(new SocketClientDelegate() {
                    @Override
                    public void onConnected(SocketClient client) {

                    }

                    @Override
                    public void onDisconnected(SocketClient client) {
                        mSocketClient.connect();
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

                mSocketClient.connect();

            }

        }).start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mSocketClient != null && mSocketClient.isConnected()){
            mSocketClient.disconnect();
        }
    }
}
