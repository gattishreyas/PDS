// Program 24 : Revenue Analysis with Breeze and Plotly-Scala
// Week 14 | Exercise Program
// Libraries : Breeze 0.13.2 (linear algebra), plotly-scala 0.5.4 - Scala 2.11

import breeze.linalg._
import plotly._, element._, layout._, Plotly._
import scala.io.Source

object Program24_BreezePlotly {

  def main(args: Array[String]): Unit = {

    // Step 1: Read the fruit dataset (skip the header row)
    val src     = Source.fromFile("fruit_prices.csv")
    val records = src.getLines().drop(1).map(_.split(",")).toArray
    src.close()
    val fruit = records.map(_(1))
    println("Records loaded : " + records.length)

    // Step 2: Store Price and Quantity as Breeze DenseVectors
    val price = DenseVector(records.map(_(2).toDouble))
    val qty   = DenseVector(records.map(_(3).toDouble))
    println("Price vector (first 5)    : " +
      price(0 until 5).toArray.map(p => f"$p%.2f").mkString(", "))
    println("Quantity vector (first 5) : " +
      qty(0 until 5).toArray.map(q => f"$q%.0f").mkString(", "))

    // Step 3: Element-wise multiplication -> revenue of every record
    val revenue = price *:* qty
    println("Revenue vector (first 5)  : " +
      revenue(0 until 5).toArray.map(r => f"$r%.2f").mkString(", "))

    // Step 4: Total revenue of each fruit using a 0/1 mask and dot product
    val fruitTypes = fruit.distinct.sorted
    val totals = fruitTypes.map { f =>
      val mask = DenseVector(fruit.map(x => if (x == f) 1.0 else 0.0))
      revenue dot mask
    }
    println("\nTotal Revenue by Fruit Type:")
    println(f"${"Fruit"}%-8s ${"Total Revenue (Rs.)"}%20s")
    fruitTypes.zip(totals).foreach { case (f, t) => println(f"$f%-8s $t%20.2f") }
    println(f"\nOverall revenue (price dot quantity) : Rs. ${price dot qty}%.2f")
    println("Highest revenue fruit : " + fruitTypes(totals.indexOf(totals.max)))

    // Step 5: Create an interactive bar chart with Plotly-Scala
    val bar = Bar(
      fruitTypes.toSeq,
      totals.toSeq,
      name         = "Total Revenue",
      text         = totals.toSeq.map(t => f"Rs. $t%.0f"),
      textposition = BarTextPosition.Auto,          // show values on the bars
      marker       = Marker(color = (Color.RGB(99, 110, 250): Color))
    )
    Seq(bar).plot(
      path          = "fruit_revenue.html",
      title         = "Total Revenue by Fruit Type",
      xaxis         = plotly.layout.Axis(title = "Fruit"),
      yaxis         = plotly.layout.Axis(title = "Total Revenue (Rs.)"),
      plot_bgcolor  = Color.RGB(229, 236, 246),     // light grey-blue plot area
      openInBrowser = false
    )
    println("\nInteractive Plotly chart saved as fruit_revenue.html")
  }
}

