package io.taig.phoenix.models

import io.circe.{ Decoder, Encoder, Json }
import io.circe.generic.semiauto._

case class Request(
    topic:   Topic,
    event:   Event,
    payload: Json  = Json.Null,
    ref:     Ref   = Ref.unique()
)

object Request {
    implicit val decoder: Decoder[Request] = deriveDecoder

    implicit val encoder: Encoder[Request] = deriveEncoder
}