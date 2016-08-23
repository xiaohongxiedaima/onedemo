/**
  * @author xiaohongxiedaima
  * @version 16/6/23
  * @E-mail redfishinaction@yahoo.com
  */
object HelloWorld {

  def main(args: Array[String]) {
//
//    for (i <- (1 to 100)) {
//      val value = i * 450000
//      print(s"'$value',")
//    }

    p(0, 100, 100, 2)

  }

  def p( min: Int,max: Int, partitionNum:Int, length: Int):Unit = {
    val step = (max - min) / partitionNum
    val range = min to max
    range.filter(_%step == 0)
      .foreach(x => {
      val value = leftPadding(x.toString, length, '0')
      print(s"'$value',")
    })
  }

  def leftPadding(src: String, length: Int, char: Char): String = {
    var dist = src;
    for (i <- (src.length until length)) {
      dist = char + dist
    }
    dist
  }

}
