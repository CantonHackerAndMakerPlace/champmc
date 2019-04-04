package com.dzwxgames.champmc.usergui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

public class GUI_UsernamePanel {
	private JPanel panel = new JPanel();
	private JTextField usernamebox;
	private JLabel usernamelabel;
	private JButton buttonconfig;
	public String username;

	GUI_UsernamePanel(GUI gui) {
		usernamelabel = new JLabel("Username: ");
		usernamebox = new JTextField(24);
		buttonconfig = new JButton("Config");
		panel.add(usernamelabel);
		panel.add(usernamebox);
		panel.add(buttonconfig);

		String musername = GUI.getPref("USERNAME", "");
		usernamebox.setText(musername);
		usernamebox.getDocument().addDocumentListener(new MyDocumentListener());
		username = usernamebox.getText();

		buttonconfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// display/center the jdialog when the button is pressed
				gui.toggleConfig(buttonconfig);
			}
		});
	}

	public void add(JFrame frame) {
		frame.getContentPane().add(BorderLayout.NORTH, panel);
	}

	class MyDocumentListener implements DocumentListener {
		String newline = "\n";

		public void insertUpdate(DocumentEvent e) {
			GUI.setPref("USERNAME", usernamebox.getText());
			username = usernamebox.getText();
		}

		public void removeUpdate(DocumentEvent e) {
			GUI.setPref("USERNAME", usernamebox.getText());
			username = usernamebox.getText();
		}

		public void changedUpdate(DocumentEvent e) {
			System.out.print("Change");
		}

	}
}
