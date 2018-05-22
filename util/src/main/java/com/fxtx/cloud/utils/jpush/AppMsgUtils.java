package com.fxtx.cloud.utils.jpush;

import cn.jpush.api.push.PushClient;
import com.fxtx.framework.util.LogUtils;
import com.fxtx.framework.util.NumberUtils;
import com.fxtx.framework.util.spring.SpringContextUtils;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by zengyang on 2017/5/27.
 */
public class AppMsgUtils {
    //卖家
    private static final String APP_KEY_SERVER = "2749647148f3fc00f90238fa";
    private static final String MASTER_SECRET_SERVER = "5cda4e29b56cc743fb7fe8b3";
    static final PushClient pushClientServer = new PushClient(MASTER_SECRET_SERVER, APP_KEY_SERVER);
    //买家
    private static final String APP_KEY = "e252b073c421fee67043cc5f";
    private static final String MASTER_SECRET = "189c842e43527d65d6d1cfae";
    static final PushClient pushClient = new PushClient(MASTER_SECRET, APP_KEY);

//    private static IAppKeyService appKeyService = SpringContextUtils.getBean(IAppKeyService.class);

    /**
     * 发送给买家 的订单消息
     *
     * @param userId
     * @param msg
     * @param status
     */
    public static void sendClientMessage(String userId, String msg, String id, String status) {
        try {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", id);
            map.put("status", status);
            MsgSend.sendAndroid(pushClient, "A"+userId, msg, map);
            MsgSend.sendiOS(pushClient, "A"+userId, msg, map);
        } catch (Exception e) {
            LogUtils.logError("", e);
        }
    }

    /**
     * 发送给卖家的订单消息
     *
     * @param userId
     * @param msg
     * @param status
     */
    public static void sendServerMessage(String userId, String msg, String id, String status) {
        try {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", id);
            map.put("status", status);
            MsgSend.sendAndroid(pushClientServer, "A"+userId, msg, map);
            MsgSend.sendiOS(pushClientServer, "A"+userId, msg, map);
        } catch (Exception e) {
            LogUtils.logError("", e);
        }
    }
    /**
     * 发送给卖家的订单消息
     *
     * @param shopId
     * @param msg
     * @param status
     */
    public static void sendServerMessage(Long shopId, String msg, String id, String status,Double money,String payType) {
        try {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", id == null ? "":id);
            map.put("status", status);
            map.put("money", NumberUtils.numberFormat(money,2));
            map.put("payType", payType);
            MsgSend.sendAndroid(pushClientServer, "A"+shopId, msg, map);
            MsgSend.sendiOS(pushClientServer, "A"+shopId, msg, map);
        } catch (Exception e) {
            LogUtils.logError("", e);
        }
    }


    public static void sendClientMessageApp(String userId, String appKeyServer, String masterSecretServe, String msg, String id, String status) {
        try {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", id);
            map.put("status", status);
            PushClient pushClient = new PushClient(masterSecretServe,appKeyServer);
            MsgSend.sendAndroid(pushClient, "A"+userId, msg, map);
            MsgSend.sendiOS(pushClient, "A"+userId, msg, map);
        } catch (Exception e) {
            LogUtils.logError("", e);
        }
    }
}
