package edu.brandeis.cs12b.PA6_Solution;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterClient {
	
	
	String[] args;
	BlockingQueue<String> msgQueue;
	
	TwitterClient (String[] args, BlockingQueue<String> msgQueue) {
		this.args = args;
		this.msgQueue = msgQueue;
	}
	
	
	
	Hosts createHosts(){
		Hosts first_cloudHosts = new HttpHosts(Constants.STREAM_HOST);
		return first_cloudHosts;
	}
	
	StatusesFilterEndpoint createEndpoint(){
		StatusesFilterEndpoint first_cloudEndpoint = new StatusesFilterEndpoint();
		return first_cloudEndpoint;
	}
	
	void getTerms(StatusesFilterEndpoint endpoint){
		List<String> terms = Arrays.asList(args);
		endpoint.trackTerms(terms);
	}
	
	Authentication createAuth(){
		Authentication first_cloudAuth = new OAuth1 (System.getenv("CONSUMER_KEY"),
				System.getenv("CONSUMER_SECRET"),System.getenv("TOKEN"),
				System.getenv("TOKEN_SECRET"));
		return first_cloudAuth;
	}
	
	
	
	
	

	
	Client buildClient(){
		StatusesFilterEndpoint endpoint = createEndpoint();
		
		getTerms(endpoint);
		
		ClientBuilder builder = new ClientBuilder()
				.hosts(createHosts())
				.authentication(createAuth())
				.endpoint(endpoint)
				.processor(new StringDelimitedProcessor(msgQueue));
		
		Client first_cloudClient = builder.build();
		
		return first_cloudClient;
		
	}
	
	
	
	

	//first_cloudClient.connect();
	
	
	
}
