package Gramer

import scala.collection.mutable
//This is about how to use mapping(also can call it tuple)
//mapping is also a very important structure

object Mapping{
  val scores = Map("Alice" -> 10,"Bob" -> 11,"Cindy" -> 12) //can't be changed
  val change_scores = scala.collection.mutable.Map("Fence" -> 16) //can change
  val empty_map = new mutable.HashMap[String,Int]

  //get the value from mapping
  val score = scores("Bob") //can use contains to check the value
  val flex = scores.getOrElse("Bob",0)

  //update the map value
  change_scores("Fence") = 0
  change_scores += ("EFF" -> 10)  //use -= to remove

  //loop the key and value
  val reversed = for((k,v) <- change_scores) yield (v,k) //also can get values and keys
}

object tuple{
  val example =(1,3.14,"str")
  val typeof = example.getClass.getName

  //get the tuple value like this
  val value1 = example._1
  val (first,second,third) = example

  //use zip to together these values
  val symbols = Array("<","-",">")
  val counts = Array(2,10,2)
  //return the tuple elem
  val pairs = symbols.zip(counts)
}

