package cpu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;

import javax.swing.table.DefaultTableModel;

public class CPU extends Thread
{
	private String[] processQueueTableHeaders;
	private DefaultTableModel processQueueTableModel;
	private boolean running;
	private boolean processing;
	private ArrayList<ActionListener> actionListeners;
	private long currentServiceTime;
	private long currentProcessServiceTime;
	private Date currentTime;
	private String currentProcessName;
	private int timeUnit;
	private ProcessSim currProcess;
	private Queue<ProcessSim> processQ;

	public CPU()
	{
		String[] processQueueTableHeaders = {"Process Name", "Service Time"};
		processQueueTableModel = new DefaultTableModel(processQueueTableHeaders, 0);
		running = false;
		processing = false;
		actionListeners = new ArrayList<ActionListener>();
		currentServiceTime = 0;
		currentProcessName = "None";
		currentProcessServiceTime = 0;
		timeUnit = 100;
	}
	
	@Override
	public void run()
	{		
		while (true)
		{
			if (running)
			{
				if (!processing)
				{
					//if not processing something, check for processes
					if (processQueueTableModel.getRowCount() > 0)
					{
						// current process is the head of the queue
						currProcess = processQ.peek();
						currentProcessName = currProcess.getProcessName();
						currentProcessServiceTime = currProcess.getServiceTime() * timeUnit;
						currentServiceTime = 0;
						currentTime = new Date();
						processQueueTableModel.removeRow(0);
						processing = true;
						
						//notify listeners that a process was popped
						for (ActionListener listener : actionListeners)
						{
							listener.actionPerformed(new ActionEvent("pop", 0, currentProcessName));
						}
					}
				}
				else
				{
					//create a temp time to subtract the old time from and add this to currentServiceTime. Set current process's service time to newly calculated temp time.
					Date tempTime = new Date();
					currentServiceTime = currentServiceTime + (tempTime.getTime() - currentTime.getTime());
					currentTime = tempTime;
					currProcess.setServiceTime(currentServiceTime);
					if (currentServiceTime >= currentProcessServiceTime)
					{
						//once currentServiceTime reaches currentProcessServiceTime, it is finished. Process Queue is popped
						processing = false;
						currentProcessName = "None";
						processQ.poll();
					}
				}
			}
			else
			{
				//program is paused so set process state to paused
				currentTime = new Date();
			}
		}
	}
	
	/**
	 * Gets the table model that acts as the process queue
	 * @return processQueueTableModel
	 */
	public DefaultTableModel getProcessQueueTableModel()
	{
		return processQueueTableModel;
	}
	
	/**
	 * Pushes a process into the process queue by adding it to the table model.
	 * @param process name of process to add
	 * @param serviceTime total amount of time needed to service the process
	 */
	public void pushProcessToQueue(String process, int serviceTime)
	{
		Object[] newRow = {process, serviceTime};
		processQueueTableModel.addRow(newRow);
		
		//notify listeners that a process was pushed
		for (ActionListener listener : actionListeners)
		{
			listener.actionPerformed(new ActionEvent("push", 0, process));
		}
	}
	
	/**
	 * Gets the running state of the processor. The processor can be processing but not running if
	 * it has a current process but the program is paused.
	 * @return running
	 */
	public boolean getRunning()
	{
		return running;
	}
	
	/**
	 * Sets the running state of the processor. Setting this to false will pause the processor and preserve any variables.
	 * @param b true to run, false to pause
	 */
	public void setRunning(boolean b)
	{
		running = b;
	}
	
	/**
	 * Gets the processing state of the processor. This can be true even if the processor is not running.
	 * @return true if the processor has a current process, false otherwise
	 */
	public boolean getProcessing()
	{
		return processing;
	}
	
	/**
	 * Gets the current process name.
	 * @return current process name or "None"
	 */
	public String getCurrentProcessName()
	{
		return currentProcessName;
	}
	
	/**
	 * Gets the total amount of time required to service the current process.
	 * @return time (ms) or 0 if no current process
	 */
	public long getCurrentProcessServiceTime()
	{
		return currentProcessServiceTime;
	}
	
	/**
	 * Gets the  amount of time spent servicing the current process so far.
	 * @return time (ms) or 0 if no current process
	 */
	public long getCurrentServiceTime()
	{
		return currentServiceTime;
	}
	
	/**
	 * Adds an action listener to the action listener list. Action listeners are notified when a process
	 * has been pushed or popped from the process queue. The action source will contain a string "push"
	 * or "pop", and the action command will contain the name of the process that was pushed/popped.
	 * @param e listener to be added
	 */
	public void addActionListenerToProcessorQueue(ActionListener e)
	{
		actionListeners.add(e);
	}

	public void setCurrProcess(ProcessSim currProcess) {
		this.currProcess = currProcess;
	}

	public void setProcessQueue(Queue<ProcessSim> processQ) {
		this.processQ = processQ;
	}
}
