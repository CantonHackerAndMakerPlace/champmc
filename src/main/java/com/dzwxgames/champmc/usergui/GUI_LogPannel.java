package com.dzwxgames.champmc.usergui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI_LogPannel {
	private JFrame frame;
	JTextArea textarea;
	boolean showing = false;
	Thread taskmanager;

	public void addLine(String line) {
		if (showing) {
			textarea.setText(textarea.getText() + line + "\n");
		}
	}

	public GUI_LogPannel() {
		frame = new JFrame("LOG");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(800, 400);
		// frame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setVisible(showing);

		textarea = new JTextArea(5, 20);
		JScrollPane scrollpane = new JScrollPane(textarea);
		textarea.setEditable(false);
		frame.add(scrollpane);

		taskmanager = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO: Color?

			}
		});
	}

	public void setShow(boolean showlog) {
		frame.setVisible(showlog);
		showing = showlog;
	}
}
