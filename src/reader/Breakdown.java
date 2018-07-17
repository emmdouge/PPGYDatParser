package reader;

public class Breakdown {
	private int frameNum;
	private String phoneme;
	private String word;
	
	public Breakdown(int frameNum, String phoneme) {
		this(frameNum, phoneme, "--");
	}
	
	public Breakdown(int frameNum, String phoneme, String word) {
		this.phoneme = phoneme;
		this.frameNum = frameNum;
		this.word = word;
	}
	
	public String getPh() {
		return this.phoneme;
	}
	
	public int getFrameNum() {
		return this.frameNum;
	}
	
	public String getWord() {
		return this.word;
	}
}
