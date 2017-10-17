package cat.gencat.springbootdemo.web.ui;

import java.net.URL;
import java.net.MalformedURLException;
import org.openqa.selenium.remote.DesiredCapabilities;

abstract class AbstractBaseUI {
	
	protected static DesiredCapabilities getDesiredCapabilities() {
        return DesiredCapabilities.firefox();
    }

    protected static URL getRemoteUrl() throws MalformedURLException {
       // return new URL("http://selenium-selenium-hub.devops:4444/wd/hub");
       return new URL("http://selenium.devops.matxa.es/wd/hub");
    }
}