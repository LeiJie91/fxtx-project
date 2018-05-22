/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fxtx.cloud.utils.cache;

import com.fxtx.framework.util.spring.SpringContextUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Cache工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class CacheUtils {
	
	private static CacheManager cacheManager = ((CacheManager) SpringContextUtils.getBean("cacheManager"));

	private static final String SYS_CACHE = "sysCache";
	private static final String SHOP_ID = "shopId";
	private static final String APPLICATION_CONFIG = "application_config";

	/**
	 * 获取SYS_CACHE缓存
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}
	/**
	 * SHOP_ID
	 * @param key
	 * @return
	 */
	public static Object getShopIdCache(String key) {
		return get(SHOP_ID, key);
	}
	
	/**
	 * 写入SYS_CACHE缓存
	 * @param key
	 * @return
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	/**
	 * 写入SHOP_ID缓存
	 * @param key
	 * @return
	 */
	public static void putShopIdCache(String key, Object value) {
		put(SHOP_ID, key, value);
	}
	
	/**
	 * 从SHOP_ID缓存中移除
	 * @param key
	 * @return
	 */
	public static void removeShopIdCache(String key) {
		remove(SHOP_ID, key);
	}

	/**
	 * 移除应用配置缓存
	 * @author wugong
	 * @date 2018/3/9 14:11
	 * @modify if true,please enter your name or update time
	 * @param
	 */
	public static void removeApplicationConfig(Long applicationId,String keyCode){
		String key = keyCode+"-"+applicationId;
		remove(APPLICATION_CONFIG, key);
	}

	/**
	 * 从SYS_CACHE缓存中移除
	 * @param key
	 * @return
	 */
	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}
	
	/**
	 * 获取缓存
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static Object get(String cacheName, String key) {
		Element element = getCache(cacheName).get(key);
		return element==null?null:element.getObjectValue();
	}

	/**
	 * 写入缓存
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}

	/**
	 * 从缓存中移除
	 * @param cacheName
	 * @param key
	 */
	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}
	
	/**
	 * 获得一个Cache，没有则创建一个。
	 * @param cacheName
	 * @return
	 */
	private static Cache getCache(String cacheName){
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null){
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}

	/**
	 * 获取店铺的应用配置
	 * @Author wugong
	 * @Date 2018/2/28 16:44
	 * @Modify if true,please enter your name or update time
	 * @params
	 * @version v1.0
	 */
	public static Object getApplicationConfig(String keyCode,Long applcationId){
		String key = keyCode+"-"+applcationId;
		return get(APPLICATION_CONFIG, key);
	}

	/**
	 * 创建店铺的应用配置
	 * @Author wugong
	 * @Date 2018/2/28 17:35
	 * @Modify if true,please enter your name or update time
	 * @params
	 * @version v1.0
	 */
	public static void putApplicationConfig(String keyCode,Long applcationId,Object object){
		String key = keyCode+"-"+applcationId;
		Element element = new Element(key, object);
		getCache(APPLICATION_CONFIG).put(element);
	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}
	
}
