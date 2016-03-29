Name: Assignment06_Nan_Yang

Author: Nan Yang

NUID: 001779924

Instruction: Open Console - cd to the root folder of project - input "sbt test"

Description: 
I have set Assignment 6. I recommend you all to work on it because it covers some things which we haven't spent much time on: tuples, currying of functions, functional composition, and it implements a reasonably general framework for reading CSV files as a set of tuples.
Here's how your grade for the assignments will work. Each assignment is, nominally worth the same, that's to say assignment grades are normalized to 100. Your best four scores will count fully, and your fifth best score will count one half. Your worst score will not count at all. Therefore, if you do feel that your assignment scores are good and you decide not to work on this assignment, that will be fine. Just so long as you have five assignments to be assessed.
Please note that the build.sbt file has changed so you will need to update your Eclipse/IntelliJ project files. All the files you need are in the class repo.
You will be running two specifications: scala/TrialSpec and scala/parser/ProductStreamSpec.
There are five expressions that you are required to implement (totaling 58 points):
scala/parser/ProductStream.scala:
	•	line 47 (5 points)
	•	line 53 (5 points)
	•	line 81 (14 points)
	•	line 177 (8 points)
	•	line 204 (3 points)
	•	line 205 (7 points)
scala/Trial.scala:
	•	line 30 (11 points)
	•	line 45 (5 points)
The code is well-documented so it should be clear what you have to achieve with your implementations. Additionally, of course, the required behavior is encoded in the specifications. There might also be a blog on this in the next few days.
The ProductStream code does in fact use the Trial code (i.e. they are related) although I have more work to do to make it totally general and flexible.