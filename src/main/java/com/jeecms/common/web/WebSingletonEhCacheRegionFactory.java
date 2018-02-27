/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2011, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package com.jeecms.common.web;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.ehcache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Settings;


/**
 * A singleton EhCacheRegionFactory implementation.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Alex Snaps
 */
public class WebSingletonEhCacheRegionFactory extends SingletonEhCacheRegionFactory {
	/*
	private static final WebSingletonEhCacheRegionFactory LOG = Logger.getMessageLogger(
			WebSingletonEhCacheRegionFactory.class,
			WebSingletonEhCacheRegionFactory.class.getName()
	);
	*/

	/**
	 * 
	 */
	private static final long serialVersionUID = 7227034033610133673L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final AtomicInteger REFERENCE_COUNT = new AtomicInteger();

	/**
	 * Constructs a SingletonEhCacheRegionFactory
	 */
	public WebSingletonEhCacheRegionFactory() {
	}

	/**
	 * Constructs a SingletonEhCacheRegionFactory
	 *
	 * @param prop Not used
	 */
	public WebSingletonEhCacheRegionFactory(Properties prop) {
		super();
	}

	@Override
	public void start(Settings settings, Properties properties) throws CacheException {
		this.settings = settings;
		try {
			manager =	CacheManager.getInstance();
			mbeanRegistrationHelper.registerMBean( manager, properties );
		}
		catch (net.sf.ehcache.CacheException e) {
			throw new CacheException( e );
		}
	}
	
	
	@Override
	public void stop() {
		try {
			if ( manager != null ) {
				if ( REFERENCE_COUNT.decrementAndGet() == 0 ) {
					manager.shutdown();
				}
				manager = null;
			}
		}
		catch (net.sf.ehcache.CacheException e) {
			throw new CacheException( e );
		}
	}
	
}
