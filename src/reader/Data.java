package reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Data {
	
	private File file;
	private String filename;
	private ArrayList<Breakdown> breakdowns;
	private File outputDir;
	public Data(String filename) throws IOException {
		this.filename = filename;
		this.file = new File(filename);
    	String parentDir = file.getParentFile().getAbsolutePath();
    	this.outputDir = new File(new String(parentDir+"/"+file.getName()+"_frames"));
		this.breakdowns = new ArrayList<Breakdown>();
	}
	
	public void setBreakdowns(ArrayList<Breakdown> bds) {
		this.breakdowns = bds;
	}
	
	public ArrayList<Breakdown> getBreakdowns() {
		return this.breakdowns;
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public File getOutputDir() {
		return this.outputDir;
	}
}
