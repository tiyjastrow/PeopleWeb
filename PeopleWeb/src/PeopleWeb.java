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
                    Integer next = null;
                    Integer previous = null;
                    String offset= request.queryParams("offset");
                    int offsetNum = 0;

                    if (offset != null){
                        offsetNum = Integer.parseInt(offset);
                    }

                    for (int i =  offsetNum; i < (offsetNum + 20); i++) {
                        person20.add(peopleList.get(i));
                    }

                    if(offsetNum >= 20){
                        previous = offsetNum - 20;
                    }

                    if(offsetNum < peopleList.size() - 20){
                        next = offsetNum + 20;
                    }

                    m.put("names", person20);
                    m.put("next", next);
                    m.put("previous", previous);
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
