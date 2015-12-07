package auction.optimisation.stage2;

import java.util.ArrayList;
import org.javasim.*;

public class Bidder extends SimulationEntity {

	private double budget;
	private String userName;
	private ArrayList<Auction> Auctions = new ArrayList<Auction>();
	private Auction outbidAuction = null;
	
	public Bidder (String userName, double budget){this.userName = userName; this.budget = budget;} //Bidder constructor
	
	public void setAuctions (ArrayList<Auction> Auctions){
		this.Auctions = Auctions;
	}
	
	public ArrayList<Auction> getAuctions(){
		return Auctions;
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
	
	private boolean decideToBid(Auction auction){
		
		double bidBudget = budget;
		
		for (int i=0; i<Auctions.size(); i++){	
			Auction tempAuction = Auctions.get(i);
			if(tempAuction.getHighBidder() == this){
				bidBudget = bidBudget - tempAuction.getCurrentPrice();
			}
		}
		
		if((auction.getUtility() > 0)&&((auction.getCurrentPrice() + auction.getMinimumBiddingIncrement()) < bidBudget)){						//If the auction is still profitable and the current price is less than the bidder's budget
			double decision = (Math.random() * (101))*(auction.getUtilityIfBidOn()); 			// Generate a random number between 0 and 100 and multiply by the utility (0-100)
			if(decision > 70){
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
				if(decideToBid(auction)){
					placeBid(auction, auction.getCurrentPrice()+auction.getMinimumBiddingIncrement(), this);
				}
			}
		}
		else{
			if(decideToBid(outbidAuction)){
				placeBid(outbidAuction, outbidAuction.getCurrentPrice()+outbidAuction.getMinimumBiddingIncrement(), this);
				outbidAuction = null;
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