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
package gemlite.shell.codegen.util;

import static java.lang.Boolean.TRUE;
import static gemlite.shell.codegen.tools.StringUtils.defaultIfNull;
import static gemlite.shell.codegen.tools.StringUtils.defaultString;
import static gemlite.shell.codegen.tools.StringUtils.isBlank;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Driver;
import java.util.List;
import java.util.Properties;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.SchemaFactory;
import gemlite.shell.codegen.Constants;
import gemlite.shell.codegen.tools.StringUtils;
import gemlite.shell.codegen.tools.jdbc.JDBCUtils;
import gemlite.shell.codegen.util.Generator;
import gemlite.shell.codegen.util.jaxb.Configuration;
import gemlite.shell.codegen.util.jaxb.Generate;
import gemlite.shell.codegen.util.jaxb.Jdbc;
import gemlite.shell.codegen.util.jaxb.Property;
import gemlite.shell.codegen.util.jaxb.Schema;
import gemlite.shell.codegen.util.jaxb.Strategy;
import gemlite.shell.codegen.util.jaxb.Target;
import gemlite.core.util.LogUtil;

public class GenerationTool
{
  private ClassLoader loader;
  private Connection connection;
  private boolean close;
  
  public void setClassLoader(ClassLoader loader)
  {
    this.loader = loader;
  }
  
  public void setConnection(Connection connection)
  {
    this.connection = connection;
  }
  
