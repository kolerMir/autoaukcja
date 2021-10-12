package ai.makeitright.utilities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "auctions")
public class AuctionData {
    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String marka;
    @DatabaseField
    private String model;
    @DatabaseField
    private String rokProdukcji;
    @DatabaseField
    private String numerRejestracyjny;
    @DatabaseField
    private String vin;
    @DatabaseField
    private String rodzajPaliwa;
    @DatabaseField
    private String klasaEuro;
    @DatabaseField
    private String kluczyki;
    @DatabaseField
    private String dowodRejestracyjny;
    @DatabaseField
    private String kartaPojazdu;
    @DatabaseField
    private Long przebieg;
    @DatabaseField
    private Long cena;
    @DatabaseField
    private String pdfUrl;
    @DatabaseField
    private String wyposazenie;
    @DatabaseField
    private Timestamp dataWyszukania;
    @DatabaseField
    private Timestamp doKoncaAukcji;
    @DatabaseField
    private String zrodlo;
    @DatabaseField
    private String typAukcji;

    public static AuctionData crateAuctionDataObjectFromJSoupDocument(Document document, String urlOfAuction) throws ParseException {
        Element divAC = document.selectFirst("#auctionArticle");
        AuctionData ad = new AuctionData();
        ad.setId(urlOfAuction.split("&")[0]);
        ad.setMarka(divAC.selectFirst("span.make").ownText());
        ad.setModel(divAC.selectFirst("span.model").ownText());

        ad.setRokProdukcji(divAC.selectFirst("span.year").ownText());
        if (divAC.selectFirst("span.label:contains(Numer rejestracyjny)") != null) {
            ad.setNumerRejestracyjny(divAC.selectFirst("span.label:contains(Numer rejestracyjny)").nextElementSibling().ownText());
        } else {
            ad.setNumerRejestracyjny("");
        }
        ad.setVin(divAC.selectFirst("span.vin").ownText());
        ad.setRodzajPaliwa(divAC.selectFirst("span.fuel").text());
        ad.setKlasaEuro("");
        ad.setKluczyki("");
        ad.setDowodRejestracyjny("");
        ad.setKartaPojazdu("");

        String przebiegWithKm = divAC.selectFirst("span.mileage").text();
        String przebieg = przebiegWithKm.replace(" km", "");
        if (przebieg.equals("no data") || przebieg.equals("Brak danych")) {
            ad.setPrzebieg(0L);
        } else {
            String przebiegWithoutSpaces = przebieg.replaceAll("\\s+", "");
            Integer przebiegInteger = Integer.valueOf(przebiegWithoutSpaces);
            Long przebiegLong = przebiegInteger.longValue();
            ad.setPrzebieg(przebiegLong);
        }

        ad.setCena(Long.valueOf(divAC.selectFirst("#priceTb").attr("placeholder").replaceAll("\\s+", "")));

        Element ekspertyza = divAC.selectFirst("span.label:contains(Niezale)");
        if (ekspertyza != null) {
            Element a = ekspertyza.parent().selectFirst("span a");
            ad.setPdfUrl("https://www.autoaukcja.com" + a.attr("href"));
        }

        StringBuilder sumOfWyposazenie = new StringBuilder();
        Elements listingFeatures = divAC.select("ul.listing-features li");
        if (listingFeatures != null) {
            for (Element feature : listingFeatures) {
                sumOfWyposazenie.append(feature.ownText()).append(" ");
            }
            ad.setWyposazenie(sumOfWyposazenie.toString());
        }

        Timestamp dataWyszukaniaTimestamp = new Timestamp(System.currentTimeMillis());
        ad.setDataWyszukania(dataWyszukaniaTimestamp);

        if (divAC.selectFirst("div.end-of-bid") != null) {
            String dateFromPage = divAC.selectFirst("span.end-date").ownText();
            String timeFromPage = divAC.selectFirst("span.end-time").ownText();
            String[] x = dateFromPage.split("[.]");
            String dd = (String) Array.get(x, 0);
            String mM = (String) Array.get(x, 1);
            String yyyy = (String) Array.get(x, 2);
            String datayyyyMMdd = yyyy + "-" + mM + "-" + dd;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(datayyyyMMdd + " " + timeFromPage);
            Timestamp koniecAukcjiTimestamp = new Timestamp(parsedDate.getTime());
            ad.setDoKoncaAukcji(koniecAukcjiTimestamp);
        } else {
            ad.setDoKoncaAukcji(new Timestamp(253402214400000L));
        }

        ad.setZrodlo(System.getProperty("inputParameters.title"));
        if (divAC.selectFirst("span.badge.circle").ownText().equals("KT")) {
            ad.setTypAukcji("kup teraz");
        }
        else if(divAC.selectFirst("span.badge.circle").ownText().contains("L")){
            ad.setTypAukcji("licytacja");
        } else {
            ad.setTypAukcji("?");
        }
        return ad;
    }
}