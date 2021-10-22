package cpu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

/**
 * The ProcessParser class reads in a formatted text file, creates ProcessSim objects per the parameters
 * of each line, and then adds the ProcessSim objects into a list for other classes to access.
 */
public class ProcessParser {
    ArrayList<ProcessSim> processQueue = new ArrayList<>();

    public void ProcessParse() {
    	System.out.println("Please select input file.");
        String fileName = "";
    	JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = fileChooser.showOpenDialog(null);
        if(option == JFileChooser.APPROVE_OPTION) {
           fileName = fileChooser.getSelectedFile().getAbsolutePath();
        }
    	
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
            
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while opening data file.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Create the list whenever another objects needs it
    public ArrayList<ProcessSim> getProcessQueue()
    {
        ProcessParse();
        return processQueue;
    }
}
