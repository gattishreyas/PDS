// Program 18 : Arrays in Scala
// Week 9 | Exercise Program

object Program18_ArraysInScala {

  // Step 2: Function that returns the sum of all elements of an integer array
  def sumOfElements(numbers: Array[Int]): Int = {
    var total = 0
    for (n <- numbers) {
      total += n
    }
    total
  }

  // Step 4: Function that concatenates all strings, separated by commas
  def concatenateWithCommas(items: Array[String]): String = {
    items.mkString(", ")
  }

  def main(args: Array[String]): Unit = {

    // Step 1: Create an array of integers
    val numbers: Array[Int] = Array(2, 5, 9, 14, 20)
    println("Integer Array      : " + numbers.mkString("[", ", ", "]"))
    println("Length of Array    : " + numbers.length)

    // Step 2: Sum of the array elements
    val total = sumOfElements(numbers)
    println("Sum of Elements    : " + total)

    // Step 3: Create an array of strings with five fruit names
    val fruits: Array[String] =
      Array("Apple", "Banana", "Mango", "Orange", "Grapes")
    println("\nFruit Array        : " + fruits.mkString("[", ", ", "]"))

    // Step 4: Concatenate fruits separated by commas
    val fruitString = concatenateWithCommas(fruits)
    println("Concatenated String: " + fruitString)

    // Step 5: Iterate over the integer array and print each element
    println("\nElements of the Integer Array:")
    for (i <- numbers.indices) {
      println("numbers(" + i + ") = " + numbers(i))
    }
  }
}

