package ai.makeitright.utilities.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBConnector {

    public static ArrayList<String> getAuctionsFromDbToUpdate() throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource(System.getProperty("inputParameters.dbo"), System.getProperty("inputParameters.dbuser"), System.getProperty("inputParameters.dbpassword"));
        Dao<Favourites, String> favouritesDataDao = DaoManager.createDao(connectionSource, Favourites.class);
        ArrayList<Favourites> favourites = (ArrayList<Favourites>) favouritesDataDao.queryForAll();
        ArrayList<String> urlsOfFavouritedAuctions = new ArrayList<>();
        for (Favourites favourite : favourites) {
            if (favourite.getZrodlo().equals(System.getProperty("inputParameters.title")) && favourite.isFavourite()) {
                urlsOfFavouritedAuctions.add(favourite.getId());
            }
        }
        return urlsOfFavouritedAuctions;
    }

    public static ArrayList<String> getAuctionsFromInputParameter() throws SQLException {
        ArrayList<String> urlsOfFavouritedAuctions = new ArrayList<>();

        String auctions = System.getProperty("inputParameters.endingAuctions");
        String str[] = auctions.split(";");
        List<String> al = new ArrayList<>();
        al = Arrays.asList(str);
        for (String s : al) {
            urlsOfFavouritedAuctions.add("https://www.autoaukcja.com/Ongoing/" + s);
        }
        return urlsOfFavouritedAuctions;
    }

    public static void sendScrappedAuctionDatas(final ArrayList<AuctionData> auctionDatas) throws SQLException, IOException {
        ConnectionSource connectionSource = new JdbcConnectionSource(System.getProperty("inputParameters.dbo"), System.getProperty("inputParameters.dbuser"), System.getProperty("inputParameters.dbpassword"));
        Dao<AuctionData, String> auctionDataDao = DaoManager.createDao(connectionSource, AuctionData.class);
        for (AuctionData auctionData : auctionDatas) {
            auctionDataDao.createOrUpdate(auctionData);
        }
        connectionSource.close();
    }
}