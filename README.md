# gemlite
Why we develop gemlite?

Our team ever joined in china railway ticket system upgrade project and other Gemfire-related POC or projects.
At the beginning, we use the gemfire directly. Then we realize that gemfire is a very good develop platform that is flexible,powerful and with amazing performance,but it's hard to use correctly.We need use it in more and more project, we need more resources involved even they have not so much knowledge about the distributed program,the parallel design , and so on. There are so many business logic we need to implement , we need more junior programmer to join in and we need them just focus on the business . So we create this framework to leverage the learning curve of gemfire and the program skill that needed.

The features :

Gemlite is a handy toolkit, which provides a well-defined framework to facilitate the developers to use Gemfire efficiently and correctly.<br>
Gemlite’s framework clearly define the coding rules. It guides the developers step by step, and shortens the developer learning curve.<br>
Gemlite is able to generate the auxiliary codes automatically to minimize the repeating tasks, and increase developer productivity.<br>
Gemlite provides a more advanced secondary indexing mechanism which is similar to SQL syntax. This indexing mechanism provides much better performance than OQL indexing.<br>
Gemlite provides a better way for the “Hot Deployment”, and is transparent to developers. Within the Gemlite framework, the business application developed by users can guarantee a hot deployment automatically.<br>
Gemlite provides a better way to monitor, collect the performance data, and analyzing bottlenecks of the business application in Gemfire servers.<br>
Gemlite provides an easy-to-use approach to enable legacy systems to migrate to cloud platforms with the Cache layer, to increase system performance.<br>


What is the next ?

Add lucene index;<br>
Enhance security;<br>
Make the hot deploy functionility better ;<br>
Easy config and management with enhanced UI;<br>
Simplify the service interface using spring data;<br>


Wiki:<br>
DataModel https://github.com/iisi-nj/GemFireLite/wiki/Data-Model<br>
Gemlite domain https://github.com/iisi-nj/GemFireLite/wiki/Gemlite-domain<br>
Secondary Index https://github.com/iisi-nj/GemFireLite/wiki/Secondary-Index<br>
