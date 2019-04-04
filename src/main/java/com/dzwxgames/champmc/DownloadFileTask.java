package com.dzwxgames.champmc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.SwingUtilities;

import com.dzwxgames.champmc.usergui.GUI_LaunchPanel;

public class DownloadFileTask extends Thread {
    GUI_LaunchPanel launchpannel;
    String infile;
    String outfile;

    DownloadFileTask(String inputfile, String outputfile, GUI_LaunchPanel gui_LaunchPanel) {
        launchpannel = gui_LaunchPanel;
        outfile = outputfile;
        infile = inputfile;

    }

    public void run() {
        try {

            URL url = new URL(infile.replace(" ", "%20"));
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            File yourFile = new File(outfile);
            yourFile.getParentFile().mkdirs();
            yourFile.createNewFile(); // if file already exists will do nothing
            java.io.FileOutputStream fos = new java.io.FileOutputStream(outfile);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                // calculate progress
                final double currentProgress = (((double) downloadedFileSize) / ((double) completeFileSize)) * 100d;

                // update progress bar
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        launchpannel.setDownloadValue(currentProgress);
                    }
                });

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("404 Error");
            System.out.println(infile);
        } catch (IOException e) {
            System.out.println("IO ERROR");
            e.printStackTrace();
            System.out.println(infile);
        }
    }
}
