import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.time.temporal.ChronoUnit.MINUTES;

public class TMModel implements ITMModel {

    private LocalTime start;
    private LocalTime stop;
    private TMModel nextStop;
    private TMModel nextStart;

    TMModel(){
        this.start = null;
        this.stop = null;
        this.nextStart = null;
        this.nextStop = null;
    }

    private static void writeToFile(String[] textLine){   // found and used on website -- http://www.homeandlearn.co.uk/java/write_to_textfile.html
           try {
               FileWriter FW = new FileWriter("log.txt", true);
               PrintWriter writer = new PrintWriter(FW);
               writer.printf("%s" + "%n", textLine);
               writer.close();
           }
           catch (IOException IOE){
           }
    }

    public boolean startTask(String name){
        LocalTime time = LocalTime.now();
        System.out.println(name + " has been started.");
        String[] starts = {":START: " + time.toString() + " "+ name};
        writeToFile(starts);
        return true;
    }

    public boolean stopTask(String name){
        LocalTime time = LocalTime.now();
        System.out.println(name + " has been stopped.");
        String[] starts = {":STOP: " + time.toString() + " "+ name};
        writeToFile(starts);
        return true;
    }

    public boolean describeTask(String name, String description) {
        String[] describes = {":DESCRIBE: " + name + " "  + description};
        writeToFile(describes);
        return true;
    }

    public boolean describeTask(String name, String description, String size){
        sizeTask(name,size);
        String[] describes = {":DESCRIBE: " + name + " "  + description};
        writeToFile(describes);
        return true;
    }

    public boolean sizeTask(String name, String size) {
        boolean flag = false;
        String[] sizes = {"XS", "S", "M","L","XL"};
        for (int i = 0; i < 5; i++){
            if (size.equals(sizes[i])){
                flag = true;
            }
        }
        if (flag){
            String[] sizeTM = {":SIZE: " + name + " " + size};
            writeToFile(sizeTM);
        } else {
            System.out.println("Please use XS, S, M, L, or XL for sizing.");
        }
        return flag;
    }

    public boolean deleteTask(String name) {
        String[] deletes = {":DELETE: " + name};
        writeToFile(deletes);
        return true;
    }

