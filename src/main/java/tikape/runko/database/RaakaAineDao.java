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
import java.util.List;
import tikape.runko.domain.RaakaAine;

/**
 *
 * @author anitapenttila
 */
public class RaakaAineDao implements Dao<RaakaAine, Integer> {
    
    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        RaakaAine raakaAine = new RaakaAine(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return raakaAine;
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (rs.next()) {
            
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            raakaAineet.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return raakaAineet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {

        if (object.getId()== null) {
            return save(object);
        } else {
            return update(object);
        }
    }
    private RaakaAine save(RaakaAine raakaAine) throws SQLException {
        
        String nimi = raakaAine.getNimi();
        nimi = siistiMerkkijono(nimi.trim());
        raakaAine.setNimi(nimi);
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine"
                + " WHERE nimi = ?");
        stmt.setString(1, raakaAine.getNimi());

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            stmt.close();
            return null;
        }
        stmt.close();
        
        stmt = conn.prepareStatement("INSERT INTO RaakaAine"
                + " (nimi)"
                + " VALUES (?)");
        stmt.setString(1, raakaAine.getNimi());

        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement("SELECT * FROM RaakaAine"
                + " WHERE nimi = ?");
        stmt.setString(1, raakaAine.getNimi());

        rs = stmt.executeQuery();
        rs.next(); 

        RaakaAine ra = new RaakaAine(rs.getInt("id"), rs.getString("nimi"));

        stmt.close();
        rs.close();

        conn.close();

        return ra;
    }

    private RaakaAine update(RaakaAine raakaAine) throws SQLException {

        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE RaakaAine SET"
                + " nimi = ? WHERE id = ?");
        stmt.setString(1, raakaAine.getNimi());
        stmt.setInt(2, raakaAine.getId());

        stmt.executeUpdate();

        stmt.close();
        conn.close();

        return raakaAine;
    }
    
    public RaakaAine findByName(String key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE nimi = ?");
        stmt.setString(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        RaakaAine ra = new RaakaAine(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return ra;
    }
    
    private String siistiMerkkijono (String merkkijono) {
        if (merkkijono.length() == 0) {
            return "";
        }
        if (merkkijono.length() == 1) {
            return merkkijono.toUpperCase();
        }

        return merkkijono.substring(0,1).toUpperCase() + merkkijono.substring(1).toLowerCase();
    }
    
}
