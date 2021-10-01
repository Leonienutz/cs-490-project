package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import cpu.ProcessSim;
import cpu.ProcessParser;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

import cpu.CPU;

import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.JSeparator;
import java.awt.Color;
import java.util.Queue;

public class GUI
{

	private JFrame frmCsProject;
	private JButton startButton;
	private JLabel unitLabel;
	private JSeparator separator1;
	private JLabel processQueue1Label;
	private JLabel cpu1Label;
	private JScrollPane processQueue1ScrollPane;
	private JPanel cpu1Panel;
	private JTextPane cpu1TextPane;
	private JLabel processQueue2Label;
	private JScrollPane processQueue2ScrollPane;
	private JLabel cpu2Label;
	private JPanel cpu2Panel;
	private JTextPane cpu2TextPane;
	private JButton stopButton;
	private JLabel systemStateLabel;
	private JSeparator separator2;
	private JLabel systemLabel;
	private JTextArea systemTextArea;
	private JFormattedTextField unitTextField;
	private JLabel unitLabel2;
	
	private JTable processQueue1Table;
	private DefaultTableModel processQueue1TableModel;
	private JTable processQueue2Table;
	private DefaultTableModel processQueue2TableModel;
	private JScrollPane systemScrollPane;
	private boolean systemState;

	/**
	 * Launch the application.
	 */
	//test
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Parse data file, create list of processes
					ProcessParser parse = new ProcessParser();
					Queue<ProcessSim> processQueue;
					processQueue = parse.getProcessQueue();

					//create CPUs
					CPU cpu1 = new CPU();
					CPU cpu2 = new CPU();
					
					//create GUI
					GUI window = new GUI(cpu1.getProcessQueueTableModel(), cpu2.getProcessQueueTableModel());
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					window.frmCsProject.setVisible(true);
					
