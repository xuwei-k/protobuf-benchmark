package com.google.protobuf

import org.openjdk.jmh.annotations.Benchmark
import scala.util.Random

object ProtoBench {

  def createSampleData(n: Int): String = {
    val chars = Iterator.continually(Random.nextInt.toChar).filterNot(_.isSurrogate).take(n).toArray
    new String(chars)
  }

  val sampleData: String = createSampleData(1000)

  def javaStd(str: String): Array[Byte] = {
    str.getBytes("UTF-8")
  }

  def proto(str: String): Array[Byte] = {
    val length = Utf8.encodedLength(str) // package private method
    val array = new Array[Byte](length)
    Utf8.encode(str, array, 0, length)
    array
  }

  // test
  (1 to 10).foreach{ _ =>
    val str = createSampleData(100)
    assert(java.util.Arrays.equals(javaStd(str), proto(str)))
  }
}

class ProtoBench {

  @Benchmark
  def javaStd(): Unit = {
    ProtoBench.javaStd(ProtoBench.sampleData)
  }

  @Benchmark
  def proto(): Unit = {
    ProtoBench.proto(ProtoBench.sampleData)
  }

}
