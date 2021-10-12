package ai.makeitright.utilities;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverConfig {

    public static WebDriver driver;

    @BeforeClass
    public static void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1400,600");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--single-process");
        options.addArguments("--use-gl=swiftshader");
        options.addArguments("--no-zygote");
        options.addArguments("--blink-settings=imagesEnabled=false");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterClass
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

}