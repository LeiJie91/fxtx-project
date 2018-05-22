package com.fxtx.cloud;

import com.fxtx.framework.util.DateUtil;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.io.GeohashUtils;
import com.spatial4j.core.shape.Rectangle;
import net.sf.jmimemagic.*;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
//        Magic parser = new Magic() ;
//        MagicMatch match = null;
//        try {
//            match = parser.getMagicMatch(new File("d:\\cxxa6VoVKmCAHxmwAATaTyzP5So193.jpg"),true);
//        } catch (MagicParseException e) {
//            e.printStackTrace();
//        } catch (MagicMatchNotFoundException e) {
//            e.printStackTrace();
//        } catch (MagicException e) {
//            e.printStackTrace();
//        }
//        System.out.println(match.getMimeType());

//        /**
//         * 1. 搜索
//         在数据库中搜索出接近指定范围内的商户，如：搜索出1公里范围内的。
//         2. 过滤
//         搜索出来的结果可能会存在超过1公里的，需要再次过滤。如果对精度没有严格要求，可以跳过。
//         3. 排序
//         距离由近到远排序。如果不需要，可以跳过。
//         4. 分页
//         如果需要2、3步，才需要对分页特殊处理。如果不需要，可以在第1步直接SQL分页。
//         */
//        double lon = 116.312528, lat = 39.983733;// 移动设备经纬度
//        int radius = 1;// 千米
//        SpatialContext geo = SpatialContext.GEO;
//        Rectangle rectangle = geo.getDistCalc().calcBoxByDistFromPt(
//                geo.makePoint(lon, lat), radius * DistanceUtils.KM_TO_DEG, geo, null);
//        System.out.println(rectangle.getMinX() + "-" + rectangle.getMaxX());// 经度范围
//        System.out.println(rectangle.getMinY() + "-" + rectangle.getMaxY());
//        GeohashUtils.encodeLatLon(lat, lon);

//        System.out.println(3.41 - Math.floor(3.41));
//        System.out.println(Math.round(3.41)-3.41);
        Date date = new Date();
    }
}
