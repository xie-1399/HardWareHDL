package Gramer

//there will be more abstract useful skills(trait and abstract class)


//show abstract types
trait Abstract{
  type T
  def transform(x:T) : T
  val initial : T
  var cur : T
}

class Concrete extends Abstract{
  override type T = String
  override def transform(x: String) = x + x
  override val initial: String = "initial"
  override var cur: String = initial
}


class UseAbstract {

}
