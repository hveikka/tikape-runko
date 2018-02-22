package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
    
        lista.add("CREATE TABLE Annos (id SERIAL PRIMARY KEY, nimi varchar(50));");
        lista.add("CREATE TABLE RaakaAine (id SERIAL PRIMARY KEY, nimi varchar(50));");
        lista.add("CREATE TABLE AnnosRaakaAine (AnnosID integer, RaakaAineID integer, jarjestys varchar(50), "
                + "maara varchar(30), ohje varchar(100),"
                + " FOREIGN KEY (AnnosID) REFERENCES Annos(id), "
                + "FOREIGN KEY (RaakaAineID) REFERENCES RaakaAine(id));");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Strawberry Banana');");
        lista.add("INSERT INTO Annos (nimi) VALUES ('Tropical Island Smoothie');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Mansikka');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Banaani');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Maito');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Kiivi');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('Appelsiini');");
        
        lista.add("INSERT INTO AnnosRaakaAine (AnnosID, RaakaAineID, maara, ohje) VALUES (1, 1, '3 dl', 'lisää jäisinä');");
        lista.add("INSERT INTO AnnosRaakaAine (AnnosID, RaakaAineID, maara) VALUES (1, 2, '2 kpl');");
        lista.add("INSERT INTO AnnosRaakaAine (AnnosID, RaakaAineID, maara) VALUES (1, 3, '2 dl');");
        lista.add("INSERT INTO AnnosRaakaAine (AnnosID, RaakaAineID, maara) VALUES (2, 4, '2 kpl');");
        lista.add("INSERT INTO AnnosRaakaAine (AnnosID, RaakaAineID, maara) VALUES (2, 5, '2 kpl');");
        lista.add("INSERT INTO AnnosRaakaAine (AnnosID, RaakaAineID, maara) VALUES (2, 2, '1 kpl');");
        lista.add("INSERT INTO AnnosRaakaAine (AnnosID, RaakaAineID, maara) VALUES (2, 3, '3 dl');");
        

        return lista;
    }
}
