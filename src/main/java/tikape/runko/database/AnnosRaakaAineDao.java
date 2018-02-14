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
        
        while(rs.next()) {
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

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
