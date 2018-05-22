package com.fxtx.cloud.utils;

import com.fxtx.framework.util.StringUtils;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.io.GeohashUtils;

/**
 * Created by Administrator on 2017/8/15.
 */
public class GeoUtils {

    /**
     * 计算两点距离
     *
     * @param lng1
     * @param lng2
     * @param lat1
     * @param lat2
     * @return
     */
    public static long getMiDistance(double lng1,double lat1,double lng2,double lat2){
        double distance = getKmDistance(lng1,lat1,lng2,lat2);
        return (long)(distance*1000);
    }

    /**
     * 计算两点距离
     *
     * @param lng1
     * @param lng2
     * @param lat1
     * @param lat2
     * @return
     */
    public static double getKmDistance(double lng1,double lat1,double lng2,double lat2){
        SpatialContext geo = SpatialContext.GEO;
        double distance = geo.calcDistance(geo.makePoint(lng1, lat1), geo.makePoint(lng2, lat2)) * DistanceUtils.DEG_TO_KM;
        return distance;
    }

    /**
     * 计算两点距离
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double getKmDistance(String lng1,String lat1,String lng2,String lat2){
        double lng1Val = 0;
        if(StringUtils.isFloatNumeric(lng1)){
            lng1Val = Double.parseDouble(lng1);
        }
        double lat1Val = 0;
        if(StringUtils.isFloatNumeric(lat1)){
            lat1Val = Double.parseDouble(lat1);
        }
        double lng2Val = 0;
        if(StringUtils.isFloatNumeric(lng2)){
            lng2Val = Double.parseDouble(lng2);
        }

        double lat2Val = 0;
        if(StringUtils.isFloatNumeric(lat2)){
            lat2Val = Double.parseDouble(lat2);
        }
        return getKmDistance(lng1Val,lat1Val,lng2Val,lat2Val);
    }

    /**
     * 计算两点距离
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static long getMiDistance(String lng1,String lat1,String lng2,String lat2){
        double distance = getKmDistance(lng1,lat1,lng2,lat2);
        return (long)(distance*1000);
    }

    /**
     * 获取gohash
     *
     * @param lng
     * @param lat
     * @return
     */
    public static String getGoeHash(String lng,String lat){
        double lng1Val = 0;
        if(StringUtils.isFloatNumeric(lng)){
            lng1Val = Double.parseDouble(lng);
        }
        double lat1Val = 0;
        if(StringUtils.isFloatNumeric(lat)){
            lat1Val = Double.parseDouble(lat);
        }
        return getGoeHash(lng1Val,lat1Val);
    }

    /**
     * 获取gohash
     *
     * @param lng
     * @param lat
     * @return
     */
    public static String getGoeHash(double lng,double lat){
        return GeohashUtils.encodeLatLon(lng, lat);
    }

    public static void main(String args[]){
        System.out.println(getGoeHash(115.89656,39.35112));
    }
}
