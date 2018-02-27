package com.jeecms.common.hibernate4;

import java.io.IOException;
import java.util.Properties;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.ObjectExistsException;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.DiskStoreConfiguration;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * 为WEB应用提供缓存。
 * 
 * 解决配置文件地址和缓存文件存放地址的问题。支持/WEB-INF的地址格式。
 */
public final class SpringEhCacheProvider extends EhCacheRegionFactory {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8836863935876686517L;

	private static final Logger log = LoggerFactory
			.getLogger(SpringEhCacheProvider.class);

	private Resource configLocation;
	private Resource diskStoreLocation;
	private CacheManager manager;
	

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setDiskStoreLocation(Resource diskStoreLocation) {
		this.diskStoreLocation = diskStoreLocation;
	}


	/**
	 * Callback to perform any necessary initialization of the underlying cache
	 * implementation during SessionFactory construction.
	 * <p/>
	 * 
	 * @param properties
	 *            current configuration settings.
	 */
	public final void start(Properties properties) throws CacheException {
		if (manager != null) {
			String s = "Attempt to restart an already started EhCacheProvider. Use sessionFactory.close() "
					+ " between repeated calls to buildSessionFactory. Using previously created EhCacheProvider."
					+ " If this behaviour is required, consider using SingletonEhCacheProvider.";
			log.warn(s);
			return;
		}
		Configuration config = null;
		try {
			if (configLocation != null) {
				config = ConfigurationFactory.parseConfiguration(configLocation
						.getInputStream());
				if (this.diskStoreLocation != null) {
					DiskStoreConfiguration dc = new DiskStoreConfiguration();
					dc.setPath(this.diskStoreLocation.getFile()
							.getAbsolutePath());
					try {
						config.addDiskStore(dc);
					} catch (ObjectExistsException e) {
						String s = "if you want to config distStore in spring,"
								+ " please remove diskStore in config file!";
						log.warn(s, e);
					}
				}
			}
		} catch (IOException e) {
			log.warn("create ehcache config failed!", e);
		}
		if (config != null) {
			manager = new CacheManager(config);
		} else {
			manager = new CacheManager();
		}
	}

	/**
	 * Callback to perform any necessary cleanup of the underlying cache
	 * implementation during SessionFactory.close().
	 */
	public final void stop() {
		if (manager != null) {
			manager.shutdown();
			manager = null;
		}
	}
	
}
