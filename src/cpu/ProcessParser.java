package cpu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * The ProcessParser class reads in a formatted text file, creates ProcessSim objects per the parameters
 * of each line, and then adds the ProcessSim objects into a list for other classes to access.
 */
public class ProcessParser {
    Queue<ProcessSim> processQueue = new LinkedList<>();

    public void ProcessParse() {
        // TODO: Remove hardcoding of data file name, allow user to enter file name
        String fileName = "inputfile.txt";
        try {
            File inputFile = new File(fileName);
            Scanner fileReader = new Scanner(inputFile);

            /* Read in line from file, separate elements using "," delimiter, cast each element
            into the appropriate datatype, create ProcessSim object from those variables and add to list */
            while (fileReader.hasNextLine())
            {
                String data = fileReader.nextLine();
                String[] splitData = data.split(",");
                long arrivalTime = Long.parseLong(splitData[0].trim());
                String processName = splitData[1].trim();
                long serviceTime = Long.parseLong(splitData[2].trim());
                int priority = Integer.parseInt(splitData[3].trim());
                ProcessSim process = new ProcessSim(arrivalTime, processName, serviceTime, priority);
                processQueue.add(process);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Create the list whenever another objects needs it
    public Queue<ProcessSim> getProcessQueue()
    {
        ProcessParse();
        return processQueue;
    }
}
