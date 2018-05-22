package com.fxtx.cloud.utils.jpush;

import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.fxtx.framework.util.LogUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zengyang on 2017/5/27.
 * 消息推送模式修改
 */
public class MsgSend {


    /**
     * 安卓 单条消息发送
     **/
    public static PushResult sendAndroid(PushClient client, Set<String> sendUserId, String msg) {
        try {
            PushPayload.Builder builder = getPushBuilder(sendUserId);
            Notification notification = Notification.alert(msg);
            builder.setNotification(notification);
            PushPayload pushPayload = builder.build();
            return client.sendPush(pushPayload);
        } catch (Exception e) {
            LogUtils.logError("发送失败", e);
        }
        return null;
    }

    /**
     * 安卓 自定义消息发送
     **/
    public static PushResult sendAndroid(PushClient client, Set<String> sendUserId, String msg, Map map) {
        try {
            PushPayload.Builder builder = getPushBuilder(sendUserId);
            Notification notification = Notification.android(msg, null, map);
            builder.setNotification(notification);
            Message message = Message.content(msg);
            builder.setMessage(message);
            PushPayload pushPayload = builder.build();
            return client.sendPush(pushPayload);
        } catch (Exception e) {
            LogUtils.logError("发送失败", e);
        }
        return null;
    }

    public static PushResult sendAndroid(PushClient client, String sendUserId, String msg) {
        Set<String> userids = new HashSet<String>();
        userids.add(sendUserId);
        return sendAndroid(client, userids, msg);
    }

    public static PushResult sendAndroid(PushClient client, String sendUserId, String msg, Map map) {
        Set<String> userids = new HashSet<String>();
        userids.add(sendUserId);
        return sendAndroid(client, userids, msg, map);
    }


    /**
     * iOS自定义消息发送
     *
     * @param client
     * @param sendUserId
     * @param msg
     * @param map
     * @return
     */
    public static PushResult sendiOS(PushClient client, Set<String> sendUserId, String msg, Map map) {
        try {
            PushPayload.Builder builder = getPushBuilder(sendUserId);
            Notification notification = Notification.ios(msg, map);
            builder.setNotification(notification);
            Message message = Message.content(msg);
            builder.setMessage(message);
            Options options = Options.newBuilder().setApnsProduction(false).build();
            builder.setOptions(options);
            PushPayload pushPayload = builder.build();
            return client.sendPush(pushPayload);
        } catch (Exception e) {
            LogUtils.logError("发送失败", e);
        }
        return null;
    }

    /**
     * iOS 单条消息发送
     *
     * @param client
     * @param sendUserId
     * @param msg
     * @return
     */
    public static PushResult sendiOS(PushClient client, Set<String> sendUserId, String msg) {
        try {
            PushPayload.Builder builder = getPushBuilder(sendUserId);
            Notification notification = Notification.alert(msg);
            Message message = Message.content(msg);
            builder.setMessage(message);
            Options options = Options.newBuilder().setApnsProduction(false).build();
            builder.setOptions(options);
            builder.setNotification(notification);
            PushPayload pushPayload = builder.build();
            return client.sendPush(pushPayload);
        } catch (Exception e) {
            LogUtils.logError("发送失败", e);
        }
        return null;
    }

    /**
     * 推送基本信息配置
     *
     * @param sendUserId
     * @return
     */
    private static PushPayload.Builder getPushBuilder(Set<String> sendUserId) {
        PushPayload.Builder builder = PushPayload.newBuilder();
        Platform platform = Platform.all();
        builder.setPlatform(platform);
        Audience audience;
        if (sendUserId == null || sendUserId.size() == 0) {
            audience = Audience.all();
        } else {
            audience = Audience.alias(sendUserId);
        }
        builder.setAudience(audience);
        return builder;
    }


    public static PushResult sendiOS(PushClient client, String sendUserId, String msg) {
        Set<String> userids = new HashSet<String>();
        userids.add(sendUserId);
        return sendiOS(client, userids, msg);
    }

    public static PushResult sendiOS(PushClient client, String sendUserId, String msg, Map map) {
        Set<String> userids = new HashSet<String>();
        userids.add(sendUserId);
        return sendiOS(client, userids, msg, map);
    }
}
