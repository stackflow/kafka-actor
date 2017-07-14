package com.lordmancer2.rank.event

import com.fasterxml.jackson.databind.JsonSerializable
import com.lordmancer2.rank.json._
import org.joda.time.DateTime

object Events {

  case class Commit[Event](id: String,
                           revision: Long,
                           tm: DateTime,
                           events: Seq[Event])

  case class SerializableCommit(id: String,
                                revision: Long,
                                tm: DateTime,
                                events: Seq[JsonSerializable])

  def deserialize[Event <: JsonSerializable](str: String): Commit[Event] = {
    val commit = mapper.readValue(str, classOf[SerializableCommit])
    Commit(commit.id, commit.revision, commit.tm, commit.events.map(_.asInstanceOf[Event]))
  }

}
