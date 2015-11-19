/**
 * 
 */
package com.a2a.adjava.generator.core.jsonmerge;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.jsonmerge.process.MergeProcessor;
import com.a2a.adjava.xmodele.IDomain;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 	
 * This class should be used to merge simple JSON file.
 * Simple means a JSON file containing an object with only String values.
 * e.g.:
 * {
 *   "aboutscreen__title": "About screen",
 *   "dangersscreen__title": "Dangers screen"
 * }
 * @author mlaffargue
 *
 * @param <D> type of Domain
 */
public abstract class AbstractSimpleJsonMergeGenerator<D extends IDomain<?, ?>> extends AbstractJsonMergeGenerator<D> {
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(AbstractSimpleJsonMergeGenerator.class);

	@Override
	protected void processJSONMerging(Path p_oOldModifiedXmlFile, Path p_oOldGeneratedXmlFile, Path p_oNewGeneratedXmlFile,
			Path p_oNewMergedFile) throws AdjavaException {
		Writer writer = null;
		MergeProcessor mergeProcessor = new MergeProcessor();
		Gson gson = new Gson();
	    Type mapType = new TypeToken<TreeMap<String, String>>() {}.getType();
	    TreeMap<String, String> userModifiedJson, oldGeneratedJson, newGeneratedJson, newMergedJson;
	    try {
	    	// Read JSON files
	    	userModifiedJson = gson.fromJson(new FileReader(p_oOldModifiedXmlFile.toFile()), mapType);
	    	oldGeneratedJson = gson.fromJson(new FileReader(p_oOldGeneratedXmlFile.toFile()), mapType);
	    	newGeneratedJson = gson.fromJson(new FileReader(p_oNewGeneratedXmlFile.toFile()), mapType);
	    	newMergedJson = gson.fromJson("{}", mapType);

			mergeProcessor.processSimpleMerge(userModifiedJson, oldGeneratedJson, newGeneratedJson, newMergedJson);
			
			// Save the file
			writer = new FileWriter(p_oNewMergedFile.toFile());
			gson.toJson(newMergedJson, mapType, writer);
			writer.close();
		} catch (Exception e) {
			throw new AdjavaException("[AbstractSimpleJsonMergeGenerator#doJsonMergeGeneration] A problem occured while merging file: " + p_oNewMergedFile.toString(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					log.error("Couldn't close writer after merging JSON file :" + p_oNewMergedFile.toString(),e);
				}
			}
		}
		
	}
}