					//create a timer to periodically update gui
					ActionListener updateProcessorText = new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							window.setCPUTextPane(cpu1.getCurrentProcessName(), cpu1.getCurrentProcessServiceTime() - cpu1.getCurrentServiceTime(), 1);
							window.setCPUTextPane(cpu2.getCurrentProcessName(), cpu2.getCurrentProcessServiceTime() - cpu2.getCurrentServiceTime(), 2);
						}
						
					};
					new Timer(30, updateProcessorText).start();
					
					//add action listener to CPU1 and start
					cpu1.addActionListenerToProcessorQueue(new ActionListener () {

						@Override
						public void actionPerformed(ActionEvent e) {
							if (e.getSource().toString().equals("pop"))
							{
								window.processPopped(e.getActionCommand(), 1);
							}
							else if (e.getSource().toString().equals("push"))
							{
								window.processPushed(e.getActionCommand(), 1);
							}
						}
						
					});
					cpu1.start();
					
					//add action Listener to CPU2 and start
					cpu2.addActionListenerToProcessorQueue(new ActionListener () {

						@Override
						public void actionPerformed(ActionEvent e) {
							if (e.getSource().toString().equals("pop"))
							{
								window.processPopped(e.getActionCommand(), 2);
							}
							else if (e.getSource().toString().equals("push"))
							{
								window.processPushed(e.getActionCommand(), 2);
							}
						}
						
					});
					cpu2.start();
					
					//add action listener to start button
					window.addStartButtonListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							cpu1.setRunning(true);
							cpu2.setRunning(true);
							window.setSystemState(true);
						}
						
					});
					
					//add action listener to stop button
					window.addStopButtonListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							cpu1.setRunning(false);
							cpu2.setRunning(false);
							window.setSystemState(false);
						}
						
					});
					
					//add processes to CPU1
					/*for (int i = 0; i < 50; i++)
					{
						cpu1.pushProcessToQueue("test " + i, i * 10);
					}*/

					// Push process queue to CPU1.
					cpu1.setProcessQueue(processQueue);
					for (ProcessSim process : processQueue) {
						cpu1.pushProcessToQueue(process.getProcessName(), (int) process.getServiceTime());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI(DefaultTableModel cpu1, DefaultTableModel cpu2) {
		processQueue1TableModel = cpu1;
		processQueue2TableModel = cpu2;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmCsProject = new JFrame();
		frmCsProject.setTitle("CS 490 Project");
		frmCsProject.setBounds(100, 100, 900, 600);
		frmCsProject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmCsProject.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][100,grow]", "[][11.00][][150,grow][][150.00,grow][][][:150:150,grow]"));
		
		startButton = new JButton("Start System");
		startButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(startButton, "flowx,cell 0 0");
		
		unitLabel = new JLabel("1 time unit = ");
		unitLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(unitLabel, "flowx,cell 1 0");
		
		separator1 = new JSeparator();
		separator1.setForeground(Color.DARK_GRAY);
		panel.add(separator1, "flowx,cell 0 1 2 1,growx");
		
		processQueue1Label = new JLabel("Process Queue");
		processQueue1Label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(processQueue1Label, "cell 0 2");
		
		cpu1Label = new JLabel("CPU 1");
		cpu1Label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(cpu1Label, "cell 1 2");
		
		processQueue1ScrollPane = new JScrollPane();
		panel.add(processQueue1ScrollPane, "cell 0 3,grow");
		
		processQueue1Table = new JTable(processQueue1TableModel);
		processQueue1Table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		processQueue1Table.setRowSelectionAllowed(false);
		processQueue1ScrollPane.setViewportView(processQueue1Table);
		
		cpu1Panel = new JPanel();
		panel.add(cpu1Panel, "cell 1 3,grow");
		cpu1Panel.setLayout(new BorderLayout(0, 0));
		
		cpu1TextPane = new JTextPane();
		cpu1TextPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cpu1TextPane.setText("CPU stuff");
		cpu1Panel.add(cpu1TextPane);
		
		processQueue2Label = new JLabel("Process Queue");
		processQueue2Label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(processQueue2Label, "cell 0 4");
		
		processQueue2ScrollPane = new JScrollPane();
		panel.add(processQueue2ScrollPane, "cell 0 5,grow");
		
		processQueue2Table = new JTable(processQueue2TableModel);
		processQueue2Table.setRowSelectionAllowed(false);
		processQueue2Table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		processQueue2ScrollPane.setViewportView(processQueue2Table);
		
		cpu2Label = new JLabel("CPU 2");
		cpu2Label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(cpu2Label, "cell 1 4");
		
		cpu2Panel = new JPanel();
		panel.add(cpu2Panel, "cell 1 5,grow");
		cpu2Panel.setLayout(new BorderLayout(0, 0));
		
		cpu2TextPane = new JTextPane();
		cpu2TextPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cpu2TextPane.setText("More CPU stuff");
		cpu2Panel.add(cpu2TextPane, BorderLayout.CENTER);
		
		stopButton = new JButton("Stop System");
		stopButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(stopButton, "cell 0 0");
		
		systemStateLabel = new JLabel("System State");
		systemStateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(systemStateLabel, "cell 0 0");
		
		separator2 = new JSeparator();
		separator2.setForeground(Color.DARK_GRAY);
		panel.add(separator2, "cell 0 6 2 1,growx");
		
		systemLabel = new JLabel("System:");
		systemLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(systemLabel, "cell 0 7");
		
		systemScrollPane = new JScrollPane();
		panel.add(systemScrollPane, "cell 0 8 2 1,grow");
		
		systemTextArea = new JTextArea();
		((DefaultCaret)systemTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		systemScrollPane.setViewportView(systemTextArea);
		
		unitTextField = new JFormattedTextField();
		unitTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		unitTextField.setText("100");
		panel.add(unitTextField, "cell 1 0");
		
		unitLabel2 = new JLabel("ms");
		unitLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(unitLabel2, "cell 1 0");
	}
	
	/**
	 * Adds an action listener to the start button.
	 * @param e listener to be added
	 */
	public void addStartButtonListener(ActionListener e)
	{
		startButton.addActionListener(e);
	}
	
	/**
	 * Adds an action listener to the stop button.
	 * @param e listener to be added
	 */
	public void addStopButtonListener(ActionListener e)
	{
		stopButton.addActionListener(e);
	}
	
	/**
	 * Prints a message to the system text area displaying which process was pushed to which queue. Does nothing if queueNumber is not 1 or 2.
	 * @param process
	 * @param queueNumber 1 or 2
	 */
	public void processPushed(String process, int queueNumber)
	{
		if (queueNumber == 1)
		{
			this.systemPrint("Added process: " + process + " to processing queue " + queueNumber + ".");
		}
		else if (queueNumber == 2)
		{
			this.systemPrint("Added process: " + process + " to processing queue " + queueNumber + ".");
		}
	}
	
	/**
	 * Prints a message to the system text area displaying which process was popped from which queue. Does nothing if queueNumber is not 1 or 2.
	 * @param queueNumber 1 or 2
	 */
	public void processPopped(String process, int queueNumber)
	{
		if (queueNumber == 1)
		{
			this.systemPrint("Removed process: " + process + " from processing queue " + queueNumber + ".");
		}
		else if (queueNumber == 2)
		{
			this.systemPrint("Removed process: " + process + " from processing queue " + queueNumber + ".");
		}
	}
	
	/**
	 * Appends a line of text to the system text area with a time stamp at the beginning and a new line character
	 * at the end.
	 * @param text string to append
	 */
	public void systemPrint(String text)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:MM:ss");
		systemTextArea.append(dateFormat.format(new Date()) + " << " + text + "\n");
	}
	
	/**
	 * Sets the variables displayed in the cpu text pane. Does nothing if cpuNumber is not 1 or 2.
	 * @param process process name
	 * @param timeRemaining time left to process (ms)
	 * @param cpuNumber cpu 1 or 2
	 */
	public void setCPUTextPane(String process, long timeRemaining, int cpuNumber)
	{
		if (cpuNumber == 1)
		{
			cpu1TextPane.setText("Process: " + process + "\nTime Remaining (ms): " + timeRemaining);
		}
		else if (cpuNumber == 2)
		{
			cpu2TextPane.setText("Process: " + process + "\nTime Remaining (ms): " + timeRemaining);
		}
	}
	
	/**
	 * Sets the system state variable and updates the system state text.
	 * @param b system state: true for running, false for paused
	 */
	public void setSystemState(boolean b)
	{
		systemState = b;
		
		if(systemState)
		{
			systemStateLabel.setText("System Running");
		}
		else
		{
			systemStateLabel.setText("System Paused");
		}
	}
}
