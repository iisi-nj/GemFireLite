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
package gemlite.core.internal.testing.generator.writer.impl;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.support.system.WorkPathHelper;
import gemlite.core.internal.testing.generator.writer.OutputWriter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.RandomStringUtils;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.internal.lang.StringUtils;

public class LogInfoWriter implements OutputWriter
{
	private static String TEST_FILE_ENCODING = "GB18030";
	private FileWriterWithEncoding fw;
	private String fullFilePath;
	private String lineSep;

	public LogInfoWriter(String filePath, String fileName, String lineSep)
	{
		this(filePath, fileName, lineSep, true);
	}

	/**
	 * support save data to absolute path or relative path.
	 * 
	 * @param filePath
	 * @param fileName
	 * @param lineSep
	 * @param deleteOldFile
	 * @param absolutePath
	 */
	public LogInfoWriter(String filePath, String fileName, String lineSep,
			boolean deleteOldFile, boolean absolutePath)
	{
		if (StringUtils.isBlank(filePath))
			filePath = RandomStringUtils.randomAlphanumeric(4) + "/";
		else
		{
			if (!filePath.endsWith("/"))
				filePath = filePath + "/";
			if (!filePath.startsWith("/"))
				filePath = "/" + filePath;
		}
		if (!absolutePath) // relative path
		{
			if (StringUtils.isBlank(fileName))
				fileName = RandomStringUtils.randomAlphanumeric(6) + ".txt";

			String nodeName = ServerConfigHelper.getConfig(ITEMS.NODE_NAME);
			if (nodeName == null || nodeName.equals(""))
				nodeName = "basic";

			String path = "/server/" + nodeName + filePath;
			String gs_work = ServerConfigHelper.getConfig(ITEMS.GS_WORK);
			WorkPathHelper.verifyPath(gs_work, path, false);
			fullFilePath = gs_work + path + fileName;
		} else
		{// absolute path
			File dirs = new File(filePath);
			if (!dirs.exists())
			{
				dirs.mkdirs();
			}
			fullFilePath = filePath + fileName;

		}
		File f = new File(fullFilePath);

		try
		{ // delete old file
			if (deleteOldFile)
			{
				if (f != null && f.isFile() && f.exists())
					FileUtils.deleteQuietly(f);
			}
		} catch (Exception e1)
		{
		}

		try
		{
			fw = new FileWriterWithEncoding(f, TEST_FILE_ENCODING);
		} catch (IOException e)
		{
			e.printStackTrace();
			fw = null;
			fullFilePath = null;
		}

		this.lineSep = (lineSep != null ? lineSep : "\n");
	}

	/**
	 * support delete old file before save it.
	 * 
	 * @param filePath
	 * @param fileName
	 * @param lineSep
	 * @param deleteOldFile
	 */
	public LogInfoWriter(String filePath, String fileName, String lineSep,
			boolean deleteOldFile)
	{
		if (StringUtils.isBlank(filePath))
			filePath = RandomStringUtils.randomAlphanumeric(4) + "/";
		else
		{
			if (!filePath.endsWith("/"))
				filePath = filePath + "/";
			if (!filePath.startsWith("/"))
				filePath = "/" + filePath;
		}
		if (StringUtils.isBlank(fileName))
			fileName = RandomStringUtils.randomAlphanumeric(6) + ".txt";

		String nodeName = ServerConfigHelper.getConfig(ITEMS.NODE_NAME);
		if (nodeName == null || nodeName.equals(""))
			nodeName = "basic";

		String path = "/server/" + nodeName + filePath;
		String gs_work = ServerConfigHelper.getConfig(ITEMS.GS_WORK);
		WorkPathHelper.verifyPath(gs_work, path, false);
		fullFilePath = gs_work + path + fileName;
		File f = new File(fullFilePath);

		try
		{ // delete old file
			if (deleteOldFile)
			{
				if (f != null && f.isFile() && f.exists())
					f.delete();
			}
		} catch (Exception e1)
		{
		}

		try
		{
			fw = new FileWriterWithEncoding(f, TEST_FILE_ENCODING);
		} catch (IOException e)
		{
			e.printStackTrace();
			fw = null;
			fullFilePath = null;
		}

		this.lineSep = (lineSep != null ? lineSep : "\n");
	}

	@Override
	public void write(Object content)
	{
		try
		{
			if (fw != null)
				fw.write(content.toString() + lineSep);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void close()
	{
		if (fw != null)
			try
			{
				fw.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
	}

	@Override
	public String getFile()
	{
		return fullFilePath;
	}

	/**
	 * write region data to file
	 * 
	 * @param region
	 */
	public static void writeRegionDataToFile(Region<?, ?> region)
	{
		if (region == null)
			return;

		String filePath = "/regiondata/";
		String regionName = region.getName();
		String fileName = regionName + ".txt";
		LogInfoWriter writer = new LogInfoWriter(filePath, fileName, "\n");
		Iterator<?> iters = region.values().iterator();
		writer.write("Region [" + regionName + "]  size >>"
				+ region.values().size());
		writer.write("");
		while (iters.hasNext())
		{
			Object obj = iters.next();
			writer.write(obj.toString());
		}
		writer.close();
	}

	/**
	 * write map data to file
	 * 
	 * @param fileName
	 * @param dataMap
	 */
	public static void writeMapDataToFile(String fileName, Map<?, ?> dataMap)
	{
		if (StringUtils.isBlank(fileName) || dataMap == null)
			return;

		String filePath = "/mapdata/";
		String newFileName = fileName + ".txt";
		LogInfoWriter writer = new LogInfoWriter(filePath, newFileName, "\n");
		writer.write("Map [" + fileName + "]  size >>"
				+ dataMap.entrySet().size());
		writer.write("");
		for (Entry<?, ?> entry : dataMap.entrySet())
		{
			Object key = entry.getKey();
			Object value = entry.getValue();
			writer.write("key >> " + key.toString() + " ,value >> "
					+ value.toString());
		}
		writer.close();
	}

	public static void writeIndexDataToFile(String indexName,
			Iterator<?> iters, boolean isKey)
	{
		if (StringUtils.isBlank(indexName) || iters == null)
			return;
		String filePath = "/indexdata/";
		String fileName = "";
		if (isKey)
			fileName = indexName + "_key.txt";
		else
			fileName = indexName + "_value.txt";

		LogInfoWriter writer = new LogInfoWriter(filePath, fileName, "\n");
		while (iters.hasNext())
		{
			if (isKey)
			{
				Object obj = iters.next();
				writer.write(obj.toString());
			} else
			{
				Set<?> dataSet = (Set<?>) iters.next();
				for (Object obj : dataSet)
				{
					if (obj != null)
					{
						writer.write(obj.toString());
					}
				}

			}
		}
		writer.close();
	}

}
