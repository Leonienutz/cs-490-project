package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import cpu.CPU;
import cpu.ProcessParser;
import cpu.ProcessSim;
import view.GUI;

/**
 * Class that controls and manages processes, cpus, and the programs virtual clock.
 * The gui is updated based on the objects contained in the controller.
 */
public class Controller extends Thread {
	
	private CPU cpu1;
	private CPU cpu2;
	private GUI window;
	private ProcessParser parse;
	private ArrayList<ProcessSim> processesFromFile;
	private Queue<ProcessSim> arrivedProcesses;
	private ArrayList<ProcessSim> finishedProcesses;
	private DefaultTableModel processTable1;
	private DefaultTableModel statsTable1;
	private DefaultTableModel processTable2;
	private DefaultTableModel statsTable2;
	private int timeUnit = 100;
	private ClockSim systemClock;
	private boolean running = false;
	
	/**
	 * Main function that runs the program
	 * @param args
	 */
	public static void main(String[] args) {
		//set look and feel for all windows
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create a new controller
		Controller os = new Controller();
		os.start();
	}
	
	/**
	 * Constructor for the Controller. Loads the processes from the input file, creates all process lists,
	 * table models, cpus, and the gui.
	 */
	public Controller() {
		//create process queues
		parse = new ProcessParser();
		processesFromFile = parse.getProcessQueue();
		arrivedProcesses = new LinkedList<>();
		finishedProcesses = new ArrayList<ProcessSim>();
		
		//create table models
		processTable1 = new DefaultTableModel(new String[] {"Arrival Time", "Process Name", "Service Time"}, 0);
		statsTable1 = new DefaultTableModel(new String[] {"Process Name", "Arrival Time", "Service Time", "Finish Time", "TAT", "nTAT"}, 0);
		processTable2 = new DefaultTableModel(new String[] {"Arrival Time", "Process Name", "Service Time"}, 0);
		statsTable2 = new DefaultTableModel(new String[] {"Process Name", "Arrival Time", "Service Time", "Finish Time", "TAT", "nTAT"}, 0);
		for (ProcessSim process : processesFromFile) {
			Vector<String> tableRow = new Vector<String>();
			tableRow.add(String.valueOf(process.getArrivalTime()));
			tableRow.add(process.getProcessName());
			tableRow.add(String.valueOf(process.getServiceTime()));
			processTable1.addRow(tableRow);
		}
		
		//create clock
		systemClock = new ClockSim();
		
		//create CPUs
		cpu1 = new CPU(systemClock);
		cpu1.start();
		cpu2 = new CPU(systemClock);
		cpu2.start();
		
		//create GUI
		window = new GUI(processTable1, statsTable1, processTable2, statsTable2);
		this.initializeActionListeners();
	}
	
	/**
	 * Runs the controller's main loop. The loop runs forever. As long as the controller's
	 * running flag is true, the system clock is incremented, processes are added to the arrived
	 * list, and processes are passed to cpus.
	 */
	@Override
	public void run() {
		while (true) {
			if (running) {
				//update system clock
				systemClock.updateClock();
				
				//check process in the processes from file list to see if the have reached their arrival time
				Iterator<ProcessSim> processIterator = processesFromFile.iterator();
				while (processIterator.hasNext()) {
					ProcessSim process = processIterator.next();
					if ((process.getArrivalTime() * timeUnit) <= systemClock.getCurrentTime()) {
						//set the actual arrival time and add it to the arrived processes list
						process.setActualArrivalTime(systemClock.getCurrentTime());
						arrivedProcesses.add(process);
						
						//print a message in the gui and remove the process from the processes from file list
						window.systemPrint(systemClock.getCurrentTime(), process.getProcessName() + " added to process queue.");
						processIterator.remove();
					}
				}
				
				//if there are processes in the arrived queue and cpu1 does not have a process,
				//pass cpu1 a process and wake it if it is sleeping. Update the process queue table.
				if (!arrivedProcesses.isEmpty()) {
					if (cpu1.getCurrProcess() == null) {
						ProcessSim process = HRRN();
						cpu1.setCurrProcess(process);
						if (cpu1.getState().equals(Thread.State.TIMED_WAITING)) {
							cpu1.interrupt();
						}
						window.systemPrint(systemClock.getCurrentTime(), process.getProcessName() + " loaded in cpu1.");

						if (!processTable1.getDataVector().isEmpty()) {
							processTable1.getDataVector().remove(0);
						}
					}
				}
				
				//if there are processes in the arrived queue and cpu2 does not have a process,
				//pass cpu2 a process and wake it if it is sleeping. Update the process queue table.
				/*if (!arrivedProcesses.isEmpty()) {
					if (cpu2.getCurrProcess() == null) {
						cpu2.setCurrProcess(arrivedProcesses.peek());
						if (cpu2.getState().equals(Thread.State.TIMED_WAITING)) {
							cpu2.interrupt();
						}
						window.systemPrint(systemClock.getCurrentTime(), arrivedProcesses.poll().getProcessName() + " loaded in cpu2.");

						if (processTable1.getRowCount() > 0) {
							processTable1.removeRow(0);
						}
					}
				}*/
				
				//yielding where a loop can potentially do nothing forever prevents the
				//thread from using all processing resources 
				Thread.yield();
			}
			else {
				//yielding where a loop can potentially do nothing forever prevents the
				//thread from using all processing resources 
				Thread.yield();
			}
		}
	}
	
