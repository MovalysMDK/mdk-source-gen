/**
 * 
 */
package com.a2a.adjava.generator.core.jsonmerge.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Process the merge of JSON file
 * @author mlaffargue
 *
 */
public class MergeProcessor {
	
	/**
	 * This function should be used to merge simple JSON file.
	 * Simple means a JSON file containing an object with only String values.
	 * e.g.:
	 * {
	 *   "aboutscreen__title": "About screen",
     *   "dangersscreen__title": "Dangers screen"
	 * }
	 */
	public final void processSimpleMerge(Map<String, String> userModifiedJson, Map<String, String> oldGeneratedJson, 
			Map<String, String> newGeneratedJson, Map<String, String> newMergedJson) {
		List<String> keysRemovedFromModel = getKeysRemovedFromModel(oldGeneratedJson, newGeneratedJson);
		List<String> keysAddedFromModel = getKeysAddedFromModel(oldGeneratedJson, newGeneratedJson);

		
		// Copy user JSON in merged JSON 
		// 1 : removing keys deleted from model
		for (Map.Entry<String, String> entry : userModifiedJson.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (! keysRemovedFromModel.contains(key)) {
            	newMergedJson.put(key, value);
            }
        }
		
		// 2 : And adding new keys
		for (String keyToAdd : keysAddedFromModel) {
			newMergedJson.put(keyToAdd, newGeneratedJson.get(keyToAdd));
		}
	}

	/**
	 * 
	 * @param oldGeneratedJson
	 * @param newGeneratedJson
	 * @return
	 */
	private List<String> getKeysAddedFromModel(Map<String, String> oldGeneratedJson, Map<String, String> newGeneratedJson) {
		List<String> addedKeys = new ArrayList<String>(newGeneratedJson.keySet());
		Set<String> oldKeys = oldGeneratedJson.keySet();
		
		// We remove old keys from new keys to keep only new ones
		for (Iterator<String> it = addedKeys.iterator();it.hasNext();) {
			String key = it.next();
			if (oldKeys.contains(key)) {
				it.remove();
			}
		}
		
		return addedKeys;
	}

	/**
	 * 
	 * @param oldGeneratedJson
	 * @param newGeneratedJson
	 * @return
	 */
	private List<String> getKeysRemovedFromModel(Map<String, String> oldGeneratedJson, Map<String, String> newGeneratedJson) {
		List<String> removedKeys = new ArrayList<String>(oldGeneratedJson.keySet());
		Set<String> newKeys = newGeneratedJson.keySet();
		
		// We remove new keys from old keys to keep only those that disappeared
		for (Iterator<String> it = removedKeys.iterator();it.hasNext();) {
			String key = it.next();
			if (newKeys.contains(key)) {
				it.remove();
			}
		}
		
		return removedKeys;
	}
}
