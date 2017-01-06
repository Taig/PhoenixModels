package io.taig.phoenix.models

import io.circe.syntax._

class EventTest extends Suite {
    it should "have a JSON encoder" in {
        ( Event( "foo" ) +: Event.all ).foreach { event ⇒
            event.asJson shouldBe event.name.asJson
        }
    }

    it should "have a JSON decoder" in {
        ( Event( "foo" ) +: Event.all ).foreach { event ⇒
            event.name.asJson.as[Event] shouldBe Right( event )
        }
    }
}