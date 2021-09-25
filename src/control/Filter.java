package control;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class Filter extends FileFilter {

	@Override
	public boolean accept(File file) {
		
		if(file.getName().endsWith(".jar") | file.getName().endsWith(".exe") 
										   | file.getName().endsWith(".bat")) {
			return true;
		}
		if(file.isDirectory()) {
			return true;			
		}
		return false;
	}

	@Override
	public String getDescription() {

		return "JAR, EXE and BAT files only";
	}

}
