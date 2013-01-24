package org.dz.launcher;

import java.util.List;

public interface Task {

	public void run(List<String> args) throws TaskException;
}
