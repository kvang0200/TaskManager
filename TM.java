// Kevin Vang
// CSC 131
// Sprint 2

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.io.BufferedReader;
import java.io.FileReader;
import static java.time.temporal.ChronoUnit.MINUTES;

public class TM{

    public LocalTime start;
    public LocalTime stop;
    public TM nextStop;
    public TM nextStart;
    public String title;
    public boolean dupe;

    TM(){
        this.start = null;
        this.stop = null;
        this.nextStart = null;
        this.nextStop = null;
        this.title = "";
        this.dupe = false;
    }


    public static void writeToFile(String[] textLine) throws IOException{   // found and used on website -- http://www.homeandlearn.co.uk/java/write_to_textfile.html
        FileWriter FW = new FileWriter("log.txt",true);
        PrintWriter writer = new PrintWriter(FW);
        writer.printf("%s" + "%n", textLine);
        writer.close();
    }

    public static String combineTM(String[] args){
        String taskName = args[1];
        int lengthArgs = args.length;
        if ((args[0].equals("size"))){
            lengthArgs--;
        }
        for (int i = 2; i<lengthArgs; i++){                                // This will combine all the arguments into one string for easier reading.
            if (args[i].contains(" ")){
                break;
            }
            taskName = taskName + args[i];
        }
        return taskName;
    }

    static void startTM(String[] args) throws IOException{
        for(int i = 0; i < args.length; i++){
            System.out.print(args[i] + " ");
        }
        logsTM(args);
    }

    static void stopTM(String[] args) throws IOException{
        for(int i = 0; i < args.length; i++){
            System.out.print(args[i] + " ");
        }
        logsTM(args);
    }

    static void describeTM(String[] args) throws IOException{
        boolean checkQoute = false;
        for (int i = 0; i < args.length; i++){                                  // checks to see if a white space is in between a string. If true, then qoutes were used.
            if (args[i].contains(" ")){
                checkQoute = true;
            }
        }
        if (checkQoute){
            for(int i = 0; i < args.length; i++){
                System.out.print(args[i] + " ");
            }
            logsTM(args);
        }
        else{
            System.out.println("Please in close the description with Quote marks: \"\" ");
        }
    }

    static void sizeTM(String[] args) throws IOException{
        for(int i = 0; i < args.length; i++){
            System.out.print(args[i] + " ");
        }
        logsTM(args);
    }

    static void summaryTM(String[] args) throws IOException{

        BufferedReader br = new BufferedReader(new FileReader("log.txt"));    //found and used on website: https://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
        String currentLn;
        String[] check = new String[50];

        if (args.length != 1){
            TM begin = new TM();
            TM end = new TM();
            String taskName = combineTM(args);
            String describeSum = "";
            String sizeTotal = " ";
            LocalTime timeOne = null;
            LocalTime timeTwo = null;
            int beginCount = 0;
            int endCount = 0;

            for(;(currentLn = br.readLine()) != null;){                                    // This will read the text file line by line.
                String[] result = currentLn.split("\\s");                             // This will split said line into single strings
                for (int i = 0; i < result.length; i++){
                    check[i] = result[i];
                }
                switch (check[0]){
                    case ":START:":
                        if (taskName.equals(check[2])){
                            timeOne = LocalTime.parse(check[1]);

                            if (begin.start == null){
                                begin.start = timeOne;
                                beginCount++;
                            }else{
                                TM temp1 = new TM();
                                temp1.start = timeOne;

                                TM current1 = begin;
                                while(current1.nextStart != null){
                                    current1 = current1.nextStart;
                                }
                                current1.nextStart = temp1;
                                beginCount++;
                            }
                        }
                        break;

                    case ":STOP:":
                        if (taskName.equals(check[2])){
                            timeTwo = LocalTime.parse(check[1]);

                            if (end.stop == null){
                                end.stop = timeTwo;
                                endCount++;
                            }else{
                                TM temp2 = new TM();
                                temp2.stop = timeTwo;

                                TM current2 = end;
                                while(current2.nextStop != null){
                                    current2 = current2.nextStop;
                                }
                                current2.nextStop = temp2;
                                endCount++;
                            }
                        }
                        break;

                    case ":DESCRIBE:":                                              // If there is a later itteration of describe, it will take the lastest one.
                        if (taskName.equals(check[1])){
                            for (int i = 2; i < result.length; i++){
                                describeSum = describeSum + result[i] + " ";
                            }
                        }
                        break;

                    case ":SIZE:":
                        if (taskName.equals(check[1])){
                            if (!(sizeTotal.equals(" "))){
                                sizeTotal = " ";
                            }
                            sizeTotal = result[2];
                        }
                        break;
                }
            }

            long timeTM = 0;
            if (!(beginCount == endCount)){
                System.out.println("Please stop the task: " + taskName);
            }
            else{
                for (int i = 0; i < beginCount; i++){
                    timeTM = timeTM + timeOne.until(timeTwo, MINUTES);
                    begin = begin.nextStart;
                    end = end.nextStop;
                }
                System.out.println("Task: " + taskName);
                System.out.println("Description: " + describeSum);
                System.out.println("Size: " + sizeTotal);
                System.out.println("Total minutes spent on this task: " + timeTM + " minutes");
            }

        } else{                                                                             // this will print out all task on the log.
            String[] argument = new String [2];
            argument[0] = "summary";
            int taskCount = 0;

            TM taskNew = new TM();
            for(;(currentLn = br.readLine()) != null;){                                    // This will read the text file line by line.
                String[] result = currentLn.split("\\s");                             // This will split said line into single strings
                for (int i = 0; i < result.length; i++){
                    check[i] = result[i];
                }
                if (check[0].equals(":START:")){
                    if ((taskNew.title).equals("")){
                        taskNew.title = check[2];
                        taskCount++;
                    }else{
                        TM temp = new TM();
                        temp.title = check[2];

                        TM current = taskNew;
                        while(current.nextStart != null){
                            current = current.nextStart;
                        }
                        current.nextStart = temp;
                        taskCount++;
                    }
                }
            }

            TM dup = taskNew;
            TM dup2 = taskNew;
            while (dup2.nextStart != null){
                while (dup.nextStart != null) {
                    if (dup.title.equals(dup2.title)) {
                        dup.dupe = true;
                    }
                    dup = dup.nextStart;
                }
                dup2 = dup2.nextStart;
            }


            for (int i = 0; i < taskCount; i++){
               if(taskNew.dupe = true) {
                   taskNew.dupe = false;
                   argument[1] = taskNew.title;
                   summaryTM(argument);
                   System.out.println();
                   taskNew = taskNew.nextStart;
               }
            }
        }
    }

