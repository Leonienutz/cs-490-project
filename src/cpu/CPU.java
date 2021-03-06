package cpu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import controller.ClockSim;

/**
 * Class representing a CPU. The CPU can be passed a process which is
 * "executed" based on its service time.
 */
public class CPU extends Thread {
	private boolean running;
	private boolean processing;
	private boolean preempting;
	private boolean finishing;
	private ArrayList<ActionListener> actionListeners;
	private long currentServiceTime;
	private long currentProcessServiceTime;
	private long lastTimeUpdate = 0;
	private long currentSliceTime;
	private ClockSim systemClock;
	private String currentProcessName;
	private int timeUnit;
	private ProcessSim currProcess;
	private ProcessSim incProcess;
	private ProcessSim incPreProcess;
	
	/**
	 * Constructor for the CPU.
	 */
	public CPU(ClockSim clock) {
		running = false;
		processing = false;
		preempting = false;
		finishing = false;
		actionListeners = new ArrayList<ActionListener>();
		currentServiceTime = 0;
		currentProcessName = "None";
		currentProcessServiceTime = 0;
		currentSliceTime = 0;
		timeUnit = 100;
		systemClock = clock;
	}
	
	/**
	 * Runs the CPU's main loop. The loop runs forever. If the current process is not null
	 * then the CPU will increment a clock until it reaches the process's service time.
	 */
	@Override
	public void run() {		
		while (true) {
			if (running) {
				
				// If we have an incoming preempt or normal process switch, handle it.
				if (preempting && incPreProcess != null) {
					incProcess = incPreProcess;
					incPreProcess = null;
					preempting = false;
				}
				
				if (incProcess != null) {
					currProcess = incProcess;
					incProcess = null;
					processing = false;
				}
				
				if (!processing) {
					
					//if not processing something, check for processes
					if (currProcess != null) {
						//set time variables and set processing to true
						currentProcessName = currProcess.getProcessName();
						currentProcessServiceTime = currProcess.getServiceTime() * timeUnit;
						currentServiceTime = currProcess.getActualServiceTime();
						currentSliceTime = 0;
						lastTimeUpdate = systemClock.getCurrentTime();
						processing = true;
					}
					else {
						//yielding where a loop can potentially do nothing forever prevents the
						//thread from using all processing resources 
						Thread.yield();
					}
					
				}
				else {
					
					if (currProcess != null) {
						
						//create a temp time to subtract the old time from and add this to currentServiceTime. Set current process's service time to newly calculated temp time.
						long temp = systemClock.getCurrentTime();
						currentServiceTime = currentServiceTime + (temp - lastTimeUpdate);
						currentSliceTime = currentSliceTime + (temp - lastTimeUpdate);
						lastTimeUpdate = temp;
						currProcess.setActualServiceTime(currentServiceTime);
						if (currentServiceTime >= currentProcessServiceTime) {
							//once currentServiceTime reaches currentProcessServiceTime, it is finished. Process Queue is popped
							finishing = true;
							currentProcessName = "None";
							currProcess.setActualFinishTime(temp);
							
							//notify listeners that a process was finished
							for (ActionListener listener : actionListeners) {
								listener.actionPerformed(new ActionEvent(currProcess, 0, "finished"));
							}
							
							//set current process to null
							currProcess = null;
							processing = false;
							finishing = false;
						}
					
					}
					
				}
				//yielding where a loop can potentially do nothing forever prevents the
				//thread from using all processing resources 
				Thread.yield();
			}
			else {
				//yielding where a loop can potentially do nothing forever prevents the
				//thread from using all processing resources. Capture current service
				//time and signal that the CPU has been paused.
				Thread.yield();
			}
		}
	}
	
	/**
	 * Gets the running state of the processor. The processor can be processing but not running if
	 * it has a current process but the program is paused.
	 * @return running
	 */
	public boolean getRunning() {
		return running;
	}
	
	/**
	 * Sets the running state of the processor. Setting this to false will pause the processor and preserve any variables.
	 * @param b true to run, false to pause
	 * @param st current system time
	 */
	public void setRunning(boolean b) {
		running = b;
	}
	
	/**
	 * Gets the processing state of the processor. This can be true even if the processor is not running.
	 * @return true if the processor has a current process, false otherwise
	 */
	public boolean getProcessing() {
		return processing;
	}
	
	/** 
	 * Sets processing state of the processor.
	 * @param b true or false
	 */
	public void setProcessing(boolean b) {
		processing = b;
	}

	/**
	 * Gets the current process name.
	 * @return current process name or "None"
	 */
	public String getCurrentProcessName() {
		return currentProcessName;
	}
	
	/**
	 * Gets the total amount of time required to service the current process.
	 * @return time (ms) or 0 if no current process
	 */
	public long getCurrentProcessServiceTime() {
		return currentProcessServiceTime;
	}
	
	/**
	 * Gets the  amount of time spent servicing the current process so far.
	 * @return time (ms) or 0 if no current process
	 */
	public long getCurrentServiceTime() {
		return currentServiceTime;
	}
	
	/**
	 * Adds an action listener to the action listener list. Action listeners are notified when a process
	 * has been finished. The action source will contain the ProcessSim object and the action command 
	 * will contain "finished".
	 * @param e listener to be added
	 */
	public void addActionListenerToProcessorQueue(ActionListener e) {
		actionListeners.add(e);
	}
	
	/**
	 * Sets the current process of the processor. This should only be done if the
	 * current process is equal to null.
	 * @param currProcess
	 */
	public void setCurrProcess(ProcessSim currProcess) {
		this.incProcess = currProcess;
	}
	
	/**
	 * Gets the current process.
	 * @return current process or null
	 */
	public ProcessSim getCurrProcess() {
		return currProcess;
	}
	
	/**
	 * Gets the time that the current process has been running during this slice.
	 * @return
	 */
	public long getCurrentSliceTime() {
		return currentSliceTime;
	}
	
	/**
	 * Sets the time unit of the processor. The time unit is how many milliseconds need to
	 * pass before incrementing the clock.
	 * @param ms milliseconds
	 */
	public void setTimeUnit(int ms) {
		timeUnit = ms;
	}
	
	
	/**
	 * Signals the CPU that it should preempt the current process with a new one.
	 * @param Process to replace current process
	 */
	public void preempt(ProcessSim newProcess) {
		
		this.preempting = true;
		this.processing = false;
		this.incPreProcess = newProcess;
		
	}
	
	/**
	 * Gets whether or not the CPU is already pending a preemption.
	 * @return
	 */
	public boolean isPreempting() {
		return preempting;
	}
	
	/**
	 * Gets whether the CPU is currently finishing a process
	 * @return finishing true or false
	 */
	public boolean isFinishing() {
		return finishing;
	}

}
