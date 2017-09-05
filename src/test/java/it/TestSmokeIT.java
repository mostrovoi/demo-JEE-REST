package loadtesting;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ListenerTest.class)			

public class TestSmokeIT {

    private WebDriver driver;
    String appURL = "http://bookstore.dev.matxa.es";

    @BeforeClass
    public void testSetUp() throws MalformedURLException {

        DesiredCapabilities capability = DesiredCapabilities.firefox();
        capability.setBrowserName("firefox");
        
        driver = new RemoteWebDriver(new URL("http://selenium.devops.matxa.es/wd/hub"), capability);
    }

    @Test
    public void verifySpringBootDemoTittle() {
        
        driver.get(appURL);
        
        Assert.assertEquals(driver.getTitle(), "springbootdemo");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
       
    }

}
