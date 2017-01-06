package io.taig.phoenix.models

import io.circe.Json
import io.circe.syntax._

class RequestTest extends Suite {
    val request = Request(
        Topic( "foobar" ),
        Event( "foo" ),
        "bar".asJson,
        Ref( "baz" )
    )

    val json = Json.obj(
        "topic" → "foobar".asJson,
        "event" → "foo".asJson,
        "payload" → "bar".asJson,
        "ref" → "baz".asJson
    )

    it should "have a JSON encoder" in {
        request.asJson shouldBe json
    }

    it should "have a JSON decoder" in {
        json.as[Request] shouldBe Right( request )
    }
}