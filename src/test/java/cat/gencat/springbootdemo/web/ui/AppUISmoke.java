package cat.gencat.springbootdemo.web.ui;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Bean;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.IntegrationTest;
import cat.gencat.springbootdemo.Application;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
/**
 * UI Test class using selenium Grid
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest
@SpringApplicationConfiguration(classes = Application.class)
public class AppUISmoke extends AbstractBaseUI {

    private static WebDriver driver;

    @Value("${server.url}")
    private String appURL;



    public static void setUp() throws Exception{
        driver = new RemoteWebDriver(getRemoteUrl(), getDesiredCapabilities()); 
    }


    public void verifySpringBootDemoTittle() {
        driver.get(appURL);
        assertThat(driver.getTitle()).isEqualTo("springbootdemo");
    }

    public static void tearDown(){
        driver.quit();
    }
}
