package com.lordmancer2.rank

import akka.actor.ActorSystem
import com.lordmancer2.rank.kafka.AutoPartitionConsumer
import com.typesafe.config.{Config, ConfigFactory}

trait ConfigModule {

  val config: Config = ConfigFactory.load()

  implicit val system: ActorSystem = ActorSystem("rank")

}

object App extends App with ConfigModule {

  system.actorOf(AutoPartitionConsumer.props(List("userevents"), config.getConfig("kafka.consumer")))

}
