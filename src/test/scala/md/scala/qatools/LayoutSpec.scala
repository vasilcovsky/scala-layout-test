package md.scala.qatools

import org.scalatest._
import org.scalatest.selenium._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver

class LayoutSpec extends FlatSpec with ShouldMatchers with WebBrowser with LayoutShooter {

  implicit val webDriver: WebDriver = new FirefoxDriver

  "Page Layout" should "be different" in {
  	go to "http://twitter.com/vasilcovsky"
  	val shot1 = layoutScreenshot

    go to "https://twitter.com/juventusfcen"
    val shot2 = layoutScreenshot

    val diff = DiffImage.diff(shot1, shot2)
    assert(diff.diffPixels > 0)

  	//val output = new java.io.File("output.png")
  	//javax.imageio.ImageIO.write(diff.image, "png", output)
  }
}