package reader;

import java.io.File;

public class Lip {
	
	public static File getFile(String ph, String path) {
		return new File("assets/"+path+"/"+ph+".png");
	}
}
