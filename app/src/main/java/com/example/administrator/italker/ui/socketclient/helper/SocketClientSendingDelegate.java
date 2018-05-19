package com.example.administrator.italker.ui.socketclient.helper;


import com.example.administrator.italker.ui.socketclient.SocketClient;

/**
 * SocketClientSendingDelegate
 * Created by vilyever on 2016/5/30.
 * Feature:
 */
public interface SocketClientSendingDelegate {
    void onSendPacketBegin(SocketClient client, SocketPacket packet);
    void onSendPacketEnd(SocketClient client, SocketPacket packet);
    void onSendPacketCancel(SocketClient client, SocketPacket packet);

    /**
     * ���ͽ��Ȼص�
     * @param client
     * @param packet ���ڷ��͵�packet
     * @param progress 0.0f-1.0f
     * @param sendedLength �ѷ��͵��ֽ���
     */
    void onSendingPacketInProgress(SocketClient client, SocketPacket packet, float progress, int sendedLength);

    class SimpleSocketClientSendingDelegate implements SocketClientSendingDelegate {
        @Override
        public void onSendPacketBegin(SocketClient client, SocketPacket packet) {

        }

        @Override
        public void onSendPacketEnd(SocketClient client, SocketPacket packet) {

        }

        @Override
        public void onSendPacketCancel(SocketClient client, SocketPacket packet) {

        }

        @Override
        public void onSendingPacketInProgress(SocketClient client, SocketPacket packet, float progress, int sendedLength) {

        }
    }
}
