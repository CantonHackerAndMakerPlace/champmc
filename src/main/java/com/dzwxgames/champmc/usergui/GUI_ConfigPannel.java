package com.dzwxgames.champmc.usergui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GUI_ConfigPannel implements DocumentListener, ChangeListener, ActionListener {
	private final JPanel panel = new JPanel(new GridBagLayout());

	public int ramsize;
	public int permsize = 256;
	public int cpucount;
	public String javafile = "java";
	public boolean showlog;

	// Java file variables
	private final JPanel lpanel;
	private final JFileChooser fc = new JFileChooser();
	private final JButton browsebutton = new JButton("BROWSE FOR JAVA.EXE");
	private final JTextField javalocation = new JTextField();

	// Cpu ram log values
	private final JSlider cpu;
	private final JCheckBox showlogbox;
	private final JSlider memory;

	public GUI_ConfigPannel(GUI gui) {
		// Config

		/* Java location Panel */ {
			// File chooser
			fc.setCurrentDirectory(new File("C:\\Program Files\\Java\\"));
			fc.setSelectedFile(new File("java.exe"));
			GridBagConstraints c = new GridBagConstraints();
			lpanel = new JPanel(new GridBagLayout());
			// Browse Button
			browsebutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// display/center the jdialog when the button is pressed
					// Handle open button action.
					if (e.getSource() == browsebutton) {
						int returnVal = fc.showOpenDialog(panel);

						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fc.getSelectedFile();
							if (file.exists()) {
								javalocation.setText(file.getAbsolutePath());
							} else {
								JOptionPane.showMessageDialog(fc,
										"The file: " + file.getAbsolutePath() + " was not found. Try again.");
							}
						} else {
							// log.append("Open command cancelled by user." + newline);
						}
					}
				}
			});
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 0;
			lpanel.add(browsebutton, c);

			// Textbox
			javalocation.setText(GUI.getPref("JAVA", ""));
			javalocation.setEditable(false);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 1;
			lpanel.add(javalocation, c);
			javalocation.getDocument().addDocumentListener(this);

			// Scrollbar
			JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
			BoundedRangeModel brm = javalocation.getHorizontalVisibility();
			scrollBar.setModel(brm);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 2;
			lpanel.add(scrollBar, c);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 0;
			panel.add(lpanel, c);
		}

		/* Java Ram and CPU config */ {
			GridBagConstraints c = new GridBagConstraints();
			final JPanel mempanel = new JPanel(new GridBagLayout());

			// Memory
			// Label
			JLabel ramlabel = new JLabel("Ram: ");
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.0;
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 0;
			mempanel.add(ramlabel, c);
			// Slider
			int rammin = 4;
			int rammax = (int) (Runtime.getRuntime().maxMemory() / 1000000000);
			memory = new JSlider(JSlider.HORIZONTAL, rammin, rammax, rammin);
			memory.setValue(Integer.parseInt(GUI.getPref("MEMORY", Integer.toString(rammin))));
			memory.addChangeListener(this);
			memory.setPaintTicks(true);
			memory.setPaintLabels(true);
			Font font = new Font("Serif", Font.ITALIC, 15);
			memory.setFont(font);
			// Create the label table
			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			for (int i = rammin; i < rammax; i += 2) {
				labelTable.put(new Integer(i), new JLabel(i + "GB"));
			}
			memory.setLabelTable(labelTable);
			memory.setMajorTickSpacing(1);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.gridwidth = 2;
			c.gridx = 1;
			c.gridy = 0;
			mempanel.add(memory, c);

			JLabel cpulabel = new JLabel("Threads: ");
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.0;
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 1;
			mempanel.add(cpulabel, c);
			cpu = new JSlider(JSlider.HORIZONTAL, 1, Runtime.getRuntime().availableProcessors(),
					Runtime.getRuntime().availableProcessors());
			cpu.addChangeListener(this);
			cpu.setPaintTicks(true);
			cpu.setPaintLabels(true);
			cpu.setFont(font);
			// Create the label table
			Hashtable<Integer, JLabel> clabelTable = new Hashtable<Integer, JLabel>();
			for (int i = 1; i <= Runtime.getRuntime().availableProcessors(); i++) {
				clabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
			}
			cpu.setLabelTable(clabelTable);
			cpu.setMajorTickSpacing(1);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.gridwidth = 2;
			c.gridx = 1;
			c.gridy = 1;
			mempanel.add(cpu, c);
			cpu.setValue(
					Integer.parseInt(GUI.getPref("CPU", Integer.toString(Runtime.getRuntime().availableProcessors()))));

			showlogbox = new JCheckBox("Show Log?");
			showlogbox.addActionListener(this);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.0;
			c.gridwidth = 3;
			c.gridx = 1;
			c.gridy = 2;
			mempanel.add(showlogbox, c);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 1;
			panel.add(mempanel, c);
		}
		updateValues();
	}

	public void updateValues() {
		ramsize = memory.getValue();
		// TODO: Set Permsize?
		cpucount = cpu.getValue();
		javafile = javalocation.getText();
		showlog = showlogbox.isSelected();

		GUI.setPref("CPU", Integer.toString(cpucount));
		GUI.setPref("MEMORY", Integer.toString(ramsize));
		GUI.setPref("JAVA", javafile);
	}

	public void add(JFrame frame) {
		frame.getContentPane().add(BorderLayout.CENTER, panel);
	}

	public void remove(JFrame frame) {
		frame.getContentPane().remove(panel);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		updateValues();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		updateValues();
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {

	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		updateValues();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {

	}

}
