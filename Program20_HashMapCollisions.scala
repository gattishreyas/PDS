import scala.collection.mutable

object Program20_HashMapCollisions {

  // Step 3: Merge two HashMaps - on a key conflict keep the higher grade
  def mergeByHigherGrade(m1: mutable.HashMap[String, Int],
                         m2: mutable.HashMap[String, Int])
                         : mutable.HashMap[String, Int] = {
    val merged = mutable.HashMap[String, Int]()
    merged ++= m1
    for ((name, grade) <- m2) {
      merged.get(name) match {
        case Some(existing) => merged(name) = math.max(existing, grade)
        case None           => merged(name) = grade
      }
    }
    merged
  }

  // Step 4: A simple hash table that resolves collisions by chaining
  class ChainedHashMap(val buckets: Int) {
    private val table = Array.fill(buckets)(List[(String, Int)]())

    def bucketIndex(key: String): Int = math.abs(key.length) % buckets

    def put(key: String, value: Int): Unit = {
      val idx = bucketIndex(key)
      table(idx) = table(idx) :+ (key -> value)   // append to the chain
    }

    def get(key: String): List[Int] = {
      table(bucketIndex(key)).filter(_._1 == key).map(_._2)
    }

    def show(): Unit = {
      for (i <- table.indices) {
        val chain =
          if (table(i).isEmpty) "empty"
          else table(i).map { case (k, v) => k + "=" + v }.mkString(" -> ")
        println("Bucket " + i + " : " + chain)
      }
    }
  }

  def printMap(title: String, m: mutable.HashMap[String, Int]): Unit = {
    println(title)
    m.toSeq.sortBy(_._1).foreach { case (k, v) => println(f"  $k%-8s -> $v") }
  }

  def main(args: Array[String]): Unit = {

    // Step 1: Create a HashMap of student names and grades
    val grades = mutable.HashMap[String, Int]()
    grades += ("Naveen" -> 85)
    grades += ("Anil"   -> 78)
    grades += ("Pavan"  -> 92)
    grades += ("Ravi"   -> 67)
    printMap("Initial HashMap:", grades)

    // Step 2: Insert a duplicate key with a different grade
    grades += ("Anil" -> 88)
    printMap("\nAfter inserting duplicate key Anil -> 88 (value overwritten):",
      grades)
    println("Size of HashMap = " + grades.size)

    // Step 3: Merge two HashMaps resolving conflicts by taking the higher grade
    val semester2 = mutable.HashMap(
      "Naveen" -> 91, "Pavan" -> 80, "Kiran" -> 74, "Ravi" -> 70)
    printMap("\nSecond HashMap:", semester2)
    val merged = mergeByHigherGrade(grades, semester2)
    printMap("\nMerged HashMap (higher grade kept):", merged)

    // Step 4: Handle collisions by chaining
    val chained = new ChainedHashMap(4)
    chained.put("Naveen", 85)
    chained.put("Anil", 78)
    chained.put("Pavan", 92)
    chained.put("Ravi", 67)
    chained.put("Kiran", 74)
    chained.put("Anil", 88)
    println("\nChained Hash Table (bucket = name length % 4):")
    chained.show()

    // Step 5: Retrieve all values stored for a key
    println("\nAll grades stored for Anil : " + chained.get("Anil"))
    println("All grades stored for Kiran: " + chained.get("Kiran"))
  }
}

