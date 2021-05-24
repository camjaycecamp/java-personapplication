/*
 * cameron campbell
 * advanced java
 * occc spring 2021
 * persongui
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.table.*;
import javax.swing.*;

public class PersonGUI extends JFrame 
implements ActionListener
{
	// global objects, fields and controls
	private JFrame aboutFrame;
	private JMenu fileMenu, helpMenu;
	private JMenuBar bar;
	private JMenuItem fileMenu_new, fileMenu_open, fileMenu_save,
	fileMenu_saveas, fileMenu_exit, helpMenu_about;
	private JTable personTable;
	private JPanel aboutPanel;
	private JScrollPane tablePane;
	private JFileChooser fc;
	private JTextArea aboutText;
	private Font font = new Font("Arial", Font.PLAIN, 10);
	private Person p;
	private RegisteredPerson rp;
	private OCCCPerson op;
	private Vector<Person> pVector = new Vector<Person>(0);
	private FileOutputStream fOut;
	private ObjectOutputStream oOut;
	private FileInputStream fIn;
	private ObjectInputStream oIn;
	private static File CURRENT_FILE;
	private DefaultTableModel tableModel;
	
	// main method
	public static void main (String[] args) 
	{
		PersonGUI pg = new PersonGUI();
	}
	
	/*
	 * the constructor houses the basic setup for the frame, menu bar and
	 * menu items. objects, fields, and controls specific to other areas of
	 * the program are setup and used in their corresponding methods.
	 */
	public PersonGUI() 
	{
		super("Person Hierarchy");
		setSize(300, 300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				dispose();
				System.exit(0);
			}
		});
		
		// fileMenu and its items
		bar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		fileMenu_new = new JMenuItem("New...");
		fileMenu_new.addActionListener((event) -> fileMaker());
		fileMenu_new.setMnemonic(KeyEvent.VK_N);
		
		fileMenu_open = new JMenuItem("Open");
		fileMenu_open.addActionListener((event) -> fileOpener());
		fileMenu_open.setMnemonic(KeyEvent.VK_O);
		
		fileMenu_save = new JMenuItem("Save");
		fileMenu_save.addActionListener((event) -> fileSave());
		fileMenu_save.setMnemonic(KeyEvent.VK_S);

		fileMenu_saveas = new JMenuItem("Save As...");
		fileMenu_saveas.addActionListener((event) -> fileSaveAs());
		fileMenu_saveas.setMnemonic(KeyEvent.VK_D);
		
		fileMenu_exit = new JMenuItem("Exit");
		fileMenu_exit.addActionListener((event) -> exitMenu());
		fileMenu_exit.setMnemonic(KeyEvent.VK_X);
		
		fileMenu.add(fileMenu_new);
		fileMenu.add(fileMenu_open);
		fileMenu.add(fileMenu_save);
		fileMenu.add(fileMenu_saveas);
		fileMenu.addSeparator();
		fileMenu.add(fileMenu_exit);
		
		// menu buttons for add and delete
		JButton addButton = new JButton("Add Person");
		JButton deleteButton = new JButton("Delete Person");
		addButton.addActionListener((event) -> addElement());
		deleteButton.addActionListener((event) -> deleteElement());
		
		bar.add(fileMenu);
		bar.add(addButton);
		bar.add(deleteButton);
		setJMenuBar(bar);
		setVisible(true);
		
		// help menu and its item
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		bar.add(Box.createHorizontalGlue());
		bar.add(helpMenu);
		
		helpMenu_about = new JMenuItem("About");
		helpMenu_about.addActionListener((event) -> about());
		helpMenu.add(helpMenu_about);
	}
	
	/*
	 * method that simply exits the program upon selecting the 'Exit'
	 * menu item.
	 */
	public void exitMenu() 
	{
		dispose();
		System.exit(0);
	}
	
	/*
	 * method that opens the file requested from the JFileChooser's open
	 * dialog. if the file is compatible with the program (a.k.a. has at least
	 * one Person object), then it is read into the program before being sent
	 * to the fileHandler method for processing into the GUI.
	 */
	public void fileOpener() 
	{
		fc = new JFileChooser();
		int returnValue = fc.showOpenDialog(PersonGUI.this);
		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			File chosenFile = fc.getSelectedFile();
			CURRENT_FILE = chosenFile;
			try 
			{
				fIn = new FileInputStream(chosenFile);
				oIn = new ObjectInputStream(fIn);
				Object o;
				while(fIn.available() > 0)
				{
					o = oIn.readObject();
					
					if (o.getClass().equals(OCCCPerson.class))
					{
						pVector.add((OCCCPerson) o);
				    }
				    else if (o.getClass().equals(RegisteredPerson.class))
					{
			    		pVector.add((RegisteredPerson) o);
					}
				    else if (o.getClass().equals(Person.class))
				    {
				    	pVector.add((Person) o);
				    }
				}
				oIn.close();
				fIn.close();
				fileHandler(pVector);
			}
			catch(IOException i) 
			{
				i.printStackTrace();
				System.exit(0);
			}
			catch(ClassNotFoundException c)
			{
				System.out.println("There is no Person in this file");
				c.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	/*
	 * method that saves the current file with a special JFileChooser save 
	 * dialog, allowing the user to specify a new name and location for the 
	 * file to be saved under.
	 */
	public void fileSaveAs()
	{
		fc = new JFileChooser();
		int returnValue = fc.showSaveDialog(PersonGUI.this);
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			String fileDir = fc.getSelectedFile().getAbsolutePath();
			Person[] saveArray = new Person[personTable.getRowCount()];
			for (int i = 0; i < personTable.getRowCount(); i++)
			{
				if(personTable.getValueAt(i, 0).toString() == "OCCC Person")
				{
					RegisteredPerson tempRP = new RegisteredPerson(personTable.getValueAt(i, 1).toString(),
							personTable.getValueAt(i, 2).toString(), personTable.getValueAt(i, 3).toString());
					OCCCPerson tempOP = new OCCCPerson(tempRP, personTable.getValueAt(i, 4).toString());
					saveArray[i] = tempOP;
				}
				else if(personTable.getValueAt(i, 0).toString() == "Registered Person") 
				{
					RegisteredPerson tempRP = new RegisteredPerson(personTable.getValueAt(i, 1).toString(),
							personTable.getValueAt(i, 2).toString(), personTable.getValueAt(i, 3).toString());
					saveArray[i] = tempRP;
				}
				else if(personTable.getValueAt(i, 0).toString() == "Person") 
				{
					Person tempP = new Person(personTable.getValueAt(i, 1).toString(), 
							personTable.getValueAt(i, 2).toString());
					saveArray[i] = tempP;
				}
			}
			
			try
			{
				FileOutputStream   fout = new FileOutputStream(fileDir);
				ObjectOutputStream oout = new ObjectOutputStream(fout);

				for(int i = 0; i < saveArray.length; ++i)
				{
					oout.writeObject(saveArray[i]);
				}
			}
			catch(IOException e)
			{
				System.out.println("Writing Error...");
		      	System.out.println(e.toString());
		    }
		}
	}
	
	/*
	 * method that handles the creation of new files should the user
	 * select the 'New...' menu item. a new Vector of type Person is
	 * instantiated and loaded with one blank Person object before it
	 * is sent to the fileHandler method for processing into the GUI.
	 */
	public void fileMaker() 
	{
		Vector<Person> newPV = new Vector<Person>(0);
		newPV.add(new Person("", ""));
		fileHandler(newPV);
	}
	
	public void fileSave() 
	{
		Person[] saveArray = new Person[personTable.getRowCount()];
		for (int i = 0; i < personTable.getRowCount(); i++)
		{
			if(personTable.getValueAt(i, 0).toString() == "OCCC Person") 
			{
				RegisteredPerson tempRP = new RegisteredPerson(personTable.getValueAt(i, 1).toString(),
						personTable.getValueAt(i, 2).toString(), personTable.getValueAt(i, 3).toString());
				OCCCPerson tempOP = new OCCCPerson(tempRP, personTable.getValueAt(i, 4).toString());
				saveArray[i] = tempOP;
			}
			else if(personTable.getValueAt(i, 0).toString() == "Registered Person") 
			{
				RegisteredPerson tempRP = new RegisteredPerson(personTable.getValueAt(i, 1).toString(),
						personTable.getValueAt(i, 2).toString(), personTable.getValueAt(i, 3).toString());
				saveArray[i] = tempRP;
			}
			else if(personTable.getValueAt(i, 0).toString() == "Person") 
			{
				Person tempP = new Person(personTable.getValueAt(i, 1).toString(), 
						personTable.getValueAt(i, 2).toString());
				saveArray[i] = tempP;
			}
		}
		
		try
		{
			fOut = new FileOutputStream(CURRENT_FILE);
			oOut = new ObjectOutputStream(fOut);

			for(int i = 0; i < saveArray.length; ++i)
			{
				oOut.writeObject(saveArray[i]);
			}
		}
		catch(IOException e)
		{
			System.out.println("Writing Error...");
	      	System.out.println(e.toString());
	    }
	}
	
	/*
	 * method that simply displays the popup frame for the helpMenu's
	 * lone item: a concise guide on the mechanics of the GUI.
	 */
	public void about() 
	{
		aboutFrame = new JFrame("About");
		aboutFrame.setLayout(new BorderLayout());
		aboutFrame.setSize(465, 150);
		aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutPanel = new JPanel();
		aboutText = new JTextArea(1, 1);
		aboutText.setFont(font);
		aboutText.setText("Use 'New' to create a new Person."
				+ "\nUse 'Open' to choose a new Person to begin editing."
				+ "\nUse 'Save' to save all changes you've made to the selected Person."
				+ "\nUse 'Save As...' to save all changes you've made to the selected Person as a new, separate file."
				+ "\nUse 'Exit' to exit the program."
				+ "\nThe 'Add Person' button adds a person to the current file."
				+ "\n the 'Delete Person' button removes the selected row from the current file."
				+ "\nYou can change the type of the selected person by clicking on their 'Type' column.");
		aboutPanel.add(aboutText);
		
		aboutFrame.add(aboutPanel, BorderLayout.CENTER);
		aboutFrame.show();
	}
	
	/*
	 * method that reads the passed Vector of type Person into a JTable
	 * control. the first column of the table is given a drop-down list
	 * that allows the user to adjust the type of the person, which in turn
	 * allows or disallows access to the government ID and student ID.
	 */
	@SuppressWarnings("serial")
	public void fileHandler(Vector<Person> pVector) 
	{
		System.out.println(pVector.size());
		this.setSize(800, 300);
		
		String[][] data = new String[pVector.size()][5];
		for(int i = 0; i < pVector.size(); i++) 
		{
			if(pVector.get(i).getClass().equals(OCCCPerson.class)) 
			{
				OCCCPerson tempOP = (OCCCPerson) pVector.get(i);
				data[i] = new String[]{"OCCC Person", tempOP.getFirstName(),
						tempOP.getLastName(), tempOP.getGovernmentID(), 
						tempOP.getStudentID()};
			}
			else if(pVector.get(i).getClass().equals(RegisteredPerson.class)) 
			{
				RegisteredPerson tempRP = (RegisteredPerson) pVector.get(i);
				data[i] = new String[]{"Registered Person", tempRP.getFirstName(),
						tempRP.getLastName(), tempRP.getGovernmentID(), null};
			}
			else if(pVector.get(i).getClass().equals(Person.class)) 
			{
				Person tempP = pVector.get(i);
				data[i] = new String[]{"Person", tempP.getFirstName(),
						tempP.getLastName(), null, null};
			}
		}
		
		String[] columnNames = {"Type", "First Name", "Last Name", "Government ID", "Student ID"};
		
		tableModel = new DefaultTableModel(data, columnNames)
		{
	        @Override
	        public boolean isCellEditable(int row, int column)
	        {
	        	if(personTable.getValueAt(row, 0).toString() == "OCCC Person") 
	        	{
	        		return true;
	        	}
	        	else if(personTable.getValueAt(row, 0).toString() == "Registered Person") 
	        	{
	        		return column == 0 || column == 1 || column == 2 || column == 3;
	        	}
	        	else
	        	{
	        		return column == 0 || column == 1 || column == 2;
	        	}
	        }
	    };
		
		personTable = new JTable(tableModel);
		personTable.setBounds(30, 40, 800, 300);
		
		columnSetup(personTable, personTable.getColumnModel().getColumn(0));
		
		tablePane = new JScrollPane(personTable);
		this.add(tablePane);
		personTable.setModel(tableModel);
	}
	
	/*
	 * method that adds a new row defaulted to type Person to the current file.
	 */
	public void addElement() 
	{
		tableModel.addRow(new Object[] {"Person", "", "", "", ""});
	}
	
	/*
	 * method that removes the row tied to the targeted cell in the current file.
	 */
	public void deleteElement() 
	{
		((DefaultTableModel)personTable.getModel()).removeRow(personTable.getSelectedRow());
	}
	
	/*
	 *  overriden, implemented ActionListener method. i had issues with responding 
	 *  to actionEvents from menu items within the actionPerformed method, so for the
	 *  sake of function i relegated the response of each ActionListener to a corresponding
	 *  method call within the object declaration.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {}
	
	/*
	 * method used within fileHandler to add drop-down menu functionality to the type column, 
	 * allowing the user to change the type of Person a Person object is.
	 */
	public void columnSetup(JTable t, TableColumn tc) 
	{
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("Person");
		comboBox.addItem("Registered Person");
		comboBox.addItem("OCCC Person");
		tc.setCellEditor(new DefaultCellEditor(comboBox));
	}
}