  public static void main(String[] args) throws Exception
  {
    if (args.length < 1)
    {
      error();
    }
    InputStream in = GenerationTool.class.getResourceAsStream(args[0]);
    // Retry loading the file, if it wasn't found. This may be helpful
    // to some users who were unaware that this file is loaded from the
    // classpath
    if (in == null && !args[0].startsWith("/"))
      in = GenerationTool.class.getResourceAsStream("/" + args[0]);
    // Also check the local file system for configuration files
    if (in == null && new File(args[0]).exists())
      in = new FileInputStream(new File(args[0]));
    if (in == null)
    {
      LogUtil.getCoreLog().error(
          "Cannot find " + args[0] + " on classpath, or in directory " + new File(".").getCanonicalPath());
      LogUtil.getCoreLog().error("-----------");
      LogUtil.getCoreLog().error("Please be sure it is located");
      LogUtil.getCoreLog().error("  - on the classpath and qualified as a classpath location.");
      LogUtil.getCoreLog().error("  - in the local directory or at a global path in the file system.");
      error();
    }
    LogUtil.getCoreLog().info("Initialising properties", args[0]);
    try
    {
      generate(load(in));
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("Cannot read " + args[0] + ". Error : " + e.getMessage());
      e.printStackTrace();
      error();
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }
    }
  }
  
  public static void generate(String xml) throws Exception
  {
    new GenerationTool().run(load(new ByteArrayInputStream(xml.getBytes("UTF-8"))));
  }
  
  public static void generate(Configuration configuration) throws Exception
  {
    new GenerationTool().run(configuration);
  }
  
  @SuppressWarnings("unchecked")
  public void run(Configuration configuration) throws Exception
  {
    Jdbc jdbc = configuration.getJdbc();
    gemlite.shell.codegen.util.jaxb.Generator g = configuration.getGenerator();
    errorIfNull(g, "The <generator/> tag is mandatory.");
    // Some default values for optional elements to avoid NPE's
    if (g.getStrategy() == null)
      g.setStrategy(new Strategy());
    if (g.getTarget() == null)
      g.setTarget(new Target());
    try
    {
      // Initialise connection
      // ---------------------
      if (connection == null && jdbc != null)
      {
        Class<? extends Driver> driver = (Class<? extends Driver>) loadClass(driverClass(jdbc));
        Properties properties = properties(jdbc.getProperties());
        if (!properties.containsKey("user"))
          properties.put("user", defaultString(jdbc.getUser()));
        if (!properties.containsKey("password"))
          properties.put("password", defaultString(jdbc.getPassword()));
        connection = driver.newInstance().connect(defaultString(jdbc.getUrl()), properties);
        close = true;
      }
      else
      {
        jdbc = defaultIfNull(jdbc, new Jdbc());
      }
      // Initialise generator
      // --------------------
      Class<Generator> generatorClass = (Class<Generator>) (!isBlank(g.getName()) ? loadClass(trim(g.getName()))
          : JavaGenerator.class);
      Generator generator = generatorClass.newInstance();
      GeneratorStrategy strategy;
      // Matchers matchers = g.getStrategy().getMatchers();
      Class<GeneratorStrategy> strategyClass = (Class<GeneratorStrategy>) (!isBlank(g.getStrategy().getName()) ? loadClass(trim(g
          .getStrategy().getName())) : DefaultGeneratorStrategy.class);
      strategy = strategyClass.newInstance();
      generator.setStrategy(strategy);
      gemlite.shell.codegen.util.jaxb.Database d = defaultIfNull(g.getDatabase(), new gemlite.shell.codegen.util.jaxb.Database());
      String databaseName = trim(d.getName());
      Class<? extends Database> databaseClass = !isBlank(databaseName) ? (Class<? extends Database>) loadClass(databaseName)
          : null;
      Database database = databaseClass.newInstance();
      database.setProperties(properties(d.getProperties()));
      List<Schema> schemata = d.getSchemata();
      // For convenience and backwards-compatibility, the schema
      // configuration can be set also directly
      // in the <database/> element
      if (schemata.isEmpty())
      {
        Schema schema = new Schema();
        schema.setInputSchema(trim(d.getInputSchema()));
        schema.setOutputSchema(trim(d.getOutputSchema()));
        schema.setOutputSchemaToDefault(d.isOutputSchemaToDefault());
        schemata.add(schema);
      }
      else
      {
        if (!StringUtils.isBlank(d.getInputSchema()))
        {
          LogUtil
              .getCoreLog()
              .warn(
                  "WARNING: Cannot combine configuration properties /configuration/generator/database/inputSchema and /configuration/generator/database/schemata");
        }
        if (!StringUtils.isBlank(d.getOutputSchema()))
        {
          LogUtil
              .getCoreLog()
              .warn(
                  "WARNING: Cannot combine configuration properties /configuration/generator/database/outputSchema and /configuration/generator/database/schemata");
        }
      }
      for (Schema schema : schemata)
      {
        if (StringUtils.isBlank(schema.getInputSchema()))
        {
          if (!StringUtils.isBlank(jdbc.getSchema()))
          {
            LogUtil
                .getCoreLog()
                .warn(
                    "WARNING: The configuration property jdbc.Schema is deprecated and will be removed in the future. Use /configuration/generator/database/inputSchema instead");
          }
          schema.setInputSchema(trim(jdbc.getSchema()));
        }
        // [#3018] Prior to <outputSchemaToDefault/>, empty
        // <outputSchema/> elements meant that
        // the outputSchema should be the default schema. This is a bit
        // too clever, and doesn't
        // work when Maven parses the XML configurations.
        if ("".equals(schema.getOutputSchema()))
        {
          LogUtil.getCoreLog().warn("");
        }
        // [#3018] If users want the output schema to be "" then, ignore
        // the actual <outputSchema/> configuration
        if (TRUE.equals(schema.isOutputSchemaToDefault()))
        {
          schema.setOutputSchema("");
        }
        else if (schema.getOutputSchema() == null)
        {
          schema.setOutputSchema(trim(schema.getInputSchema()));
        }
      }
      if (schemata.size() == 1)
      {
        if (StringUtils.isBlank(schemata.get(0).getInputSchema()))
        {
          LogUtil.getCoreLog().info("No <inputSchema/> was provided. Generating ALL available schemata instead!");
        }
      }
      database.setConnection(connection);
      database.setConfiguredSchemata(schemata);
      database.setIncludes(new String[] { defaultString(d.getIncludes()) });
      database.setExcludes(new String[] { defaultString(d.getExcludes()) });
      database.setIncludeExcludeColumns(TRUE.equals(d.isIncludeExcludeColumns()));
      database.setRecordVersionFields(new String[] { defaultString(d.getRecordVersionFields()) });
      database.setRecordTimestampFields(new String[] { defaultString(d.getRecordTimestampFields()) });
      database.setSyntheticPrimaryKeys(new String[] { defaultString(d.getSyntheticPrimaryKeys()) });
      database.setOverridePrimaryKeys(new String[] { defaultString(d.getOverridePrimaryKeys()) });
      database.setConfiguredCustomTypes(d.getCustomTypes());
      database.setConfiguredEnumTypes(d.getEnumTypes());
      database.setConfiguredForcedTypes(d.getForcedTypes());
      if (d.getRegexFlags() != null)
        database.setRegexFlags(d.getRegexFlags());
      SchemaVersionProvider svp = null;
      if (!StringUtils.isBlank(d.getSchemaVersionProvider()))
      {
        try
        {
          svp = (SchemaVersionProvider) Class.forName(d.getSchemaVersionProvider()).newInstance();
          LogUtil.getCoreLog().info("Using custom schema version provider : " + svp);
        }
        catch (Exception ignore)
        {
        }
      }
      if (svp == null)
        svp = new ConstantSchemaVersionProvider(null);
      database.setSchemaVersionProvider(svp);
      if (d.getEnumTypes().size() > 0)
        LogUtil
            .getCoreLog()
            .warn(
                "DEPRECATED",
                "The configuration property /configuration/generator/database/enumTypes is experimental and deprecated and will be removed in the future.");
      if (Boolean.TRUE.equals(d.isDateAsTimestamp()))
        LogUtil
            .getCoreLog()
            .warn(
                "DEPRECATED",
                "The configuration property /configuration/generator/database/dateAsTimestamp is deprecated as it is superseded by custom bindings and converters. It will thus be removed in the future.");
      if (d.isDateAsTimestamp() != null)
        database.setDateAsTimestamp(d.isDateAsTimestamp());
      if (d.isUnsignedTypes() != null)
        database.setSupportsUnsignedTypes(d.isUnsignedTypes());
      if (StringUtils.isBlank(g.getTarget().getPackageName()))
        g.getTarget().setPackageName("gemlite.shell.codegen.generated");
      if (StringUtils.isBlank(g.getTarget().getDirectory()))
        g.getTarget().setDirectory("target/generated-sources/gemlite-codegen");
      generator.setTargetPackage(g.getTarget().getPackageName());
      generator.setTargetDirectory(g.getTarget().getDirectory());
      // [#1394] The <generate/> element should be optional
      if (g.getGenerate() == null)
        g.setGenerate(new Generate());
      if (g.getGenerate().isPojos() != null)
        generator.setGeneratePojos(true);
      if (g.getGenerate().isPojosEqualsAndHashCode() != null)
        generator.setGeneratePojosEqualsAndHashCode(true);
      // [#3669] Optional Database element
      if (g.getDatabase() == null)
        g.setDatabase(new gemlite.shell.codegen.util.jaxb.Database());
      if (!StringUtils.isBlank(g.getDatabase().getSchemaVersionProvider()))
        generator.setUseSchemaVersionProvider(true);
      // Generator properties that should in fact be strategy properties
      strategy.setInstanceFields(generator.generateInstanceFields());
      generator.generate(database);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
      // Close connection only if it was created by the GenerationTool
      if (close && connection != null)
      {
        connection.close();
      }
    }
  }
  
  private Properties properties(List<Property> properties)
  {
    Properties result = new Properties();
    for (Property p : properties)
    {
      result.put(p.getKey(), p.getValue());
    }
    return result;
  }
  
  private String driverClass(Jdbc j)
  {
    String result = j.getDriver();
    if (result == null)
    {
      result = JDBCUtils.driver(j.getUrl());
      LogUtil.getCoreLog().info("Database", "Inferring driver " + result + " from URL " + j.getUrl());
    }
    return result;
  }
  
  private Class<?> loadClass(String className) throws ClassNotFoundException
  {
    try
    {
      // [#2283] If no explicit class loader was provided try loading the
      // class
      // with "default" techniques
      if (loader == null)
      {
        try
        {
          return Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
          return Thread.currentThread().getContextClassLoader().loadClass(className);
        }
      }
      // Prefer the explicit class loader if available
      else
      {
        return loader.loadClass(className);
      }
    }
    // [#2801]
    catch (ClassNotFoundException e)
    {
      if (className.startsWith("gemlite.shell.codegen.util."))
      {
        LogUtil.getCoreLog().warn("");
      }
      throw e;
    }
  }
  
  private static String trim(String string)
  {
    return (string == null ? null : string.trim());
  }
  
  private static void errorIfNull(Object o, String message)
  {
    if (o == null)
    {
      LogUtil.getCoreLog().error(message + " For details, see " + Constants.NS_CODEGEN);
      System.exit(-1);
    }
  }
  
  private static void error()
  {
    LogUtil.getCoreLog().error("Usage : GenerationTool <configuration-file>");
    System.exit(-1);
  }
  
  public static long copyLarge(InputStream input, OutputStream output) throws IOException
  {
    byte[] buffer = new byte[1024 * 4];
    long count = 0;
    int n = 0;
    while (-1 != (n = input.read(buffer)))
    {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }
  
  public static Configuration load(InputStream in) throws IOException
  {
    // If there is no namespace defined, add the default one
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    copyLarge(in, out);
    String xml = out.toString();
    xml = xml.replace("<configuration>", "<configuration xmlns=\"" + Constants.NS_CODEGEN + "\">");
    try
    {
      SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      javax.xml.validation.Schema schema = sf.newSchema(GenerationTool.class.getResource("/xsd/"
          + Constants.XSD_CODEGEN));
      JAXBContext ctx = JAXBContext.newInstance(Configuration.class);
      Unmarshaller unmarshaller = ctx.createUnmarshaller();
      unmarshaller.setSchema(schema);
      unmarshaller.setEventHandler(new ValidationEventHandler()
      {
        @Override
        public boolean handleEvent(ValidationEvent event)
        {
          LogUtil.getCoreLog().warn("Unmarshal warning", event.getMessage());
          return true;
        }
      });
      return (Configuration) unmarshaller.unmarshal(new StringReader(xml));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
