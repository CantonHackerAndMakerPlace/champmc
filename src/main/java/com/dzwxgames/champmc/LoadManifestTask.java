package com.dzwxgames.champmc;

import java.util.List;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Vector;

import com.dzwxgames.champmc.usergui.*;

public class LoadManifestTask extends Thread {
	GUI_LaunchPanel launchpannel;
	DownloadFileTask downloadtask;
	GUI gui;

	public LoadManifestTask(GUI_LaunchPanel gui_LaunchPanel, GUI mgui) {
		launchpannel = gui_LaunchPanel;
		gui = mgui;
		gui_LaunchPanel.setStatus("LOADING");
		gui_LaunchPanel.setStatusValue(0);
	}

	@Override
	public void run() {
		// Download & Parse Manifest
		launchpannel.setStatus("Downloading Manifest");
		launchpannel.setStatusValue(0);
		gui.manifest = new Manifest(gui.website);
		// Compare Directory to manifest
		launchpannel.setStatus("Syncing Game Files");
		launchpannel.setStatusValue(0);
		launchpannel.setStatusMax(gui.manifest.filelist.size());
		int i = 0;
		// Download New Files
		for (Map.Entry<String, String> entry : gui.manifest.filelist.entrySet()) {
			launchpannel.setStatusValue(i++);
			// Compare hash of oldfile to newfile
			try {
				String filename = gui.gamedir + "\\" + entry.getKey();
				filename = filename.replace("/", "\\");
				if (!gui.manifest.checkFileHash(filename, entry.getValue())) {
					launchpannel.setDownload(entry.getKey());
					downloadtask = new DownloadFileTask(gui.website + "download?file=" + entry.getKey(), filename,
							launchpannel);
					downloadtask.start();
					downloadtask.join();
				}
			} catch (NoSuchAlgorithmException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Check mods folder
		launchpannel.setStatusValue(0);
		checkFolderForShit("mods");
		// Check Versions folder
		checkFolderForShit("versions");
		// Check librarys folder
		checkFolderForShit("libraries");
		launchpannel.finishedCallback();
	}

	private void checkFolderForShit(String foldername) {

		launchpannel.setStatus("Deleteing old " + foldername + " files");
		File folder = new File(gui.gamedir + "\\" + foldername);
		List<String> filesinfolders = listFilesForFolder(folder);
		for (String s : filesinfolders) {
			Boolean found = false;
			String testfilename = gui.gamedir + "\\" + foldername + "\\" + s;
			for (Map.Entry<String, String> entry : gui.manifest.filelist.entrySet()) {
				String compare = gui.gamedir + "\\" + entry.getKey();
				compare = compare.replace("/", "\\");
				if (testfilename.equals(compare)) {
					found = true;
				}
			}
			if (!found) {
				File file = new File(testfilename);
				if (file.delete()) {
					System.out.println(testfilename + " File deleted");
				}
			}
		}
	}

	private List<String> listFilesForFolder(final File folder) {

		List<String> output = new Vector<String>();
		for (final File fileEntry : folder.listFiles()) {

			if (fileEntry.isDirectory()) {
				List<String> filesinfolder = listFilesForFolder(fileEntry);
				for (String filename : filesinfolder) {
					output.add(fileEntry.getName() + "\\" + filename);
				}
			} else {
				output.add(fileEntry.getName());
			}

		}
		return output;
	}
}
