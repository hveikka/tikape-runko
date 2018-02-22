package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Annos;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:annokset.db");
        database.init();
        
        AnnosDao annosDao = new AnnosDao(database);
        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database, annosDao, raakaAineDao);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/annokset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annokset", annosDao.findAll());
            map.put("raakaAineet", raakaAineDao.findAll());

            return new ModelAndView(map, "annokset");
        }, new ThymeleafTemplateEngine());
        
        get("/annokset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("annos", annosDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("raakaAineet", annosRaakaAineDao.haeAnnoksenRaakaAineet(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "annos");
        }, new ThymeleafTemplateEngine());
        
        get("/raakaaineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAineDao.findAll());

            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());
        
        get("/raakaaineet/tilastoja", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttomaarat", annosRaakaAineDao.raakaAineidenKayttomaarat());
            map.put("annosLkm", Integer.toString(annosDao.findAll().size()));
            map.put("raakaAineLkm", Integer.toString(raakaAineDao.findAll().size()));
            map.put("keskiarvo", annosRaakaAineDao.annostenKeskimaarainenRaakaAineMaara());

            return new ModelAndView(map, "tilastoja");
        }, new ThymeleafTemplateEngine());
        
        
        
        post("/annokset", (req, res) -> {
            if (req.queryParams("nimi").isEmpty()) {
                res.redirect("/annokset");
                return "";
            }
            annosDao.saveOrUpdate(new Annos(null, req.queryParams("nimi")));
            res.redirect("/annokset");
            return "";
        });

        post("/raakaaineet", (req, res) -> {
            if (req.queryParams("nimi").isEmpty()) {
                res.redirect("/raakaaineet");
                return "";
            }
            raakaAineDao.saveOrUpdate(new RaakaAine(null, req.queryParams("nimi")));
            res.redirect("/raakaaineet");
            return "";
        });
        
        post("/raakaaineet/:id/poista", (req, res)-> {
            raakaAineDao.delete(Integer.parseInt(req.params(":id")));
            annosRaakaAineDao.poistaRaakaAine(Integer.parseInt(req.params(":id")));
            res.redirect("/raakaaineet");
            return "";
        });
        
        post("/annokset/:id/poista", (req, res)-> {
            annosDao.delete(Integer.parseInt(req.params(":id")));
            annosRaakaAineDao.poistaAnnos(Integer.parseInt(req.params(":id")));
            res.redirect("/annokset");
            return "";
        });
        
        post("/annokset/raakaAine", (req, res) -> {
            Annos a = annosDao.findByName(req.queryParams("annos"));
            RaakaAine ra = raakaAineDao.findByName(req.queryParams("raakaAine"));
            if (a == null || ra == null) {
                res.redirect("/annokset");
                return "";
            }
            annosRaakaAineDao.saveOrUpdate(new AnnosRaakaAine(a, ra, req.queryParams("jarjestys"),
                    req.queryParams("maara"), req.queryParams("ohje")));
            res.redirect("/annokset");
            return "";
        });
    }
}
