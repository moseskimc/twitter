package edu.brandeis.cs12b.PA6_Solution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.twitter.hbc.core.Client;

import wordcloud.CollisionMode;
import wordcloud.WordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.scale.LinearFontScalar;

public class TwitterCloud {
	
	/**
	 * The number of tokens you should extract from tweets
	 */
	private static final int NUMBER_TOKENS = 4000;

	
	
	public static void main(String[] a) throws  IOException, InterruptedException{
		TwitterCloud tc = new TwitterCloud();
		tc.makeCloud(new String[] {"donald", "trump"}, "test.png");
	}

	public void makeCloud(String[] args, String filename) throws InterruptedException, IOException {
		// make it happen here! Feel free to throw exceptions.
		
		//We configure our twitter app called "first_cloud"
		
		//create blockingqueue object
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		
		//create a client and connect
		
		TwitterClient me = new TwitterClient(args,msgQueue);
		Client first_cloudClient = me.buildClient();
		first_cloudClient.connect();
		
		
		
		//declare some arrays in order to store the unique stems and all stems.
		List<String> tokens = new LinkedList<String>();
		ArrayList<String> unique = new ArrayList<>();
		
		//Now we are ready to go through the tweets having the key words in the array args
		
		while (tokens.size() < NUMBER_TOKENS) {
			String msg = msgQueue.take();
		
			
			
			JSONObject tc_json = new JSONObject();
			JSONParser tc_parser = new JSONParser();
			
			
			try {
				tc_json = (JSONObject) tc_parser.parse(msg);
				
				String tweet = (String) tc_json.get("text");
				
				//we use the class AnalyzeTweet to tokenize and stem.
				AnalyzeTweet analyze = new AnalyzeTweet(tweet);
				

				
				for (String word: analyze.generateTokens()){
					tokens.add(word);
					if(!unique.contains(word)){
						unique.add(word);
					}
				}
				
				
				
				
			} catch (Exception e) {
				
			}
			
			
			
			
			
		}
		
		//we terminate the connection
		first_cloudClient.stop();
		
		//we declare a map containing the unique tokens and how often they appear.
		Map<String,Integer> fMap = createMap(unique,tokens);
		
		//we call the createCloud which takes fMap and turns it into a list of frequency,
		//which is then passed to WordCloud method build. Lastly, we write the 
		//the cloud to a file. (Please see method below).
		
		createCloud(fMap, filename);
		
		
		
		
	}
	
	public Map<String,Integer> createMap(ArrayList<String> array, List<String> tweetArray){
		
		Map<String,Integer> fMap = new HashMap<>();
		String token = "";
		int tokenNo = 0;
		
		for (String word: array){
			token = word;
			tokenNo = 0;
			for (String temp: tweetArray){
				if (token.equals(temp)){
					tokenNo++;
				}
			}
			fMap.put(token,tokenNo);
		}
		
		return fMap;
		
	}
	
	void createCloud(Map<String,Integer> fMap, String filename){
		List<WordFrequency> freqList = new LinkedList<>();

		for (String key: fMap.keySet()){
			freqList.add(new WordFrequency(key,fMap.get(key)));
		}
		
		WordCloud wordCloud = new WordCloud(400,400, CollisionMode.RECTANGLE);
		wordCloud.setPadding(0);
		wordCloud.setBackground(new RectangleBackground(400,400));
		wordCloud.setFontScalar(new LinearFontScalar(14,40));
		wordCloud.build(freqList);
		wordCloud.writeToFile(filename);
		
	}

}
