package auction.optimisation.sniping;

public class Auction {
	
	private Item item;
	private double currentPrice;
	private double minimumBiddingIncrement;
	private double utility;
	private double utilityIfBidOn;
	private int duration;
	private Bidder highestBidder;
	
	public Auction (Item item, double currentPrice, double minimumBiddingIncrement, double utility, double utilityIfBidOn, int duration, Bidder highestBidder){this.item = item; this.currentPrice = currentPrice; this.utility = utility; this.utilityIfBidOn = utilityIfBidOn; this.duration = duration; this.highestBidder = highestBidder; this.minimumBiddingIncrement = minimumBiddingIncrement;} //Auction constructor

	public synchronized void acceptBid(double currentPrice, Bidder highestBidder){
		this.currentPrice = currentPrice;
		this.highestBidder = highestBidder;
		utility = item.getRrp() - currentPrice;
		utility = (int)(utility*100);	
		utility = utility/100;
		utilityIfBidOn = utility - minimumBiddingIncrement;
	}
	
	public synchronized Bidder getHighBidder(){
		return highestBidder;
	}
	
	public int getDuration(){
		return duration;
	}
	public synchronized double getUtilityIfBidOn(){
		return utilityIfBidOn;
	}
	
	public synchronized double getUtility(){
		return utility;
	}
	
	public double getMinimumBiddingIncrement(){
		return minimumBiddingIncrement;
	}
	
	public synchronized double getCurrentPrice(){
		return currentPrice;
	}
	
	public Item getItem(){
		return item;
	}
}
