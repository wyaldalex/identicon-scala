package com.tudux.identicon

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.security.MessageDigest
import scala.io.StdIn.readLine

object Main extends App {
  //Translation from Elixir Identicon to Scala
  case class Image(hex: List[Int], color: List[Int] = List(), grid: List[(Int,Int)] = List(), pixel_map: List[((Int, Int),(Int, Int))] = List())

  def hash_input(input: String) : Image = {
    //do some hashing and get the list
    val hex = MessageDigest.getInstance("MD5")
      .digest(input.getBytes())
      .toList.map(x => x.toInt.abs)
    Image(hex)
  }

  def pick_color(image: Image): Image = {
    val color = image.hex.take(3)
    image.copy(color = color)
  }

  def build_grid(image: Image): Image = {
    val grid = image.hex.grouped(3)
      .toList
      .filter(x => x.size > 2)
      .map(mirror_row).flatten
      .zipWithIndex

    image.copy(grid = grid)
  }

  def mirror_row(row: List[Int]): List[Int] = {
    row ++ List(row(1), row(0))
  }

  def filter_odd_squares(image: Image): Image = {
    val grid = image.grid.filter(x => x._1 % 2 == 0)
    image.copy(grid = grid)
  }

  def build_pixel_map(image: Image): Image =  {
    val pixel_map = image.grid.map(generatePixelTuple)
    image.copy(pixel_map = pixel_map)
  }

  def generatePixelTuple(tup: (Int, Int)): ((Int, Int), (Int, Int)) = {
    val index = tup._2
    val horizontal = (index % 5) * 50
    val vertical = (index / 5) * 50
    val top_left = (horizontal, vertical)
    val bottom_right = (horizontal + 50, vertical + 50)
    (top_left, bottom_right)
  }

  /*
     WARNING: Side effects fn
     TODO: Add side effects handling, cats, slf4j loggin, clean code etc blabla xD
   */

  def generateImage(imageVals: Image, name: String): Unit = {

    val img = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB)
    val color = new Color(imageVals.color(0),imageVals.color(1),imageVals.color(2))

    //val color = 10//imageVals.color(Random.nextInt(2)) // RGBA value, each component in a byte

    imageVals.pixel_map.foreach(
      pixel => {
        for(x <- pixel._1._1 until pixel._2._1) {
          for(y <- pixel._1._2 until pixel._2._2) {
            img.setRGB(x,y,color.getRGB)
          }
        }
        //img.setRGB(pixel._1._1,pixel._1._2,50,50,imageVals.color.toArray,1,1)
      }
    )
//    for (x <- 0 until 500) {
//      for (y <- 20 until 300) {
//        img.setRGB(x, y, col)
//      }
//    }

    import javax.imageio.ImageIO
    val outputfile = new File(s"$name")
    ImageIO.write(img, "PNG", outputfile)
  }

//  def randomImageGenerator(): Unit = {
//    try {
//      val img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
//      val f = new File("MyFile.png")
//      val r = 5
//      val g = 25
//      val b = 255
//      val col = (r << 16) | (g << 8) | b
//      for (x <- 0 until 500) {
//        for (y <- 20 until 300) {
//          img.setRGB(x, y, col)
//        }
//      }
//      ImageIO.write(img, "PNG", f)
//    } catch {
//      case e: Exception =>
//        e.printStackTrace()
//    }
//  }


  //Testing:
  print("Enter some string like a github username: ")
  val user_string = readLine()
  val sampleStr = List(user_string)
      val result = sampleStr
        .map(hash_input)
        .map(pick_color)
        .map(build_grid)
        .map(filter_odd_squares)
        .map(build_pixel_map)

  generateImage(result(0),s"$user_string.png")
  //randomImageGenerator()
  println(s"result ${result}")
//  for( i <- 1 to 10) {
//    val result = sampleStr
//      .map(hash_input)
//      .map(pick_color)
//      .map(build_grid)
//    println(s"Iteration $i with result ${result}")
//  }

}
