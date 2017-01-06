package io.taig.phoenix

import io.circe.Json
import io.taig.phoenix.models.{ Inbound, Response, Topic }

import scala.language.higherKinds

trait Phoenix[A[_], B[_]] {
    def stream: A[Inbound]

    def join(
        topic:   Topic,
        payload: Json  = Json.Null
    ): B[Either[Option[Response.Error], Channel[A, B]]]

    def close(): Unit
}