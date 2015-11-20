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
package gemlite.core.internal.testing.client;

import gemlite.core.api.SimpleClient;
import gemlite.core.internal.testing.comparator.impl.MapComparator;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * K��w{
 * 
 * (�Regionpn�BeachMark localpn��
 *  MSqlGenerateTest�w{(
 * 
 *
 */
public class RegionCompare
{
	@Option(name = "-name",usage="")
	private String name;

	private MapComparator mapCompare;

	public static void main(String[] args) throws Exception
	{
		SimpleClient.connect();
		RegionCompare compare = new RegionCompare();
		CmdLineParser parser = new CmdLineParser(compare);
		parser.parseArgument(args);
		compare.run();

		SimpleClient.disconnect();
	}

	private void run()
	{
		if (name == null)
			name = new String();
		if (name.equals(""))
		{
			System.out.println(" e��p[-name]:zpn��1%.");
			return;
		}
			
		String[] regionArray = name.split(",");
		for (String regionName : regionArray)
		{
			if (!regionName.trim().equals(""))
			{
				mapCompare = new MapComparator(regionName);
				String resullt = mapCompare.compareData();
				System.out.println(resullt);
			}
		}
	}
}
