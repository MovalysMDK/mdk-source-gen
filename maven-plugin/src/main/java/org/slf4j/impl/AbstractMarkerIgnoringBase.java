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
package org.slf4j.impl;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * This class serves as base for adapters or native implementations of logging
 * systems lacking Marker support. In this implementation, methods taking marker
 * data simply invoke the corresponding method without the Marker argument,
 * discarding any marker data passed as argument.
 * 
 * @author Ceki Gulcu
 */
public abstract class AbstractMarkerIgnoringBase implements Logger {

    /**
     * {@inheritDoc}
     */
    public boolean isTraceEnabled(Marker p_oMarker) {
        return isTraceEnabled();
    }

    /**
     * {@inheritDoc}
     */
    public void trace(Marker p_oMarker, String p_sFormat, Object p_oArg1, Object p_oArg2) {
        this.trace(p_sFormat, p_oArg1, p_oArg2);
    }

    /**
     * {@inheritDoc}
     */
    public void trace(Marker p_oMarker, String p_sFormat, Object p_oArg) {
    	this.trace(p_sFormat, p_oArg);
    }

    /**
     * {@inheritDoc}
     */
    public void trace(Marker p_oMarker, String p_sFormat, Object[] p_t_oArgArray) {
        trace(p_sFormat, p_t_oArgArray);
    }

    /**
     * {@inheritDoc}
     */
    public void trace(Marker p_oMarker, String p_sMsg, Throwable p_oThrowable) {
        trace(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    public void trace(Marker p_oMarker, String p_sMsg) {
        trace(p_sMsg);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDebugEnabled(Marker p_oMarker) {
        return isDebugEnabled();
    }

    /**
     * {@inheritDoc}
     */
    public void debug(Marker p_oMarker, String p_sMsg) {
        debug(p_sMsg);
    }

    /**
     * {@inheritDoc}
     */
    public void debug(Marker p_oMarker, String p_sFormat, Object p_Arg) {
        debug(p_sFormat, p_Arg);
    }

    /**
     * {@inheritDoc}
     */
    public void debug(Marker p_oMarker, String p_sFormat, Object p_Arg1, Object p_Arg2) {
        debug(p_sFormat, p_Arg1, p_Arg2);
    }

    /**
     * {@inheritDoc}
     */
    public void debug(Marker p_oMarker, String p_sFormat, Object[] p_t_oArgArray) {
        debug(p_sFormat, p_t_oArgArray);
    }

    /**
     * {@inheritDoc}
     */
    public void debug(Marker p_oMarker, String p_sMsg, Throwable p_oThrowable) {
        debug(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInfoEnabled(Marker p_oMarker) {
        return isInfoEnabled();
    }

    /**
     * {@inheritDoc}
     */
    public void info(Marker p_oMarker, String p_sMsg) {
        info(p_sMsg);
    }

    /**
     * {@inheritDoc}
     */
    public void info(Marker p_oMarker, String p_sFormat, Object p_Arg) {
        info(p_sFormat, p_Arg);
    }

    /**
     * {@inheritDoc}
     */
    public void info(Marker p_oMarker, String p_sFormat, Object p_Arg1, Object p_Arg2) {
        info(p_sFormat, p_Arg1, p_Arg2);
    }

    /**
     * {@inheritDoc}
     */
    public void info(Marker p_oMarker, String p_sFormat, Object[] p_t_oArgArray) {
        info(p_sFormat, p_t_oArgArray);
    }

    /**
     * {@inheritDoc}
     */
    public void info(Marker p_oMarker, String p_sMsg, Throwable p_oThrowable) {
        info(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWarnEnabled(Marker p_oMarker) {
        return isWarnEnabled();
    }

    /**
     * {@inheritDoc}
     */
    public void warn(Marker p_oMarker, String p_sMsg) {
        warn(p_sMsg);
    }

    /**
     * {@inheritDoc}
     */
    public void warn(Marker p_oMarker, String p_sFormat, Object p_Arg2) {
        warn(p_sFormat, p_Arg2);
    }

    /**
     * {@inheritDoc}
     */
    public void warn(Marker p_oMarker, String p_sFormat, Object p_Arg1, Object p_Arg2) {
        warn(p_sFormat, p_Arg1, p_Arg2);
    }

    /**
     * {@inheritDoc}
     */
    public void warn(Marker p_oMarker, String p_sFormat, Object[] p_t_oArgArray) {
        warn(p_sFormat, p_t_oArgArray);
    }

    /**
     * {@inheritDoc}
     */
    public void warn(Marker p_oMarker, String p_sMsg, Throwable p_oThrowable) {
        warn(p_sMsg, p_oThrowable);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isErrorEnabled(Marker p_oMarker) {
        return isErrorEnabled();
    }

    /**
     * {@inheritDoc}
     */
    public void error(Marker p_oMarker, String p_sMsg) {
        error(p_sMsg);
    }

    /**
     * {@inheritDoc}
     */
    public void error(Marker p_oMarker, String p_sFormat, Object p_Arg) {
        error(p_sFormat, p_Arg);
    }

    /**
     * {@inheritDoc}
     */
    public void error(Marker p_oMarker, String p_sFormat, Object p_Arg1, Object p_Arg2) {
        error(p_sFormat, p_Arg1, p_Arg2);
    }

    /**
     * {@inheritDoc}
     */
    public void error(Marker p_oMarker, String p_sFormat, Object[] p_t_oArgArray) {
        error(p_sFormat, p_t_oArgArray);
    }

    /**
     * {@inheritDoc}
     */
    public void error(Marker p_oMarker, String p_sMsg, Throwable p_oThrowable) {
        error(p_sMsg, p_oThrowable);
    }

}
