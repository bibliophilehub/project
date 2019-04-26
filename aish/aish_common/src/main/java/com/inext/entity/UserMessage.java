package com.inext.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserMessage {

    /**
     * 消息状态
     */
    public static final Map<Integer, String> USER_STATUS = new HashMap<Integer, String>();
    public static final Integer STATUS_UNREAD = 0;
    public static final Integer STATUS_READED = 1;
    public static final Integer STATUS_SUCCESS = 2;
    public static final Integer STATUS_FAILD = 3;
    /**
     * 消息类型
     */
    public static final Map<Integer, String> NOTICE_TYPE = new HashMap<Integer, String>();
    public static final Integer MESSAGE = 1;
    public static final Integer EMAIL = 2;
    public static final Integer SMS = 0;

    static {
        USER_STATUS.put(STATUS_UNREAD, "未读");
        USER_STATUS.put(STATUS_READED, "已读");
        USER_STATUS.put(STATUS_SUCCESS, "发送成功");
        USER_STATUS.put(STATUS_FAILD, "发送失败");

        NOTICE_TYPE.put(SMS, "手机短信");
        NOTICE_TYPE.put(MESSAGE, "站内信");
        NOTICE_TYPE.put(EMAIL, "邮件");
    }

    private int id;//短信ID
    private int sendUserId;//发送
    private int receiveUserId;//接收
    private String messageTitle;//短信标题
    private String messageContent;//短信内容
    private int messageStatus;//消息状态  0表示未读 1表示已读 2,发送成功，3，发送失败
    private Date messageSendDatetime;//发送时间
    private String messageSendIp;//发送的IP地址
    private String messageAddress;//手机或者邮箱
    private String messageType;//消息类型 0：短信，1：站内信，2：邮件

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(int sendUserId) {
        this.sendUserId = sendUserId;
    }

    public int getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(int receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Date getMessageSendDatetime() {
        return messageSendDatetime;
    }

    public void setMessageSendDatetime(Date messageSendDatetime) {
        this.messageSendDatetime = messageSendDatetime;
    }

    public String getMessageSendIp() {
        return messageSendIp;
    }

    public void setMessageSendIp(String messageSendIp) {
        this.messageSendIp = messageSendIp;
    }

    public String getMessageAddress() {
        return messageAddress;
    }

    public void setMessageAddress(String messageAddress) {
        this.messageAddress = messageAddress;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }


}
