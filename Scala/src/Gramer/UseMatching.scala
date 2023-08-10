package Gramer

// this is about case class and pattern matching

trait Expr

case class Var(name:String) extends Expr{
  //in the case class parameter will be add with the val
  //implement the toString hashCode equals methods
  //and support the pattern matching

}
case class Num(number:Int) extends Expr
case class Unop(operator:String,arg:Expr) extends Expr
case class Binop(operator:String,left:Expr,right:Expr) extends Expr


object UseMatching extends App{

  //a simple pattern matching
  def simplify(expr: Expr):Expr = {
    expr match {
      case Unop("-", Unop("-", e)) => e
      case Binop("+",e,Num(0)) if e == Num(0) => e
      //add some guards
      case _ => expr //must contain all cases
    }
  }

  val v1 = Var("xxl")
  println(v1.name)   // it can not be shown in the class

  //get a new object
  val v2 = v1.copy(name = "new")
  println(v2.name)
  println(simplify(v2))

  //using variable
  var expr = 0
  expr = 10
  val value = expr match {
    case 0 => "zero"
    case otherthing => s"$otherthing"
  }
  println(value)

  //match Array
  val xs = List(1,2,3)
  var res = xs match {
    case List(1,_,_) => "found it"
    case List(0,_*) => "with zero"
    case _ => "not found"
  }
  println(res)
}
