package Gramer

import scala.collection.mutable.ArrayBuffer

//some structures about Array and BufferArray

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

//see more details at https://docs.scala-lang.org/ about Array
