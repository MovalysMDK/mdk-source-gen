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

/**
 * Simple {@link Log} adapter to {@link Logger}.
 * 
 * @author Francois Lecomte
 */
public class MavenLogAdapter extends AbstractMarkerIgnoringBase {

    /**
     * Maven {@link Log} delegate
     */
    private Log log;

    /**
     * Constructor
     * 
     * @param p_oLog Maven {@link Log} delegate
     */
    public MavenLogAdapter(Log p_oLog) {
        super();
        this.log = p_oLog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return log.getClass().getName();
    }

    // debug

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String p_sFormat, Object p_oArg1, Object p_oArg2) {
        if (log.isDebugEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg1, p_oArg2);
            log.debug(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String p_sFormat, Object p_oArg) {
        if (log.isDebugEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg);
            log.debug(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String p_sFormat, Object[] p_t_oArgArray) {
        if (log.isDebugEnabled()) {
            final String sMsg = MessageFormatter.arrayFormat(p_sFormat, p_t_oArgArray);
            log.debug(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String p_sMsg, Throwable p_oThrowable) {
        log.debug(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(String p_sMsg) {
        log.debug(p_sMsg);
    }

    // error

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String p_sFormat, Object p_oArg1, Object p_oArg2) {
        if (log.isErrorEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg1, p_oArg2);
            log.error(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String p_sFormat, Object p_oArg) {
        if (log.isErrorEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg);
            log.error(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String p_sFormat, Object[] p_t_oArgArray) {
        if (log.isErrorEnabled()) {
            final String sMsg = MessageFormatter.arrayFormat(p_sFormat, p_t_oArgArray);
            log.error(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String p_sMsg, Throwable p_oThrowable) {
        log.error(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String p_sMsg) {
        log.error(p_sMsg);
    }

    // info

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String p_sFormat, Object p_oArg1, Object p_oArg2) {
        if (log.isInfoEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg1, p_oArg2);
            log.info(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String p_sFormat, Object p_oArg) {
        if (log.isInfoEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg);
            log.info(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String p_sFormat, Object[] p_t_oArgArray) {
        if (log.isInfoEnabled()) {
            final String sMsg = MessageFormatter.arrayFormat(p_sFormat, p_t_oArgArray);
            log.info(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String p_sMsg, Throwable p_oThrowable) {
        log.info(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(String p_sMsg) {
        log.info(p_sMsg);
    }

    // trace

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTraceEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(String p_sFormat, Object p_oArg1, Object p_oArg2) {
        if (log.isDebugEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg1, p_oArg2);
            log.debug(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(String p_sFormat, Object p_oArg) {
        if (log.isDebugEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg);
            log.debug(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(String p_sFormat, Object[] p_t_oArgArray) {
        if (log.isDebugEnabled()) {
            final String sMsg = MessageFormatter.arrayFormat(p_sFormat, p_t_oArgArray);
            log.debug(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(String p_sMsg, Throwable p_oThrowable) {
        log.debug(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(String p_sMsg) {
        log.debug(p_sMsg);
    }

    // warn

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String p_sFormat, Object p_oArg1, Object p_oArg2) {
        if (log.isWarnEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg1, p_oArg2);
            log.warn(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String p_sFormat, Object p_oArg) {
        if (log.isWarnEnabled()) {
            final String sMsg = MessageFormatter.format(p_sFormat, p_oArg);
            log.warn(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String p_sFormat, Object[] p_t_oArgArray) {
        if (log.isWarnEnabled()) {
            final String sMsg = MessageFormatter.arrayFormat(p_sFormat, p_t_oArgArray);
            log.warn(sMsg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String p_sMsg, Throwable p_oThrowable) {
        log.warn(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(String p_sMsg) {
        log.warn(p_sMsg);
    }

}
