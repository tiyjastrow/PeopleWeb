import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PeopleWeb {

    static ArrayList<Person> peopleList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Spark.init();
        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);
        String line;
        while (fileScanner.hasNext()) {
            line = fileScanner.nextLine();
            while (line.startsWith("id,first_name")) {
                line = fileScanner.nextLine();
            }
            String[] columns = line.split(",");
            Person person = new Person(columns[0], columns[1], columns[2], columns[3], columns[4], columns[5]);
            peopleList.add(person);

        }



        Spark.get("/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    ArrayList<Person> person20 = new ArrayList();
                    int startingIndex = 0;
                    int endIndex = 20;

                    for (int i = startingIndex; i <endIndex ; i++) {
                        Person person = peopleList.get(i);
                        person20.add(person);
                    }

                    m.put("names", person20);
                    return new ModelAndView(m, "people.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.get("/person",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    String personId = request.queryParams("personId");
                    int id = Integer.parseInt(personId);
                    m.put("personId", peopleList.get(id-1));
                    return new ModelAndView(m, "person.html");

                }),
                new MustacheTemplateEngine()
        );
    }
}
