package md.scala.qatools

import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import org.openqa.selenium.OutputType.BYTES
import org.openqa.selenium.{WebDriver, TakesScreenshot, JavascriptExecutor}

trait LayoutShooter {
	
  lazy val blankScript = io.Source.fromInputStream(getClass.getResourceAsStream("/blank.js")).mkString

  /** Makes page layout screenshot
    *
    * Hide text on webpage, replace images with placeholder and make screenshot
    * 
    * @param webdriver with JavascriptExecutor and TakesScreenshot behaviour
    * @return screenshot of webpage layout
    */
  def layoutScreenshot(implicit webdriver: WebDriver) = {
    webdriver
      .asInstanceOf[JavascriptExecutor]
      .executeScript(blankScript)
    
    val bytes = webdriver
                  .asInstanceOf[TakesScreenshot]
                  .getScreenshotAs(BYTES)
    
		val stream = new ByteArrayInputStream(bytes)

    ImageIO.read(stream)
	}
	
}
