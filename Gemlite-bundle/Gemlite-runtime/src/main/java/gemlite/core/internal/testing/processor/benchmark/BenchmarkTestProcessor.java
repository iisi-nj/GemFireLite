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
package gemlite.core.internal.testing.processor.benchmark;

import gemlite.core.internal.mq.parser.AntlrTableParser;
import gemlite.core.internal.mq.parser.ITableParser;
import gemlite.core.internal.mq.processor.IMapperDao;
import gemlite.core.internal.mq.processor.SimpleMapperDao;
import gemlite.core.internal.mq.sender.IDomainSender;
import gemlite.core.internal.mq.sender.LocalDomainSender;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.testing.processor.ITestProcessor;
import gemlite.core.util.LogUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class BenchmarkTestProcessor extends ITestProcessor
{
	private String[] param;
	private BufferedReader br;
	
	public BenchmarkTestProcessor(String[]  args)
	{
		input = new FileInputGenerator();
		ITableParser parser = new AntlrTableParser();
		IMapperDao dao = new SimpleMapperDao();
		IDomainSender sender = new LocalDomainSender();
		processor = new BenchmarkMessageProcessor(parser, dao, sender);
		param = args;
	}
	
	@Override
	public void open()
	{
		CmdLineParser parser = new CmdLineParser(input);
		try
		{
			parser.parseArgument(param);
			Map<?,?> inputParam = input.getInput();
			String fileName = (String)inputParam.get("fileName");
			String fileEncoding = (String)inputParam.get("fileEncoding");
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), fileEncoding));
		} catch (CmdLineException | UnsupportedEncodingException | FileNotFoundException e) 
		{
			LogUtil.getTestLog().error("Parse file arguments error.", e);
			throw new GemliteException("Parse file arguments error.", e);
		}
	}
	
	@Override
	public void process()
	{
		open();
		try
		{
			String line = null;
			while ((line = br.readLine()) != null)
			{
				if (!line.trim().equals(""))
					processor.parserOneMessage(line);
			}
		} catch (IOException e)
		{
			LogUtil.getTestLog().error("", e);
			throw new GemliteException("", e);
		}
		close();
		
	}

	@Override
	public void close()
	{
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
