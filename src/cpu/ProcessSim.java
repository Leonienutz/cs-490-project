package cpu;

import java.util.Date;

/**
 * The ProcessSim class provides the template for the ProcessSim object and its associated functions.
 */

public class ProcessSim {
    private long arrivalTime;
    private long serviceTime;
    private Date currentTime = new Date();
    private String processName;
    private int priority;

    public ProcessSim (long arrivalTime, String processName, long serviceTime, int priority)
    {
        this.arrivalTime = arrivalTime;
        this.processName = processName;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public long getServiceTime() {
        return serviceTime;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public String getProcessName() {
        return processName;
    }

    public int getPriority() {
        return priority;
    }
}


