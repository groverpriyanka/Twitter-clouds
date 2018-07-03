package edu.brandeis.cs12b.PA8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class HosebirdAPI {
	
	/**		
	 *
	 * Method to configure the Hosebird Logging System 
	 * to stop the unnecessary debugging information
	 * @param No arguments
	 * @return the hosts that will capture the specific tweets
	 */
	public Hosts configuringHosebird(){
		BasicConfigurator.configure();
		List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
		loggers.add(LogManager.getRootLogger());
		for(Logger logger : loggers){
			logger.setLevel(Level.OFF);
		}
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		return hosebirdHosts;
	}
	
	/**		
	 *
	 * Method to configure Hosebird to filter statuses for search terms 
	 * to stop the unnecessary debugging information
	 * @param Takes the terms array to search
	 * @return HosebirdEndpoint which contains statuses with these terms
	 */
	public StatusesFilterEndpoint getHosebirdEndpoint(String[] args){
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		List<String> terms = new ArrayList<String>();
		for(int i = 0; i<args.length; i++){
			terms.add(args[i]);
		}
		hosebirdEndpoint.trackTerms(terms);
		return hosebirdEndpoint;
	}
}
