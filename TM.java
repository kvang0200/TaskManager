// Kevin Vang
// CSC 131
// Sprint 3

import java.io.IOException;
import java.util.Set;

public class TM{

    private TMModel ModelTm = new TMModel();

    private void summaryTM(){
        Set<String> Names = ModelTm.taskNames();
        Set<String> Sizes = ModelTm.taskSizes();
        System.out.println("===============================================================");
        for (String s : Names){
            summaryTM(s);
        }
        System.out.println("===============================================================");
        for (String s : Sizes){
            if (!(ModelTm.avgTimeForSize(s).equals(""))) {
                System.out.println("Smallest time for size " + s + ": " + ModelTm.minTimeForSize(s));
                System.out.println("Largest time for size " + s + ": " + ModelTm.maxTimeForSize(s));
                System.out.println("Average time for size " + s + ": " + ModelTm.avgTimeForSize(s));
                System.out.println("===============================================================");
            }
        }
        for (String s : Sizes){
            System.out.println("All tasks with size " + s + ": " + ModelTm.taskNamesForSize(s));
        }
        System.out.println("All Task Sizes: " + ModelTm.taskSizes());
        System.out.println("Total time for all tasks: " + ModelTm.elapsedTimeForAllTasks());
        System.out.println("===============================================================");
    }

    private void summaryTM(String name){
        System.out.println("Task: " + name);
        System.out.println("Description: " + ModelTm.taskDescription(name));
        System.out.println("Size: " + ModelTm.taskSize(name));
        System.out.println("Total minutes spent on this task: " + ModelTm.taskElapsedTime(name) + " minutes");
        System.out.println();
    }

    private void mainTM(String[] args) throws IOException{
        if (args.length == 0){
            System.out.println("Please use commands: start, stop, describe, size, or summary followed by a task name.");
        } else {
            switch (args[0]) {
                case "start":
                    ModelTm.startTask(args[1]);
                    break;
                case "stop":
                    ModelTm.stopTask(args[1]);
                    break;
                case "describe":
                    if (args[args.length-1].matches("XS|S|M|L|XL")){
                        ModelTm.describeTask(args[1], args[args.length-2], args[args.length-1]);
                    } else{
                        ModelTm.describeTask(args[1], args[args.length-1]);
                    }
                    break;
                case "summary":
                    if (args.length == 1){
                        summaryTM();
                    }else {
                        summaryTM(args[1]);
                    }
                    break;
                case "size":
                    ModelTm.sizeTask(args[1], args[args.length-1]);
                    break;
                case "delete":
                    ModelTm.deleteTask(args[1]);
                    break;
                case "rename":
                    ModelTm.renameTask(args[1], args[2]);
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