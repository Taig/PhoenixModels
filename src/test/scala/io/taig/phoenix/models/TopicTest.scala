package io.taig.phoenix.models

import io.circe.DecodingFailure
import io.circe.syntax._

class TopicTest extends Suite {
    it should "have a String representation" in {
        Topic( "foo", "bar" ).toString shouldBe "Topic(foo:bar)"
        Topic( "foo" ).toString shouldBe "Topic(foo)"
    }

    it should "have a JSON encoder" in {
        Topic( "foo", "bar" ).asJson shouldBe "foo:bar".asJson
        Topic( "foo" ).asJson shouldBe "foo".asJson
    }

    it should "have a JSON decoder" in {
        "foo:bar".asJson.as[Topic] shouldBe Right( Topic( "foo", "bar" ) )
        "foo".asJson.as[Topic] shouldBe Right( Topic( "foo" ) )
        "foo§$%baz".asJson.as[Topic] shouldBe
            Left( DecodingFailure( "Invalid format", List.empty ) )
    }

    "isSubscribedTo" should "be valid when comparing equal topics" in {
        Topic( "foo", "bar" ) isSubscribedTo Topic( "foo", "bar" ) shouldBe true
        Topic( "foo" ) isSubscribedTo Topic( "foo" ) shouldBe true
    }

    it should "be valid when comparing a specific topic with a broadcast" in {
        Topic( "foo", "bar" ) isSubscribedTo Topic( "foo" ) shouldBe true
    }

    it should "be invalid when comparing topics with different identifiers" in {
        Topic( "foo", "bar" ) isSubscribedTo Topic( "foo", "baz" ) shouldBe false
    }

    it should "be invalid when comparing a broadcast with a specific topic" in {
        Topic( "foo" ) isSubscribedTo Topic( "foo", "bar" ) shouldBe false
    }
}