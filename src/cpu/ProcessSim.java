package cpu;

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
    
	/**
	 * Gets normalized turnaround time.
	 * @return normalized turnaround time
	 */
	public double getNtat() {
		return ntat;
	}
	
	/**
	 * Sets normalized turnaround time.
	 * @param new normalized turnaround time
	 */
	public void setNtat(double ntat) {
		this.ntat = ntat;
	}

	/**
	 * Gets turnaround time.
	 * @return turnaround time
	 */
	public long getTat() {
		return tat;
	}
	
	/**
	 * Sets turnaround time.
	 * @param new turnaround time
	 */
	public void setTat(long tat) {
		this.tat = tat;
	}
	
	/**
	 * Gets the actual service time.
	 * @return actual service time
	 */
    public long getActualServiceTime() {
		return actualServiceTime;
	}

    /** 
     * Sets the actual service time.
     * @param new actual service time
     */
	public void setActualServiceTime(long actualServiceTime) {
		this.actualServiceTime = actualServiceTime;
	}
	
	/**
	 * Gets the actual finish time.
	 * @return actual finish time
	 */
    public long getActualFinishTime() {
		return actualFinishTime;
	}

    /**
     * Sets the actual finish time.
     * @param new actual finish time
     */
	public void setActualFinishTime(long actualFinishTime) {
		this.actualFinishTime = actualFinishTime;
	}
	
	/**
	 * Gets the actual arrival time.
	 * @return actual arrival time
	 */
    public long getActualArrivalTime() {
		return actualArrivalTime;
	}

    /** 
     * Sets the actual arrival time.
     * @param new actual arrival time
     */
	public void setActualArrivalTime(long actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}

	/**
	 * Constructor for ProcessSim.
	 * @param arrival time
	 * @param process name
	 * @param service time
	 * @param priority
	 */
	public ProcessSim (long arrivalTime, String processName, long serviceTime, int priority)
    {
        this.arrivalTime = arrivalTime;
        this.processName = processName;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }

	/**
	 * Sets service time.
	 * @param new service time
	 */
    public void setServiceTime(long serviceTime) {
        this.serviceTime = serviceTime;
    }

    /**
     * Gets arrival time.
     * @return arrival time
     */
    public long getArrivalTime() {
        return arrivalTime;
    }
    
    /**
     * Gets service time.
     * @return service time
     */
    public long getServiceTime() {
        return serviceTime;
    }

    /**
     * Gets process name.
     * @return process name
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Gets priority.
     * @return priority
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * Make copy of this process
     * @return copy of process
     */
    public ProcessSim clone() {
    	
    	ProcessSim copy = new ProcessSim(this.arrivalTime, this.processName, this.serviceTime, this.priority);
        return copy;
        
    }
    
    
}


