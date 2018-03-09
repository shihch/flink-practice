## Apache Flink
You are free to use your own flink distribution for this coding challenge.
We are using `1.4.0` version by default.

### Code template
We prepared code template to get you started quickly. You can find them here:

- [Scala template](./src/main/scala/com/zendesk/FlinkRulesEngine.scala)
- [Java template](./src/main/java/com/zendesk/FlinkRulesEngineJava.java)

#### Run Scala code with sbt
You can run the template out of the box by `sbt "flink/run"`
If you want to pass in parameters you can just add them in: `sbt "flink/run --port 1234"`
Here is the output for a simple word count program.

```
$ sbt "flink/run"
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/hsun/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/project
[info] Loading settings from build.sbt ...
[info] Set current project to rules_engine (in build file:/Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/)
[info] Packaging /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/flink/target/scala-2.11/rules-engine_2.11-1.0.0.jar ...
[info] Done packaging.
[info] Running com.zendesk.FlinkRulesEngine
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Connected to JobManager at Actor[akka://flink/user/jobmanager_1#-1577037198] with leader session id 82286a04-d861-4045-b62f-521a2b9c5fd3.
02/13/2018 15:55:29	Job execution switched to status RUNNING.
02/13/2018 15:55:29	Source: Collection Source(1/1) switched to SCHEDULED
02/13/2018 15:55:29	Flat Map -> Map(1/8) switched to SCHEDULED
02/13/2018 15:55:29	Flat Map -> Map(2/8) switched to SCHEDULED
02/13/2018 15:55:29	Flat Map -> Map(3/8) switched to SCHEDULED
...
WordWithCount(place,1)
WordWithCount(hello,1)
WordWithCount(hello,2)
WordWithCount(world,1)
WordWithCount(zendesk,1)
WordWithCount(a,1)
WordWithCount(great,1)
WordWithCount(to,1)
WordWithCount(work,1)
WordWithCount(zendesk,2)
WordWithCount(is,1)
02/13/2018 15:55:29	aggregation(2/8) switched to FINISHED
...
[success] Total time: 3 s, completed Feb 13, 2018 3:55:29 PM
```

#### Run Java code with sbt
```
$ sbt "flink/runMain com.zendesk.FlinkRulesEngineJava"
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/hsun/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/project
[info] Loading settings from build.sbt ...
[info] Set current project to rules_engine (in build file:/Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/)
[info] Running com.zendesk.FlinkRulesEngineJava  
...
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Connected to JobManager at Actor[akka://flink/user/jobmanager_1#-397365486] with leader session id b4fca3bf-2566-47e6-92a9-b6741e3cf9eb.
02/14/2018 10:25:35	Job execution switched to status RUNNING.
02/14/2018 10:25:35	Source: Collection Source(1/1) switched to SCHEDULED
02/14/2018 10:25:35	Flat Map(1/8) switched to SCHEDULED  
...
02/14/2018 10:25:36	Keyed Aggregation -> Sink: Unnamed(8/8) switched to RUNNING
6> (zendesk,1)
6> (a,1)
5> (world,1)
3> (place,1)
8> (is,1)
6> (great,1)
3> (hello,1)
6> (to,1)
6> (work,1)
3> (hello,2)
6> (zendesk,2)
```
### Setup Flink environment
You have two options to run Flink locally:
- From sbt or your editor, you do not have to do anything in addition.
- Flink standalone cluster, this is more complex, not required.

#### Run flink with sbt
Use `sbt "flink/run"`

#### Run Flink in standalone mode
This approach is more complex, good for production use case.
But this is not required for this challenge.

##### Setup
Please get flink [here](http://www.apache.org/dyn/closer.lua/flink/flink-1.4.0/flink-1.4.0-bin-scala_2.11.tgz)
```
export FLINK_HOME=~/Downloads/flink-1.4.0
$FLINK_HOME/bin/start-local.sh
```

##### Use Flink
Now you should have a Flink standalone cluster working.
Navigate to [http://localhost:8081/#/overview](http://localhost:8081/#/overview)
```
tail $FLINK_HOME/log/flink-*-jobmanager-*.log
```

##### Cleanup
```
$FLINK_HOME/bin/stop-local.sh
```

### Reference
- [Flink quickstart](https://ci.apache.org/projects/flink/flink-docs-release-1.4/quickstart/setup_quickstart.html)
- [Examples](https://github.com/apache/flink/tree/master/flink-examples/flink-examples-streaming/src/main/scala/org/apache/flink/streaming/scala/examples)
