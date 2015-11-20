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
package gemlite.core.internal.testing.generator.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class RandomUtil
{
	private static Random randGen = new Random();
	private static final char[] numberAndLetter = "01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static final int stringLength = 10;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String getValue(String type)
	{
		if(type.equalsIgnoreCase("String"))
			return "'"+randomString(stringLength)+"'";
		else if(type.equalsIgnoreCase("Long"))
			return String.valueOf(randomLong());
		else if(type.equalsIgnoreCase("Integer") || type.equalsIgnoreCase("int"))
			return String.valueOf(randomInt());
		else if(type.equalsIgnoreCase("Double"))
			return String.valueOf(randomDouble());
		else if(type.equalsIgnoreCase("Date"))
			return "'"+sdf.format(randomDate())+"'";
		
		return null;
	}
	
	private static Date randomDate()
	{
		try {
			Date start = sdf.parse("2000-01-01 00:00:00");
			Date end = sdf.parse("2015-12-31 00:00:00");
			long dif = end.getTime() - start.getTime();
			long date = (long)(Math.random()*dif) + start.getTime();
			
			return new Date(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static int randomInt()
	{
		return randGen.nextInt(Integer.MAX_VALUE);
	}
	
	private static long randomLong()
	{
		return randGen.nextLong();
	}
	
	private static double randomDouble()
	{
		double temp = randGen.nextDouble();
		BigDecimal decimal = new BigDecimal(temp);
		double value = decimal.setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		return value;
	}
	
	private static String randomString(int length)
	{
		if(length < 1)
			return null;
		
		char[] randBuffer = new char[length];
		for(int i=0; i<randBuffer.length; i++)
		{
			randBuffer[i] = numberAndLetter[randGen.nextInt(numberAndLetter.length)];
		}
		
		return new String(randBuffer);
	}
	
	public static void main(String[] args)
	{
		String out = RandomUtil.getValue("Date");
		System.out.println(out);
		
	}
}
