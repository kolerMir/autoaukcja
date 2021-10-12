package ai.makeitright.tests;

import ai.makeitright.utilities.DriverConfig;
import ai.makeitright.utilities.db.AuctionData;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import static ai.makeitright.utilities.crawler.Crawler.*;
import static ai.makeitright.utilities.crawler.Crawler.logIn;
import static ai.makeitright.utilities.db.DBConnector.*;
import static ai.makeitright.utilities.scraper.Scraper.scrapeAuctions;
import static ai.makeitright.utilities.scraper.Scraper.scrapeUrlsOfAuctions;
import static ai.makeitright.utilities.xlsx.XlsxCreator.convertArrayOfAuctionDatasToExcelFile;


public class AutoAukcja extends DriverConfig {


    @Test
    public void doTest() throws InterruptedException, IOException, SQLException, ParseException {
        boolean scrapeAllAuctions = Boolean.parseBoolean(System.getProperty("inputParameters.scrapeAllAuctions"));
        boolean scrapeFavouritedAuctions = Boolean.parseBoolean(System.getProperty("inputParameters.scrapeFavouritedAuctions"));
        boolean scrapeAuctionsFromInputParameter = Boolean.parseBoolean(System.getProperty("inputParameters.scrapeAuctionsFromInputParameter"));

        long start = System.currentTimeMillis();

        ArrayList<String> urlsOfAuctionsToScrape = new ArrayList<>();
        logIn();
        if (scrapeAllAuctions) {
            ArrayList<String> pagesWithSearchResults = crawl();
            urlsOfAuctionsToScrape = scrapeUrlsOfAuctions(pagesWithSearchResults);
        }
        if (scrapeFavouritedAuctions) {
            urlsOfAuctionsToScrape = getAuctionsFromDbToUpdate();
        }
        if (scrapeAuctionsFromInputParameter) {
            urlsOfAuctionsToScrape = getAuctionsFromInputParameter();
        }
        ArrayList<AuctionData> auctionDatas = scrapeAuctions(urlsOfAuctionsToScrape);
        convertArrayOfAuctionDatasToExcelFile(auctionDatas, System.getProperty("inputParameters.title"));
        sendScrappedAuctionDatas(auctionDatas);
        long stop = System.currentTimeMillis();
        System.out.println("Czas:  " + (stop - start));
    }
}