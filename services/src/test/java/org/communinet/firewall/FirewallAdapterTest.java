package org.communinet.firewall;

import java.io.File;
import java.io.IOException;

import org.communinet.billing.domain.FileUtilities;
import org.communinet.billing.impl.dao.FirewallAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FirewallAdapterTest {

	FirewallAdapter manager;
	
	@Before
	public void setup() throws IOException{
		createTestFile();
		manager = new FirewallAdapter(FileUtilities.FIREWALL_FILENAME);
	}

	
	@Test
	public void testFileCreation(){
		File file = new File(FileUtilities.FIREWALL_FILENAME);
		Assert.assertEquals(true, file.exists());
	}
	
	@Test
	public void testFileDeletion(){
		deleteTestFile();
		File file = new File(FileUtilities.FIREWALL_FILENAME);
		Assert.assertEquals(false, file.exists());
	}

	
	@After
	public void tearDown(){
		deleteTestFile();
	}
	
	private void createTestFile() throws IOException{
		File testFile = new File(FileUtilities.FIREWALL_FILENAME);
		testFile.getParentFile().mkdirs();
		testFile.createNewFile();
		
	}
	
	private void deleteTestFile(){
		File testFile = new File(FileUtilities.FIREWALL_FILENAME);
		deleteFolder(testFile.getParentFile());
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
}
