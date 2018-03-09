package com.zendesk

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._

import scala.util.Random

object RulesEngine extends App {
  implicit val system = ActorSystem("RulesEngine")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  RunnableGraph.fromGraph(GraphDSL.create() {
    implicit builder =>
      import GraphDSL.Implicits._

      val source = Source.cycle(() => List(1, 2, 3).iterator)
      val transform = Flow[Int].map(_ * Random.nextInt).take(10)
      val sink = Sink.foreach[Int](println(_))

      source ~> transform ~> sink
      ClosedShape
  }).run()
}
