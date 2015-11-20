/*                                                                         
 * Copyright 2010-2013 the original author or authors.                     
 *                                                                         
 * Licensed under the Apache License, Version 2.0 (the "License");         
 * you may not use this file except in compliance with the License.        
 * You may obtain a copy of the License at                                 
 *                                                                         
 *      http://www.apache.org/licenses/LICENSE-2.0                         
 *                                                                         
 * Unless required by applicable law or agreed to in writing, software     
 * distributed under the License is distributed on an "AS IS" BASIS,       
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and     
 * limitations under the License.                                          
 */                                                                        
package gemlite.core.internal.index.def;

import gemlite.core.internal.index.antlr.CreateIndexLexer;
import gemlite.core.internal.index.antlr.CreateIndexParser;
import gemlite.core.internal.index.antlr.CreateIndexParser.create_index_clause_return;
import gemlite.core.internal.index.antlr.CreateIndexParser.create_rangeindex_clause_return;
import gemlite.core.internal.index.antlr.CreateIndexParser.produce_index_code_return;
import gemlite.core.internal.index.antlr.CreateIndexParser.produce_mappingRegionResolver_code_return;
import gemlite.core.internal.index.antlr.CreateIndexParser.produce_mappinglocalcache_code_return;
import gemlite.core.internal.index.antlr.CreateIndexParser.produce_mappingregion_code_return;
import gemlite.core.internal.index.antlr.CreateIndexParser.produce_rangeindex_code_return;
import gemlite.core.internal.index.antlr.CreateIndexParser.produce_test_index_code_return;
import gemlite.core.internal.index.dynamic.DynamicEngine;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.hotdeploy.GemliteClassLoader;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.support.system.WorkPathHelper;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.lang.StringUtils;

public class IndexDefLoader implements IDefLoader
{
	private static String INDEX_DEF_STG = System.getProperty("gemlite.core.index-def-stg", "IndexCode.stg");
	private static StringTemplateGroup templates;
	private String idxName;
	
	
	private static synchronized StringTemplateGroup getTemplates()
	{
		if(templates == null)
		{
			InputStream inputSTG = GemliteClassLoader.getInstance().getResourceAsStream(INDEX_DEF_STG);
			templates = new StringTemplateGroup(new InputStreamReader(inputSTG));
			if(LogUtil.getCoreLog().isDebugEnabled())
			{
				LogUtil.getCoreLog().debug("Index Def stg file: " + INDEX_DEF_STG);
			}
			
			try {
				inputSTG.close();
			} catch (IOException e) {
				LogUtil.getCoreLog().error("Close stg file Error.", e);
				throw new GemliteException("Index Def String Template Error! file name: " + INDEX_DEF_STG);
			}
		}
		
		if(templates == null)
		{
			throw new GemliteException("Index Def String Template Error! file name: " + INDEX_DEF_STG);
		}
		
		return templates;
	}
	
