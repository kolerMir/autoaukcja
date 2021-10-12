package ai.makeitright.utilities.pageobjects;

import ai.makeitright.utilities.DriverConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchResultsPage extends DriverConfig {


    @FindBy(css = "li.paginationRightArrow:not(.disabled) a[rel='next']")
    public WebElement nextSubpageArrow;

    public SearchResultsPage clickNextSubpageArrow() {
        nextSubpageArrow.click();
        return this;
    }

    public boolean ifNextSubpageArrowEnabledExists() {
        boolean x = false;
        if (!driver.findElements(By.cssSelector("li.paginationRightArrow:not(.disabled) a[rel='next']")).isEmpty()) {
            x = true;
        }
        return x;
    }
}