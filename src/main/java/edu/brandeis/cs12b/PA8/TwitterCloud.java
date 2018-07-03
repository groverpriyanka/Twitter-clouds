package edu.brandeis.cs12b.PA8;

	import java.io.Reader;
/* 
	 * We're including a lot of import statements for you because several of the classes
	 * You'll be using in this assignment have the same names as classes in other
	 * packages, and we don't want you to get confused and use the wrong one. 
	 * You may not use all of these imports, and you might use some that aren't included
	 * here. That's ok!
	*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.sun.prism.paint.Color;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import wordcloud.CollisionMode;
import wordcloud.WordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.CircleBackground;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.font.scale.SqrtFontScalar;
import wordcloud.palette.LinearGradientColorPalette;

import org.json.*;

public class TwitterCloud {
	
	/**
	 * The number of tokens you should extract from tweets
	 */
	private static final int NUMBER_TOKENS = 1000;
	private JSONLuceneAPI JSONLuceneObject = new JSONLuceneAPI();
	private List<String> tokens;
	/**		
	 * Your main client code should go here.
	 * Decide the parameters you want to collect tweets by here, as well as the filename
	 * @param args
	 * @throws InterruptedException 
	 */
	/*
	public static void main(String[] args) throws InterruptedException {
		TwitterCloud tc = new TwitterCloud();
	
		//You may use any search terms to test your code, but we're defaulting to 
		//"donald" and "trump" because there will be a lot of results!
		//tc.makeCloud(new String[] { "donald", "trump" }, "test.png");
		//}
	*/
	/**
	 * Create a word cloud! Remember to use all the tools available in your libraries,
	 * make good decisions on which collections to use to store your data,
	 * and create additional methods as necessary. Use the PA PDF as a guide on how 
	 * to use the various libraries to solve the problem.
	 * 
	 * Remember: There are no tests for this PA, so don't worry about edge cases
	 * or handling bad inputs, etc. Just make your code work, keep it organized,
	 * and be creative!
	 * 
	 * @param args an array of strings to use as a filter for incoming Tweets
	 * @param filename the filename of the image file you should create for your word cloud
	 * @throws InterruptedException 
	 */
	public void makeCloud(String[] args, String filename) throws InterruptedException {
		// make it happen here! Feel free to throw exceptions.
		tokens = JSONLuceneObject.stemmingTokens(NUMBER_TOKENS, args);
		List<WordFrequency> objects = getWordFrequencyList(args);
		WordCloud wordCloud = new WordCloud(400, 400,CollisionMode.RECTANGLE);
		wordCloud.setPadding(0);
		wordCloud.setBackground(new RectangleBackground(400, 400));
		wordCloud.setFontScalar(new LinearFontScalar(14, 40));
		wordCloud.build(objects);
		wordCloud.writeToFile(filename);
		/* Circular word cloud code
		WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
		wordCloud.setPadding(2);
		wordCloud.setBackground(new CircleBackground(300));
		wordCloud.setFontScalar( new SqrtFontScalar(10, 40));
		wordCloud.build(objects);
		wordCloud.writeToFile(filename);
		*/
	}
	public List<WordFrequency> getWordFrequencyList(String[] args) throws InterruptedException{
		List<WordFrequency> objects = new ArrayList<WordFrequency>();
		String[] tempArr = JSONLuceneObject.getSearchTermsArray(args);
		int num = JSONLuceneObject.getNumberOfTokens(NUMBER_TOKENS);
		List<String> temp = JSONLuceneObject.stemmingTokens(num, tempArr);
		HashMap<String, Integer> tempHashMap = tokensToHashMap(temp);
		Set<String> keys = tempHashMap.keySet();
		Iterator<String> keysIte = keys.iterator();
		while(keysIte.hasNext()){
			String key = (String)keysIte.next();
			objects.add(new WordFrequency(key, tempHashMap.get(key)));
		}
		return objects;
	}
	
	/**		
	 *
	 * Method to store the collected tokens in a hashmap
	 * The hashmap maps each token to its value which is the number of
	 * times the token was observed
	 * @param list of tokens
	 * @return the hashmap
	 */
	public HashMap<String, Integer> tokensToHashMap(List<String> tokens){
		HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
		for(String str: tokens){
			if(freqMap.containsKey(str)){
				freqMap.put(str, (freqMap.get(str)+1));
			}
			else {
				freqMap.put(str, 1);
			}
		}
		return freqMap;
	}

	/**
	 * This method will only be called after makeCloud
	 * @return a list of all tokenized words from your word cloud
	 */
	public List<String> getWords(){
		return tokens;
	}
}
