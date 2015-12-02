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
package gemlite.core.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

/**
 * @author dyang39
 *
 */
public class Testar4 {

	@Option(name = "-r", usage = "recursively run something")
	private boolean recursive;

	@Option(name = "-o", usage = "output to this file", metaVar = "OUTPUT")
	private File out = new File(".");

	@Option(name = "-str") // no usage
	private String str = "(default value)";

	@Option(name = "-n", usage = "repeat <n> times\nusage can have new lines in it and also it can be verrrrrrrrrrrrrrrrrry long")
	private int num = -1;

	// using 'handler=...' allows you to specify a custom OptionHandler
	// implementation class. This allows you to bind a standard Java type
	// with a non-standard option syntax
	@Option(name = "-custom", handler = BooleanOptionHandler.class, usage = "boolean value for checking the custom handler")
	private boolean data;

	// receives other command line parameters than options
	@Argument
	private List<String> arguments = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		new Testar4().doMain(args);
	}

	public void doMain(String[] args) throws IOException {
		CmdLineParser parser = new CmdLineParser(this);

		// if you have a wider console, you could increase the value;
		// here 80 is also the default
		parser.setUsageWidth(80);

		try {
			// parse the arguments.
			parser.parseArgument(args);

			// you can parse additional arguments if you want.
			// parser.parseArgument("more","args");

			// after parsing arguments, you should check
			// if enough arguments are given.
			if (arguments.isEmpty())
				throw new CmdLineException("No argument is given");

		} catch (CmdLineException e) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java SampleMain [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();

			// print option sample. This is useful some time
			System.err.println(" Example: java SampleMain" + parser.printExample(ExampleMode.ALL));

			return;
		}

		// this will redirect the output to the specified output
		System.out.println(out);

		if (recursive)
			System.out.println("-r flag is set");

		if (data)
			System.out.println("-custom flag is set");

		System.out.println("-str was " + str);

		if (num >= 0)
			System.out.println("-n was " + num);

		// access non-option arguments
		System.out.println("other arguments are:");
		for (String s : arguments)
			System.out.println(s);
	}
}