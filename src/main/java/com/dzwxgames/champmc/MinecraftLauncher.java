package com.dzwxgames.champmc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import com.dzwxgames.champmc.usergui.GUI_LogPannel;

public class MinecraftLauncher {
	private String javaexe = "";
	private String folder;
	Map<String, String> arguments = new HashMap<String, String>();
	private String classname = "net.minecraft.launchwrapper.Launch";
	Map<String, String> classarguments = new HashMap<String, String>();
	List<String> librarys = new Vector<String>();

	// Online UUID: GET
	// https://api.mojang.com/users/profiles/minecraft/<username>?at=<timestamp>
	// Ofline UUID: UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" +
	// "deathmock5").getBytes());

	public MinecraftLauncher(String version, String mfolder, String javafile) {
		javaexe = javafile;
		folder = mfolder;

		// addArgKey("Dos.name","Windows 10");
		// addArgKey("Dos.version","10.0");
		addArgKey("Djava.library.path", String.format("%s\\versions\\%s\\natives", folder, version));
		addArgKey("Dminecraft.applet.TargetDirectory", folder);
		// addArgKey("-XX:+UseConcMarkSweepGC","");
		addArgKey("Dfml.ignoreInvalidMinecraftCertificates", "true");
		addArgKey("Dfml.ignorePatchDiscrepancies", "true");
		// net.minecraft.launchwrapper.Launch

		addClassKey("gameDir", folder); // --gameDir C:\Users\death\AppData\Roaming\.minecraft
		addClassKey("version", version);// --version 1.12.2-forge1.12.2-14.23.5.2824
		addClassKey("assetsDir", folder + "\\assets");// --assetsDir C:\Users\death\AppData\Roaming\.minecraft\assets
		addClassKey("assetIndex", "1.12");// --assetIndex 1.12 //TODO: Fix me.
		addClassKey("accessToken", "null");// --accessToken null
		addClassKey("userType", "legacy");// --userType legacy
		addClassKey("tweakClass", "net.minecraftforge.fml.common.launcher.FMLTweaker");// --tweakClass
																						// net.minecraftforge.fml.common.launcher.FMLTweaker
		addClassKey("versionType", "Forge");// --versionType Forge
		addClassKey("width", "800");// --width 925
		addClassKey("height", "600");// --height 530
	}

	public void addLibrarys(List<String> libs) {
		for (String key : libs) {
			addLibrary(key);
		}
	}

	public void addLibrary(String lib) {
		librarys.add(folder + lib);
	}

	public void setPermSize(int size) {
		addArgKey(String.format("Xmn%dM", size), "");
	}

	public void setRamSize(int size) {
		addArgKey(String.format("Xmx%dG", size), "");
	}

	private void addArgKey(String key, String value) {
		if (value.contains(" ")) {
			value = "\"" + value + "\"";
		}
		arguments.put(key, value);
	}

	private void addClassKey(String key, String value) {
		if (value.contains(" ")) {
			value = "\"" + value + "\"";
		}
		classarguments.put(key, value);
	}

	public String launch(String name, int ram, int perm, int cpucount, GUI_LogPannel log) {
		/* TODO: dont set these values. Calculate them. */ {
			setRamSize(ram);
			setPermSize(perm);
			addClassKey("username", name); // --username deathmock5
			UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
			addClassKey("uuid", offlineUUID.toString());// --uuid 00000000-0000-0000-0000-000000000000

		}

		String command = "";

		/* Set JAVA EXE */ {
			command += "\"" + javaexe + "\" "; // Set JAVA command
		}

		/* Set java configs. */ {
			for (String key : arguments.keySet()) {
				if (arguments.get(key).length() != 0) {
					command += "-" + key + "=" + arguments.get(key) + " ";
				} else {
					command += "-" + key + " ";
				}
			}
		}

		/* Load java librarys */ {
			command += "-cp ";
			for (String key : librarys) {
				command += key + ";";
			}

		}

		// Set java class
		command += " " + classname + " ";

		/* Set arguments for .java */ {
			for (String key : classarguments.keySet()) {
				if (classarguments.get(key).length() != 0) {
					command += "--" + key + " " + classarguments.get(key) + " ";
				} else {
					command += "--" + key + " ";
				}
			}
		}

		System.out.println(command);
		return launchCMD(command, log);
	}

	String launchCMD(String command, GUI_LogPannel log) {
		String errorval = "";
		try {
			Process p = Runtime.getRuntime().exec(command, null, new File(folder));
			String s = null;
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				log.addLine(s);
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				errorval += s + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return errorval;
	}
}
