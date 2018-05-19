package com.example.administrator.italker.ui.socketclient.helper;

import android.util.Log;

import java.util.Arrays;

/**
 * SocketPacketHelper
 * Created by vilyever on 2016/5/19.
 * Feature:
 */
public class SocketPacketHelper {
    final SocketPacketHelper self = this;


    /* Constructors */
    public SocketPacketHelper() {
    }

    public SocketPacketHelper copy() {
        SocketPacketHelper helper = new SocketPacketHelper();
        helper.setOriginal(this);

        helper.setSendHeaderData(getSendHeaderData());
        helper.setSendPacketLengthDataConvertor(getSendPacketLengthDataConvertor());
        helper.setSendTrailerData(getSendTrailerData());
        helper.setSendSegmentLength(getSendSegmentLength());
        helper.setSendSegmentEnabled(isSendSegmentEnabled());
        helper.setSendTimeout(getSendTimeout());
        helper.setSendTimeoutEnabled(isSendTimeoutEnabled());

        helper.setReadStrategy(getReadStrategy());

        helper.setReceiveHeaderData(getReceiveHeaderData());
        helper.setReceivePacketLengthDataLength(getReceivePacketLengthDataLength());
        helper.setReceivePacketDataLengthConvertor(getReceivePacketDataLengthConvertor());
        helper.setReceiveTrailerData(getReceiveTrailerData());
        helper.setReceiveSegmentLength(getReceiveSegmentLength());
        helper.setReceiveSegmentEnabled(isReceiveSegmentEnabled());
        helper.setReceiveTimeout(getReceiveTimeout());
        helper.setReceiveTimeoutEnabled(isReceiveTimeoutEnabled());

        return helper;
    }

    /* Public Methods */
    public void checkValidation() {
        switch (getReadStrategy()) {
            case Manually:
                return;
            case AutoReadToTrailer:
                if (getReceiveTrailerData() == null
                        || getReceiveTrailerData().length <= 0) {
                    throw new IllegalArgumentException("we need ReceiveTrailerData for AutoReadToTrailer");
                }
                return;
            case AutoReadByLength:
                if (getReceivePacketLengthDataLength() <= 0
                        || getReceivePacketDataLengthConvertor() == null) {
                    throw new IllegalArgumentException("we need ReceivePacketLengthDataLength and ReceivePacketDataLengthConvertor for AutoReadByLength");
                }
                return;
        }

        throw new IllegalArgumentException("we need a correct ReadStrategy");
    }

    public byte[] getSendPacketLengthData(int packetLength) {
        if (getSendPacketLengthDataConvertor() != null) {
            return getSendPacketLengthDataConvertor().obtainSendPacketLengthDataForPacketLength(getOriginal(), packetLength);
        }

        return null;
    }

    public int getReceivePacketDataLength(byte[] packetLengthData) {
        if (getReadStrategy() == ReadStrategy.AutoReadByLength) {
            if (getReceivePacketDataLengthConvertor() != null) {
                return getReceivePacketDataLengthConvertor().obtainReceivePacketDataLength(getOriginal(), packetLengthData);
            }
        }

        return 0;
    }

    /* Properties */
    private SocketPacketHelper original;
    protected SocketPacketHelper setOriginal(SocketPacketHelper original) {
        this.original = original;
        return this;
    }
    public SocketPacketHelper getOriginal() {
        if (this.original == null) {
            return this;
        }
        return this.original;
    }

    /**
     * ������Ϣʱ�Զ���ӵİ�ͷ
     */
    private byte[] sendHeaderData;
    public SocketPacketHelper setSendHeaderData(byte[] sendHeaderData) {
        if (sendHeaderData != null) {
            this.sendHeaderData = Arrays.copyOf(sendHeaderData, sendHeaderData.length);
        }
        else {
            this.sendHeaderData = null;
        }
        return this;
    }
    public byte[] getSendHeaderData() {
        return this.sendHeaderData;
    }

