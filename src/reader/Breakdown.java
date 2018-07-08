package reader;

public class Breakdown {
	private int frameNum;
	private String phoneme;
	
	public Breakdown(int frameNum, String phoneme) {
		this.phoneme = phoneme;
		this.frameNum = frameNum;
	}
	
	public String getPh() {
		return this.phoneme;
	}
	
	public int getFrameNum() {
		return this.frameNum;
	}
}
