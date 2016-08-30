package exetuor.reload;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReloadThread extends Thread {
	private Logger log = null;
	private File reloadDir;
	private ClassFileFilter filter;
	private Instrumentation inst;
	private Map<String, Entry> entries = new LinkedHashMap();

	public ReloadThread(File reloadDir, File logPath, Instrumentation inst) {
		super("ReloadThread");
		this.inst = inst;
		this.reloadDir = reloadDir;
		this.filter = new ClassFileFilter();
		this.log = new Logger("ReloadThread", logPath);

		setDaemon(true);
		setPriority(10);
	}

	public void run() {
		this.log.info("--------------------start--------------------");
		for (;;) {
			try {
				sleep(5000L);
			} catch (InterruptedException e) {
			}
			String absPath = this.reloadDir.getAbsolutePath() + File.separatorChar;
			List<String> dirNames = FileUtil.listDirAllFiles(absPath, this.filter);
			if ((dirNames != null) && (!dirNames.isEmpty())) {
				for (String dirName : dirNames) {
					File file = new File(absPath + dirName).getAbsoluteFile();

					Entry e = new Entry();
					e.name = dirName.substring(0, dirName.lastIndexOf(".")).replace(File.separatorChar, '/');
					e.file = file;
					if ((!this.entries.containsKey(e.name)) || (((Entry) this.entries.get(e.name)).lastModified != file.lastModified())) {
						this.entries.put(e.name, e);
					}
				}
				findGroups();
				if (!this.entries.isEmpty()) {
					this.log.debug("Checking changes...");
					List<Entry> aux = new ArrayList(this.entries.values());
					for (Entry e : aux) {
						if (e.isDirty()) {
							e.forceDirty();
						}
					}
					File dir = new File(absPath);
					try {
						reload(dir, aux);
						if (dir.exists()) {
							FileUtil.deleteAllFiles(dir);
						}
						this.entries.clear();
					} catch (Exception e) {
						this.log.error("Reload Error", e);
					}
				}
			}
		}
	}

	private void reload(File dir, List<Entry> aux) throws IOException, ClassNotFoundException, UnmodifiableClassException {
		List<ClassDefinition> definitionList = new ArrayList();
		for (Entry e : aux) {
			if ((e.isDirty()) && (e.parent == null)) {
				try {
					String className = e.name.replaceAll("\\/", ".");
					className = className.replaceAll("\\\\", ".");

					URL[] urls = { dir.toURL() };
					URLClassLoader urlcl = new URLClassLoader(urls);
					Class clazz = urlcl.loadClass(className);

					this.log.info("Reload : className = " + className + ", FileName = " + e.file);

					byte[] bytes = FileUtil.loadClassData(e.file);
					ClassDefinition definition = new ClassDefinition(clazz, bytes);
					definitionList.add(definition);
				} catch (Exception t) {
					this.log.error("Could not reload " + e.name, t);
				}
				e.clearDirty();
			} else {
				this.log.error("Reload : isDiry = " + e.isDirty() + ", fileName = " + e.file.getAbsolutePath());
			}
		}
		if (definitionList.size() > 0) {
			this.inst.redefineClasses((ClassDefinition[]) definitionList.toArray(new ClassDefinition[definitionList.size()]));
		}
	}

	private void findGroups() {
		for (Map.Entry<String, Entry> e : this.entries.entrySet()) {
			String n = ((Entry) e.getValue()).name;
			int p = n.indexOf('$');
			if (p != -1) {
				String parentName = n.substring(0, p);
				((Entry) this.entries.get(parentName)).addChild((Entry) e.getValue());
			}
		}
	}

	private class ClassFileFilter implements FileFilter {
		private ClassFileFilter() {
		}

		public boolean accept(File pathname) {
			return (pathname.isDirectory()) || (pathname.getName().endsWith(".class"));
		}
	}
}