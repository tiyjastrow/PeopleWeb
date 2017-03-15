import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Main {

    static ArrayList<Person> personArrayList = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Starting PeopleWeb app...");
        File f = new File("people.csv");
        Scanner scanner = null;
        try {
            scanner = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("File has not been found!");
        }
        scanner.nextLine();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] columns = line.split(",");
            Person person = new Person(columns[0], columns[1], columns[2], columns[3], columns[4], columns[5]);
            personArrayList.add(person);
        }

        Spark.init();
        Spark.get("/", (request, response) -> {

                    HashMap m = new HashMap();
                    String offset = request.queryParams("offset");
                    int offsetNum = 0;
                    if (offset != null && !offset.isEmpty()) {
                        offsetNum = Integer.parseInt(offset);
                    }
                    int nextOffset = offsetNum + 20;
                    if (nextOffset <= personArrayList.size()) {
                        m.put("nextOffset", nextOffset);
                    }
                    int prevOffset = offsetNum - 20;
                    if (prevOffset >= 0) {
                        m.put("prevOffset", prevOffset);
                    }

                    if (nextOffset <= personArrayList.size()) {
                        List<Person> subList = personArrayList.subList(offsetNum, nextOffset);
                        m.put("subList", subList);
                    } else {
                        List<Person> subList = personArrayList.subList(offsetNum, personArrayList.size());
                        m.put("subList", subList);
                    }

                    m.put("nextOffset", nextOffset);
                    return new ModelAndView(m, "home.html");
                },
                new MustacheTemplateEngine()
        );

        Spark.get("/person", (request, response) -> {
                    HashMap m = new HashMap();
                    int id = Integer.parseInt(request.queryParams("id"));
                    Person person;
                    person = personArrayList.get(id - 1);
                    m.put("person", person);
                    return new ModelAndView(m, "person.html");
                },
                new MustacheTemplateEngine()
        );
    }
}

