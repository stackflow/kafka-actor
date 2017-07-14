package com.lordmancer2.rank

import com.fasterxml.jackson.annotation.{JsonInclude, JsonTypeInfo}
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.lordmancer2.rank.hero.Events

package object json {

  implicit val mapper: ObjectMapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    .registerModule(new JodaModule())
    //.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
    // .configure(SerializationFeature.INDENT_OUTPUT, true)
    // .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
    .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)

  mapper.registerSubtypes(
    //// События пользователя.
    new NamedType(classOf[Events.DominanceUpdated], "HeroEvents.DominanceUpdated")
  )

  /** Трэйт метка, о том что класс при сериализации должен иметь поле $type */
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "$type")
  trait JsonSerializable

}
