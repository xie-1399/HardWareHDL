package Gramer
import scala.math._
//just about the scala structure grammer

object loop{
  for(i <- 1 to 3; j <- 2 to 4) println( i + j)
  for(i <- 1 to 10) yield i % 3  //generate a vec
  def abs(x:Double) = if (x >= 0) x else -x
  def sum(args:Int*) = for(i <- args ) print(i)
  //let range become parameter
  val s = sum(1 to 5:_*)

  //using val def or lazy val
  val words = scala.io.Source.fromFile("XXX").mkString
  lazy val wordsd = scala.io.Source.fromFile("XXX").mkString  //first time use
  def wordsds = scala.io.Source.fromFile("XXX").mkString

  //exception
  val x = 10
  try{
     sqrt(x)
  }catch {
    case _ : Exception => println("Exception")
  }
  finally {
    println("finally")
  }

}


//datatype : Byte,Int,Long,Short,Double,Float

object Structure{
  //the Control Structure
  val x = 10
  if(x < 0 ){???}
  else if(???){???}
  else{???}

  //match just like switch
  val day = x match {
    case 0 | 1 => "???"
    case _ => "???"
  }
  //also more match examples

/*
  def pattern(x: Matchable): String = x match {  //set you match type
    // constant patterns
    case 0 => "zero"
    case true => "true"
    case "hello" => "you said 'hello'"
    case Nil => "an empty List"

    // sequence patterns
    case List(0, _, _) => "a 3-element list with 0 as the first element"
    case List(1, _*) => "list, starts with 1, has any number of elements"
    case Vector(1, _*) => "vector, starts w/ 1, has any number of elements"

    // tuple patterns
    case (a, b) => s"got $a and $b"
    case (a, b, c) => s"got $a, $b, and $c"

    // constructor patterns
    case Person(first, "Alexander") => s"Alexander, first name = $first"
    case Dog("Zeus") => "found a dog named Zeus"

    // type test patterns
    case s: String => s"got a string: $s"
    case i: Int => s"got an int: $i"
    case f: Float => s"got a float: $f"
    case a: Array[Int] => s"array of int: ${a.mkString(",")}"
    case as: Array[String] => s"string array: ${as.mkString(",")}"
    case d: Dog => s"dog: ${d.name}"
    case list: List[?] => s"got a List: $list"
    case m: Map[?, ?] => m.toString

    // the default wildcard pattern
    case _ => "Unknown"
  }
*/

}