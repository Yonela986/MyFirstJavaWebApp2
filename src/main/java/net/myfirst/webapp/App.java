package net.myfirst.webapp;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

//import static spark.Spark.*;

public class App {
        public static void main(String[] args){

            port(getHerokuAssignedPort());
            //List<String> usernames = new ArrayList<>();
            Map<String, Integer> users = new HashMap<>();

            get("/greet/:name", (request, response) -> {
                return "Hello: " + request.params(":name");
            });
            post("/greet/:username", (request, response) -> {
               return "Hello" + request.params("username");
            });

            get("/hello", (req, res) -> {
                Map<String, Object> map = new HashMap<>();

                map.put("users" , users);
                map.put("counter" , users.size());
                return new ModelAndView(map, "hello.handlebars");
            }, new HandlebarsTemplateEngine());

            post("/hello", (req, res) -> {
                Map<String, Object> map = new HashMap<>();

                // create the greeting message
                String username = req.queryParams("username");

                String greeting = ""  +  username;

              //  System.out.println(req.queryParams("language"));

                if(users.containsKey(username)){
                    users.put(username, users.get(username)+1);
                }
                else{
                    users.put(username,1);
                }
                // put it in the map which is passed to the template - the value will be merged into the template
                map.put("greeting", greeting);
                map.put("counter" , users.size());
                map.put("users", users);

                return new HandlebarsTemplateEngine().render(new ModelAndView(map, "hello.handlebars"));

                //create a Switch statement for selecting languages
                /*String language = "language";

                switch (language){
                    case 1:
                        System.out.println("Xhosa has been selected");
                        break;
                    case 2 :
                        System.out.println("English has been selected");
                        break;
                    case 3:
                        System.out.println("Afrikaans has been selected");
                        break;
                    default:
                        System.out.println("correct language selected");
                }*/
            });

        }
   static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
