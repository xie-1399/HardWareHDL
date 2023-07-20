package Gramer

import scala.collection.mutable.ArrayBuffer

//some structures about Array and BufferArray
//Array can be change, but List can't

object impArray {
  val nums = new Array[Int](10) //init with 0
  val strArray = Array("Hello", "World")
  strArray(0) = "change"

  val buffer = ArrayBuffer[Int]()
  //add at last position
  buffer += 1
  buffer += (2, 3, 4)
  //add with array
  buffer ++= Array(5, 6, 7)
  //remove last set of it
  buffer.trimEnd(5)
  //at any position
  buffer.insert(2, 6) // or with(6,7,8)
  buffer.remove(2, 3) //remove how many values
  val getArray = buffer.toArray

  //increase loop
  for (elem <- buffer) {
    println(buffer)
  }
  //skip with 2
  for (i <- 0 until(buffer.length, 2)) {
    println(buffer(i))
  }
  //get a new array by the old array
  for (elem <- buffer if elem % 2 == 0) yield 2 * elem

  //some functions
  val sum = Array(1,2,3).sum
  val max = Array(7,8,9).max
  val sortArrayBuffer = ArrayBuffer(1,2,7,5)
  val sortArray = Array(1,2,7,5)
  val sorted = sortArrayBuffer.sortWith(_ < _) //or use sortWith define
  sortArray.mkString("<",",",">")  //set the seperate signal
  val quick = scala.util.Sorting.quickSort(sortArray) //use array

  //get more dim matrix
  val matrix = Array.ofDim[Double](3,4)
}

object impList{
  //List can not be changed , some ways to create the List
  val list1 = List(1,2,3)
  val list2 = list1.drop(2)  //return a new List instead
  val list3 = (1 to 10 by 2).toList

  //flatten
  val num = List(List(1,3),List(2,4)).flatten

  //flatten Map
  val nums = List("at","as")
  val newnums = nums.map(_.toUpperCase)
  val flattennums = nums.flatMap(_.toUpperCase)
  //more function : https://www.runoob.com/scala/scala-lists.html
}

object UseArray extends App{
  //flatten map value
  println(impList.newnums)
  println(impList.flattennums)
}


//see more details at https://docs.scala-lang.org/ about Array
