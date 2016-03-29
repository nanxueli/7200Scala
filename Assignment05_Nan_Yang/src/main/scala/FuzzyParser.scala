package edu.neu.coe.scala.parse

import scala.util.parsing.combinator._
import edu.neu.coe.scala.numerics.Fuzzy

/**
 * @author scalaprof
 */
class FuzzyParser extends JavaTokenParsers { self =>
  // TODO 35 marks:
  // Implement the following grammar:
  // fuzzy ::= integer fraction? fuzz? exponent?
  // integer ::= """\-?\d+""".r
  // fraction ::= regex("""\.\d*""".r)
  // exponent ::= ("E"|"e") regex("""[+-]?\d+""".r)
  // fuzz ::= "(" regex("""\d+""".r) ")"
  // Note that you will have to transform the exponent and fuzz parsers (using ^^) to give you exactly what is needed by fuzzy,
  // i.e. you need to strip off the "E"|"e" and the parentheses.
  // And note that you will have to transform the fuzzy parser into a Parser[Fuzzy] (using ^^)

    def fuzzy: Parser[Fuzzy] = integer ~ fraction.?  ~ fuzz.? ~ exponent.? ^^ {
      case intR ~ fraR  ~ fuzzR ~ expR => Fuzzy.apply(intR, fraR, fuzzR, expR)
    }
    def integer: Parser[String] = """\-?\d+""".r^^ {_.toInt.toString()}
    def fraction: Parser[String] = """\.\d*""".r
    def exponent: Parser[String] = ("E"|"e")~>"""([+-]?\d+)""".r ^^ {_.toInt.toString()}
    def fuzz: Parser[String] = "("~>"""(\d+)""".r<~")"^^ {_.toString()}
}
