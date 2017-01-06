package io.taig.phoenix.models

import io.circe.Json

import scala.language.higherKinds

trait Phoenix[A[_], B[_]] {
    def stream: A[Inbound]

    def join(
        topic:   Topic,
        payload: Json  = Json.Null
    ): B[Either[Option[Response.Error], Channel[A, B]]]

    def close(): Unit
}