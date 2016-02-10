package edu.neu.coe.scala.minidatabase

import scala.io.Source
import scala.util._

/**
 * @author scalaprof
 */
object MiniDatabase {
  def load(filename: String) = {
    val src = Source.fromFile(filename)
    val database = src.getLines.toList.map(e => Entry(e.split(",")))
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
  
  def main(args: Array[String]): Unit = {
    println("================Start===============")
    val db = load("/Users/yangnan/workspace/Assignment01/src/edu/neu/coe/scala/minidatabase/minidatabase.csv")
    //print(db)
    db.foreach { e => println(e) }
    if (args.length>0) {
      val db = load(args(0))
      print(db)
    }
  }
}

case class Entry(name: Name, social: Social, dob: Date, height: Height, weight: Int)

case class Height(feet: Int, in: Int) {
  def inches = feet*12+in
}

object Entry {
  def apply(name: String, social: String, dob: String, height: String, weight: String): Entry =
    Entry(Name(name),Social(social),Date(dob),Height(height),weight.toInt)
  def apply(entry: Seq[String]): Entry = apply(entry(0),entry(1),entry(2),entry(3),entry(4))
}

object Height {
  val rHeightOptimize = """^\s*\"?(\d+)\s*(?:ft|\')(\s*(\d+)\s*(?:in|\"))\"*\s*$""".r
  val rHeightFtIn = """^\s*(\d+)\s*(?:ft|\')(\s*(\d+)\s*(?:in|\"))?\s*$""".r
  val rHeightFt = """^\s*(\d+)\s*(?:ft|\')$""".r
  def apply(ft: String, in: String) = new Height(ft.toInt,in.toInt)
  def apply(ft: Int) = new Height(ft,0)
  def apply(height: String): Height = height match {
    case rHeightFt(ft) => Height(ft.toInt)
    case rHeightFtIn(ft,_,in) => Height(ft,in)
    case rHeightOptimize(ft,_,in) => Height(ft,in)
    case _ => throw new IllegalArgumentException(height)
  }
}

case class Name(first: String, middle: String, last: String)

case class Social(area: Int, group: Int, serial: Int)

case class Date(year: Int, month: Int, day: Int)

object Name {
  val rName4 = """^\"(\w+)\s+(\w.*)\s+(\w.*)\s\"\"(\w+)\"\"\s*(\w+)\"$""".r //"Thomas E. P. ""Tom"" Brady"
  val rName3 = """^(\w+)\s+(\w.*)\s+(\w+)$""".r //Ex: John J. Jinklehiemer-Smith
  val rName2 = """^(\w+)\s+(\w+)$""".r          //Ex: David Wade
  val rName1 = """^(\w+)$""".r                  //Ex: Ed
  def apply(name: String): Name = {
    name match {
      //Tom Brady's name in CSV file is not in a regular format. Is that matter?
      case rName4(first, middle1, middle2, nick, last) => Name(first, middle1 + " " + middle2, last)
      case rName3(first, middle, last) => Name(first, middle, last) 
      case rName2(first, last) => Name(first, "", last) 
      case rName1(first) => Name(first,"","")
      case _ => Name("","","")
    }
    
  }
}

object Date {
  val rDate1 = """^(\w+)\s+(\d+)\w\w\s(\d{4})$""".r  //Ex: Jan 1th 2016
  val rDate2 = """^(\d+)\/(\d+)\/(\d+)$""".r         //Ex: 1(Month)/1(Day)/2016
  def apply(year: String, month: String, day: String): Date = Date(applyYear(year),applyMonth(month),applyDay(day))
  
  //*************************Normalize Year's Format*************************
  val rYear1 = """^(\d{4})$""".r //Ex: "1997"
  val rYear2 = """^(\d{2})$""".r //Ex: "01" -> 2001
  def applyYear(year:String) : Int = {
    year match {
      case rYear1(year) => year.toInt
      case rYear2(year) => {
        if(year.toInt < 16) (year.toInt + 2000)
        else (year.toInt + 1900)
      }  
      case _ => 0
    }
  }
  
  //*************************Normalize Month's Format*************************
  val rMonth1 = """^(\w+)$""".r     //Ex: "Jan" or "January"
  val rMonth2 = """^(\d)$""".r      //Ex: "3"(March) or "12" (December)
  def applyMonth(month:String) : Int = {
    month match {
      case rMonth2(month) => month.toInt
      // 3 can match pattern rMonth1, need modify Regex case sequence
      case rMonth1(month) => {
          month.substring(0,3).toLowerCase() match {         
            case "jan" => 1
            case "feb" => 2
            case "mar" => 3
            case "apr" => 4
            case "may" => 5
            case "jun" => 6
            case "jul" => 7
            case "aug" => 8
            case "sep" => 9
            case "oct" => 10
            case "nov" => 11
            case "dec" => 12
            case _ => 0
            }
          }
      case _ => 0
    } 
  }
  
  //*************************Normalize Day's Format*************************
  val rDay1 = """^(\d+)$""".r      //Ex: "3"or "12"
  def applyDay(day:String) : Int = {
    day match {
      case rDay1(day) => day.toInt
      case _ => 0
    }
  }
  
  
  def apply(date: String): Date = {
    date match{
      case rDate1(month,day,year) => apply(year,month,day)
      case rDate2(month,day,year) => apply(year,month,day)
      case _ => {
          println("Birth of Date"+ date + "cannot match pattern.")
          Date(0,0,0)
      }
    }
  }
}

object Social {
  val rSsn = """^(\d{3})\-(\d{2})\-(\d{4})$""".r
  def apply(ssn: String): Social = {    
       ssn match{
       case rSsn(area, group, serial) => Social(area.toInt,group.toInt,serial.toInt)
       case _ => {
          println("SSN"+ ssn + "cannot match pattern.")
          Social(0,0,0)
      }
    }
  }
}
