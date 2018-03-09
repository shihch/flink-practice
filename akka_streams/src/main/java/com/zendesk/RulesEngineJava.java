package com.zendesk;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.ClosedShape;
import akka.stream.Materializer;
import akka.stream.Outlet;
import akka.stream.javadsl.*;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;

import static java.lang.System.out;

public class RulesEngineJava {
    public static void main(String[] argv) {
        final ActorSystem system = ActorSystem.create("RulesEngineJava");
        final Materializer materializer = ActorMaterializer.create(system);

        final RunnableGraph<NotUsed> result = RunnableGraph.fromGraph(GraphDSL.create((builder) -> {
            final Source<Integer, NotUsed> in = Source.from(Arrays.asList(1, 2, 3, 4, 5));
            final Flow<Integer, Integer, NotUsed> f1 = Flow.of(Integer.class).map(elem -> elem + 10);
            final Sink<Integer, CompletionStage<Done>> sink = Sink.foreach(elem -> out.println(elem));
            final Outlet<Integer> source = builder.add(in).out();
            builder.from(source).via(builder.add(f1)).to(builder.add(sink));
            return ClosedShape.getInstance();
        }));

        result.run(materializer);
    }
}
