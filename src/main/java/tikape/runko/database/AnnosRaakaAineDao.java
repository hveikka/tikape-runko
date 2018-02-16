/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tikape.runko.domain.Annos;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;

/**
 *
 * @author anitapenttila
 */
public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {
    private Database database;
    private AnnosDao annosDao;
    private RaakaAineDao raakaAineDao;
    
    public AnnosRaakaAineDao(Database database, AnnosDao annosDao, RaakaAineDao raakaAineDao) {
        this.database = database;
        this.annosDao = annosDao;
        this.raakaAineDao = raakaAineDao;
    }

    @Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {
        Connection conn = database.getConnection();
        
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine");
        
        List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Integer annosID = rs.getInt("AnnosID");
            Annos annos = annosDao.findOne(annosID);
            Integer raakaAineID = rs.getInt("RaakaAineID");
            RaakaAine raakaAine = raakaAineDao.findOne(raakaAineID);
            String jarjestys = rs.getString("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");
            
            AnnosRaakaAine annosRaakaAine = new AnnosRaakaAine(annos, raakaAine, jarjestys, maara, ohje);
            annosRaakaAineet.add(annosRaakaAine);
        }
        rs.close();
        stmt.close();
        conn.close();
        return annosRaakaAineet;
    }
    
    public List<AnnosRaakaAine> haeAnnoksenRaakaAineet(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE AnnosID = " + key);

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        
        while (rs.next()) {
            Annos annos = annosDao.findOne(key);
            Integer raakaAineID = rs.getInt("RaakaAineID");
            RaakaAine raakaAine = raakaAineDao.findOne(raakaAineID);
            String jarjestys = rs.getString("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");
            
            AnnosRaakaAine annosRaakaAine = new AnnosRaakaAine(annos, raakaAine, jarjestys, maara, ohje);
            annosRaakaAineet.add(annosRaakaAine);
        }
        
        rs.close();
        stmt.close();
        connection.close();

        return annosRaakaAineet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void poistaAnnos(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE AnnosID = " + key);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
    
    public void poistaRaakaAine(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE RaakaAineID = " + key);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
    
    public HashMap raakaAineidenKayttomaarat() throws SQLException {
        HashMap<String, Integer> kayttomaarat = new HashMap<>();
        List<RaakaAine> raakaAineet= raakaAineDao.findAll();
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT (AnnosID) FROM "
                + "AnnosRaakaAine WHERE RaakaAineID = ?");
        
        for (int i = 0; i < raakaAineet.size(); i++) {
            stmt.setInt(1, raakaAineet.get(i).getId());
            
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            kayttomaarat.put(raakaAineet.get(i).getNimi(), rs.getInt(1));   
        }
        
        stmt.close();
        conn.close();
        return kayttomaarat;
    }
    
    public Double annostenKeskimaarainenRaakaAineMaara() throws SQLException {
        List<Annos> annokset= annosDao.findAll();
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT (RaakaAineID) FROM "
                + "AnnosRaakaAine WHERE AnnosID = ?");
        
        int summa = 0;
        for (int i = 0; i < annokset.size(); i++) {
            stmt.setInt(1, annokset.get(i).getId());
            
            ResultSet rs = stmt.executeQuery();
            rs.next();
            summa+=rs.getInt(1);
        }
        stmt.close();
        conn.close();
        
        double keskiarvo = 1.0 * summa / annokset.size();
        
        keskiarvo = Math.round(keskiarvo * 100.0) / 100.0;

        return keskiarvo;
    }
    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine object) throws SQLException {

        return save(object);
    }
    private AnnosRaakaAine save(AnnosRaakaAine annosRaakaAine) throws SQLException {
          
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine "
                + "WHERE AnnosID = ? AND RaakaAineID = ?");
        stmt.setInt(1, annosRaakaAine.getAnnos().getId());
        stmt.setInt(2, annosRaakaAine.getRaakaAine().getId());
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return null;
        }

        stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine"
                + " (AnnosID, RaakaAineID, jarjestys, maara, ohje)"
                + " VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, annosRaakaAine.getAnnos().getId());
        stmt.setInt(2, annosRaakaAine.getRaakaAine().getId());
        stmt.setString(3, annosRaakaAine.getJarjestys());
        stmt.setString(4, annosRaakaAine.getMaara());
        stmt.setString(5, annosRaakaAine.getOhje());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine"
                + " WHERE maara = ? AND AnnosID = ? AND RaakaAineID = ? AND jarjestys = ? AND ohje = ?");
        stmt.setString(1, annosRaakaAine.getMaara());
        stmt.setInt(2, annosRaakaAine.getAnnos().getId());
        stmt.setInt(3, annosRaakaAine.getRaakaAine().getId());
        stmt.setString(4, annosRaakaAine.getJarjestys());
        stmt.setString(5, annosRaakaAine.getOhje());
        

        rs = stmt.executeQuery();
        rs.next(); 
        
        AnnosRaakaAine annosRa = new AnnosRaakaAine(annosRaakaAine.getAnnos(), 
        annosRaakaAine.getRaakaAine(), rs.getString("jarjestys"), rs.getString("maara"), rs.getString("ohje"));


        stmt.close();
        rs.close();

        conn.close();

        return annosRa;
    }
    
}
