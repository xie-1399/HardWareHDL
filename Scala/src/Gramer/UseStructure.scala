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
}
