package com.dzwxgames.champmc.usergui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.dzwxgames.champmc.LoadManifestTask;

public class GUI_LaunchPanel {
	private JPanel panel = new JPanel();
	private JButton launchbutton;
	private JProgressBar statusbar;
	private JProgressBar downloadbar;
	private LoadManifestTask manifesttask;

	public GUI_LaunchPanel(GUI gui) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Launch button
		launchbutton = new JButton("LAUNCH");
		launchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(launchbutton);
		launchbutton.setVisible(false);
		launchbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.launchGame();
			}
		});

		// Status
		statusbar = new JProgressBar(0, 100);
		statusbar.setValue(0);
		statusbar.setStringPainted(true);
		statusbar.setString("INIT");
		panel.add(statusbar);

		// Download
		downloadbar = new JProgressBar(0, 100);
		downloadbar.setValue(0);
		downloadbar.setStringPainted(true);
		panel.add(downloadbar);

		// Load up the manifest.
		manifesttask = new LoadManifestTask(this, gui);
		manifesttask.start();
	}

	public void finishedCallback() {
		launchbutton.setVisible(true);
		downloadbar.setVisible(false);
		statusbar.setVisible(false);
	}

	public void add(JFrame frame) {
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
	}

	public void setStatus(String string) {
		statusbar.setString(string);
	}

	public void setStatusValue(int i) {
		statusbar.setValue(i);
	}

	public void setDownload(String string) {
		downloadbar.setString(string);
	}

	public void setDownloadValue(double currentProgress) {
		downloadbar.setValue((int) (currentProgress));
	}

	public void setStatusMax(int i) {
		statusbar.setMaximum(i);
	}
}
