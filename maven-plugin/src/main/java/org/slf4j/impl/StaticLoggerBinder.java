/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * This file is part of slf4j-maven-plugin-log.
 *
 * slf4j-maven-plugin-log is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * slf4j-maven-plugin-log is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with slf4j-maven-plugin-log.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.slf4j.impl;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * Singleton implementation of {@link LoggerFactoryBinder}.<br>
 * This class is directly useable from SLF4J via deferred loading, but Mojo must
 * inject {@link Log} instance before it can be used.
 * 
 * @author Francois Lecomte
 */
public class StaticLoggerBinder implements LoggerFactoryBinder {

    /**
     * {@link StaticLoggerBinder} singleton
     */
    public static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    /**
     * Delegate {@link MavenLoggerFactory}
     */
    private MavenLoggerFactory loggerFactory;

    /**
     * Constructor
     */
    private StaticLoggerBinder() {
        super();
    }

    /**
     * @return {@link StaticLoggerBinder} singleton
     */
    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    /**
     * {@inheritDoc}
     */
    public ILoggerFactory getLoggerFactory() {
        if (loggerFactory == null) {
            try {
                final Log log = (Log ) Class.forName("org.apache.maven.plugin.logging.SystemStreamLog").newInstance();
                setLog(log);

                log.warn("LoggerFactory has not been explicitly initialized. Default system-logger will be used. "
                                + "Please invoke StaticLoggerBinder#setLog(org.apache.maven.plugin.logging.Log) "
                                + "with Mojo's Log instance at the early start of your Mojo");
            } catch (final Exception e) {
                throw new IllegalStateException("LoggerFactory has not been initialized yet."
                                + "Please invoke StaticLoggerBinder#setLog(org.apache.maven.plugin.logging.Log) "
                                + "with Mojo's Log instance at the early start of your Mojo");
            }

        }
        return loggerFactory;
    }

    /**
     * {@inheritDoc}
     */
    public String getLoggerFactoryClassStr() {
        return MavenLoggerFactory.class.getName();
    }

    /**
     * @param log {@link Log}
     */
    public void setLog(Log log) {
        loggerFactory = new MavenLoggerFactory(log);
    }
}
