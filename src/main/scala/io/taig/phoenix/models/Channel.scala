package io.taig.phoenix.models

import io.circe.Json

import scala.language.higherKinds

trait Channel[A[_], B[_]] {
    def topic: Topic

    def stream: A[Inbound]

    def send( event: Event, payload: Json ): B[Option[Response]]
}