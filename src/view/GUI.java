package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import javax.swing.text.NumberFormatter;

import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JSeparator;
import java.awt.Color;

/**
 * Class that contains a GUI for displaying CPU and process data.
 */
public class GUI {
	private boolean systemState;
	private int timeUnit = 100;
	private JFrame frmCsProject;
	private JButton startButton;
	private JLabel unitLabel;
	private JSeparator separator1;
	private JLabel processQueue1Label;
	private JLabel cpu1Label;
	private JScrollPane processQueue1ScrollPane;
	private JPanel cpu1Panel;
	private JTextPane cpu1TextPane;
	private JLabel statsTableLabel;
	private JScrollPane statsScrollPane;
	private JLabel cpu2Label;
	private JPanel cpu2Panel;
	private JTextPane cpu2TextPane;
	private JButton stopButton;
	private JLabel systemStateLabel;
	private JLabel systemLabel;
	private JTextArea systemTextArea;
	private JFormattedTextField unitTextField;
	private JLabel unitLabel2;
	private JTable processQueue1Table;
	private DefaultTableModel processQueue1TableModel;
	private JTable statsTable;
	private DefaultTableModel statsTableModel;
	private JScrollPane systemScrollPane;
	private JLabel currentTimeLabel;
	private JLabel currentTimeString;
	private JLabel throughputLabel;
	private JLabel throughputValueLabel;

