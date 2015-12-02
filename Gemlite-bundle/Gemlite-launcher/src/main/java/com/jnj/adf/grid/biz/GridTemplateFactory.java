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
package com.jnj.adf.grid.biz;

/**
 * @author dyang39
 *
 */
public class GridTemplateFactory  {
	/***
	 * there 3 types GridTemplate
	 * client-side
	 * server-side
	 * @return
	 */
	public final static GridTemplate getGridTemplateInstance()
	{
		return new GridTemplate();
	}
	
	/***
	 * when federal invoked
	 * 1.invoke in local grid
	 * 2.visit federal system to see the tree under local grid 
	 * 3.visit the other grids
	 * 4.merge the result
	 * @return
	 */
	public final static FederalTemplate getFederalTemplateInstance()
	{
		return new FederalTemplate(null);
	}

}
