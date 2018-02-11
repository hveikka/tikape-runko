/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

/**
 *
 * @author anitapenttila
 */
public class AnnosRaakaAine {
    private Annos annos;
    private RaakaAine raakaAine;
    private String jarjestys;
    private String maara;
    private String ohje;
    
    public AnnosRaakaAine (Annos annos, RaakaAine raakaAine, String jarjestys,
            String maara, String ohje) {
        
        this.annos = annos;
        this.raakaAine = raakaAine;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
    }
    
    public Annos getAnnos() {
        return this.annos;
    }
    
    public RaakaAine getRaakaAine() {
        return this.raakaAine;
    }
    
    public String getJarjestys() {
        return this.jarjestys;
    }
    
    public String getMaara() {
        return this.jarjestys;
    }
    
    public String getOhje() {
        return this.ohje;
    }
    
    public void setAnnos(Annos annos) {
        this.annos = annos;
    }
    
    public void setRaakaAine(RaakaAine raakaAine) {
        this.raakaAine = raakaAine;
    }
    
    public void setJarjestys(String jarjestys) {
        this.jarjestys = jarjestys;
    }
    
    public void setMaara(String maara) {
        this.maara= maara;
    }
    
    public void setOhje(String ohje) {
        this.ohje = ohje;
    }
}
