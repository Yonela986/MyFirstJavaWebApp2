package net.myfirst.webapp;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

//import static spark.Spark.*;

public class App {
        public static void main(String[] args){

            String dbDiskURL = "jdbc:h2:file:./greetdb";
             //String dbMemoryURL = "jdbc:h2:mem:greetdb";
            Jdbi jdbi = Jdbi.create(dbDiskURL, "sa", "");

             //get a handle to the database
            Handle handle = jdbi.open();

            //create the table if needed
            handle.execute("create table if not exists greet ( id integer identity, name varchar(50), counter int )");
            port(getHerokuAssignedPort());

            //List<String> users = new ArrayList<>();
            Map<String, Integer> users = new HashMap<>();

            get("/greet/:name", (request, response) -> {
                return "Hello: " + request.params(":name");
            });
            post("/greet/:username", (request, response) -> {
               return "Hello" + request.params("username");
            });

            get("/hello", (req, res) -> {

                List<String> names = handle.createQuery("select name from greet")
                        .mapTo(String.class)
                        .list();
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
                            users.put(username, users.get(username)+1);}
                            // add a user to the database
                   //handle.execute("insert into greet (name) values (?)", username);
                   else{ users.put(username,1);
                            // check if a user exists
                            int count = handle.select("select count(*) from greet where name = ?", username)
                                    .mapTo(int.class)
                                    .findOnly();
                            if (count == 0){
                                handle.execute("insert into greet (name) values (?)", username);
                            }

                   //if the user wasn't greeted before
//                   if (!users.containsKey(username)) {
//                       users.add(username);
//                   }
               }
                // get all the usernames from the database
                List<String> names = handle.createQuery("select name from greet")
                        .mapTo(String.class)
                        .list();
                //Map<String, Object> users = new HashMap<>();
                // put it in the map which is passed to the template - the value will be merged into the template
                map.put("greeting", users);
                map.put("counter" , names.size());
                map.put("users", names);

                return new HandlebarsTemplateEngine().render(new ModelAndView(map, "hello.handlebars"));


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
