Name: Assignment02_Nan_Yang

Author: Nan Yang

NUID: 001779924

Instruction: Open Console - cd to the root folder of project - input "sbt test"

Description: This assignment is designed to test your understanding of most of what we've talked about so far. You will probably find it quite a bit harder than Assignment 1. However, there is nothing here that you don't know how to do already.
You have been tasked with writing a package which provides an immutable (functional-programming-style) random number generator for various types of value. The trait which these classes will extend looks like this:
trait RNG[+A] {
  def next: RNG[A]
  def value: A
}

In particular, you are required to create three concrete random number generators for the following types:
•	LongRNG: based on A=Long
•	UniformDoubleRNG: based on A=UniformDouble
•	GaussianRNG: based on A=(Double,Double)
There are only the two methods: next and value. Note that RNG is covariant in A so that RNG[Int] would be a subtype of RNG[AnyVal] because Int is a subtype of AnyVal. However, in practice, you won't have to worry about this aspect of the definition.
So, method next must return the next RNG in the pseudo-random sequence, while value returns the actual value (of type A) for this RNG instance.

In order to make life a little simpler for you, I have provided an abstract base class called RNG_Java[+A] which extends RNG[A] and provides the common functionality for wrapping a Java Random instance to give us the current state of the RNG. By default, we will get the next RNG by using the nextLong value as a seed.

Essentially, what has happened is that your boss has done the system architecture for the package but, being a busy person, has asked you to flesh out some of the details in the methods. The source code that he has provided is in RNG.scala. Everywhere that you see ??? is a place that you must actually implement working code. You will then test your code by running the specification RNGSpec.scala, using ScalaTest (there's one place where you have to supply code here too). You can do this in your IDE or you can simply type sbt test in your shell (assuming you are in the project directory). Don't forget that I will want to see the output of the test run.
The sources (RNG.scala and RNGSpec.scala) can be found under the class repo (in FunctionalProgramming project: src/main and src/test directories respectively). Please be sure to pull the latest.
There are a few things which will require some explanation:
•	Part of the (intentional) complexity of this problem arises from the requirement of providing a set of normally distributed values. If you follow the link Generating values from Normal Distribution (Box-Muller method), you will find that in order to achieve this, you actually have to generate pairs of Doubles. And that pair of Doubles has to derive from a pair of Doubles which are uniformly distributed on 0..1 Don't worry too much about the math but still, because I want you to see what's going on and get a taste of math programming in Scala, I have left it up to you to flesh out the value method.
•	Note also that because what we really want to be useful is not a stream of RNG objects but a stream of values, we provide, in addition to the method  rngs, the methods values, and values2. You have to implement these.
•	In RNGSpec.scala, there is some rather ugly code where we define two sum and mean methods. We're not quite at the point where we can do these generically. But we will.
•	The two places where you need to write code in RNGSpec.scala both involve using the method reduceLeft. We haven't covered this yet, but it is yet another standard method on list-type collections and which takes a function that adds a value to a current total value. The latter is initialized to 0. Just looking at the method signature, try to figure out how to use it. Hint: it's not dissimilar to the use of foldLeft which occurs a couple of lines below in RNGSpec.
Points schedule (total 45)
•	LongRNG: 3+2
•	DoubleRNG: 3+3
•	UniformDoubleRNG: 1
•	GaussianRNG: 1+2+5
•	RNG.rngs: 4
•	RNG.values: 4
•	RNG.values2: 7
RNGSpec: 4,6
