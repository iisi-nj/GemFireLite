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

import gemlite.core.internal.support.hotdeploy.GemliteClassLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class DynamicEngine
{
    private static DynamicEngine instance = new DynamicEngine();
 
    public static DynamicEngine getInstance()
    {
        return instance;
    }
    

    private DynamicEngine()
    {
    }
        
    @SuppressWarnings("rawtypes")
	public Map<String, byte[]> compile(Map<String,String> javaCodeFiles) throws IllegalAccessException, InstantiationException, Exception
    {
		Map<String, byte[]> result = new HashMap<String, byte[]>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));
 
        List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
        
        for(Iterator<String> it=javaCodeFiles.keySet().iterator(); it.hasNext();)
        {
        	String fullClassName = it.next();
        	String javaCode = javaCodeFiles.get(fullClassName);
            jfiles.add(new JavaStringObject(fullClassName, javaCode));
        }
        List<String> options = new ArrayList<String>();
        options.add("-encoding");
        options.add("UTF-8");
        options.add("-classpath");
        options.add(GemliteClassLoader.getInstance().getFullClassPath());
 
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, jfiles);
        boolean success = task.call();
 
        if (success)
        {
        	
        	Iterable<JavaClassObject> it = fileManager.getJavaClassObjects();
    		for (JavaClassObject jco : it)
    		{
    			String name = jco.getName();
    			byte[] bytes = jco.getBytes();
    			result.put(name, bytes);
    		}
        }
        else
        {
            String error = "";
            for (Diagnostic diagnostic : diagnostics.getDiagnostics())
            {
                error = error + compilePrint(diagnostic);
            }
            
            result.put("error", error.getBytes());
        }
        
        fileManager.close();
        
        return result;
    }
 
    @SuppressWarnings("rawtypes")
	private String compilePrint(Diagnostic diagnostic)
    {
        StringBuffer res = new StringBuffer();
        res.append("Code:[" + diagnostic.getCode() + "]\n");
        res.append("Kind:[" + diagnostic.getKind() + "]\n");
        res.append("Position:[" + diagnostic.getPosition() + "]\n");
        res.append("Start Position:[" + diagnostic.getStartPosition() + "]\n");
        res.append("End Position:[" + diagnostic.getEndPosition() + "]\n");
        res.append("Source:[" + diagnostic.getSource() + "]\n");
        res.append("Message:[" + diagnostic.getMessage(null) + "]\n");
        res.append("LineNumber:[" + diagnostic.getLineNumber() + "]\n");
        res.append("ColumnNumber:[" + diagnostic.getColumnNumber() + "]\n");
        return res.toString();
    }
}

