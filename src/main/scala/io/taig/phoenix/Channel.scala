package io.taig.phoenix

import io.circe.Json
import io.taig.phoenix.models.{ Event, Inbound, Response, Topic }

import scala.language.higherKinds

trait Channel[A[_], B[_]] {
    def topic: Topic

    def stream: A[Inbound]

    def send( event: Event, payload: Json ): B[Option[Response]]
}