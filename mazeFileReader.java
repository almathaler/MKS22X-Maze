import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
public class mazeFileReader{
  public static void main(String args[]) throws FileNotFoundException{
    //printing just line by line
    File f = new File("Maze1.txt");
    Scanner inf1 = new Scanner(f);
    while (inf1.hasNextLine()){
      String toPrint = inf1.nextLine();
      if (!toPrint.equals("")){
        System.out.println(toPrint);
      }
    }
    inf1.close();
    //printing as an array looks so good
    ArrayList<char[]> chars = new ArrayList<char[]>();
    Scanner inf = new Scanner(f);
    while (inf.hasNextLine()){
      String toAdd = inf.nextLine();
    //  System.out.println("This is the line we're adding to array: " + toAdd);
      if (!toAdd.equals("\n") && !toAdd.equals("") ){
        char[] arrayChar = toAdd.toCharArray();
        chars.add(arrayChar);
      }
    }
    for (int k = 0; k<chars.size(); k++){
      System.out.println(Arrays.toString(chars.get(k)));
    }
    inf.close();
  }
}
