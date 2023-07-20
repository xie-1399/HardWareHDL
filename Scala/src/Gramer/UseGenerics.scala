package Gramer

//the Generics can define the type when use the data

//Generics Class(can set more types)
class Stack[T]{
  private var elements:List[T] = Nil
  def push(data : T): Unit = {
    elements = elements :+ data
  }
  def pop():T = {
    val current = elements.head
    elements = elements.tail
    current
  }


}


object UseGenerics extends App{
  val stack = new Stack[String]
  stack.push("first")

  //Generics function
  def getMiddle[T](arr:Array[T]) = arr( arr.length / 2)

  val func = getMiddle[String] _  //get the function
  val middle = func(Array("1","2","3"))
  println(middle)
}