    private SendPacketLengthDataConvertor sendPacketLengthDataConvertor;
    public SocketPacketHelper setSendPacketLengthDataConvertor(SendPacketLengthDataConvertor sendPacketLengthDataConvertor) {
        this.sendPacketLengthDataConvertor = sendPacketLengthDataConvertor;
        return this;
    }
    public SendPacketLengthDataConvertor getSendPacketLengthDataConvertor() {
        return this.sendPacketLengthDataConvertor;
    }
    public interface SendPacketLengthDataConvertor {
        byte[] obtainSendPacketLengthDataForPacketLength(SocketPacketHelper helper, int packetLength);
    }

    /**
     * ������Ϣʱ�Զ���ӵİ�β
     */
    private byte[] sendTrailerData;
    public SocketPacketHelper setSendTrailerData(byte[] sendTrailerData) {
        if (sendTrailerData != null) {
            this.sendTrailerData = Arrays.copyOf(sendTrailerData, sendTrailerData.length);
        }
        else {
            this.sendTrailerData = null;
        }
        return this;
    }
    public byte[] getSendTrailerData() {
        return this.sendTrailerData;
    }

    /**
     * ������Ϣʱ�ֶη��͵�ÿ�δ�С
     * �ֶη��Ϳ��Իص�����
     * ����ֵ��ʾÿ�η���byte�ĳ���
     * ������0��ʾ���ֶ�
     */
    private int sendSegmentLength;
    public SocketPacketHelper setSendSegmentLength(int sendSegmentLength) {
        this.sendSegmentLength = sendSegmentLength;
        return this;
    }
    public int getSendSegmentLength() {
        return this.sendSegmentLength;
    }

    /**
     * ��sendSegmentLength������0������false
     */
    private boolean sendSegmentEnabled;
    public SocketPacketHelper setSendSegmentEnabled(boolean sendSegmentEnabled) {
        this.sendSegmentEnabled = sendSegmentEnabled;
        return this;
    }
    public boolean isSendSegmentEnabled() {
        if (getSendSegmentLength() <= 0) {
            return false;
        }
        return this.sendSegmentEnabled;
    }

    /**
     * ���ͳ�ʱʱ��������ʱ���޷�д���Զ��Ͽ�����
     * ����ÿ�����Ͱ���ʼ����ʱ��ʱ�����������ü�ʱ
     */
    private long sendTimeout;
    public SocketPacketHelper setSendTimeout(long sendTimeout) {
        this.sendTimeout = sendTimeout;
        return this;
    }
    public long getSendTimeout() {
        return this.sendTimeout;
    }

    private boolean sendTimeoutEnabled;
    public SocketPacketHelper setSendTimeoutEnabled(boolean sendTimeoutEnabled) {
        this.sendTimeoutEnabled = sendTimeoutEnabled;
        return this;
    }
    public boolean isSendTimeoutEnabled() {
        return this.sendTimeoutEnabled;
    }

    private ReadStrategy readStrategy = ReadStrategy.Manually;
    public SocketPacketHelper setReadStrategy(ReadStrategy readStrategy) {
        this.readStrategy = readStrategy;
        return this;
    }
    public ReadStrategy getReadStrategy() {
        return this.readStrategy;
    }
    public enum ReadStrategy {
        /**
         * �ֶ���ȡ
         * �ֶ�����{@link com.vilyever.socketclient.SocketClient#readDataToData(byte[])}��{@link com.vilyever.socketclient.SocketClient#readDataToLength(int)}��ȡ
         */
        Manually,
        /**
         * �Զ���ȡ����β
         * �����ð�β�����Ϣ
         * �Զ���ȡ��Ϣֱ����ȡ�����β��ͬ�����ݺ󣬻ص����հ�
         */
        AutoReadToTrailer,
        /**
         * �Զ������ȶ�ȡ
         * �����ó��������Ϣ
         * �Զ���ȡ��������Ϣ��ת���ɰ����Ⱥ��ȡ�ó����ֽں󣬻ص����հ�
         */
        AutoReadByLength,
    }

