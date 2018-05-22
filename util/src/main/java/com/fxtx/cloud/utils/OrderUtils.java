package com.fxtx.cloud.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/6/23.
 */
public class OrderUtils {

    private static SimpleDateFormat dateTimeFormater = new SimpleDateFormat("yyyyMMddHHmmss");
    private static String qbOrderPrefix = "QB";
    private static String refundOrderPrefix = "RE";
    private static String transOrderPrefix = "TR";
    private static String drawTransOrderPrefix = "DTR";

    /**
     * 获取钱包支付订单流水号
     *
     * @return
     */
    public static String getQBOrderSn(){

        //生成订单号
        StringBuffer buffer = new StringBuffer();
        buffer.append(qbOrderPrefix);
        buffer.append(dateTimeFormater.format(new Date()));
        buffer.append(getOrderIdByUUId());
        return buffer.toString();
    }
    /**
     * 获取退款支付订单流水号
     *
     * @return
     */
    public static String getRefundOrderSn(){

        //生成订单号
        StringBuffer buffer = new StringBuffer();
        buffer.append(refundOrderPrefix);
        buffer.append(dateTimeFormater.format(new Date()));
        buffer.append(getOrderIdByUUId());
        return buffer.toString();
    }

    /**
     * 获取转账支付订单流水号
     *
     * @return
     */
    public static String getTransOrderSn(){

        //生成订单号
        StringBuffer buffer = new StringBuffer();
        buffer.append(transOrderPrefix);
        buffer.append(dateTimeFormater.format(new Date()));
        buffer.append(getOrderIdByUUId());
        return buffer.toString();
    }
    /**
     * 获取钱包转账支付订单流水号
     *
     * @return
     */
    public static String getDrawTransOrderSn(){

        //生成订单号
        StringBuffer buffer = new StringBuffer();
        buffer.append(drawTransOrderPrefix);
        buffer.append(dateTimeFormater.format(new Date()));
        buffer.append(getOrderIdByUUId());
        return buffer.toString();
    }

    private static String getOrderIdByUUId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return String.format("%015d", hashCodeV);
    }
}
