package exetuor.reload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Entry {
	Entry parent;
	List<Entry> children;
	String name;
	File file;
	long lastModified;

	void addChild(Entry e) {
		this.children = (this.children == null ? new ArrayList() : this.children);
		this.children.add(e);
		e.parent = this;
	}

	boolean isDirty() {
		return this.file.lastModified() > this.lastModified;
	}

	void clearDirty() {
		this.lastModified = this.file.lastModified();
		if (this.children != null) {
			for (Entry e : this.children) {
				e.lastModified = e.file.lastModified();
			}
		}
	}

	public void forceDirty() {
		if (this.parent == null) {
			this.lastModified = 0L;
			if (this.children != null) {
				for (Entry e : this.children) {
					e.lastModified = 0L;
				}
			}
		} else {
			this.parent.forceDirty();
		}
	}
}