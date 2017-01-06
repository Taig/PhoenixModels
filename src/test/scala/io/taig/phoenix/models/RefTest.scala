package io.taig.phoenix.models

import io.circe.syntax._

class RefTest extends Suite {
    it should "create incremental, unique ids" in {
        val refs = Stream.continually( Ref.unique() ).take( 10 ).map( _.value )
        refs.distinct shouldBe refs
    }

    it should "have a JSON encoder" in {
        Ref( "foo" ).asJson shouldBe "foo".asJson
    }

    it should "have a JSON decoder" in {
        "foo".asJson.as[Ref] shouldBe Right( Ref( "foo" ) )
    }
}