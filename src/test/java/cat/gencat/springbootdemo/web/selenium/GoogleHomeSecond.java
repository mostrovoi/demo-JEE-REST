package cat.gencat.springbootdemo.web.selenium;



import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(cat.gencat.springbootdemo.web.selenium.ListenerTest.class)

public class GoogleHomeSecond {

    private WebDriver driver;
    String appURL = "http://google.com";

    @BeforeClass
    public void testSetUp() throws MalformedURLException {

        //driver = new FirefoxDriver();

        DesiredCapabilities capability = DesiredCapabilities.firefox();
        // Say you want a specific node to thread your request, just specify the node name (it must be running a selenium configuration though)
        capability.setBrowserName("firefox");
        
        driver = new RemoteWebDriver(new URL("http://selenium-hub:4444/wd/hub"), capability);

    }

    @Test(description="Launches the google site")
    public void verifySeconTestExample() {
        //driver.navigate().to(appURL);
        //String getTitle = driver.getTitle();
        //Assert.assertEquals(getTitle, "Google");

        driver.get("http://www.google.es");
        Assert.assertEquals(driver.getTitle(), "Google");
		driver.get("https://es.yahoo.com");
        Assert.assertEquals(driver.getTitle(), "Yahoo");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}