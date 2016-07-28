package com.xiaohongxiedaima.demo.scala.packagetest {
  /**
    * @author xiaohongxiedaima
    * @version 16-7-14
    * @E-mail redfishinaction@yahoo.com
    */

  package package01 {
    class TestPackage01 {

    }
  }


  package package02 {
    class TestPackage02 {

    }
  }

}


package com.xiaohongxiedaima.demo.scala.packagetest.package02 {
  package main {

    import com.xiaohongxiedaima.demo.scala.packagetest.package01.TestPackage01

    object PackageTest {
      def main(args: Array[String]) {
        val p1 = new TestPackage01
        val p2 = new TestPackage02
      }
    }

  }
}

