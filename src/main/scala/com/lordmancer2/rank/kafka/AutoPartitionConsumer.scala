package com.lordmancer2.rank.kafka

import akka.actor.{Actor, ActorLogging, Props}
import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, KafkaConsumerActor}
import com.typesafe.config.Config
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.duration._

object AutoPartitionConsumer {

  /*
   * Starts an ActorSystem and instantiates the below Actor that subscribes and
   * consumes from the configured KafkaConsumerActor.
   */
  def props(topics: List[String], config: Config): Props = {
    val consumerConf = KafkaConsumer.Conf(
      new StringDeserializer,
      new StringDeserializer,
      groupId = "rank",
      enableAutoCommit = false,
      autoOffsetReset = OffsetResetStrategy.EARLIEST)
      .withConf(config)

    val actorConf = KafkaConsumerActor.Conf(50.millis, 3.seconds)

    Props(new AutoPartitionConsumer(topics.map(config.getString("topic.prefix") + _), consumerConf, actorConf))
  }

}

class AutoPartitionConsumer(topics: List[String],
                            kafkaConfig: KafkaConsumer.Conf[String, String],
                            actorConfig: KafkaConsumerActor.Conf) extends Actor with ActorLogging {

  private val recordsExt = ConsumerRecords.extractor[String, String]

  private val consumer = context.actorOf(
    KafkaConsumerActor.props(kafkaConfig, actorConfig, self)
  )

  context.watch(consumer)

  consumer ! Subscribe.AutoPartition(topics)

  override def receive: Receive = {
    case recordsExt(records) => // Records from Kafka
      processRecords(records.pairs)
      sender() ! Confirm(records.offsets, commit = true)
  }

  private def processRecords(records: Seq[(Option[String], String)]) = {
    records.foreach { case (key, value) =>

      log.info(s"Received [$key,$value]")
    }
  }

}
