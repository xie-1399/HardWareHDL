package Gramer
//object also calls static method and variable
//the apply method is also very important in object
class Accounts(val initvalue:Int){
  val id = Accounts.newUniquenumber
}

object Accounts{
  private var number = 0
  def newUniquenumber = {number = number + 1;number}

  def apply(initvalue:Int) = {
    new Accounts(initvalue)
  }
}

// use abstract class to override some methods

abstract class UndoAction(val description:String) {
  def undo():Unit
  def redo():Int
}

object DoNothingAction extends UndoAction("do nothing"){
  override def undo(): Unit = {}
  override def redo(): Int = {0}
}


//the Enumeration
object TrafficColor extends Enumeration{
  val Red,Yellow,Green = Value
  //use Value set id and color
  val color = Value(10,"color")
}


object UseObject extends App{
  val accounts = Accounts(10) //no need for use new

  val actions = Map("open" -> DoNothingAction,"save" -> DoNothingAction)  //a key/value map

  println(TrafficColor.color.id)
}
