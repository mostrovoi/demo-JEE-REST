package loadtesting;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(ListenerTest.class)

public class TestAutoTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String appURL = "http://bookstore.dev.matxa.es";


    @BeforeClass
    public void testSetUp() throws MalformedURLException {

        DesiredCapabilities capability = DesiredCapabilities.firefox();
        capability.setBrowserName("firefox");

        driver = new RemoteWebDriver(new URL("http://selenium.devops.matxa.es/wd/hub"), capability);
    }

    @Test(priority = 0)
    public void verifyUrlRegisterTittle() {

        wait = new WebDriverWait(driver, 15);
        driver.get(appURL);

        doClick("//*[@id=\"navbar-collapse\"]/ul/li[2]/a", wait);
        doClick("//*[@id=\"navbar-collapse\"]/ul/li[2]/ul/li[2]/a/span[2]", wait);

        Assert.assertEquals(driver.getCurrentUrl(), "http://bookstore.dev.matxa.es/#/register");
    }

    @Test(priority = 1)
    public void verifyTextLanguage() {

        wait = new WebDriverWait(driver, 25);

        doClick("//*[@id=\"navbar-collapse\"]/ul/li[3]/a", wait);
        doClick("//*[@id=\"navbar-collapse\"]/ul/li[3]/ul/li[2]/a", wait);

        Assert.assertEquals(driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/div/h1")).getText(), "Registre");
    }

    public void doClick(String xpath, WebDriverWait wait) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        driver.findElement(By.xpath(xpath)).click();

    }

    @AfterClass
    public void tearDown() {
        driver.quit();

    }

}
