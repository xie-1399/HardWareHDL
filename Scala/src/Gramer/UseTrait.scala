package Gramer

import scala.collection.mutable.ArrayBuffer

// the trait is more likely interface in the Java
// let the thin interface to the heavy interface

trait Philosophical{
  def phase = "this is a trait method"
}

//use extends or with to mix the trait
class UseTrait extends Philosophical {
  override def phase: String = super.phase + " of UseTrait"
}

//some special trait
class Rational(n:Int) extends UseTrait with Ordered[Rational]{
  val number = n
  def compare(that: Rational): Int = this.number - that.number
}

//modify the method in the class
abstract class IntQueue{
  def get():Int
  def push(x:Int):Unit
}

class BasicIntQueue extends IntQueue{
  private val buf = ArrayBuffer.empty[Int]
  def get() = buf.remove(0)
  override def push(x: Int): Unit = buf += x
}

//push the elem double
trait Doubling extends IntQueue{ //only in scala3 can accept the parameter
  abstract override def push(x: Int): Unit = super.push(2 * x)
}

trait Incrementing extends IntQueue{
  abstract override def push(x: Int): Unit = super.push(x + 1)
}

class MyQueue1 extends BasicIntQueue with Doubling with Incrementing
class MyQueue2 extends BasicIntQueue with Incrementing with Doubling

object UseTrait extends App{
  val philosophical:Philosophical = new UseTrait
  println(philosophical.phase)

  val rational1 = new Rational(10)
  val rational2 = new Rational(20)
  println(rational1 < rational2)

  //the super method is Linearization
  val queue1 = new MyQueue1
  queue1.push(10)
  println(queue1.get()) //get 22

  val queue2 = new MyQueue2
  queue2.push(10)
  println(queue2.get()) //get 21
}