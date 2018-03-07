import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Write {

    public void writeToFile(String[] textLine){   // found and used on website -- http://www.homeandlearn.co.uk/java/write_to_textfile.html
        try {
            FileWriter FW = new FileWriter("log.txt", true);
            PrintWriter writer = new PrintWriter(FW);
            writer.printf("%s" + "%n", textLine);
            writer.close();
        }
        catch (IOException IOE){
        }
    }
}