    /**
     * ������Ϣʱÿһ����Ϣ��ͷ����Ϣ
     * ����Ϊnull��ÿһ��������Ϣ��������д�ͷ����Ϣ�������޷���ȡ
     */
    private byte[] receiveHeaderData;
    public SocketPacketHelper setReceiveHeaderData(byte[] receiveHeaderData) {
        if (receiveHeaderData != null) {
            this.receiveHeaderData = Arrays.copyOf(receiveHeaderData, receiveHeaderData.length);
        }
        else {
            this.receiveHeaderData = null;
        }
        return this;
    }
    public byte[] getReceiveHeaderData() {
        return this.receiveHeaderData;
    }

    /**
     * ����ʱ��������data�Ĺ̶��ֽ���
     */
    private int receivePacketLengthDataLength;
    public SocketPacketHelper setReceivePacketLengthDataLength(int receivePacketLengthDataLength) {
        this.receivePacketLengthDataLength = receivePacketLengthDataLength;
        return this;
    }
    public int getReceivePacketLengthDataLength() {
        return this.receivePacketLengthDataLength;
    }

    private ReceivePacketDataLengthConvertor receivePacketDataLengthConvertor;
    public SocketPacketHelper setReceivePacketDataLengthConvertor(ReceivePacketDataLengthConvertor receivePacketDataLengthConvertor) {
        this.receivePacketDataLengthConvertor = receivePacketDataLengthConvertor;
        return this;
    }
    public ReceivePacketDataLengthConvertor getReceivePacketDataLengthConvertor() {
        return this.receivePacketDataLengthConvertor;
    }
    public interface ReceivePacketDataLengthConvertor {
        int obtainReceivePacketDataLength(SocketPacketHelper helper, byte[] packetLengthData);
    }

    /**
     * ������Ϣʱÿһ����Ϣ��β����Ϣ
     * ����Ϊnull��ÿһ��������Ϣ��������д�β����Ϣ����������һ���������ϲ�
     */
    private byte[] receiveTrailerData;
    public SocketPacketHelper setReceiveTrailerData(byte[] receiveTrailerData) {
        if (receiveTrailerData != null) {
            this.receiveTrailerData = Arrays.copyOf(receiveTrailerData, receiveTrailerData.length);
            Log.i("daley",String.format("setReceiveTrailerData:%d",receiveTrailerData[0]));
        }
        else {
            this.receiveTrailerData = null;
        }
        return this;
    }
    public byte[] getReceiveTrailerData() {
        return this.receiveTrailerData;
    }

    /**
     * �ֶν�����Ϣ��ÿ�γ��ȣ����ڰ����ȶ�ȡʱ��Ч
     * �����ô���0ʱ��receiveSegmentEnabled�Զ����Ϊtrue����֮��Ȼ
     * ���ú���ֶ����receiveSegmentEnabled
     */
    private int receiveSegmentLength;
    public SocketPacketHelper setReceiveSegmentLength(int receiveSegmentLength) {
        this.receiveSegmentLength = receiveSegmentLength;
        return this;
    }
    public int getReceiveSegmentLength() {
        return this.receiveSegmentLength;
    }

    /**
     * ��receiveSegmentLength������0������false
     */
    private boolean receiveSegmentEnabled;
    public SocketPacketHelper setReceiveSegmentEnabled(boolean receiveSegmentEnabled) {
        this.receiveSegmentEnabled = receiveSegmentEnabled;
        return this;
    }
    public boolean isReceiveSegmentEnabled() {
        if (getReceiveSegmentLength() <= 0) {
            return false;
        }
        return this.receiveSegmentEnabled;
    }

    /**
     * ��ȡ��ʱʱ��������ʱ��û�ж�ȡ���κ���Ϣ�Զ��Ͽ�����
     */
    private long receiveTimeout;
    public SocketPacketHelper setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
        return this;
    }
    public long getReceiveTimeout() {
        return this.receiveTimeout;
    }

    private boolean receiveTimeoutEnabled;
    public SocketPacketHelper setReceiveTimeoutEnabled(boolean receiveTimeoutEnabled) {
        this.receiveTimeoutEnabled = receiveTimeoutEnabled;
        return this;
    }
    public boolean isReceiveTimeoutEnabled() {
        return this.receiveTimeoutEnabled;
    }

    /* Overrides */
    
    
    /* Delegates */
    
    
    /* Private Methods */
    
}