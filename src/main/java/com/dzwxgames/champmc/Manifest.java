package com.dzwxgames.champmc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.DatatypeConverter;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Manifest {

	public Map<String, String> filelist = new HashMap<String, String>(); // <Filename>,<MD5HASH>
	public List<String> librarylist = new Vector<String>();
	public String forgeversion = "1.12.2-forge1.12.2-14.23.5.2824";

	public Manifest(String site) {
		String jsonstring = getUrlContents(site + "manifest");
		if (jsonstring.length() != 0) {
			try {
				read(jsonstring);
			} catch (Exception e) {
				System.out.println("Failed to parse from website.");
			}
		} else {
			try {
				jsonstring = new String(Files.readAllBytes(Paths.get("filetest.json")));
				read(jsonstring);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed to parse From file...");
			}
		}
	}

	private String getUrlContents(String site) {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		String contents = "";
		try {
			url = new URL(site);
			is = url.openStream(); // throws an IOException

			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				contents += line + "\n";
				// System.out.println(line);
			}
		} catch (MalformedURLException mue) {
			// mue.printStackTrace();
			System.out.println("INVALID URL: " + site);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Timed out URL: " + site);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
		return contents;
	}

	public boolean checkFileHash(String filename, String checksum) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		try {
			md.update(Files.readAllBytes(Paths.get(filename)));
		} catch (IOException e) {
			System.out.println("File " + filename + " not on disk");
			return false;
		}
		byte[] digest = md.digest();
		String myChecksum = DatatypeConverter.printHexBinary(digest).toUpperCase();
		checksum = checksum.toUpperCase();
		if (!myChecksum.equals(checksum)) {
			System.out.println(checksum + " != " + myChecksum);
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void read(String jsoncontent) throws Exception {
		Object obj = new JSONParser().parse(jsoncontent);

		// typecasting obj to JSONObject
		JSONObject jo = (JSONObject) obj;

		// getting forgeversion
		forgeversion = (String) jo.get("forgeversion");

		// Get list of files and hashes
		Object filelistobj = jo.get("filelist");
		if (filelistobj instanceof HashMap) {
			filelist = ((Map<String, String>) filelistobj);
		}

		// Get list of librarys
		Object liblistobj = jo.get("librarylist");
		if (liblistobj instanceof List) {
			librarylist = ((List<String>) liblistobj);
		}
	}

	public static void write() // throws FileNotFoundException
	{
		/*
		 * JSONObject jo = new JSONObject();
		 * 
		 * // putting data to JSONObject //jo.put("firstName", "John");
		 * //jo.put("lastName", "Smith"); //jo.put("age", 25);
		 * 
		 * // for address data, first create LinkedHashMap Map m = new LinkedHashMap(4);
		 * m.put("streetAddress", "21 2nd Street"); m.put("city", "New York");
		 * m.put("state", "NY"); m.put("postalCode", 10021);
		 * 
		 * // putting address to JSONObject jo.put("address", m);
		 * 
		 * // for phone numbers, first create JSONArray JSONArray ja = new JSONArray();
		 * 
		 * m = new LinkedHashMap(2); m.put("type", "home"); m.put("number",
		 * "212 555-1234");
		 * 
		 * // adding map to list ja.add(m);
		 * 
		 * m = new LinkedHashMap(2); m.put("type", "fax"); m.put("number",
		 * "212 555-1234");
		 * 
		 * // adding map to list ja.add(m);
		 * 
		 * // putting phoneNumbers to JSONObject jo.put("phoneNumbers", ja);
		 * 
		 * // writing JSON to file:"JSONExample.json" in cwd PrintWriter pw = new
		 * PrintWriter("JSONExample.json"); pw.write(jo.toJSONString());
		 * 
		 * pw.flush(); pw.close();
		 * 
		 */
	}
}