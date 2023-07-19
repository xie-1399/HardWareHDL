package Gramer

// this is about use class

class Counter{
  private var value = 0
  def increasement(): Unit = {
    value +=1
  }
  def current():Int = value

  //also can use setter and getter
  def getvalue = value
  def getvalue_ = (newValue : Int){
    if(newValue > value) value - newValue
  }

}





class UseClass {
  //use class is just a simple way
}
