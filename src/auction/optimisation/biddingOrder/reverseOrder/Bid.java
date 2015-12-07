package auction.optimisation.biddingOrder.reverseOrder;

import org.javasim.*;

public class Bid extends SimulationProcess {
	
	private Auction auction;
	private Bidder bidder;
	private double amount;
	private Bidder outbid;

	public Bid (Auction auction, Bidder bidder, double amount){this.auction = auction; this.bidder = bidder; this.amount = amount;} //Bid constructor
	
	@Override 
	public void run(){
		outbid  = auction.getHighBidder();
		auction.acceptBid(amount, bidder);
		if(outbid != null){
		try {
			outbid.setOutbidAuction(auction);
			outbid.activateAfter(this);
		} catch (SimulationException e) {e.printStackTrace();
		} catch (RestartException e) {e.printStackTrace();}
		}
	}
}
