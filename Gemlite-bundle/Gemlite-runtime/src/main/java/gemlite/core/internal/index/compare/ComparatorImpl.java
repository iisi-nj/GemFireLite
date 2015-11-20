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
package gemlite.core.internal.index.compare;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

import org.apache.commons.lang.builder.CompareToBuilder;

public class ComparatorImpl<T> implements Comparator<T>
{
	@Override
	public int compare(T o1, T o2)
	{
		if (o1 == null)
			return -1;
		if (o2 == null)
			return -1;

		if (o1 instanceof Integer)
		{
			return compareInt((Integer) o1, (Integer) o2);
		} else if (o1 instanceof String)
		{
			return compareString((String) o1, (String) o2);
		} else if (o1 instanceof BigDecimal)
		{
			return compareBigDecimal((BigDecimal) o1, (BigDecimal) o2);
		} else if (o1 instanceof Double)
		{
			return compareDouble((Double) o1, (Double) o2);
		} else if (o1 instanceof Long)
		{
			return compareLong((Long) o1, (Long) o2);
		} else if (o1 instanceof Map)
		{
			return compareBigMap((Map<?, ?>) o1, (Map<?, ?>) o2);
		} else
		{
			return compareOther(o1, o2);
		}
	}

	private int compareInt(Integer o1, Integer o2)
	{
		return o1.compareTo(o2);

	}

	private int compareString(String o1, String o2)
	{
		return o1.compareTo(o2);
	}

	private int compareDouble(Double o1, Double o2)
	{
		return o1.compareTo(o2);
	}

	private int compareLong(Long o1, Long o2)
	{
		return o1.compareTo(o2);
	}

	private int compareBigDecimal(BigDecimal o1, BigDecimal o2)
	{
		return o1.compareTo(o2);

	}

	private int compareBigMap(Map<?, ?> o1, Map<?, ?> o2)
	{

		if (o1 == null || o1.values() == null)
			return -1;
		if (o2 == null || o2.values() == null)
			return 1;
		
		CompareToBuilder cb = new CompareToBuilder();
		cb.append(o1.toString(), o2.toString());
		return cb.toComparison();

	}

	private int compareOther(Object o1, Object o2)
	{
		CompareToBuilder cb = new CompareToBuilder();
		String value1 = o1.toString();
		String value2 = o2.toString();
		cb.append(value1, value2);
		return cb.toComparison();
	}

}
