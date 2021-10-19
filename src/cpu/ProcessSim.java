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
    private long actualArrivalTime = 0;
    private long actualServiceTime = 0;
	private long actualFinishTime = 0;
	private long tat = 0;
	private double ntat = 0;
    
	public double getNtat() {
		return ntat;
	}
	
	public void setNtat(double ntat) {
		this.ntat = ntat;
	}

	public long getTat() {
		return tat;
	}
	
	public void setTat(long tat) {
		this.tat = tat;
	}
	
    public long getActualServiceTime() {
		return actualServiceTime;
	}

	public void setActualServiceTime(long actualServiceTime) {
		this.actualServiceTime = actualServiceTime;
	}
	
    public long getActualFinishTime() {
		return actualFinishTime;
	}

	public void setActualFinishTime(long actualFinishTime) {
		this.actualFinishTime = actualFinishTime;
	}
	
    public long getActualArrivalTime() {
		return actualArrivalTime;
	}

	public void setActualArrivalTime(long actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}

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


