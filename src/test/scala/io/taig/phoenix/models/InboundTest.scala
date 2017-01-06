package io.taig.phoenix.models

import io.circe.{ DecodingFailure, Json }
import io.circe.syntax._

class InboundTest extends Suite {
    val event = Event( "foobar" )

    val topic = Topic( "foo", "bar" )

    val ref = Ref.unique()

    val payload = Json.obj( "foo" → "bar".asJson )

    it should "have a JSON decoder for Response.Confirmation" in {
        val json = Json.obj(
            "event" → ( Event.Reply: Event ).asJson,
            "topic" → topic.asJson,
            "payload" → Json.obj(
                "status" → "ok".asJson,
                "response" → Json.obj()
            ),
            "ref" → ref.asJson
        )

        json.as[Inbound] shouldBe
            Right( Response.Confirmation( topic, Json.obj(), ref ) )
    }

    it should "have a JSON decoder for Response.Error" in {
        val json = Json.obj(
            "event" → ( Event.Reply: Event ).asJson,
            "topic" → topic.asJson,
            "payload" → Json.obj(
                "status" → "error".asJson,
                "response" → Json.obj(
                    "reason" → "foobar".asJson
                )
            ),
            "ref" → ref.asJson
        )

        json.as[Inbound] shouldBe
            Right( Response.Error( topic, "foobar", ref ) )
    }

    it should "have a JSON decoder for Push" in {
        val json = Json.obj(
            "event" → event.asJson,
            "topic" → topic.asJson,
            "payload" → payload
        )

        json.as[Inbound] shouldBe
            Right( Push( topic, event, payload ) )
    }

    it should "fail on unknown status" in {
        val json = Json.obj(
            "event" → ( Event.Reply: Event ).asJson,
            "topic" → topic.asJson,
            "payload" → Json.obj(
                "status" → "foobar".asJson,
                "response" → Json.obj()
            ),
            "ref" → ref.asJson
        )

        json.as[Inbound] shouldBe
            Left( DecodingFailure( "Invalid status: foobar", List.empty ) )
    }

    it should "fail if it's neither a Response, nor a Push" in {
        val json = Json.obj(
            "event" → Event( "foo" ).asJson,
            "topic" → topic.asJson,
            "payload" → Json.obj(
                "status" → "ok".asJson,
                "response" → Json.obj()
            )
        )

        json.as[Inbound] shouldBe
            Left( DecodingFailure( "Neither a Response, nor a Push", List.empty ) )
    }
}