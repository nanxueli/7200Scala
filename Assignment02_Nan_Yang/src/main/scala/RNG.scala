

import java.util.Random
import scala.Stream

trait RNG[+A] {
  def next: RNG[A]
  def value: A
}

abstract class RNG_Java[+A](n: Long) extends RNG[A] { 
  // must be overridden by sub-classes
  def value: A
  def newRNG(n: Long): RNG[A]
  // may be overridden (if you want to define your own pseudo-random sequence)
  def nextSeed: Long = RNG_Java.nextSeed(n)
  // base method -- not normally overridden
  def next: RNG[A] = newRNG(nextSeed)
  def state = n
}

object RNG_Java {
  def nextSeed(n: Long): Long = new Random(n).nextLong
}

//=====================LongRNG====================
case class LongRNG(n: Long) extends RNG_Java[Long](n) {
  def newRNG(n: Long): RNG[Long] = new LongRNG(n) // generate new Long random number 
  def value = n
}

//====================DoubleRNG===================
case class DoubleRNG(n: Long) extends RNG_Java[Double](n) {
  def newRNG(n: Long) = new DoubleRNG(n)//generate Double random number
  def value = n.toDouble/Long.MaxValue
  override def toString = s"DoubleRNG: $n->$value"
}

/**
 * This class is a random-number-generator for values uniformly distributed in the range 0..1
 */
//=================UniformDoubleRNG===============
case class UniformDoubleRNG(n: Long) extends RNG_Java[UniformDouble](n) {
  def newRNG(n: Long) =  new UniformDoubleRNG(n)    
  // the following, which calls the apply(Double,Unit) method will check that result is in range 0..1
  def value = UniformDouble(math.abs(n.toDouble/Long.MaxValue),Unit)
  override def toString = s"UniformDoubleRNG: $n->$value"
}

/**
 * This class is a random-number-generator for values which are normally distributed with mean: 0 and standard deviation: 1
 * 
 * See <a href="https://en.wikipedia.org/wiki/Normal_distribution#Generating_values_from_normal_distribution">
 * Generating values from normal distribution (Box-Muller method)
 * </a>
// */

//===================GaussianRNG==================
case class GaussianRNG (n: Long) extends RNG_Java[(Double, Double)](n) {
  def newRNG(n: Long) = new GaussianRNG(n)
  var r1 = UniformDoubleRNG(n)
  var r2 = r1.next
  def value: (Double, Double) = {
    var u = 2 * r1.value.x - 1 // between -1.0 and 1.0
    var v = 2 * r2.value.x - 1 // between -1.0 and 1.0
    var w = u * u + v * v
    while ( w > 1 || w == 0) {
      r1 = UniformDoubleRNG(r1.nextSeed)
      r2 = r1.next
      u = 2 * r1.value.x - 1  // between -1.0 and 1.0
      v = 2 * r2.value.x - 1  // between -1.0 and 1.0
      w = u * u + v * v
    }
    //val k = if (v<=0) 0 else math.sqrt(-2*math.log(w)/w)
    val k = math.sqrt(-2*math.log(w)/w)
    (u * k, v * k)
  }
  override def nextSeed: Long = RNG_Java.nextSeed(r2.asInstanceOf[RNG_Java[UniformDoubleRNG]].state)
  override def toString = s"GaussianRNG: $n->(${value._1},${value._2})"
} 

object RNG {
  type SRNG[A] = Stream[RNG[A]]
  def rngs[A](r: RNG[A]): SRNG[A] = Stream.cons(r,rngs(r.next)) 
  def values[A](s: SRNG[A]): Stream[A] = Stream.cons(s.head.value,values(s.tail))
  def values2[A](s: SRNG[(A,A)]): Stream[A] = s flatMap {x=>Stream(x.value._1,x.value._2)}  
}

object LongRNG {
  def apply: RNG[Long] = LongRNG(System.currentTimeMillis())
  def main(args: Array[String]): Unit = {
    val x = LongRNG(0L)
    println(x)
    println(x.next)
  }  
}

/**
 *  This is essentially a wrapper of Double where (implicitly) 0 <= x <= 1.
 *  Note that we would like to specify a require statement but such are not legal in Value types
 */
case class UniformDouble(x: Double) extends AnyVal {
    def + (y: Double) = x + y
}

object UniformDoubleRNG {
  def apply: RNG[UniformDouble] = UniformDoubleRNG(System.currentTimeMillis())
}

object GaussianRNG {
  def apply: RNG[(Double,Double)] = GaussianRNG(System.currentTimeMillis())
}

object UniformDouble {
  def apply(x: Double, y: Unit) = if (x>=0 && x<=1) new UniformDouble(x) else throw new IllegalArgumentException(s"$x is not in range 0..1")
  def + (x: Double, y: UniformDouble) = y+x
}