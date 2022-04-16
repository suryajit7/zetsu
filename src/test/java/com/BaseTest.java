package com;

import com.page.GooglePage;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.remote.CapabilityType.*;

public class BaseTest {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected String host;
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static final String APP_URL = "https://www.google.com/";
    public static final String API_URL = "https://reqres.in/";

    protected GooglePage googlePage;


    @BeforeSuite
    public void beforeSuiteTestSetup(){
        sleepUninterruptibly(5, SECONDS); //intentional, as chrome containers take few ms to register to hub
    }


    @BeforeTest
    public void beforeClassTestSetup(ITestContext context) throws MalformedURLException {
        getRemoteDriver(context);
    }

    @BeforeClass
    public void beforeClassSetup(ITestContext context){
        googlePage = new GooglePage(driver.get());
    }


    @AfterSuite
    public void tearDownDriver(ITestContext context) {
        this.driver.get().close();
        if (null != this.driver) {
            this.driver.get().quit();
        }
    }

    private void getRemoteDriver(ITestContext context) throws MalformedURLException {
        MutableCapabilities dc = new ChromeOptions();

        dc.merge(configureChromeOptions());
        dc.setCapability("name", context.getCurrentXmlTest().getName());

        host = System.getenv("HUB_HOST") != null ? System.getenv("HUB_HOST") : "chrome";

        driver.set(new RemoteWebDriver(new URL("http://" + host + ":4444/wd/hub"), dc));

        logger.info("Remote Chrome Driver Started...");

        driver.get().manage().deleteAllCookies();
        driver.get().manage().window().maximize();

        context.setAttribute("WebDriver", driver.get());

        logger.info("Window Size: " + driver.get().manage().window().getSize().getHeight() + "x" + driver.get().manage().window().getSize().getWidth());
    }

    private ChromeOptions configureChromeOptions() {

        Proxy proxy = new Proxy();
        proxy.setAutodetect(true);

        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.setCapability(ELEMENT_SCROLL_BEHAVIOR, true);
        chromeOptions.setCapability(SUPPORTS_ALERTS, true);
        chromeOptions.setCapability(SUPPORTS_JAVASCRIPT, true);
        chromeOptions.setCapability(SUPPORTS_APPLICATION_CACHE, true);
        chromeOptions.setCapability(ACCEPT_SSL_CERTS, true);
        chromeOptions.setCapability(ELEMENT_SCROLL_BEHAVIOR, true);
        chromeOptions.setCapability(PROXY, proxy);
        chromeOptions.setCapability(BROWSER_NAME, "chrome");

        chromeOptions.setCapability("chrome.switches", asList("--disable-extensions"));
        chromeOptions.setExperimentalOption("excludeSwitches", singletonList("enable-automation"));
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--allow-insecure-localhost");
        chromeOptions.addArguments("--no-default-browser-check");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
        chromeOptions.setExperimentalOption("prefs", prefs);

        return chromeOptions;
    }

}
