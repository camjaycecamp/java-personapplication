import java.util.*;
import java.io.*;

public class TestPerson{
  public static void main(String [] args){

    // Make an array (for simplicity) of Person
    // note that since OCCCPerson is a Person, it holds those as well

    Person [] p = new Person[6];
    
    p[0] = new Person("George", "Washington");
    p[1] = new RegisteredPerson("Abraham", "Lincoln", "B1320AF");
    p[2] = new Person("Grover", "Cleveland");
    RegisteredPerson rp1 = new RegisteredPerson("John", "Henry", "9VDNIO234");
    p[3] = new OCCCPerson(rp1, "KJDMF1254");
    
    Person p4 = new Person("James", "Carter");
    Person p5 = new Person("Ronald", "Reagan");

    p[4] = p4;
    p[5] = p5;

    // Display them on the screen to make sure it all worked

    for(int i = 0; i < p.length; ++i){
      System.out.println(p[i].toString());
    }

    // Now dump them to a file and read them back in

    System.out.print("Please enter file name: ");
    Scanner s = new Scanner(System.in);
    String fileName = s.next();
    
    System.out.println("Dumping objects to " + fileName + "...");

    try{
      FileOutputStream   fout = new FileOutputStream(fileName);
      ObjectOutputStream oout = new ObjectOutputStream(fout);

      for(int i = 0; i < p.length; ++i){
        oout.writeObject(p[i]);
      }
    }
    catch(IOException e){
      System.out.println("OH NO BAD THINGS HAPPEN");
      System.out.println(e.toString());
    }

    System.out.println("Now we read them back in...");

    Person [] q = new Person[6];

    try{
      FileInputStream   fin = new FileInputStream(fileName);
      ObjectInputStream oin = new ObjectInputStream(fin);
      Object o;
      for(int i = 0; i < q.length; ++i){
        o = oin.readObject();
        System.out.println(o.getClass());

        // here we have to figure out what kind of Person we have and write them to the array with the appropriate type cast.
        // for code like this always start at the bottom of the inheritance chain and work your way up

        if (o.getClass().equals(OCCCPerson.class)){
          System.out.println("Got me an OCCC Person");
          q[i] = (OCCCPerson) o;
        }
        else{
          System.out.println("This is a Person");
          q[i] = (Person) o;
        }
      }
    }
    catch(Exception e){
      System.out.println("INPUT ERROR");
      System.out.println(e.toString());
    }

    for(int i = 0; i < q.length; ++i){
      System.out.println(q[i].toString());
    }
  }
}
    
    