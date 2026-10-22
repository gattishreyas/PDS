// Program 23 : Data Manipulation with Saddle and Visualization with Vegas
// Week 13 | Exercise Program
// Libraries : Saddle 1.3.4 (data frames), Vegas 0.3.11 (Vega-Lite plots)
// Scala version : 2.11

import org.saddle._
import org.saddle.io._
import vegas._
import java.nio.file.{Files, Paths}

object Program23_SaddleVegas {

  def main(args: Array[String]): Unit = {

    // Step 1: Load the dataset into a Saddle Frame (first row = header)
    val frame = CsvParser.parse(CsvFile("fruit_prices.csv")).withColIndex(0)
    println("Dataset loaded : " + frame.numRows + " rows x " +
      frame.numCols + " columns")
    println("Columns        : " + frame.colIx.toSeq.mkString(", "))

    // Step 2: Extract each column as a typed Saddle Series
    val date  = frame.firstCol("Date")
    val fruit = frame.firstCol("Fruit")
    val price = frame.firstCol("Price").mapValues(_.toDouble)
    val qty   = frame.firstCol("Quantity").mapValues(_.toInt)

    println("\nFirst 8 records:")
    println(f"${"Date"}%-12s ${"Fruit"}%-8s ${"Price"}%8s ${"Quantity"}%9s")
    for (i <- 0 until 8)
      println(f"${date.values.raw(i)}%-12s ${fruit.values.raw(i)}%-8s " +
          f"${price.values.raw(i)}%8.2f ${qty.values.raw(i)}%9d")

    // Step 3: Filter the rows that belong to Apple
    val appleIx    = fruit.filter(_ == "Apple").index
    val applePrice = price.reindex(appleIx)
    val appleDate  = date.reindex(appleIx)
    val appleQty   = qty.reindex(appleIx)

    println("\nApple records (filtered):")
    println(f"${"Date"}%-12s ${"Price"}%8s ${"Quantity"}%9s")
    for (i <- 0 until applePrice.length)
      println(f"${appleDate.values.raw(i)}%-12s " +
          f"${applePrice.values.raw(i)}%8.2f ${appleQty.values.raw(i)}%9d")

    // Step 4: Calculate the average price of apples
    val avgPrice = applePrice.values.mean
    println(f"\nNumber of apple records : ${applePrice.length}")
    println(f"Average apple price     : Rs. $avgPrice%.2f per kg")
    println(f"Minimum apple price     : Rs. ${applePrice.values.min.get}%.2f")
    println(f"Maximum apple price     : Rs. ${applePrice.values.max.get}%.2f")

    // Step 5: Visualize the apple price trend over time using Vegas
    val plotData = appleDate.values.toSeq.zip(applePrice.values.toSeq).map {
      case (d, p) => Map("Date" -> d, "Price" -> p)
    }
    val plot = Vegas("Apple Price Trend Over Time", width = 600.0, height = 300.0)
      .withData(plotData)
      .encodeX("Date", Temporal,
        axis = vegas.spec.Spec.Axis(labelAngle = Some(0.0),   // short dates
                                    format = Some("%b %d"))) // e.g. Aug 09
      .encodeY("Price", Quantitative,
        scale = vegas.spec.Spec.Scale(zero = Some(false))) // y from min
      .mark(Line)
      .configOverlay(line = true)                          // dot on every point

    // Vega-Lite 1 has no chart title, so add one as a heading on the page
    val html = plot.html.pageHTML().replaceFirst("<body>",
      "<body><h3>Apple Price Trend Over Time</h3>")
    Files.write(Paths.get("apple_price_trend.html"), html.getBytes("UTF-8"))
    println("\nInteractive Vegas chart saved as apple_price_trend.html")
    // plot.show   // displays the chart directly inside a notebook / window
  }
}

