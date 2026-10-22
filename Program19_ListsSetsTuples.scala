object Program19_ListsSetsTuples {

  // Step 2: Function that returns a new list with each element squared
  def squareList(nums: List[Int]): List[Int] = {
    nums.map(n => n * n)
  }

  // Step 4: Function that returns the intersection of two sets
  def intersectSets(a: Set[Char], b: Set[Char]): Set[Char] = {
    a.intersect(b)
  }

  def main(args: Array[String]): Unit = {

    // Step 1: Create a list of the first five prime numbers
    val primes: List[Int] = List(2, 3, 5, 7, 11)
    println("Prime Numbers List : " + primes)

    // Step 2: Square every element of the list
    val squared = squareList(primes)
    println("Squared List       : " + squared)

    // Step 3: Create a set of unique characters from "hello world"
    val text = "hello world"
    val helloSet: Set[Char] = text.filter(_ != ' ').toSet
    println("\nUnique characters of \"" + text + "\" : " +
      helloSet.toList.sorted.mkString("{", ", ", "}"))
    println("Number of unique characters    : " + helloSet.size)

    // Step 4: Intersection of two sets
    val otherSet: Set[Char] = "scala world".filter(_ != ' ').toSet
    println("Second Set (\"scala world\")     : " +
      otherSet.toList.sorted.mkString("{", ", ", "}"))
    val common = intersectSets(helloSet, otherSet)
    println("Intersection of the two sets   : " +
      common.toList.sorted.mkString("{", ", ", "}"))

    // Step 5: Create a tuple with an Int, a String and a Boolean
    val record: (Int, String, Boolean) = (17, "Scala", true)
    println("\nTuple              : " + record)

    // Step 6: Access and print each element of the tuple
    println("First element  (_1): " + record._1 + "  -> type Int")
    println("Second element (_2): " + record._2 + "  -> type String")
    println("Third element  (_3): " + record._3 + "  -> type Boolean")
  }
}

