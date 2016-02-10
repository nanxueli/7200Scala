package edu.neu.coe.scala.minidatabase2

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
  
  def map2[A,B,C](a: Try[A], b: Try[B])(f: (A,B) => C): Try[C] = {
    for{
        aa <- a
        bb <- b
        }yield f(aa,bb)
  }
  
  /*
   * 
   * def map2leftRight[A, B, C, S](a: Either[S,A], b: Either[S,B])(fr: (A, B) => C)(fl: (S, S) => S): Either[S,C] =
      (a,b) match {
        case (Left(aa),Left(bb)) => Left(fl(aa,bb))
        case _ => map2right(a,b)(fr)
}
   * */

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
  val rHeightFtIn = """^\s*(\d+)\s*(?:ft|\')(\s*(\d+)\s*(?:in|\"))?\s*$""".r
  def parse(ft: String, in: String): Try[Height] = {
    val ft_try = Try(ft.toInt)
    val in_try = Try(in.toInt)
    MiniDatabase2.map2(ft_try,in_try)(Height.apply _)    
  }
  def parse(height: String): Try[Height] = height match {
    case rHeightFtIn(ft,_,in) => Height.parse(ft,in)
    case _ => Failure(new IllegalArgumentException(height)) // Better to not throw an exception
  }
}

case class Name(first: String, middle: String, last: String)

object Name {
  val rName="""^(\w+)\s+((.*)\s+)?(\w+)$""".r
  def parse(name: String): Try[Name] = name match{
    case rName(first,_,middle,last) => Success(Name(first,middle,last))
    case rName(first,last) => Success(Name(first,"",last))
    case _ => Failure(new IllegalArgumentException(name)) // Better to not throw an exception
  }
}