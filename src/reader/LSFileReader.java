package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads in files
 * @author Emmanuel Douge
 *
 */
public class LSFileReader {

	/**
	 * Reads in a file and outputs the data in it
	 * @param filename the file containing the data
	 * @throws IOException if file not found
	 */
	public static Data readFile(String filename) throws IOException
	{
		Data d = new Data(filename);
		System.out.println(d.getFile().getName());
		int dotIndex = filename.lastIndexOf('.');
		String ext = filename.substring(dotIndex+1);
		if(ext.equals("dat")) {
			readDatFile(d);
		} else if (ext.equals("pgo")) {
			readPGOFile(d);
		} else if (ext.equals("osu") || ext.equals("txt")) {
			readOSUFile(d);
		}

		return d;
	}

	private static void readDatFile(Data d) throws FileNotFoundException, IOException {

		FileReader r =  new FileReader(d.getFilename());
		BufferedReader br = new BufferedReader(r);
		String currentLine = null;
		//eat first line
		currentLine = br.readLine();
		ArrayList<Breakdown> bds = new ArrayList<Breakdown>();
		while ((currentLine = br.readLine()) != null) {
			System.out.println(currentLine);
	        String[] data = currentLine.split(" ");
	        if(data.length == 1) {
	        	bds.add(new Breakdown(Integer.parseInt(data[0])+1, "etc"));
	        } else {
	        	bds.add(new Breakdown(Integer.parseInt(data[0])+1, data[1]));
	        }
		}
		bds.add(new Breakdown(bds.get(bds.size()-1).getFrameNum()+1, "filler"));
		d.setBreakdowns(bds);
		
		br.close();
	}

	private static void readPGOFile(Data d) throws FileNotFoundException, IOException {

		FileReader r =  new FileReader(d.getFilename());
		BufferedReader br = new BufferedReader(r);
		String currentLine = null;
		//eat 8 lines
		for(int i = 0; i < 8; i++)
		currentLine = br.readLine();
		
		ArrayList<Breakdown> bds = new ArrayList<Breakdown>();
		while ((currentLine = br.readLine()) != null) {
	        String[] wordData = currentLine.split(" ");
	        if(wordData.length == 4) {
		        for(int i = 0; i < Integer.parseInt(wordData[3]); i++) {
		        	currentLine = br.readLine();
			        String[] data = currentLine.split(" ");
			        
			        bds.add(new Breakdown(Integer.parseInt(data[0].replaceAll("\\s", ""))+1, data[1], wordData[0])); 
		        }
	        }
		}
		bds.add(new Breakdown(bds.get(bds.size()-1).getFrameNum()+1, "filler"));
		d.setBreakdowns(bds);
		
		br.close();
	}


	private static void readOSUFile(Data d) throws FileNotFoundException, IOException {

		FileReader r =  new FileReader(d.getFilename());
		BufferedReader br = new BufferedReader(r);
		String currentLine = null;
		while ((currentLine = br.readLine()) != null && !currentLine.contains("[TimingPoints]")) { }
		ArrayList<Breakdown> bds = new ArrayList<Breakdown>();
    	bds.add(new Breakdown(0, "rest"));
		while ((currentLine = br.readLine()) != null && !currentLine.contains("[HitObjects]") && !currentLine.trim().isEmpty()) {
			System.out.println(currentLine);
	        String[] data = currentLine.split(",");
	        int frameNum = (Integer.parseInt(data[0])/42)+1;
        	bds.add(new Breakdown(frameNum, "etc"));
        	bds.add(new Breakdown(frameNum+1, "rest"));
		}
		d.setBreakdowns(bds);
		
		br.close();
	}

}
