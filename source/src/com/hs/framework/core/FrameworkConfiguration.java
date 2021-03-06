package com.hs.framework.core;

import android.content.Context;
import android.content.res.Resources;

import com.hs.framework.core.HttpRequestEngine.HttpRequestConfiguration;
import com.hs.framework.thirdpart.api.qq.QqObject;
import com.hs.framework.thirdpart.api.sina.SinaObject;
import com.hs.framework.thirdpart.api.wxapi.WeixinObject;
import com.hs.framework.utils.L;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.a.a.b;

/**
 *
 * @author wanghuan
 * @date 2014年10月11日 下午2:57:18
 * @email hunter.v.wang@gmail.com
 *
 */
public final class FrameworkConfiguration {
	
	/**
	 * 定义程序的运行环境
	 * 默认是开发环境
	 * @author wanghuan
	 */
	public static enum Environment{
		
		/**
		 * 开发环境
		 */
		DEVELOPMENT,
		
		/**
		 * 预发布环境
		 */
		PRE_RELEASE,
		
		/**
		 * 线上环境
		 */
		PRODUCTION
	};
	
	private Environment environment;
	final Context context;
	final Resources resources;
	final ImageLoaderConfiguration imageLoaderConfiguration;
	final HttpRequestConfiguration httpRequestConfiguration;
	final WeixinObject weixinObject;
	final QqObject qqObject;
	final SinaObject sinaObject;
	
	private FrameworkConfiguration(final Builder builder) {
		context = builder.context;
		resources = context.getResources();
		this.environment = builder.environment;
		this.weixinObject = builder.weixinObject;
		this.qqObject = builder.qqObject;
		this.sinaObject = builder.sinaObject;
		this.imageLoaderConfiguration = builder.imageLoaderConfiguration;
		this.httpRequestConfiguration = builder.httpRequestConfiguration;
		L.writeDebugLogs(builder.writeLogs);
	}
	
	/**
	 * 框架配置对象的构建类 {@link FrameworkConfiguration}
	 * @author wanghuan
	 *
	 */
	public static class Builder {
		
		private Context context;
		private Environment environment;
		private ImageLoaderConfiguration imageLoaderConfiguration;
		private HttpRequestConfiguration httpRequestConfiguration;
		private boolean writeLogs = false;
		
		private WeixinObject weixinObject;
		private QqObject qqObject;
		private SinaObject sinaObject;
		
		public Builder(Context context) {
			this.context = context.getApplicationContext();
		}
		
		/**
		 * 设置程序的运行环境为开发环境 {@link Environment}
		 * @return
		 */
		public Builder development(){
			this.environment = Environment.DEVELOPMENT;
			return this;
		}
		
		/**
		 * 设置程序的运行环境为预发布环境 {@link Environment}
		 * @return
		 */
		public Builder prerelease(){
			this.environment = Environment.PRE_RELEASE;
			return this;
		}
		
		/**
		 * 设置程序的运行环境为线上环境 {@link Environment}
		 * @return
		 */
		public Builder production(){
			this.environment = Environment.PRODUCTION;
			return this;
		}
		
		/**
		 * Universal Image Loader 初始化配置
		 * Version: {@code universal-image-loader-1.9.3.jar}
		 * @link https://github.com/nostra13/Android-Universal-Image-Loader
		 * @param imageLoaderConfiguration
		 * @return
		 */
		public Builder initImageLoadConfig(ImageLoaderConfiguration imageLoaderConfiguration){
			this.imageLoaderConfiguration = imageLoaderConfiguration;
			if(this.imageLoaderConfiguration != null){
				ImageLoader.getInstance().init(imageLoaderConfiguration);
			}
			return this;
		}
		
		/**
		 * 
		 * @param httpRequestConfiguration
		 * @return
		 */
		public Builder initHttpRequestConfig(HttpRequestConfiguration httpRequestConfiguration){
			this.httpRequestConfiguration = httpRequestConfiguration;
			if(this.httpRequestConfiguration != null){
				HttpRequestEngine.getInstance().init(httpRequestConfiguration);
			}
			return this;
		}
		
		/**
		 * 
		 * @param weixinConfig
		 * @return
		 */
		public Builder initWeixinConfig(WeixinObject weixinObject){
			this.weixinObject = weixinObject;
			return this;
		}
		
		/**
		 * 
		 * @param qqConfig
		 * @return
		 */
		public Builder initQqConfig(QqObject qqObject){
			this.qqObject = qqObject;
			return this;
		}
		
		/**
		 * 
		 * @param sinaConfig
		 * @return
		 */
		public Builder initSinaConfig(SinaObject sinaObject){
			this.sinaObject = sinaObject;
			return this;
		}
		
		/**
		 * 打开日志输出
		 * @see L
		 */
		public Builder writeDebugLogs() {
			this.writeLogs = true;
			return this;
		}

		/**
		 * 创建框架配置对象
		 * @see FrameworkConfiguration
		 * @return 框架配置对象
		 */
		public FrameworkConfiguration build() {
			initEmptyFieldsWithDefaultValues();
			return new FrameworkConfiguration(this);
		}

		/**
		 * 配置默认设置
		 */
		private void initEmptyFieldsWithDefaultValues() {
			if(environment == null){
				development();
			}
			if(weixinObject == null){
				weixinObject = WeixinObject.getInstance(context);
			}
			if(qqObject == null){
				qqObject = QqObject.getInstance(context);
			}
			if(sinaObject == null){
				sinaObject = SinaObject.getInstance(context);
			}
			if(imageLoaderConfiguration == null){
				ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration
						.Builder(context)
						.threadPriority(Thread.NORM_PRIORITY - 2)
						.denyCacheImageMultipleSizesInMemory()
						.diskCacheFileNameGenerator(new Md5FileNameGenerator())
						.diskCacheSize(50 * 1024 * 1024)
						.tasksProcessingOrder(QueueProcessingType.LIFO)
						.build();
				ImageLoader.getInstance().init(imageLoaderConfiguration);
			}
			if(httpRequestConfiguration == null){
				HttpRequestConfiguration httpRequestConfiguration = new HttpRequestConfiguration();
				// make default ..
				HttpRequestEngine.getInstance().init(httpRequestConfiguration);
			}
		}
	}

}
