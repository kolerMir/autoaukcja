package ai.makeitright.utilities.crawler;

import ai.makeitright.utilities.DriverConfig;
import ai.makeitright.utilities.pageobjects.SearchResultsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;

public class Crawler extends DriverConfig {

    static SearchResultsPage searchResultsPage = PageFactory.initElements(driver, SearchResultsPage.class);


    public static ArrayList<String> crawl() throws InterruptedException {
        String uri = System.getProperty("inputParameters.startPage");
        driver.navigate().to(uri);
        ArrayList<String> arrayListOfPageSources = new ArrayList<>();
        Thread.sleep(3000);
        arrayListOfPageSources.add(driver.getPageSource());
        while ((searchResultsPage.ifNextSubpageArrowEnabledExists())) {
            searchResultsPage.clickNextSubpageArrow();
            Thread.sleep(1000);
            arrayListOfPageSources.add(driver.getPageSource());
        }
        return arrayListOfPageSources;
    }

    public static void logIn() throws InterruptedException {
        driver.navigate().to("https://www.autoaukcja.com/Account/Login");
        driver.findElement(By.cssSelector("#IdLoginTb")).sendKeys(System.getProperty("inputParameters.user"));
        driver.findElement(By.cssSelector("#IdPasswordTb")).sendKeys(System.getProperty("inputParameters.password"));
        WebElement submitButton = driver.findElement(By.cssSelector("#button-submit"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        Thread.sleep(100);

        submitButton.click();
    }

}