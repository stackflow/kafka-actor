package com.lordmancer2.rank.hero

import com.lordmancer2.rank.json._

object Events {

  trait Event extends JsonSerializable

  trait HeroEvent extends Event {

    val heroId: String

  }

  /** Очки доминирования изменились */
  case class DominanceUpdated(heroId: String,
                              dominance: Long,
                              reason: Option[String] = None) extends HeroEvent

}
