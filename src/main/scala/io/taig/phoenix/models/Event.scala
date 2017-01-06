package io.taig.phoenix.models

import io.circe.{ Decoder, Encoder }

sealed case class Event( name: String )

object Event {
    object Close extends Event( "phx_close" )
    object Error extends Event( "phx_error" )
    object Join extends Event( "phx_join" )
    object Reply extends Event( "phx_reply" )
    object Leave extends Event( "phx_leave" )

    val all = Close :: Error :: Join :: Reply :: Leave :: Nil

    implicit val encoder: Encoder[Event] = {
        Encoder[String].contramap( _.name )
    }

    implicit val decoder: Decoder[Event] = {
        Decoder[String].map { name ⇒
            all.find( _.name == name ).getOrElse( Event( name ) )
        }
    }
}