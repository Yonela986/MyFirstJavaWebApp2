package net.myfirst.webapp;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

//import static spark.Spark.*;

public class App {
        public static void main(String[] args){
            get("/greet/:name", (request, response) -> {
                return "Molo: " + request.params(":name");
            });
            post("/greet/:username", (request, response) -> {
               return "Hello" + request.params("username");
            });

            get("/hello", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                return new ModelAndView(map, "hello.handlebars");
            }, new HandlebarsTemplateEngine());

            post("/hello", (req, res) -> {
                Map<String, Object> map = new HashMap<>();
                // create the greeting message
                String greeting = "Hello, " + req.queryParams("username");
                // put it in the map which is passed to the template - the value will be merged into the template
                map.put("greeting", greeting);
                return new ModelAndView(map, "hello.handlebars");
            }, new HandlebarsTemplateEngine());
            //port(getHerokuAssignedPort());
        }
   /* static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }*/
}
