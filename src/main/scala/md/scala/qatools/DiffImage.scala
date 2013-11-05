package md.scala.qatools

import java.lang.Math.max
import java.awt.Color
import java.awt.image.BufferedImage

case class DiffImage(diffPixels: Long, image: BufferedImage)

object DiffImage {

  final val DEVIATION_BARRIER = 20

  implicit def ColorToInt(c: Color) = c.getRGB
  implicit def IntToColor(rgb: Int) = new Color(rgb)

  def diff(origin: BufferedImage, modified: BufferedImage): DiffImage = {

    val width  = max(origin.getWidth, modified.getWidth)
    val height = max(origin.getHeight, modified.getHeight)

    val pixels = for {
          x <- (0 until width).toStream
          y <- (0 until height).toStream
        } yield (pixel(origin, x, y), pixel(modified, x, y), (x, y))

    val diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    var diffPixels: Long = 0

    pixels.foreach{
      _ match {
        case (Some(c1), Some(c2), (x, y)) if deviation(c1, c2) > DEVIATION_BARRIER => {
          diffImage.setRGB(x, y, Color.RED)
          diffPixels += 1
        }
        case (Some(color), Some(_), (x, y)) => diffImage.setRGB(x, y, color)
        case (Some(color), None, (x, y)) => diffImage.setRGB(x, y, color)
        case (None, Some(color), (x, y)) => diffImage.setRGB(x, y, color)
        case (_, _, (x, y)) => diffImage.setRGB(x, y, Color.BLACK)
      }
    }

    DiffImage(diffPixels, diffImage)
  }

  def pixel(image: BufferedImage, x: Int, y: Int): Option[Color] =
    if (x < image.getWidth && y < image.getHeight)
      Some(image.getRGB(x, y))
    else
      None

  def deviation(c1: Color, c2: Color) = {
    def rgb(c: Color) = List(c.getRed, c.getGreen, c.getBlue)
    (rgb(c1), rgb(c2)).zipped map (_ - _) map Math.abs max
  }

}