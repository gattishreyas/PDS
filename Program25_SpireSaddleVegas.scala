// Program 25 : Fruit Price Statistics with Spire, Saddle and Vegas
// Week 14 | Exercise Program
// Libraries : Spire 0.13.0, Saddle 1.3.4, Vegas 0.3.11 - Scala 2.11

import org.saddle._
import org.saddle.io._
import spire.math.Rational
import vegas._
import java.nio.file.{Files, Paths}

object Program25_SpireSaddleVegas {

  // Step 3: Exact mean and population variance using Spire Rational numbers
  def mean(xs: Seq[Rational]): Rational = xs.reduce(_ + _) / Rational(xs.size)

  def variance(xs: Seq[Rational]): Rational = {
    val m = mean(xs)
    xs.map(x => (x - m) * (x - m)).reduce(_ + _) / Rational(xs.size)
  }

  def main(args: Array[String]): Unit = {

    // Step 1: Load the dataset into a Saddle Frame
    val frame = CsvParser.parse(CsvFile("fruit_prices.csv")).withColIndex(0)
    val date  = frame.firstCol("Date")
    val fruit = frame.firstCol("Fruit")
    val price = frame.firstCol("Price")
    println("Dataset loaded : " + frame.numRows + " rows x " +
      frame.numCols + " columns")

    // Step 2: Group prices by fruit using Saddle filtering
    val fruitTypes = fruit.values.toSeq.distinct.sorted
    println("Fruit types    : " + fruitTypes.mkString(", "))

    // Step 3 & 4: Mean and variance of prices for each fruit using Spire
    println("\nPrice statistics per fruit " +
      "(computed with exact Rational arithmetic):")
    println(f"${"Fruit"}%-8s ${"Count"}%5s ${"Mean (Rs.)"}%11s " +
      f"${"Variance"}%10s ${"Std Dev"}%8s")
    for (f <- fruitTypes) {
      val ix     = fruit.filter(_ == f).index
      val prices = price.reindex(ix).values.toSeq
        .map(p => Rational(BigDecimal(p)))
      val m      = mean(prices)
      val v      = variance(prices)
      println(f"$f%-8s ${prices.size}%5d ${m.toDouble}%11.3f " +
          f"${v.toDouble}%10.4f ${math.sqrt(v.toDouble)}%8.3f")
    }

    // Step 5: Exact rational form for Apple (no floating point rounding)
    val appleIx  = fruit.filter(_ == "Apple").index
    val appleRat = price.reindex(appleIx).values.toSeq
      .map(p => Rational(BigDecimal(p)))
    println("\nExact Apple mean     (Rational) = " + mean(appleRat))
    println("Exact Apple variance (Rational) = " + variance(appleRat))

    // Step 6: Visualize the price trend of every fruit over time using Vegas
    val plotData = (0 until frame.numRows).map { i =>
      Map("Date"  -> date.values.raw(i),
          "Fruit" -> fruit.values.raw(i),
          "Price" -> price.values.raw(i).toDouble)
    }
    val plot = Vegas("Fruit Price Trends Over Time",
      width = 600.0, height = 320.0)
      .withData(plotData)
      .encodeX("Date", Temporal,
        axis = vegas.spec.Spec.Axis(labelAngle = Some(0.0),   // short dates
                                    format = Some("%b %d"))) // e.g. Aug 09
      .encodeY("Price", Quantitative)
      .encodeColor("Fruit", Nominal)
      .mark(Line)
      .configOverlay(line = true)                          // dot on every point

    // Vega-Lite 1 has no chart title, so add one as a heading on the page
    val html = plot.html.pageHTML().replaceFirst("<body>",
      "<body><h3>Fruit Price Trends Over Time</h3>")
    Files.write(Paths.get("fruit_price_trends.html"), html.getBytes("UTF-8"))
    println("\nInteractive Vegas chart saved as fruit_price_trends.html")
  }
}