	private Map<String, Object> process(String def)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		{
			// PARSE INPUT AND BUILD TEMPLATES
	        ANTLRStringStream input = new ANTLRStringStream(def);
	        CreateIndexLexer lexer = new CreateIndexLexer(input); // create lexer
	        // create a buffer of tokens pulled from the lexer
	        CommonTokenStream tokens = new CommonTokenStream(lexer);
	        CreateIndexParser parser = new CreateIndexParser(tokens); // create parser
	        parser.setTemplateLib(getTemplates()); // where to look up templates
	        
	        if(def.toLowerCase().contains("hashindex"))
	        {
	        	produce_mappingregion_code_return mr;
	  	        try
	  	        {
	  				mr = parser.produce_mappingregion_code();
	  			}
	  	        catch (RecognitionException e)
	  	        {
	  				throw new GemliteException("Parser Index Code Error!", e);
	  			}
	  	        
	  	        StringTemplate mrOutput = (StringTemplate) mr.getTemplate();
	  	        String regionPackageName = mr.packageName;
	  	        String regionIndexName = mr.indexName+"Region";
		        idxName = mr.indexName;
	  	        map.put(regionPackageName+":"+regionIndexName, mrOutput);
	  	        
	  	        if(mrOutput == null)
	  	        {
	  	        	throw new GemliteException("Produce IndexRegion Code Error ==> StringTemplate is null!");
	  	        }
	  	        
	  	        parser.reset();
	  	        
	  	        produce_mappinglocalcache_code_return mlc;
	  	        try
	  	        {
	  				mlc = parser.produce_mappinglocalcache_code();
	  			}
	  	        catch (RecognitionException e)
	  	        {
	  				throw new GemliteException("Parser Index Code Error!", e);
	  			}
	  	        
	  	        StringTemplate mlcOutput = (StringTemplate) mlc.getTemplate();
	  	        String localCachePackageName = mlc.packageName;
	  	        String localCacheIndexName = mlc.indexName+"LocalCache";
	  	        map.put(localCachePackageName+":"+localCacheIndexName, mlcOutput);
	  	        
	  	        if(mlcOutput == null)
	  	        {
	  	        	throw new GemliteException("Produce IndexRegion LocalCache Code Error ==> StringTemplate is null!");
	  	        }
	  	        
	  	        parser.reset();
	  	        
	  	        produce_mappingRegionResolver_code_return mrr;
	  	        try
	  	        {
	  				mrr = parser.produce_mappingRegionResolver_code();
	  			}
	  	        catch (RecognitionException e)
	  	        {
	  				throw new GemliteException("Parser Index Code Error!", e);
	  			}
	  	        
	  	        StringTemplate mrrOutput = (StringTemplate) mrr.getTemplate();
	  	        String mrrPackageName = mrr.packageName;
	  	        String mrrIndexName = mrr.indexName+"Resolver";
	  	        map.put(mrrPackageName+":"+mrrIndexName, mrrOutput);
	  	        
	  	        if(mrrOutput == null)
	  	        {
	  	        	throw new GemliteException("Produce IndexRegion Resolver Code Error ==> StringTemplate is null!");
	  	        }
	        }
	        else if(def.toLowerCase().contains("rangeindex"))
	        {
	        	produce_rangeindex_code_return rr;
				try
				{
					rr = parser.produce_rangeindex_code();
				}
				catch (RecognitionException e) 
				{
					throw new GemliteException("Parser Index Code Error!", e);
				}
				
		        StringTemplate output = (StringTemplate)rr.getTemplate();
		        
		        String packageName = rr.packageName;
		        String indexName = rr.indexName;
		        idxName = indexName;
		        map.put(packageName+":"+indexName, output);
		        
		        if(output == null)
		        {
		        	throw new GemliteException("Produce Index Code Error ==> StringTemplate is null!");
		        }
		        
		        parser.reset();
		        
		        create_rangeindex_clause_return krr;
		        try
		        {
					krr = parser.create_rangeindex_clause();
				}
		        catch (RecognitionException e)
		        {
					throw new GemliteException("Parser Index Code Error!", e);
				}
		        
		        StringTemplate keyOutput = (StringTemplate) krr.getTemplate();
		        String keyPackageName = krr.packageName;
		        String indexKeyName = krr.indexName+"Key";
		        map.put(keyPackageName+":"+indexKeyName, keyOutput);
		        
		        if(keyOutput == null)
		        {
		        	throw new GemliteException("Produce Index KeyCode Error ==> StringTemplate is null!");
		        }
		    }
	        else
		    {
		        produce_index_code_return r;
				try
				{
					r = parser.produce_index_code();
				}
				catch (RecognitionException e) 
				{
					throw new GemliteException("Parser Index Code Error!", e);
				}
				
		        StringTemplate output = (StringTemplate)r.getTemplate();
		        
		        String packageName = r.packageName;
		        String indexName = r.indexName;
		        idxName = indexName;
		        map.put(packageName+":"+indexName, output);
		        
		        if(output == null)
		        {
		        	throw new GemliteException("Produce Index Code Error ==> StringTemplate is null!");
		        }
		        
		        parser.reset();
		        
		        create_index_clause_return kr;
		        try
		        {
					kr = parser.create_index_clause();
				}
		        catch (RecognitionException e)
		        {
					throw new GemliteException("Parser Index Code Error!", e);
				}
		        
		        StringTemplate keyOutput = (StringTemplate) kr.getTemplate();
		        String keyPackageName = kr.packageName;
		        String indexKeyName = kr.indexName+"Key";
		        map.put(keyPackageName+":"+indexKeyName, keyOutput);
		        
		        if(keyOutput == null)
		        {
		        	throw new GemliteException("Produce Index KeyCode Error ==> StringTemplate is null!");
		        }
		        
		        parser.reset();
		        
		        produce_test_index_code_return tr;
		        try
		        {
					tr = parser.produce_test_index_code();
				}
		        catch (RecognitionException e)
		        {
					throw new GemliteException("Parser Index Code Error!", e);
				}
		        
		        StringTemplate testOutput = (StringTemplate) tr.getTemplate();
		        String testPackageName = tr.packageName;
		        String testIndexName = tr.indexName+"Test";
		        map.put(testPackageName+":"+testIndexName, testOutput);
		        
		        if(testOutput == null)
		        {
		        	throw new GemliteException("Produce Index TestCode Error ==> StringTemplate is null!");
		        }
		    }
		}
		
