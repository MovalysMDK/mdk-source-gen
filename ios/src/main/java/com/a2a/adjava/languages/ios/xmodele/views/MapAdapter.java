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
package com.a2a.adjava.languages.ios.xmodele.views;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
 
/**
 * Xml map adapter for ios
 */
public class MapAdapter extends XmlAdapter<MapAdapter.AdaptedMap, Map<String, String>> {

	/**
	 * Adapted map
	 */
	public static class AdaptedMap {
        
		/**
		 * list of entries of the AdapterMap
		 */
        private List<Entry> entry = new ArrayList<Entry>();

        /**
         * returns the list of entries
         * @return the list of entries
         */
		public List<Entry> getEntry() {
			return entry;
		}

		/**
         * Sets the list of entries
         * @params p_lEntry the list of entries
         */
		public void setEntry(List<Entry> p_lEntry) {
			this.entry = p_lEntry;
		}
  
    }
    
	/**
	 * Map entry
	 */
    public static class Entry {
        
    	/**
    	 * key of the entry
    	 */
    	@XmlAttribute
        public String key;
    	
    	/**
    	 * value of the entry
    	 */
		@XmlValue
		public String value;
    }
 
    @Override
    public Map<String, String> unmarshal(AdaptedMap p_oAdaptedMap) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        for(Entry oEntry : p_oAdaptedMap.entry) {
            map.put(oEntry.key, oEntry.value);
        }
        return map;
    }
 
    @Override
    public AdaptedMap marshal(Map<String, String> p_oMap) throws Exception {
        AdaptedMap r_oAdaptedMap = new AdaptedMap();

        if (p_oMap != null) {

	        for(Map.Entry<String, String> mapEntry : p_oMap.entrySet()) {
	            Entry oEntry = new Entry();
	            oEntry.key = mapEntry.getKey();
	            oEntry.value = mapEntry.getValue();
	            r_oAdaptedMap.entry.add(oEntry);
	        }
	         
        }
        
        return r_oAdaptedMap;
    }
 
}