package Gramer

// this is about use class

class Counter{
  private var value = 0
  def increasement(): Unit = {
    value +=1
  }
  def current():Int = value
  //also can use setter and getter
}



class UseClass {
  //use class is just a simple way
  def method(value:Int) = value + 1
  val num = method(10)
  def show() = {println(num)}
}



object UseClass extends App{
  val c = new UseClass()
  c.show()
}