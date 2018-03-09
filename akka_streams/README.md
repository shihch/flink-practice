## Akka streams
Akka streams is one of the popular implementation of reactive streams.
You can easily combine stages into a very complex stream processing graph.

### Code template
We prepared code template to get you started quickly. You can find them here:

- [Scala template](./src/main/scala/com/zendesk/RulesEngine.scala)
- [Java template](./src/main/java/com/zendesk/RulesEngineJava.java)

#### Run Scala code with sbt
We are using `sbt` to run programs, you can follow these steps to run the [scala example program](./akka_streams/src/main/scala/com/zendesk/RulesEngine.scala).

```
# This will run the main class you specified in the build.sbt file
sbt akka/run

# You can specify main class here as well
sbt "akka/runMain <..your main class..>"
```
You can modify `build.sbt` to customize the behavior.
Currently `sbt run` give you this output
```
$ sbt akka/run
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/hsun/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/akka_streams/project
[info] Loading settings from build.sbt ...
[info] Set current project to rules-engine (in build file:/Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/akka_streams/)
[info] Compiling 1 Scala source to /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/akka_streams/target/scala-2.12/classes ...
[info] Done compiling.
[info] Packaging /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/akka_streams/target/scala-2.12/rules-engine_2.12-1.0.0.jar ...
[info] Done packaging.
[info] Running com.zendesk.RulesEngine
[debug] Waiting for threads to exit or System.exit to be called.
[debug]   Classpath:
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/job-1/target/6bf8193b/rules-engine_2.12-1.0.0.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/target/f2e496f2/scala-library-2.12.3.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/target/6123ce5b/akka-stream_2.12-2.5.9.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/target/b6dc6f63/akka-actor_2.12-2.5.9.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/target/d6ac0ce0/config-1.3.2.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/target/1e6f1e74/scala-java8-compat_2.12-0.8.0.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/target/323964c3/reactive-streams-1.0.2.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/target/8a357d49/ssl-config-core_2.12-0.2.2.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_4d5b2ee4/target/7c5f25a2/scala-parser-combinators_2.12-1.0.4.jar
[debug] Waiting for thread run-main-0 to terminate.
[debug] 	Thread run-main-0 exited.
[debug] Waiting for thread RulesEngine-akka.actor.default-dispatcher-2 to terminate.
1050082104
-1962036788
120057194
-267804827
735338976
-435543669
1583621593
678897584
843266398
2027436004
```

#### Run Java code with sbt
Here is how you run the java version with `sbt`.

```
$ sbt "akka/runMain com.zendesk.RulesEngineJava"
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/hsun/.sbt/1.0/plugins
[info] Loading settings from plugins.sbt ...
[info] Loading project definition from /Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/akka_streams/project
[info] Loading settings from build.sbt ...
[info] Set current project to rules-engine (in build file:/Users/hsun/Code/zendesk/orca-lab-days/interview_questions/rules_engine/akka_streams/)
[info] Running com.zendesk.RulesEngineJava
[debug] Waiting for threads to exit or System.exit to be called.
[debug]   Classpath:
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/job-1/target/c8c8c08b/rules-engine_2.12-1.0.0.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/target/f2e496f2/scala-library-2.12.3.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/target/6123ce5b/akka-stream_2.12-2.5.9.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/target/b6dc6f63/akka-actor_2.12-2.5.9.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/target/d6ac0ce0/config-1.3.2.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/target/1e6f1e74/scala-java8-compat_2.12-0.8.0.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/target/323964c3/reactive-streams-1.0.2.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/target/8a357d49/ssl-config-core_2.12-0.2.2.jar
[debug] 	/var/folders/37/6tz6x9x97d19_ltcm_1tl_f00000gp/T/sbt_1f4a394a/target/7c5f25a2/scala-parser-combinators_2.12-1.0.4.jar
Starting RulesEngineJava...
[debug] Waiting for thread run-main-0 to terminate.
[debug] 	Thread run-main-0 exited.
[debug] Waiting for thread QuickStart-akka.actor.default-dispatcher-3 to terminate.
11
12
13
14
15
```

#### Boilerplate
This is a typical boilerplate to run a Akka streams graph.

```
implicit val system = ActorSystem("RulesEngine")
implicit val materializer = ActorMaterializer()

RunnableGraph.fromGraph(GraphDSL.create() {
    implicit builder =>
    
    val source = <..read json files in..>
    val transform = <..build your rules here with various builtin stream stages..>
    val sink = <..show your results..>
    
    source ~> transform ~> sink
    
    ClosedGraph
}.run()
```

### Tips
- Design the graph first. What is your source and sink? What is the simplest transformation you can do?
- Make sure data types do match between your streaming stages
- How to debug? Breakpoints works well for akka streams, or you can just use `println`

### References
- [Akka streams overview](https://doc.akka.io/docs/akka/2.5/stream/index.html)
- [stream stages overview](https://doc.akka.io/docs/akka/2.5/stream/stages-overview.html)
