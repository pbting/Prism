package exetuor.reload;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class Agent {
	static ReloadThread t;
	static Instrumentation inst;

	public static void premain(String args, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException {
		System.out.println("+---------------------------------------------------+");
		System.out.println("| JReloader Agent " + String.format("%-34s", new Object[] { "1.0" }));
		System.out.println("+---------------------------------------------------+");
		if (args == null) {
			System.exit(0);
			return;
		}
		inst = inst;

		String loaddir = null;
		String logDir = null;
		String[] params = args.split("\\,");
		loaddir = params[0];
		logDir = params[1];
		System.out.println("load dir:"+loaddir+"; log dir:"+logDir);
		try {
			File relaodDir = FileUtil.createDir(loaddir);

			File reloadLogDir = FileUtil.createDir(logDir);
			File reloadLogFile = FileUtil.createFile(reloadLogDir.getAbsolutePath() + "//jreloader.log");
			ReloadThread thread = new ReloadThread(relaodDir, reloadLogFile, inst);
			thread.start();
		} catch (Exception e) {
			System.err.println("Args = " + args);
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println(args);
	}
}
