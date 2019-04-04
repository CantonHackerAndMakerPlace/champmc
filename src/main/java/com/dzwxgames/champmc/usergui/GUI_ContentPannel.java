package com.dzwxgames.champmc.usergui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

public class GUI_ContentPannel {
	JEditorPane website;
	JScrollPane panel;

	public GUI_ContentPannel(String url) {
		website = new JEditorPane();
		website.setEditable(false);
		website.setContentType("text/html");
		panel = new JScrollPane(website);

		new Thread(new Runnable() {
			@Override
			public void run() {
				URL site;
				String document = "";
				try {
					site = new URL(url);
					BufferedReader in = new BufferedReader(new InputStreamReader(site.openStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null)
						document += inputLine;
					in.close();
				} catch (MalformedURLException e) {
					// e.printStackTrace();
					System.out.println("INVALID URL: " + url);
				} catch (IOException e) {
					// e.printStackTrace();
					System.out.println("Failed to access: " + url);
				}
				website.setText(document);
				website.revalidate();
			}
		}).start();
	}

	public void add(JFrame frame) {
		frame.getContentPane().add(BorderLayout.CENTER, panel);
	}

	public void remove(JFrame frame) {
		frame.getContentPane().remove(panel);
	}

}
