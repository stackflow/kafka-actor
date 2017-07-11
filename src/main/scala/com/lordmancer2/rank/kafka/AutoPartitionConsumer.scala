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
  def props(config: Config): Props = {
    val consumerConf = KafkaConsumer.Conf(
      new StringDeserializer,
      new StringDeserializer,
      groupId = "test_group",
      enableAutoCommit = false,
      autoOffsetReset = OffsetResetStrategy.EARLIEST)
      .withConf(config)

    val actorConf = KafkaConsumerActor.Conf(1.seconds, 3.seconds)

    Props(new AutoPartitionConsumer(consumerConf, actorConf))
  }

}

class AutoPartitionConsumer(kafkaConfig: KafkaConsumer.Conf[String, String],
                            actorConfig: KafkaConsumerActor.Conf) extends Actor with ActorLogging {

  private val recordsExt = ConsumerRecords.extractor[String, String]

  private val consumer = context.actorOf(
    KafkaConsumerActor.props(kafkaConfig, actorConfig, self)
  )
  context.watch(consumer)

  consumer ! Subscribe.AutoPartition(List("topic1"))

  override def receive: Receive = {

    // Records from Kafka
    case recordsExt(records) =>
      processRecords(records.pairs)
      sender() ! Confirm(records.offsets, commit = true)
  }

  private def processRecords(records: Seq[(Option[String], String)]) =
    records.foreach { case (key, value) =>
      log.info(s"Received [$key,$value]")
    }
}