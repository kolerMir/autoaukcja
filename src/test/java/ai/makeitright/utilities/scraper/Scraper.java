package ai.makeitright.utilities.scraper;

import ai.makeitright.utilities.DriverConfig;
import ai.makeitright.utilities.db.AuctionData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static ai.makeitright.utilities.db.AuctionData.crateAuctionDataObjectFromJSoupDocument;

public class Scraper extends DriverConfig {

    public static ArrayList<String> scrapeUrlsOfAuctions(ArrayList<String> arrayListOfPageSources) {
        List<String> hrefsOfAllAuctions = scrapePartialLinksToAuctionsDetials(arrayListOfPageSources, "a.offerLink");
        ArrayList<String> urlsOfSpecificAuctions = new ArrayList<>();
        for (String hrefOfAuction : hrefsOfAllAuctions) {
            urlsOfSpecificAuctions.add("https://www.autoaukcja.com" + hrefOfAuction);
        }
        return urlsOfSpecificAuctions;
    }

    public static ArrayList<AuctionData> scrapeAuctions(final ArrayList<String> urlsOfAllAuctions) throws ParseException {
        System.out.println("------------------------------------------------------");
        System.out.println("Quantity of urls of all auctions: " + urlsOfAllAuctions.size());
        System.out.println("");
        ArrayList<AuctionData> auctionDatas = new ArrayList<>();
        int i = 1;
        for (String urlOfAuction : urlsOfAllAuctions) {
            System.out.println("Downloading url " + i + "/" + urlsOfAllAuctions.size() + ": " + urlOfAuction);
            driver.navigate().to(urlOfAuction);
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("small.copyright")));
            if (driver.findElements(By.xpath("//h2[contains(text(),'nieoczekiwany b')]")).isEmpty() &&
                    driver.findElements(By.xpath("//h2[contains(text(),'Nie posiadasz wystarczaj')]")).isEmpty()) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.listing-additional-info")));
                Document document = Jsoup.parse(driver.getPageSource());
                AuctionData ad = crateAuctionDataObjectFromJSoupDocument(document, urlOfAuction);
                auctionDatas.add(ad);
            }
            i++;
        }
        return auctionDatas;
    }

    private static List<String> scrapePartialLinksToAuctionsDetials(final ArrayList<String> htmlPagesAsString,
                                                                    final String selectForAElement) {
        List<String> finalListOfPartialLinks = new ArrayList<>();
        for (String htmlPageAsString : htmlPagesAsString) {
            Document parsedHtmlPage = Jsoup.parse(htmlPageAsString);
            Elements rows = parsedHtmlPage.select("h2.listing-title");
            for (Element row : rows) {
                Elements aTags = row.select(selectForAElement);
                List<String> temporarylistOfPartialLinks = aTags.eachAttr("href");
                finalListOfPartialLinks.addAll(temporarylistOfPartialLinks);
            }
        }
        return finalListOfPartialLinks;
    }
}