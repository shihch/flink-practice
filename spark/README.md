## Apache Spark
You are free to use your own flink distribution for this coding challenge.
We are using `2.2.1` version by default.

### Code template
We prepared code template to get you started quickly. You can find them here:

- [Scala template](./src/main/scala/com/zendesk/SparkRulesEngine.scala)
- [Java template](./src/main/java/com/zendesk/SparkRulesEngineJava.java)

#### Run Scala code with sbt
You can run the template out of the box by `sbt "spark/run"`.
Here is the output for a simple word count program.

```
$ sbt "spark/run"
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/hsun/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/project
[info] Loading settings from build.sbt ...
[info] Set current project to rules_engine (in build file:/Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/)
[info] Packaging /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/spark/target/scala-2.11/rules-engine_2.11-1.0.0.jar ...
[info] Done packaging.
[info] Running (fork) com.zendesk.SparkRulesEngine
.....
[info] (place,1)
[info] (a,1)
[info] (work,1)
[info] (to,1)
[info] (zendesk,2)
[info] (is,1)
[info] (hello,2)
[info] (world,1)
[info] (great,1)
```

#### Run Java code with sbt
Run the Java example by `sbt "spark/runMain com.zendesk.SparkRulesEngineJava"`.
Here is the output for a simple word count program

```
$ sbt "spark/runMain com.zendesk.SparkRulesEngineJava"
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/hsun/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/project
[info] Loading settings from build.sbt ...
[info] Set current project to rules_engine (in build file:/Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/)
[info] Compiling 1 Scala source and 1 Java source to /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/spark/target/scala-2.11/classes ...
[info] Done compiling.
[info] Packaging /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/spark/target/scala-2.11/rules-engine_2.11-1.0.0.jar ...
[info] Done packaging.
[info] Running (fork) com.zendesk.SparkRulesEngineJava
....
[info] great: 1
[info] world: 1
[info] is: 1
[info] a: 1
[info] work: 1
[info] hello: 2
[info] zendesk: 2
[info] place: 1
[info] to: 1
```

### Setup Spark environment
You have two options to run Spark locally:
- From sbt or your editor, you do not have to do anything in addition.
- Spark standalone cluster, this is more complex, not required.

#### Run Spark from sbt
Just do `sbt "spark/run"`

#### Run Spark in standalone mode
This approach is more complex, good for production use case.
But this is not required for this challenge.

##### Setup
Please get spark [here](https://www.apache.org/dyn/closer.lua/spark/spark-2.2.1/spark-2.2.1-bin-hadoop2.7.tgz)
```
export SPARK_HOME=~/Downloads/spark-2.2.1-bin-hadoop2.7
$SPARK_HOME/./sbin/start-all.sh
```
Navigate to [http://localhost:8080](http://localhost:8080)
You should see Spark is running with one slave

You can add more slaves by this command:
```
$SPARK_HOME/sbin/start-slave.sh <master-spark-URL>
```

##### Use Spark
Now you should have a Spark standalone cluster working.

```
$SPARK_HOME/spark-submit <path-to-jar> <other options>

# You can check logs like this as well
tail -f $SPARK_HOME/logs/*org.apache.spark.deploy*.out
```

##### Cleanup
```
$SPARK_HOME/sbin/stop-all.sh
```

### Reference
- [Spark quickstart](https://spark.apache.org/docs/latest/quick-start.html)
- [Run Spark with Intellij](http://blog.miz.space/tutorial/2016/08/30/how-to-integrate-spark-intellij-idea-and-scala-install-setup-ubuntu-windows-mac)
- [Spark Examples](https://github.com/apache/spark/tree/v2.2.1/examples/src/main)
