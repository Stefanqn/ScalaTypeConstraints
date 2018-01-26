package example

import org.scalatest.{Matchers, WordSpec}

import scala.annotation.implicitNotFound

@implicitNotFound(msg = "Cannot prove that ${From} <:< ${To}.")
trait <::<[-From, +To]

object <::< {
  implicit def conforms[A]: A <::< A = new <::<[A,A]{}
}


class SubtypeTest extends WordSpec with Matchers {
  class Fruit
  class Apple  extends Fruit
  class Mac extends Apple
  class Banana extends Fruit

  "Apple is subtype of fruit" in {
    val res: <::<[Apple, Fruit] = implicitly[Apple <::< Fruit]
  }

  "Fruit is not a subtype of apple" in {
    assertDoesNotCompile("implicitly[Fruit <::< Apple]")
  }


  def tupleIfSubtype[T, U](t: T, u: U)(implicit ev: T <::< U) = (t, u)
  "tupleIfSubtype does not compile if not subtypes" in {
    assertDoesNotCompile("""tupleIfSubtype("Lincoln", 42)""")
  }

  "tupleIfSubtype compiles if subtype " in {
    tupleIfSubtype(new Apple(), new Fruit())(<::<.conforms[Fruit])
    tupleIfSubtype(new Apple(), new Fruit())(<::<.conforms[Apple])
  }

  "tupleIfSubtype does not compile if not a subtype " in {
    assertDoesNotCompile("""tupleIfSubtype(new Fruit(), new Apple())""")
  }
}