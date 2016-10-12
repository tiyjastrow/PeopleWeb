/**
 * Created by joe on 22/09/2016.
 */
public class Person {
    int id;
    String lastName;
    String firstName;
    String email;
    String country;
    String ipAddr;

    public Person(int id, String firstName,String lastName, String email, String country, String ipAddr) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.ipAddr = ipAddr;
    }

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }
}