	/**
	 * Adds action listeners to any of the Controllers objects that need one. This is
	 * for organizational purposes so that all action listeners are located in one place.
	 */
	private void initializeActionListeners() {
		//create a timer to periodically update gui at around 30fps
		ActionListener updateProcessorText = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//update gui elements that are based on time
				window.setCPUTextPane(cpu1.getCurrentProcessName(), cpu1.getCurrentProcessServiceTime() - cpu1.getCurrentServiceTime(), 1);
				window.setCPUTextPane(cpu2.getCurrentProcessName(), cpu2.getCurrentProcessServiceTime() - cpu2.getCurrentServiceTime(), 2);
				window.setSystemTime(systemClock.getCurrentTime());
				//should change this to calculate current average nTAT for each processor
				//window.setThroughputValue((double)statsTable1.getRowCount() / (double)Math.round((double)systemClock.getCurrentTime() / (double)timeUnit));
			}
			
		};
		new Timer(30, updateProcessorText).start();
		
		//add action listener to CPU1
		cpu1.addActionListenerToProcessorQueue(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("finished")) {
					//get the finished process and set its finished time
					ProcessSim finishedProcess = (ProcessSim)e.getSource();
					//finishedProcess.setActualFinishTime(systemClock.getCurrentTime());
					finishedProcess.setTat(finishedProcess.getActualFinishTime() - finishedProcess.getActualArrivalTime());
					finishedProcess.setNtat((double)Math.round((double)finishedProcess.getTat() / (double)timeUnit) / (double)Math.round((double)finishedProcess.getActualServiceTime() / (double)timeUnit));
					
					//create a new table row for the stats table
					Vector<String> tableRow = new Vector<String>();
					DecimalFormat formatter = new DecimalFormat("#.##");
					tableRow.add(finishedProcess.getProcessName());
					tableRow.add(String.valueOf(Math.round((double)finishedProcess.getActualArrivalTime() / (double)timeUnit)));
					tableRow.add(String.valueOf(Math.round((double)finishedProcess.getActualServiceTime() / (double)timeUnit)));
					tableRow.add(String.valueOf(Math.round((double)finishedProcess.getActualFinishTime() / (double)timeUnit)));
					tableRow.add(String.valueOf(Math.round((double)finishedProcess.getTat() / (double)timeUnit)));
					tableRow.add(formatter.format(finishedProcess.getNtat()));
					statsTable1.addRow(tableRow);
					
					//print message in gui and add process to finished process list
					window.systemPrint(systemClock.getCurrentTime(), finishedProcess.getProcessName() + " finished in processor 1.");
					finishedProcesses.add(finishedProcess);
				}
			}
			
		});
		
		//add action Listener to CPU2
		cpu2.addActionListenerToProcessorQueue(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("finished")) {
					//get the finished process and set its finished time, tat, and ntat
					ProcessSim finishedProcess = (ProcessSim)e.getSource();
					//finishedProcess.setActualFinishTime(systemClock.getCurrentTime());
					finishedProcess.setTat(finishedProcess.getActualFinishTime() - finishedProcess.getActualArrivalTime());
					finishedProcess.setNtat((double)Math.round((double)finishedProcess.getTat() / (double)timeUnit) / (double)Math.round((double)finishedProcess.getActualServiceTime() / (double)timeUnit));
					
					//create a new table row for the stats table
					Vector<String> tableRow = new Vector<String>();
					DecimalFormat formatter = new DecimalFormat("#.##");
					tableRow.add(finishedProcess.getProcessName());
					tableRow.add(String.valueOf(Math.round((double)finishedProcess.getActualArrivalTime() / (double)timeUnit)));
					tableRow.add(String.valueOf(Math.round((double)finishedProcess.getActualServiceTime() / (double)timeUnit)));
					tableRow.add(String.valueOf(Math.round((double)finishedProcess.getActualFinishTime() / (double)timeUnit)));
					tableRow.add(String.valueOf(Math.round((double)finishedProcess.getTat() / (double)timeUnit)));
					tableRow.add(formatter.format(finishedProcess.getNtat()));
					statsTable1.addRow(tableRow);
					
					//print message in gui and add process to finished process list
					window.systemPrint(systemClock.getCurrentTime(), finishedProcess.getProcessName() + " finished in processor 2.");
					finishedProcesses.add(finishedProcess);
				}
			}
			
		});
		
		//add action listener to start button
		window.addStartButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//when the start button is pressed change all running flags to true
				//and get the time unit from the gui
				if (!running) {
					timeUnit = window.getTimeUnit();
					window.setTimeUnit(timeUnit);
					cpu1.setTimeUnit(timeUnit);
					cpu2.setTimeUnit(timeUnit);
					window.setSystemState(true);
					running = true;
					systemClock.resumeClock();
					cpu1.setRunning(true);
					cpu2.setRunning(true);
				}
			}
			
		});
		
		//add action listener to stop button
		window.addStopButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//when the stop button is pressed set all running flags to false
				window.setSystemState(false);
				running = false;
				systemClock.pauseClock();
				cpu1.setRunning(false);
				cpu2.setRunning(false);
			}
			
		});
	}

	/**
	 * Iterates over list of processes, calculates response ratio for each process
	 * Returns the process with the highest response ratio.
	 * Response ratio is calculated as (current time - waiting time) + service time
	 * all divided by service time.
	 * @return next process to be executed
	 */
	private ProcessSim HRRN() {
		long currentTime = systemClock.getCurrentTime();
		float responseRatio = 0.0f;
		ProcessSim nextProcess = arrivedProcesses.peek();
		for (ProcessSim process : arrivedProcesses){
			float waitingTime = currentTime - process.getArrivalTime() * timeUnit;
			float temp;
			temp = responseRatio;
			responseRatio = (waitingTime + process.getServiceTime()) / process.getServiceTime();
			if (responseRatio > temp) {
				nextProcess = process;
			}
		}
		arrivedProcesses.remove(nextProcess);

		return nextProcess;
	}
}
