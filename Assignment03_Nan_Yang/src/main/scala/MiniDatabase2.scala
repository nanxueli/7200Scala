//package edu.neu.coe.scala.minidatabase2

import scala.io.Source
import scala.util._

/**
 * @author scalaprof
 */
object MiniDatabase2 extends App {
  def load(filename: String) = {
    val src = Source.fromFile(filename)
    val database = src.getLines.toList.map(e => Entry.parse(e.split(",")))
    val result = database.toSeq
    src.close
    result
  }
  
  def measure(height: Height) = height match {
    case Height(8,_) => "giant"
    case Height(7,_) => "very tall"
    case Height(6,_) => "tall"
    case Height(5,_) => "normal"
    case Height(_,_) => "short"
  }
  
  def map2[A,B,C](a: Try[A], b: Try[B])(f: (A,B) => C): Try[C] = (a,b) match{
    case (Success(a), Success(b)) => Try(f(a,b))
    case _ => throw new Exception()
  }// When a & b are both fromat corrected, than create the Entry

  if (args.length>0) {
    val db = load(args(0))
    print(db)
  }
}

case class Entry(name: Name, height: Height)

case class Height(feet: Int, in: Int) {
  def inches = feet*12+in
}

object Entry {
  def parse(name: Try[Name],height: Try[Height]) = MiniDatabase2.map2(name,height)(Entry.apply _)
  def parse(name: String, height: String): Try[Entry] =
    Entry.parse(Name.parse(name),Height.parse(height))
  def parse(entry: Seq[String]): Try[Entry] = parse(entry(0),entry(3))
}

object Height {
  val rHeightOptimize = """^\s*\"?(\d+)\s*(?:ft|\')(\s*(\d+)\s*(?:in|\"))\"*\s*$""".r
  val rHeightFtIn = """^\s*(\d+)\s*(?:ft|\')(\s*(\d+)\s*(?:in|\"))?\s*$""".r
  //val rHeightFt = """^\s*(\d+)\s*(?:ft|\')$""".r
  def parse(ft: String, in: String): Try[Height] = Try(new Height(ft.toInt,in.toInt))
  def parse(height: String): Try[Height] = height match {
    //case rHeightFt(ft) => Try(Height(ft.toInt,0))
    case rHeightFtIn(ft,_,in) => Try(Height(ft.toInt,in.toInt))
    case rHeightOptimize(ft,_,in) => Try(Height(ft.toInt,in.toInt))
    case _ => throw new IllegalArgumentException(height)
  }//Same as Assignment 1, except 6' should not be parsed
}

case class Name(first: String, middle: String, last: String)

object Name {
  val rName="""^(\w+)\s+((.*)\s+)?(\w+)$""".r
  
  val rName4 = """^\"(\w+)\s+(\w.*)\s+(\w.*)\s\"\"(\w+)\"\"\s*(\w+)\"$""".r //"Thomas E. P. ""Tom"" Brady"
  val rName3 = """^(\w+)\s+(\w.*)\s+(\w+)$""".r //Ex: John J. Jinklehiemer-Smith
  val rName2 = """^(\w+)\s+(\w+)$""".r          //Ex: David Wade
  val rName1 = """^(\w+)$""".r                  //Ex: Ed
  
  def parse(name: String): Try[Name] = name match {
      //Tom Brady's name in CSV file is not in a regular format. Is that matter?
      case rName4(first, middle1, middle2, nick, last) => Try(Name(first, middle1 + " " + middle2, last))
      case rName3(first, middle, last) => Try(Name(first, middle, last))
      case rName2(first, last) => Try(Name(first, "", last))
      case rName1(first) => Try(Name(first,"",""))
      case _ => Try(Name("","",""))
    } //Same as Assignment 1


    
}