		return map;
	}
	
//	private List<String> read(InputStream in)
//	{
//		List<String> defs = new ArrayList<String>();
//		BufferedReader br = new BufferedReader(new InputStreamReader(in));
//		String line = "";
//		try {
//			while((line =br.readLine()) != null && StringUtils.isNotEmpty(line.trim()))
//			{
//				defs.add(line.trim());
//				if(LogUtil.getCoreLog().isDebugEnabled())
//				{
//					LogUtil.getCoreLog().debug("Read Index Def, content: " + line.trim());
//				}
//			}
//		} catch (IOException e)
//		{
//			LogUtil.getCoreLog().error("Read Index Def Error!", e);
//		}
//		finally
//		{
//			try {
//				if( br != null)
//					br.close();
//			} catch (IOException e) {
//				LogUtil.getCoreLog().error("Read Index Def Error!", e);
//			}
//		}
//		
//		return defs;
//	}
	
	private Map<String, byte[]> write(Map<String, Object> outputs)
	{
		Map<String, byte[]> clazzMap = new HashMap<String, byte[]>();
		DynamicEngine engine = DynamicEngine.getInstance();
		
		Map<String, String> javaCodeFiles = new HashMap<String,String>();
		for(Iterator<String> it=outputs.keySet().iterator(); it.hasNext();)
		{
			String fullName = it.next();
			String[] arr = fullName.split(":");
			String packageName = arr[0];
			String className = arr[1];
			StringTemplate st = (StringTemplate) outputs.get(fullName);
			
			javaCodeFiles.put(packageName + "." + className, st.toString());
			
			String flag = System.getProperty("Gemlite.Generate.IndexCode");
			if(StringUtils.isNotEmpty(flag) && flag.equalsIgnoreCase("true"))
			{
				LogUtil.getCoreLog().debug("Dynamic Compile ==> " + packageName + "." + className);
				File file;
				try {
					WorkPathHelper.verifyPath( "code");
					file = new File(ServerConfigHelper.getConfig(ITEMS.GS_HOME)+"/code/"+className+".java");
					FileOutputStream fo = new  FileOutputStream(file);
					fo.write(st.toString().getBytes("utf-8"));
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		try {
			clazzMap = engine.compile(javaCodeFiles);
		} catch (Exception e) 
		{
			LogUtil.getCoreLog().error("Generate index class Exception!", e);
			throw new GemliteException("Generate index class Exception!", e);
		}
			
		if(clazzMap.get("error") != null)
		{
			LogUtil.getCoreLog().error("Generate index class Error! Reason: " + new String(clazzMap.get("error")));
			throw new GemliteException("Generate index class Error! Reason: " + new String(clazzMap.get("error")));
		}
		return clazzMap;
	}

	@Override
	public Map<String, byte[]> parseDefs(String def)
	{
		Map<String, byte[]> clazzMap = write(process(def));
		
		return clazzMap;
	}
	
	public String getName()
	{
		return idxName;
	}

}
