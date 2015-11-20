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
package gemlite.core.internal.index.dynamic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ClassFileManager extends ForwardingJavaFileManager
{
    public Iterable<JavaClassObject> getJavaClassObjects()
    {
        return jcoList;
    }
 
    private List<JavaClassObject> jcoList;

	public ClassFileManager(StandardJavaFileManager standardManager)
	{
        super(standardManager);
        jcoList = new ArrayList<JavaClassObject>();
    }
	
    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
        String className, JavaFileObject.Kind kind, FileObject sibling)
            throws IOException
    {
    	JavaClassObject jclassObject = new JavaClassObject(className, kind);
    	jcoList.add(jclassObject);
        return jclassObject;
    }
    
}
