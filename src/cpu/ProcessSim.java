package cpu;

import java.util.Date;

/**
 * The ProcessSim class provides the template for the ProcessSim object and its associated functions.
 */

public class ProcessSim {
    private long arrivalTime;
    private long serviceTime;
    private String processName;
    private int priority;

    public ProcessSim (long arrivalTime, String processName, long serviceTime, int priority)
    {
        this.arrivalTime = arrivalTime;
        this.processName = processName;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }

    public void setServiceTime(long serviceTime) {
        this.serviceTime = serviceTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public long getServiceTime() {
        return serviceTime;
    }

    public String getProcessName() {
        return processName;
    }

    public int getPriority() {
        return priority;
    }
}


