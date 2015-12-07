package auction.optimisation.junitTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import auction.optimisation.stage2.Auction;
import auction.optimisation.stage2.Bidder;
import auction.optimisation.stage2.Main;
import auction.optimisation.stage2.OptimisedBidder;
import auction.optimisation.stage2.Optimiser;
import auction.optimisation.stage2.ScenarioGenerator;


public class Prototype2Test {
	
	OptimisedBidder OptimisedBidderTest;
	ScenarioGenerator scenGenTest;
	ArrayList<Bidder> bidders;
	ArrayList<Auction> auctions;
	Optimiser optimiserTest;
	Main mainTest;
	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	DecimalFormat decFor = new DecimalFormat("00.00");

    @Before
    public void setUp() throws Exception
    {
        OptimisedBidderTest = new OptimisedBidder("OptimisedBidderTest", 200);
        scenGenTest = new ScenarioGenerator(5, 20);
        scenGenTest.createItemAndAuction();
        scenGenTest.createBidders();
    	bidders = scenGenTest.getBidders();
    	auctions = scenGenTest.getAuctions();
    }
    
    @Test
    public void testOptimisedBidder() {

    	assertEquals("OptimisedBidderTest", OptimisedBidderTest.getUserName());	
    	assertEquals(200, OptimisedBidderTest.getBudget(), 0);
    }
    
    @Test
    public void testScenarioGenerator() {
    	
    	assertEquals(5, bidders.size());
    	assertEquals(20, auctions.size());
    	
    	assertEquals("Bidder 1", bidders.get(0).getUserName());				
    	assertEquals("item 01", auctions.get(0).getItem().getTitle());
    	assertTrue(auctions.get(0).getItem().getRrp()<=100);
    	assertTrue(auctions.get(0).getItem().getRrp()>0);
    	assertEquals(0, auctions.get(0).getCurrentPrice(), 0);
    	double utility = (int)((auctions.get(0).getItem().getRrp()-auctions.get(0).getCurrentPrice())*100);
    	utility = utility/100;
    	assertEquals((auctions.get(0).getUtility()), utility, 0);	//Test utility is calculated right
    }
        
    @Test
    public void testPlaceBid() {
    	
    	OptimisedBidderTest.placeBid(auctions.get(0), 10, OptimisedBidderTest);
    	//Go through all running threads to get the bid thread created by the placeBid() method
    	Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
    	Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
    	for(int i=0; i<threadArray.length; i++){
    		Thread thread = threadArray[i];
    		if(thread.getName() == "bidThread"){
    			try {
    				//Wait for the bidThread to finish executing to ensure the auction has been updated
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}
    	assertEquals(10, auctions.get(0).getCurrentPrice(), 0);	
    	
    	//Test outbid activates outbid bidder thread
    	bidders.get(0).placeBid(auctions.get(0), auctions.get(0).getCurrentPrice()+auctions.get(0).getMinimumBiddingIncrement(), bidders.get(0));
    	threadSet = Thread.getAllStackTraces().keySet();
    	threadArray = threadSet.toArray(new Thread[threadSet.size()]);
    	for(int i=0; i<threadArray.length; i++){
    		Thread thread = threadArray[i];
    		if(thread.getName() == "bidThread"){
    			try {
					thread.join();
					assertEquals(OptimisedBidderTest, Thread.currentThread());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
    @Test
    public void testAcceptBid() {
    	auctions.get(0).acceptBid(10, OptimisedBidderTest);			
    	assertEquals(10, auctions.get(0).getCurrentPrice(), 0);
    	assertEquals(OptimisedBidderTest, auctions.get(0).getHighBidder());
    	auctions.get(0).acceptBid((auctions.get(0).getCurrentPrice()+auctions.get(0).getMinimumBiddingIncrement()), bidders.get(0));
    	assertEquals((10+auctions.get(0).getMinimumBiddingIncrement()), auctions.get(0).getCurrentPrice(), 0);
    	assertEquals(bidders.get(0), auctions.get(0).getHighBidder());
    }

    @Test
    public void testOptimiser(){
    	
    	optimiserTest = new Optimiser();
    	double [] coefficients = optimiserTest.optimiseAllocation(auctions, OptimisedBidderTest.getBudget(), OptimisedBidderTest);
    	double coefficient;
    	double combinedTotal = 0;
    	for (int i=0; i<coefficients.length; i++){
    		coefficient = coefficients[i];
    		if(coefficient == 1){
    			combinedTotal = combinedTotal + auctions.get(i).getCurrentPrice() + auctions.get(i).getMinimumBiddingIncrement();
    		}
    	}
    	assertTrue(combinedTotal <= OptimisedBidderTest.getBudget());
    }
    
    @Test
    public void testResults() {
    	
    	mainTest = new Main();
    	System.setOut(new PrintStream(outContent));
    	auctions.get(0).acceptBid(auctions.get(0).getCurrentPrice()+auctions.get(0).getMinimumBiddingIncrement(), bidders.get(0));
    	mainTest.printResults(bidders, auctions, OptimisedBidderTest);
    	assertEquals(bidders.get(0).getUserName()+" has made a utility of $"+decFor.format(auctions.get(0).getUtility())+"\n"+bidders.get(1).getUserName()+" has made a utility of $"+decFor.format(0)+"\n"+bidders.get(2).getUserName()+" has made a utility of $"+decFor.format(0)+"\n"+bidders.get(3).getUserName()+" has made a utility of $"+decFor.format(0)+"\n"+bidders.get(4).getUserName()+" has made a utility of $"+decFor.format(0)+"\n"+OptimisedBidderTest.getUserName()+" has made a utility of $"+decFor.format(0)+"\n", outContent.toString());
    }
}
