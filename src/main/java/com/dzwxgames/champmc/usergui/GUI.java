package com.dzwxgames.champmc.usergui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.prefs.Preferences;

import javax.swing.*;
import com.dzwxgames.champmc.*;

public class GUI {
	private JFrame frame;
	GUI_UsernamePanel toppannel;
	GUI_LaunchPanel bottompannel;
	GUI_ContentPannel middlepannel;
	GUI_ConfigPannel configpannel;
	public GUI_LogPannel logpannel;
	boolean viewconfig = false;
	public Manifest manifest;

	public final String website = "http://minecraft.dzwxgames.com/";
	public final String gamedir = System.getProperty("user.dir") + "\\minecraft"; // System.getenv("APPDATA") +
	// "\\.minecraft";
	private static Preferences prefs;

	public static String getPref(String name, String deft) {
		if (prefs != null)
			return prefs.get(name, deft);
		return deft;
	}

	public static void setPref(String name, String value) {
		prefs.put(name, value);
	}

	public GUI() {

		System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
		System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, "mclauncher.prefs");
		FilePreferencesFactory factory = new FilePreferencesFactory();
		prefs = factory.userRoot();

		// Set up frame
		frame = new JFrame("DZWX MINECRAFT LAUNCHER");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(450, 400);
		frame.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

		// Username Entry Panel
		toppannel = new GUI_UsernamePanel(this);
		toppannel.add(frame);

		// Info page
		middlepannel = new GUI_ContentPannel(website);
		middlepannel.add(frame);

		// Launch Pannel
		bottompannel = new GUI_LaunchPanel(this);
		bottompannel.add(frame);

		// Config Pannel
		configpannel = new GUI_ConfigPannel(this);

		// LogPannel
		logpannel = new GUI_LogPannel();

		// System.out.println(System.getenv("APPDATA") + "\\.minecraft");
	}

	public void show() {
		// Add content
		frame.setVisible(true);
	}

	public void toggleConfig(JButton btn) {
		if (viewconfig) {
			// We are showing config
			viewconfig = false;
			middlepannel.add(frame);
			configpannel.remove(frame);
			btn.setText("Config");
		} else {
			configpannel.add(frame);
			middlepannel.remove(frame);
			viewconfig = true;
			btn.setText("Close");
		}
		frame.revalidate();
		frame.repaint(); // sometimes needed
	}

	public void launchGame() {
		MinecraftLauncher launcher = new MinecraftLauncher(manifest.forgeversion, gamedir, configpannel.javafile);
		for (String lib : manifest.librarylist) {
			launcher.addLibrary(lib);
		}
		logpannel.setShow(configpannel.showlog);
		frame.setVisible(false);
		launcher.launch(toppannel.username, configpannel.ramsize, configpannel.permsize, configpannel.cpucount,
				logpannel);
		frame.setVisible(true);
	}
}