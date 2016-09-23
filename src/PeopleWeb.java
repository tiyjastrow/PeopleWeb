import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class PeopleWeb {

    public static ArrayList<Person> people = new ArrayList<>();

    static String fullName;

    public static void main(String[] args) throws Exception {

        readFile();

        Spark.init();

        Spark.get("/", ((request, response) -> {

            String offset = request.queryParams("offset");
            int offsetNum=0;

            Integer previous = null;
            Integer next = null;

            if(offset!=null){
                offsetNum=Integer.parseInt(offset);
            }

            HashMap m = new HashMap();

            //substring to hold people's names for linking
           List subList = people.subList(offsetNum,offsetNum+20);

            if(offsetNum>=20){
                previous=offsetNum-20;
            }

            if (offsetNum<people.size()-20) {
                next=offsetNum+20;
            }

            m.put("subList", subList);
            m.put("next",next);
            m.put("previous",previous);
            return new ModelAndView(m, "people.html");
        }), new MustacheTemplateEngine());

        Spark.get("/person", ((request, response) -> {
            String id = request.queryParams("id");

            HashMap m = new HashMap();

            m.put("individual",people.get(Integer.parseInt(id)-1));
            return new ModelAndView(m,"people.html");
        }),new MustacheTemplateEngine());

    }//end main



    public static void readFile() throws FileNotFoundException {
        File f = new File("People.csv");
        Scanner fileScanner = new Scanner(f);
        String line = fileScanner.nextLine();

        while (fileScanner.hasNext()) {
            line = fileScanner.nextLine();
            String[] columns = line.split(",");
            //pass information into user profile: fn, ln, email, country, ip
            Person person = new Person(Integer.parseInt(columns[0]), columns[1], columns[2], columns[3], columns[4], columns[5]);
            //add to array list
            people.add(person);
        }
    }
}
