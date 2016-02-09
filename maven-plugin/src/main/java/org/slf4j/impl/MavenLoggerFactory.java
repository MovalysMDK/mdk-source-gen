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
import org.slf4j.Logger;

/**
 * Simple {@link MavenLogAdapter} adapter to {@link ILoggerFactory}.
 * 
 * @author Fran�ois Lecomte
 */
public class MavenLoggerFactory implements ILoggerFactory {

    /**
     * Singleton Maven {@link Logger} instance
     */
    private MavenLogAdapter mvnLog;

    /**
     * Constructor
     * 
     * @param log Maven {@link Logger} instance
     */
    public MavenLoggerFactory(Log log) {
        super();
        this.mvnLog = new MavenLogAdapter(log);
    }

    /**
     * {@inheritDoc}
     */
    public Logger getLogger(String name) {
        return mvnLog;
    }

}