    static void logsTM(String[] args) throws IOException{                       // this will write the description or time to file.
        LocalTime time = LocalTime.now();
        String taskName = combineTM(args);
        switch(args[0]){
            case "start":
                String[] starts = {":START: " + time.toString() + " "+ taskName};
                writeToFile(starts);
                break;

            case "stop":
                String[] stops = {":STOP: " + time.toString() + " "+ taskName};
                writeToFile(stops);
                break;

            case "describe":
                String descpt = "";
                for (int i = 0; i < args.length; i++){
                    if (args[i].contains(" ")){
                        descpt = args[i];
                    }
                    if ((i == (args.length - 1) && !args[i].equals(descpt))){
                        String[] argSize = {"size", taskName, args[i]};
                        logsTM(argSize);
                    }
                }
                String[] describes = {":DESCRIBE: " + taskName + " "  + descpt};
                writeToFile(describes);
                break;

            case "size":
                String sizeVal = "";
                String[] sizes = {"XS", "S", "M","L","XL"};
                boolean flag = false;
                if (args.length < 2){
                    System.out.println("Please input a size: XS, S, M, L, or XL");
                }
                else{
                    for (int i = 0; i < sizes.length; i++){
                        if (args[args.length-1].equals(sizes[i])){
                            sizeVal = args[args.length-1];
                            flag = true;
                        }
                    }
                }
                if (flag){
                    String[] size = {":SIZE: " + taskName + " " + sizeVal};
                    writeToFile(size);
                }
                else {
                    System.out.println("Please input a size: XS, S, M, L, or XL");
                }
                break;

            default:
                System.out.println("Please use commands: start, stop, describe, size, or summary. sdd");
        }
    }

    static void mainTM(String[] args) throws IOException{
        if (args.length == 0 && !args[0].equals("summary")){
            System.out.println("Please use commands: start, stop, describe, size, or summary followed by a task name.");
        } else {
            switch (args[0]) {
                case "start":
                    startTM(args);
                    break;
                case "stop":
                    stopTM(args);
                    break;
                case "describe":
                    describeTM(args);
                    break;
                case "summary":
                    summaryTM(args);
                    break;

                case "size":
                    sizeTM(args);
                    break;

                default:
                    System.out.println("Please use commands: start, stop, describe, size, or summary followed by a task name.");
            }
        }
    }

    public static void main(String[] args) throws IOException{
        TM tm = new TM();
        tm.mainTM(args);
    }
}