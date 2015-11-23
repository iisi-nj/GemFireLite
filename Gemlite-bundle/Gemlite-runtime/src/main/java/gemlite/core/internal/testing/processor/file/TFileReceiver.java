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
package gemlite.core.internal.testing.processor.file;

import gemlite.core.internal.mq.MqReceiveException;
import gemlite.core.internal.mq.ShellParameter;
import gemlite.core.internal.mq.processor.IMessageProcessor;
import gemlite.core.internal.mq.receiver.HAMqReceiver;
import gemlite.core.internal.mq.receiver.file.FileShellParameter;
import gemlite.core.util.LogUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

/**
 * @author ynd
 * 
 */
public class TFileReceiver extends HAMqReceiver
{
	private FileShellParameter param;

	private LineNumberReader lr;
	
	private final static int packageCount = 20;
	private String line;

	public TFileReceiver(FileShellParameter param, IMessageProcessor processor)
	{
		super(processor);
		this.param = param;
	}

	@Override
	public void close()
	{
		LogUtil.getMqSyncLog().info(" MQ Client closing ... ");
		doClose();
		LogUtil.getMqSyncLog().info(" MQ Client closed. ");
	}

	@Override
	public void connect()
	{
		try
		{
			lr = new LineNumberReader(new InputStreamReader(
					new FileInputStream(param.fileName), param.fileEncoding));
			//line = readLine();
		} catch (UnsupportedEncodingException | FileNotFoundException e)
		{
			LogUtil.getMqSyncLog().error(param.fileName, e);
			throw new MqReceiveException(true);
		}
	}
	
	@Override
	public void start()
	{
		open();
		try
		{
			line = null;
			int currentLength = 0;
			try
			{
				StringBuffer msgBuffer = new StringBuffer();
				while ((line = lr.readLine()) != null)
				{
					if (!StringUtils.isEmpty(line))
					{
						currentLength++;
						msgBuffer.append(line).append("\n");
					}
					if (currentLength == packageCount)
					{
						dealReceiveMessages(msgBuffer.toString());
						currentLength = 0;
						msgBuffer = new StringBuffer();
					}
				}
				if (currentLength > 0)
					dealReceiveMessages(msgBuffer.toString());

			} catch (IOException e)
			{
				e.printStackTrace();
				LogUtil.getMqSyncLog().error("File read error.",e);
			}
		} finally
		{
			close();
		}
	}
	
	/***
	 * check if batchSize reached
	 * 
	 * @throws IOException
	 */
	protected void doCommit() throws MqReceiveException
	{
	}

	@Override
	public void reConnect()
	{

	}
	
	@Override
	public ShellParameter getParam()
	{
		return param;
	}
	
	private void dealReceiveMessages(String msg)
	{
		if (!StringUtils.isBlank(msg))
		{
			// 处理消息
			processor.parserOneMessage(msg);
			processor.remoteProcess();
			doCommit();
		}
	}

	@Override
	protected void doClose()
	{
		if (lr != null)
		{
			try
			{
				lr.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	@Override
	protected String readOneMessage()
	{
		return "";
	}

}
