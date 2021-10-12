package ai.makeitright.utilities.xlsx;

import ai.makeitright.utilities.db.AuctionData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class XlsxCreator {

    public static void convertArrayOfAuctionDatasToExcelFile(ArrayList<AuctionData> auctionDatas, String name) throws
            IOException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        try (XSSFWorkbook ideaGetinWorkbook = new XSSFWorkbook()) {
            XSSFSheet sheet = ideaGetinWorkbook.createSheet(name);

            int rowCount = 0;
            for (AuctionData auctionData : auctionDatas) {
                Row row = sheet.createRow(++rowCount);
                Cell id = row.createCell(0);
                id.setCellValue(auctionData.getId());
                Cell marka = row.createCell(1);
                marka.setCellValue(auctionData.getMarka());
                Cell model = row.createCell(2);
                model.setCellValue(auctionData.getModel());
                Cell rokProdukcji = row.createCell(3);
                rokProdukcji.setCellValue(auctionData.getRokProdukcji());
                Cell numerRejestracyjny = row.createCell(4);
                numerRejestracyjny.setCellValue(auctionData.getNumerRejestracyjny());
                Cell vin = row.createCell(5);
                vin.setCellValue(auctionData.getVin());
                Cell rodzajPaliwa = row.createCell(6);
                rodzajPaliwa.setCellValue(auctionData.getRodzajPaliwa());
                Cell klasaEuro = row.createCell(7);
                klasaEuro.setCellValue(auctionData.getKlasaEuro());
                Cell kluczyki = row.createCell(8);
                kluczyki.setCellValue(auctionData.getKluczyki());
                Cell dowodRejestracyjny = row.createCell(9);
                dowodRejestracyjny.setCellValue(auctionData.getDowodRejestracyjny());
                Cell kartaPojazdu = row.createCell(10);
                kartaPojazdu.setCellValue(auctionData.getKartaPojazdu());
                Cell przebieg = row.createCell(11);
                przebieg.setCellValue(auctionData.getPrzebieg());
                Cell cena = row.createCell(12);
                cena.setCellValue(auctionData.getCena());
                Cell pdfUrl = row.createCell(13);
                pdfUrl.setCellValue(auctionData.getPdfUrl());
                Cell wyposazenie = row.createCell(14);
                wyposazenie.setCellValue(auctionData.getWyposazenie());
                Cell dataWyszukania = row.createCell(15);
                dataWyszukania.setCellValue(simpleDateFormat.format(auctionData.getDataWyszukania()));
                Cell doKoncaAukcji = row.createCell(16);
                doKoncaAukcji.setCellValue(simpleDateFormat.format(auctionData.getDoKoncaAukcji()));
                Cell zrodlo = row.createCell(17);
                zrodlo.setCellValue(auctionData.getZrodlo());
                Cell typAukcji = row.createCell(18);
                typAukcji.setCellValue(auctionData.getTypAukcji());
            }
            try (FileOutputStream outStream = new FileOutputStream(System.getProperty("SHARED_ORDER_PATH") + System.getProperty("file.separator") + name + ".xlsx")) {
                ideaGetinWorkbook.write(outStream);
            }
            try (OutputStream outStream = Files.newOutputStream(
                    Paths.get(System.getProperty("ARTIFACTS_PATH") + System.getProperty("file.separator") + name + ".xlsx"))
            ) {
                ideaGetinWorkbook.write(outStream);
            }
            System.out.println("same .xlsx files created in shared and artifacts directories");
        }
    }
}