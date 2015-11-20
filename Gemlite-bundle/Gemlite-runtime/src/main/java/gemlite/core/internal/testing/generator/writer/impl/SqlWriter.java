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

import org.apache.commons.io.output.FileWriterWithEncoding;

public class SqlWriter implements OutputWriter
{
	private static String TEST_FILE_ENCODING = System.getProperty("gemlite.test.test-file-encoding", "GB18030");
	private FileWriterWithEncoding fw;
	private String fullFilePath;
	
	public SqlWriter(String tableName)
	{
		String nodeName = ServerConfigHelper.getConfig(ITEMS.NODE_NAME);
		if (nodeName==null || nodeName.equals(""))
			nodeName="basic";
		String path = "test/" + nodeName;
		WorkPathHelper.verifyPath(path);
		File f = new File(ServerConfigHelper.getConfig(ITEMS.GS_HOME) + "/"
				+ path + "/" + tableName + ".sql");
		try
		{
			fw = new FileWriterWithEncoding(f, TEST_FILE_ENCODING);
			fullFilePath = ServerConfigHelper.getConfig(ITEMS.GS_HOME) + "/"
					+ path + "/" + tableName + ".sql";
		} catch (IOException e)
		{
			e.printStackTrace();
			fw = null;
			fullFilePath = null;
		}
	}
	
	@Override
	public void write(Object content) 
	{
		try {
			if(fw != null)
				fw.write(content.toString()+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() 
	{
		if(fw != null)
			try {
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
	
}
