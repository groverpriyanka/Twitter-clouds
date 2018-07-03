package edu.brandeis.cs12b.PA8;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.json.JSONObject;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class JSONLuceneAPI {
	private String[] termsToSearchArray;
	private HosebirdAPI hosebirdAPI = new HosebirdAPI();
	private int numberOfTokens;
	private Client hosebirdClient;
	
	/**		
	 *
	 * Method to store tweet data in a list called BlockingQueue
	 * @param number of tokens, array of search terms
	 * @return BlockingQueue
	 */
	
	public BlockingQueue<String> gettingBlockingQueue(int n, String[] temp){
		Authentication hosebirdAuth = new OAuth1(System.getenv("Consumer Key (API Key)"), System.getenv("Consumer Secret (API Secret)"), System.getenv("Access Token"), System.getenv("Access Token Secret"));
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		ClientBuilder builder = new ClientBuilder().hosts(hosebirdAPI.configuringHosebird()).authentication(hosebirdAuth).endpoint(hosebirdAPI.getHosebirdEndpoint(temp)).processor(new StringDelimitedProcessor(msgQueue));
		hosebirdClient = builder.build();
		hosebirdClient.connect();
		return msgQueue;
	}

	/**		
	 *
	 * Method to process tokens and reduce them to stems
	 * @param number of tokens, array of search terms
	 * @return list of stems
	 */
	public List<String> stemmingTokens(int n, String[] temp) throws InterruptedException{
		BlockingQueue<String> msgQueue = gettingBlockingQueue(n,temp);
		List<String> tokens = new LinkedList<String>();
		while(tokens.size() < n){
			try{
			String msg = msgQueue.take();
			//System.out.println(msg);
			JSONObject obj = new JSONObject(msg);
			String text= obj.getString("text");
		    //System.out.println(text);
				try (EnglishAnalyzer an = new EnglishAnalyzer()){
					TokenStream sf = an.tokenStream(null, text);
					try {
						sf.reset();
						while (sf.incrementToken()){
							CharTermAttribute cta = sf.getAttribute(CharTermAttribute.class);
							tokens.add(cta.toString());
						}
					} catch (Exception e) {
						System.err.println("Could not tokenize string: " + e);
					}
				}
			}
			catch (org.json.JSONException exception) {
					System.err.println("Unable to get the access.");
			}
		}
		hosebirdClient.stop();
		return tokens;
	}
	public String[] getSearchTermsArray(String[] termsToSearchArray) {
		return this.termsToSearchArray = termsToSearchArray;
	}
	public int getNumberOfTokens(int numberOfTokens) {
		return this.numberOfTokens = numberOfTokens;
	}
}
