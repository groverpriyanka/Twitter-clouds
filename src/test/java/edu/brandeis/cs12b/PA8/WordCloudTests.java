package edu.brandeis.cs12b.PA8;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import edu.brandeis.cs12b.PA8.TwitterCloud;

public class WordCloudTests {

	
	@Test
	public void test1() throws InterruptedException{
		TwitterCloud tc = new TwitterCloud();
		tc.makeCloud(new String[] {"donald" , "trump" }, "test.png");
		File file = new File("test.png");
		assertTrue(file.exists());
	}

	
	@Test
	public void test2() throws InterruptedException{
		TwitterCloud tc = new TwitterCloud();
		tc.makeCloud(new String[] {"donald" , "trump" }, "test.png");
		List<String> tokenList = tc.getWords();
		assertTrue(!tokenList.isEmpty());
	}
	
	@Test
	public void test3() throws InterruptedException{
		TwitterCloud tc = new TwitterCloud();
		tc.makeCloud(new String[] {"george" , "clooney" }, "test1.png");
		File file = new File("test1.png");
		assertTrue(file.exists());
	}
	
	@Test
	public void test4() throws InterruptedException{
		TwitterCloud tc = new TwitterCloud();
		tc.makeCloud(new String[] {"george" , "clooney" }, "test1.png");
		List<String> tokenList = tc.getWords();
		assertTrue(!tokenList.isEmpty());
	}
}