    public boolean renameTask(String oldName, String newName) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("log.txt"));    //found and used on website: https://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
            String currentLn;
            String[] check = new String[50];
            String[] sizeTM = {":SIZE: " + newName + " " + taskSize(oldName)};
            String[] describes = {":DESCRIBE: " + newName + " "  + taskDescription(oldName)};
            List<String> timeStart = new ArrayList<>();
            List<String> timeStop = new ArrayList<>();

            for(;(currentLn = br.readLine()) != null;) {                                    // This will read the text file line by line.
                String[] result = currentLn.split("\\s");                             // This will split said line into single strings
                for (int i = 0; i < result.length; i++) {
                    check[i] = result[i];
                }
                if (check[0].equals(":START:") && check[2].equals(oldName)){
                    timeStart.add(check[1]);
                }
                if (check[0].equals(":STOP:") && check[2].equals(oldName)){
                    timeStop.add(check[1]);
                }
                if (check[0].equals(":DELETE:") && check[1].equals(oldName)){
                    timeStop = new ArrayList<>();
                    timeStart = new ArrayList<>();
                }
            }
            for (String s : timeStart){
                String[] startTime = {":START: " + s + " "+ newName};
                writeToFile(startTime);
            }
            for (String s : timeStop){
                String[] stopTime = {":STOP: " + s + " "+ newName};
                writeToFile(stopTime);
            }
            writeToFile(sizeTM);
            writeToFile(describes);
            deleteTask(oldName);
            return true;

        } catch (IOException IOE){
        }
        return false;
    }

    public String taskElapsedTime(String name) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("log.txt"));    //found and used on website: https://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
            String currentLn;
            String[] check = new String[50];

            TMModel begin = new TMModel();
            TMModel end = new TMModel();
            LocalTime timeOne = null;
            LocalTime timeTwo = null;
            int beginCount = 0;
            int endCount = 0;
            long timeTM = 0;

            for(;(currentLn = br.readLine()) != null;){                                    // This will read the text file line by line.
                String[] result = currentLn.split("\\s");                             // This will split said line into single strings
                for (int i = 0; i < result.length; i++){
                    check[i] = result[i];
                }
                switch (check[0]){
                    case ":START:":
                        if (name.equals(check[2])){
                            timeOne = LocalTime.parse(check[1]);
                            if (begin.start == null){
                                begin.start = timeOne;
                                beginCount++;
                            }else{
                                TMModel temp1 = new TMModel();
                                temp1.start = timeOne;

                                TMModel current1 = begin;
                                while(current1.nextStart != null){
                                    current1 = current1.nextStart;
                                }
                                current1.nextStart = temp1;
                                beginCount++;
                            }
                        }
                        break;

                    case ":STOP:":
                        if (name.equals(check[2])){
                            timeTwo = LocalTime.parse(check[1]);
                            if (end.stop == null){
                                end.stop = timeTwo;
                                endCount++;
                            }else{
                                TMModel temp2 = new TMModel();
                                temp2.stop = timeTwo;

                                TMModel current2 = end;
                                while(current2.nextStop != null){
                                    current2 = current2.nextStop;
                                }
                                current2.nextStop = temp2;
                                endCount++;
                            }
                        }
                        break;
                    case ":DELETE:":
                        if (name.equals((check[1]))){
                            begin = new TMModel();
                            end = new TMModel();
                            timeOne = null;
                            timeTwo = null;
                            beginCount = 0;
                            endCount = 0;
                            timeTM = 0;
                        }
                        break;
                }
            }

            if (!(beginCount == endCount)){
                System.out.println("Please stop the task: " + name);
            } else {
                for (int i = 0; i < beginCount; i++){
                    timeTM = timeTM + timeOne.until(timeTwo, MINUTES);
                }
            }
            return String.valueOf(timeTM);
        }
        catch(IOException IOE){
        }
        return null;
    }

    public String taskSize(String name) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("log.txt"));    //found and used on website: https://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
            String currentLn;
            String[] check = new String[50];
            String sizeTotal = "";

            for(;(currentLn = br.readLine()) != null;){                                    // This will read the text file line by line.
                String[] result = currentLn.split("\\s");                             // This will split said line into single strings
                for (int i = 0; i < result.length; i++){
                    check[i] = result[i];
                }
                if (check[0].equals(":SIZE:")){
                    if(name.equals(check[1])){
                        if(!(sizeTotal.equals(check[2]))){
                            sizeTotal = " ";
                        }
                        sizeTotal = result[2];
                    }
                } else if (check[0].equals(":DELETE:")){
                    if(name.equals(check[1])){
                        sizeTotal = "";
                    }
                }
            }
            return sizeTotal;

        } catch (IOException IOE){

        }
        return null;
    }

    public String taskDescription(String name) {
        try{
            BufferedReader br = new BufferedReader(new FileReader("log.txt"));    //found and used on website: https://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
            String currentLn;
            String[] check = new String[50];
            String describeSum = "";

            for(;(currentLn = br.readLine()) != null;){
                String[] result = currentLn.split("\\s");                             // This will split said line into single strings
                for (int i = 0; i < result.length; i++){
                    check[i] = result[i];
                }
                if (check[0].equals(":DESCRIBE:")){
                    if (name.equals(check[1])){
                        for (int i = 2; i < result.length; i++){
                            describeSum = describeSum + result[i] + " ";
                        }
                    }
                }else if (check[0].equals(":DELETE:")){
                    if (name.equals(check[1])){
                       describeSum = "";
                    }
                }
            }
            return describeSum;

        } catch (IOException IOE){

        }
        return null;
    }

    public String minTimeForSize(String size) {
        String Smallest = "";
        Set<String> Names = taskNames();
        int Limit = 100000;
        int count = 0;
        for (String s : Names){
            if (taskSize(s).equals(size)){
                if (Limit > Integer.valueOf(taskElapsedTime(s))){
                    Limit = Integer.valueOf(taskElapsedTime(s));
                }
                count++;
            }
        }
        if (count > 1){
            Smallest = String.valueOf(Limit);
        }
        return Smallest;
    }

    public String maxTimeForSize(String size) {
        String Biggest = "";
        Set<String> Names = taskNames();
        int Limit = 0;
        int count = 0;
        for (String s : Names){
            if (taskSize(s).equals(size)){
                if (Limit < Integer.valueOf(taskElapsedTime(s))){
                    Limit = Integer.valueOf(taskElapsedTime(s));
                }
                count++;
            }
        }
        if (count > 1){
            Biggest = String.valueOf(Limit);
        }
        return Biggest;
    }

    public String avgTimeForSize(String size) {
        Set<String> Names = taskNames();
        String AVG = "";
        int count = 0;
        int TotalMins = 0;
        for (String s : Names){
            if (taskSize(s).equals(size)){
                TotalMins += Integer.valueOf(taskElapsedTime(s));
                count++;
            }
        }
        if (count > 1){
            AVG = String.valueOf(TotalMins/count);
        }
        return AVG;
    }

    public Set<String> taskNamesForSize(String size) {
        Set<String> taskSizesNames = new HashSet<>();
        Set<String> Names = taskNames();
        for (String s : Names){
            if (taskSize(s).equals(size)){
                taskSizesNames.add(s);
            }
        }
        return taskSizesNames;
    }

    public String elapsedTimeForAllTasks() {
        Set<String> Names = taskNames();
        int total = 0;
        for (String s : Names){
            total += Integer.valueOf(taskElapsedTime(s));
        }
        return String.valueOf(total);
    }

    public Set<String> taskNames() {
        Set<String> Names = new HashSet<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader("log.txt"));    //found and used on website: https://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
            String[] check = new String[50];
            String currentLn;

            for(;(currentLn = br.readLine()) != null;){                                    // This will read the text file line by line.
                String[] result = currentLn.split("\\s");                             // This will split said line into single strings
                for (int i = 0; i < result.length; i++){
                    check[i] = result[i];
                }
                if (check[0].equals(":START:")){
                    Names.add(check[2]);
                }
                if (check[0].equals(":DELETE:")){
                    Names.remove(check[1]);
                }
            }
            return Names;
        } catch(IOException IOE){
        }
        return null;
    }

    public Set<String> taskSizes() {
        Set<String> Sizes = new HashSet<>();
        Set<String> Names = taskNames();
        for (String s : Names){
            Sizes.add(taskSize(s));
        }
        return Sizes;
    }
}
