package cat.gencat.springbootdemo.web.ui;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.IntegrationTest;
import cat.gencat.springbootdemo.Application;
import org.springframework.boot.test.SpringApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest
@SpringApplicationConfiguration(classes = Application.class)
public class AutoUIIT extends AbstractBaseUI {

    private static WebDriver driver;
    
    private WebDriverWait wait;

    @Value("${server.url}")
    private String appURL;

    @BeforeClass
    public static void setUp() throws Exception{
        driver = new RemoteWebDriver(getRemoteUrl(), getDesiredCapabilities());
        
    }

    public void verifyUrlRegisterTittle() {

        wait = new WebDriverWait(driver, 15);
        driver.get(appURL);

        doClick("//*[@id=\"navbar-collapse\"]/ul/li[2]/a", wait);
        doClick("//*[@id=\"navbar-collapse\"]/ul/li[2]/ul/li[2]/a/span[2]", wait);

        assertThat(driver.getCurrentUrl()).isEqualTo(appURL+ "/#/register");
    }

    public void verifyTextLanguage() {

        wait = new WebDriverWait(driver, 25);

        doClick("//*[@id=\"navbar-collapse\"]/ul/li[3]/a", wait);
        doClick("//*[@id=\"navbar-collapse\"]/ul/li[3]/ul/li[2]/a", wait);
        assertThat(driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/div/h1")).getText()).isEqualTo( "Registre");
    }

    public void doClick(String xpath, WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        driver.findElement(By.xpath(xpath)).click();

    }

    @AfterClass
    public static void tearDown(){
        driver.quit();
    }

}
