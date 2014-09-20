package org.communinet.billing.impl.dao;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FirewallAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(FirewallAdapter.class);
	private List<String> rules;
	private String filename;
	
	public FirewallAdapter() {
	}
	
	public FirewallAdapter(String filename) {
		this.filename = filename;
	}
	
	public void addRule(String subnet){
		rules.add(subnet);
	}
	
	public void deleteRule(String subnet){
		rules.remove(subnet);
	}
	
	public void writeRules(){
		Path path = FileSystems.getDefault().getPath(".", getFirewallFilename());
		try {
			Files.write(path, rules, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING );
		} catch (IOException e) {
			logger.error("Unable to write to the file {}", getFirewallFilename());
		}
	}
	
	public String getFirewallFilename(){
		return filename;
	}
	
	public void loadRulesFile (){
		Path path = FileSystems.getDefault().getPath(".", getFirewallFilename());
		try {
			rules = Files.readAllLines(path, Charset.defaultCharset());
		} catch (IOException e) {
			logger.error("Unable to read the file {}", getFirewallFilename());
		}
	}

	public void clearRules() {
		rules = new ArrayList<String>();
		
	}
}
