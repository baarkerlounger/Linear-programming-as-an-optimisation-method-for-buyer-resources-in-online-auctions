package auction.optimisation.stage4;

import java.util.ArrayList;
import java.util.HashMap;

import org.javasim.RestartException;
import org.javasim.SimulationEntity;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;

public class Bidder extends SimulationEntity {

	private double budget;
	private String userName;
	private ArrayList<Auction> Auctions = new ArrayList<Auction>();
	private Auction outbidAuction = null;
	private double biddingProbability; 
	private HashMap<Auction, Double> perceivedRrps = new HashMap<Auction, Double>();
	
	public Bidder (String userName, double budget){this.userName = userName; this.budget = budget;} //Bidder constructor
	
	public void setAuctions (ArrayList<Auction> Auctions){
		this.Auctions = Auctions;
	}
	
	public ArrayList<Auction> getAuctions(){
		return Auctions;
	}
	
	public void setPerceivedRrp(){
		
		for (int i=0; i<Auctions.size(); i++){
			Auction auction = Auctions.get(i);
			double perceivedRrp = auction.getItem().getRrp();
			double min = perceivedRrp-(perceivedRrp/10);
			double max = perceivedRrp+(perceivedRrp/10);
			
			perceivedRrp = min + (int)(Math.random() * ((max - min) + 1));	//Get a perceived RRP within 10% of the absolute rrp
			perceivedRrps.put(auction, perceivedRrp);
		}
	}
	
	public void setOutbidAuction(Auction auction){
		outbidAuction = auction;
	}
	
	public synchronized void placeBid(Auction auction, double bidAmount, Bidder bidder){
			Bid bid = new Bid(auction, bidder, bidAmount);
			bid.setName("bidThread");
			bid.start();
	}
	
	public String getUserName(){
		return userName;
	}
	
	public double getBudget(){
		return budget;
	}
	
	public void modelBidderBehaviour(Auction auction){
		
	}
	
	public boolean checkIfAuctionEnded(Auction auction){
		if(SimulationProcess.currentTime() > auction.getDuration()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setBiddingProbability(double biddingProbability){
		this.biddingProbability = biddingProbability;
	}
	
	private boolean decideToBid(Auction auction){
		
		modelBidderBehaviour(auction);
		double bidBudget = budget;
		
		for (int i=0; i<Auctions.size(); i++){	
			Auction tempAuction = Auctions.get(i);
			if(tempAuction.getHighBidder() == this){
				bidBudget = bidBudget - tempAuction.getCurrentPrice();
			}
		}
		
		double perceivedUtility = perceivedRrps.get(auction)-(auction.getCurrentPrice()+auction.getMinimumBiddingIncrement());
		
		if((perceivedUtility > 0)&&((auction.getCurrentPrice() + auction.getMinimumBiddingIncrement()) < bidBudget)){						//If the auction is still profitable and the current price is less than the bidder's budget
			double decision = (Math.random() * (101))*(perceivedUtility); 			// Generate a random number between 0 and 100 and multiply by the utility (0-100)
			if(decision > (100-biddingProbability)){
				return true;
			}
			else {return false;}
		}
		else {return false;}	
	}
	
	@Override
	public void run(){
		
		for(;;){
		//Decide if to bid
		if(outbidAuction == null){
			for (int i=0; i<Auctions.size(); i++){	//For each auction
				Auction auction = Auctions.get(i);
				if(!(checkIfAuctionEnded(auction))){
					if(decideToBid(auction)){
						placeBid(auction, auction.getCurrentPrice()+auction.getMinimumBiddingIncrement(), this);
					}
				}
			}
		}
		else{
			if(!(checkIfAuctionEnded(outbidAuction))){
				if(decideToBid(outbidAuction)){
					placeBid(outbidAuction, outbidAuction.getCurrentPrice()+outbidAuction.getMinimumBiddingIncrement(), this);
					outbidAuction = null;
				}
			}
		}
		try {
			hold(1);
		} catch (SimulationException e1) {
			e1.printStackTrace();
		} catch (RestartException e1) {
			e1.printStackTrace();
		}
	}
  }	
}