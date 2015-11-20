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
package batch.joblf;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration
{
  
  // tag::readerwriterprocessor[]
  @Bean
  public ItemReader<Person> reader()
  {
    FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
    reader.setResource(new ClassPathResource("sample-data.csv"));
    reader.setLineMapper(new DefaultLineMapper<Person>()
    {
      {
        setLineTokenizer(new DelimitedLineTokenizer()
        {
          {
            setNames(new String[] { "firstName", "lastName" });
          }
        });
        setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>()
        {
          {
            setTargetType(Person.class);
          }
        });
      }
    });
    return reader;
  }
  
  @Bean
  public ItemProcessor<Person, Person> processor()
  {
    return null;
  }
  
  @Bean
  public ItemWriter<Person> writer(DataSource dataSource)
  {
    JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
    writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
    writer.setDataSource(dataSource);
    return writer;
  }
  
  // end::readerwriterprocessor[]
  
  // tag::jobstep[]
  @Bean
  public Job importUserJob(JobBuilderFactory jobs, Step s1)
  {
    return jobs.get("importUserJob").incrementer(new RunIdIncrementer()).flow(s1).end().build();
  }
  
  @Bean
  public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader, ItemWriter<Person> writer,
      ItemProcessor<Person, Person> processor)
  {
    return stepBuilderFactory.get("step1").<Person, Person> chunk(10).reader(reader).processor(processor)
        .writer(writer).build();
  }
  
  // end::jobstep[]
  
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource)
  {
    return new JdbcTemplate(dataSource);
  }
  
}
