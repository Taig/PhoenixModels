package io.taig.phoenix.models

import cats.syntax.either._
import io.circe.{ Decoder, DecodingFailure, Json }

sealed trait Inbound {
    def topic: Topic
}

object Inbound {
    implicit val decoder: Decoder[Inbound] = Decoder.instance { cursor ⇒
        ( for {
            event ← cursor.get[Event]( "event" )
            topic ← cursor.get[Topic]( "topic" )
            payload = cursor.downField( "payload" )
            status ← payload.get[Option[String]]( "status" )
            ref ← cursor.get[Option[Ref]]( "ref" )
        } yield ( event, status, topic, payload, ref ) ).flatMap {
            case ( Event.Reply, Some( "ok" ), topic, payload, Some( ref ) ) ⇒
                payload
                    .get[Json]( "response" )
                    .map( Response.Confirmation( topic, _, ref ) )
            case ( Event.Reply, Some( "error" ), topic, payload, Some( ref ) ) ⇒
                payload
                    .downField( "response" )
                    .get[String]( "reason" )
                    .map( Response.Error( topic, _, ref ) )
            case ( Event.Reply, Some( status ), _, _, _ ) ⇒
                val message = s"Invalid status: $status"
                Left( DecodingFailure( message, cursor.history ) )
            case ( event, None, topic, payload, None ) ⇒
                payload.as[Json].map( Push( topic, event, _ ) )
            case ( _, _, _, _, _ ) ⇒
                val message = "Neither a Response, nor a Push"
                Left( DecodingFailure( message, cursor.history ) )
        }
    }
}

sealed trait Response extends Inbound {
    def ref: Ref
}

object Response {
    case class Confirmation(
        topic:   Topic,
        payload: Json,
        ref:     Ref
    ) extends Response

    case class Error(
        topic:   Topic,
        message: String,
        ref:     Ref
    ) extends Response
}

case class Push(
    topic:   Topic,
    event:   Event,
    payload: Json
) extends Inbound