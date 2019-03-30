package edu.brandeis.cs12b.PA6_Solution;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;



public class AnalyzeTweet {
	String tweet;

	
	public AnalyzeTweet(String tweet){
		this.tweet = tweet;
	}
	
	//we write a method that creates a TokenStream object
	
	public TokenStream createStream(String tweet){
		TokenStream sf = null;
		
		try (EnglishAnalyzer an = new EnglishAnalyzer()){
		
		try {
		
			sf = an.tokenStream(null, tweet);
		}
		catch (Exception e){
			System.err.println("Could not tokenize string: " + e);
		}
		
		
		return sf;
	}
	}
	
	//we write a method that returns a chartermattribute object
	
	public CharTermAttribute createAttribute(TokenStream stream){
		CharTermAttribute cta = stream.getAttribute(CharTermAttribute.class);
		
		return cta;
	}
	
	//next, we generate the array that will contain the stems.
	
	public LinkedList<String> generateTokens() throws IOException{
		LinkedList<String> list = new LinkedList<>();
		
		String token;
		
		
		try (EnglishAnalyzer an1 = new EnglishAnalyzer()){
			
			TokenStream stream = an1.tokenStream(null,tweet);
		
			//stream.reset();
		
		try {
			stream.reset();
			while (stream.incrementToken()){
				CharTermAttribute cta = stream.getAttribute(CharTermAttribute.class);
				token = cta.toString(); //stem and convert to string.
			
				
				
				
				if (!token.startsWith("http") && !token.startsWith("t.co") && !token.startsWith("rt")){
					list.add(token);
				}
				
				
				
				
				
			}
		}
		catch (Exception e){
			System.err.println("Could not tokenize string: " + e);
		}
		}

		
		return list;
	}
	
	
	
	public Map<String,Integer> generateMap() throws IOException{
		LinkedList<String> list = new LinkedList<>();
		TokenStream stream = createStream(tweet);
		String token;
		int tokenNo;
		
		Map<String,Integer> fMap = new HashMap<String,Integer>();
		
	
		
		try {
			stream.reset();
			while (stream.incrementToken()){
				
				token = createAttribute(stream).toString(); //stem and convert to string.
				
				if (!list.contains(token)){
					list.add(token);
				}
				
				
			}
		}
		catch (Exception e){
			System.err.println("Could not tokenize string: " + e);
		}
		
	
		
		
		for (String word: list){
			token = word;
			tokenNo = 0;
			for (String temp: generateTokens()){
				if (token.equals(temp)){
					tokenNo++;
				}
			}
			fMap.put(token,tokenNo);
		}
		
		return fMap;
		
		
	}
	
	
}