	/**
	 * Constructor of the GUI.
	 * @param processTable table model containing the data for the process table
	 * @param statsModel table model containing the data for the stats table
	 */
	public GUI(DefaultTableModel processTable, DefaultTableModel statsModel) {
		processQueue1TableModel = processTable;
		processQueue1TableModel.addTableModelListener(new TableModelListener () {

			@Override
			public void tableChanged(TableModelEvent e) {
				//revalidate the table any time the table model is change so it is not laggy
				processQueue1Table.revalidate();
			}
			
		});
		
		statsTableModel = statsModel;
		statsTableModel.addTableModelListener(new TableModelListener () {

			@Override
			public void tableChanged(TableModelEvent e) {
				//revalidate the table any time the table model is change so it is not laggy
				statsTable.revalidate();
			}
			
		});
		
		initialize();
		frmCsProject.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//create the frame
		frmCsProject = new JFrame();
		frmCsProject.setTitle("CS 490 Project");
		frmCsProject.setBounds(100, 100, 900, 600);
		frmCsProject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//add a panel with a mig layout to the frame
		JPanel panel = new JPanel();
		frmCsProject.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[grow][100,grow]", "[][11.00][][150,grow][][150.00,grow][][][:150:150,grow]"));
		
		//add the start button the panel
		startButton = new JButton("Start System");
		startButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(startButton, "flowx,cell 0 0");
		
		//add the time unit label to the panel
		unitLabel = new JLabel("1 time unit = ");
		unitLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(unitLabel, "flowx,cell 1 0");
		
		//add a separator between the buttons and the tables
		separator1 = new JSeparator();
		separator1.setForeground(Color.DARK_GRAY);
		panel.add(separator1, "flowx,cell 0 1 2 1,growx");
		
		//add a label for the process queue table
		processQueue1Label = new JLabel("Process Queue");
		processQueue1Label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(processQueue1Label, "cell 0 2");
		
		//add a label for cpu 1
		cpu1Label = new JLabel("CPU 1");
		cpu1Label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(cpu1Label, "cell 1 2");
		
		//add a scroll pane for the process queue table
		processQueue1ScrollPane = new JScrollPane();
		panel.add(processQueue1ScrollPane, "cell 0 3,grow");
		
		//add the process queue table to its scroll pane
		processQueue1Table = new JTable(processQueue1TableModel);
		processQueue1Table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		processQueue1Table.setRowSelectionAllowed(false);
		processQueue1ScrollPane.setViewportView(processQueue1Table);
		
		//create a panel for cpu 1
		cpu1Panel = new JPanel();
		panel.add(cpu1Panel, "cell 1 3,grow");
		cpu1Panel.setLayout(new BorderLayout(0, 0));
		
		//add a text pane to cpu 1's panel to display data
		cpu1TextPane = new JTextPane();
		cpu1TextPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cpu1TextPane.setText("CPU stuff");
		cpu1Panel.add(cpu1TextPane);
		
		//add a label for the stats table to the panel
		statsTableLabel = new JLabel("Finished Processes");
		statsTableLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(statsTableLabel, "cell 0 4");
		
		//add scroll pane for the stats table to the panel
		statsScrollPane = new JScrollPane();
		panel.add(statsScrollPane, "cell 0 5,grow");
		
		//add the stats table to its scroll pane
		statsTable = new JTable(statsTableModel);
		statsTable.setRowSelectionAllowed(false);
		statsTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		statsScrollPane.setViewportView(statsTable);
		
		//add a label for cpu 2 to the panel
		cpu2Label = new JLabel("CPU 2");
		cpu2Label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(cpu2Label, "cell 1 4");
		
		//create a panel for cpu 2
		cpu2Panel = new JPanel();
		panel.add(cpu2Panel, "cell 1 5,grow");
		cpu2Panel.setLayout(new BorderLayout(0, 0));
		
		//add a text pane to cpu 2's panel to display data 
		cpu2TextPane = new JTextPane();
		cpu2TextPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cpu2TextPane.setText("More CPU stuff");
		cpu2Panel.add(cpu2TextPane, BorderLayout.CENTER);
		
		//add a stop button to the panel
		stopButton = new JButton("Stop System");
		stopButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(stopButton, "cell 0 0");
		
		//add a label to display the system's state to the panel
		systemStateLabel = new JLabel("System State");
		systemStateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(systemStateLabel, "cell 0 0");
		
		//add a label for the throughput to the panel
		throughputLabel = new JLabel("Current Throughput: ");
		throughputLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(throughputLabel, "flowx,cell 0 6");
		
		//add a label for the system output to the panel
		systemLabel = new JLabel("System:");
		systemLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(systemLabel, "cell 0 7");
		
		//add a label for the current system time to the panel
		currentTimeLabel = new JLabel("Current System Time: ");
		currentTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(currentTimeLabel, "flowx,cell 1 7");
		
		//add a scroll pane for the systems output to the panel
		systemScrollPane = new JScrollPane();
		panel.add(systemScrollPane, "cell 0 8 2 1,grow");
		
		//add a text area to display system output to its scroll pane
		systemTextArea = new JTextArea();
		((DefaultCaret)systemTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		systemScrollPane.setViewportView(systemTextArea);
		
		//add a text field that only accepts integers greater than 1 for the time time unit to the panel
	    NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(1);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(false);
		unitTextField = new JFormattedTextField(formatter);
		unitTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		unitTextField.setValue((int)100);
		panel.add(unitTextField, "cell 1 0");
		
		//add a label displaying the units of the time unit to the panel
		unitLabel2 = new JLabel("ms");
		unitLabel2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(unitLabel2, "cell 1 0");
		
		//add a label to contain the current time's value to the panel
		currentTimeString = new JLabel("0");
		currentTimeString.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(currentTimeString, "cell 1 7");
		
		//add a label to contain the current throughput value to the panel
		throughputValueLabel = new JLabel("0");
		throughputValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(throughputValueLabel, "cell 0 6");
	}
	
	/**
	 * Adds an action listener to the start button.
	 * @param e listener to be added
	 */
	public void addStartButtonListener(ActionListener e) {
		startButton.addActionListener(e);
	}
	
	/**
	 * Adds an action listener to the stop button.
	 * @param e listener to be added
	 */
	public void addStopButtonListener(ActionListener e) {
		stopButton.addActionListener(e);
	}
	
	/**
	 * Prints a message to the system output with a timestamp at the beginning and new line character at the end.
	 * @param systemTime time stamp
	 * @param text message
	 */
	public void systemPrint(long systemTime, String text) {
		//SimpleDateFormat dateFormat = new SimpleDateFormat("hh:MM:ss");
		systemTextArea.append(Math.round((double)systemTime / (double)timeUnit) + " << " + text + "\n");
	}
	
	/**
	 * Sets the variables displayed in the cpu text pane. Does nothing if cpuNumber is not 1 or 2.
	 * @param process process name
	 * @param timeRemaining time left to process (ms)
	 * @param cpuNumber cpu 1 or 2
	 */
	public void setCPUTextPane(String process, long timeRemaining, int cpuNumber) {
		if (cpuNumber == 1) {
			cpu1TextPane.setText("Process: " + process + "\nTime Remaining (ms): " + Math.round((double)timeRemaining / (double)timeUnit));
		}
		else if (cpuNumber == 2) {
			cpu2TextPane.setText("Process: " + process + "\nTime Remaining (ms): " + Math.round((double)timeRemaining / (double)timeUnit));
		}
	}
	
	/**
	 * Sets the system state variable and updates the system state text.
	 * @param b system state: true for running, false for paused
	 */
	public void setSystemState(boolean b) {
		systemState = b;
		
		if(systemState) {
			systemStateLabel.setText("System Running");
		}
		else {
			systemStateLabel.setText("System Paused");
		}
	}
	
	/**
	 * Gets the current amount (milliseconds) in the time unit text field.
	 * @return milliseconds
	 */
	public int getTimeUnit() {
		return (int) unitTextField.getValue();
	}
	
	/**
	 * Sets the time unit variable that will be used to create time stamps.
	 * @param ms
	 */
	public void setTimeUnit(int ms) {
		timeUnit = ms;
	}
	
	/**
	 * Sets the current system time that is displayed to the gui.
	 * @param time
	 */
	public void setSystemTime(long time) {
		currentTimeString.setText(String.valueOf((int)(time / timeUnit)));
	}
	
	/**
	 * Sets the throughput value displayed in the gui.
	 * @param value
	 */
	public void setThroughputValue(double value) {
		DecimalFormat formatter = new DecimalFormat("#.##");
		throughputValueLabel.setText(formatter.format(value));
	}
}
