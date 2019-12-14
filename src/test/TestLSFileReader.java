package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import reader.LSFileReader;

class TestLSFileReader {

	@Test
	void test() throws IOException {
		LSFileReader.readFile("C:\\Users\\Herby\\Desktop\\repos\\BM\\ARPG\\bin\\Debug\\data.txt");
	}

}
