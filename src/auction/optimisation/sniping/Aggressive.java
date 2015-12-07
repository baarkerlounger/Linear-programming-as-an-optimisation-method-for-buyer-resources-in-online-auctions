package auction.optimisation.sniping;

import org.javasim.SimulationProcess;

public class Aggressive extends Bidder{
	
	private double biddingProbability;
	
	public Aggressive(String userName, double budget) {
		super(userName, budget);
	}
	
	@Override
	public void modelBidderBehaviour(Auction auction){
		
		int sampleSize = 10;
		
		double t1 = 0.70, t2 = 0.45, t3 = 0.45, t4 = 0.45, t5 = 0.45, t6 = 0.45, t7 = 0.50, t8 = 0.60, t9 = 0.75, t10 = 0.80;
		
		int timeLeft = auction.getDuration() - (int)SimulationProcess.currentTime();
		int  auctionFraction = auction.getDuration()/sampleSize;
		
		if (timeLeft <= auctionFraction){
			biddingProbability = t10;
		}
		else if ((timeLeft > auctionFraction) && (timeLeft <= 2*auctionFraction)){
			biddingProbability = t9;
		}
		else if ((timeLeft > 2*auctionFraction) && (timeLeft <= 3*auctionFraction)){
			biddingProbability = t8;
		}
		else if ((timeLeft > 3*auctionFraction) && (timeLeft <= 4*auctionFraction)){
			biddingProbability = t7;
		}
		else if ((timeLeft > 4*auctionFraction) && (timeLeft <= 5*auctionFraction)){
			biddingProbability = t6;
		}
		else if ((timeLeft > 5*auctionFraction) && (timeLeft <= 6*auctionFraction)){
			biddingProbability = t5;
		}
		else if ((timeLeft > 6*auctionFraction) && (timeLeft <= 7*auctionFraction)){
			biddingProbability = t4;
		}
		else if ((timeLeft > 7*auctionFraction) && (timeLeft <= 8*auctionFraction)){
			biddingProbability = t3;
		}
		else if ((timeLeft > 8*auctionFraction) && (timeLeft <= 9*auctionFraction)){
			biddingProbability = t2;
		}
		else if ((timeLeft > 9*auctionFraction) && (timeLeft <= 10*auctionFraction)){
			biddingProbability = t1;
		}
		setBiddingProbability(biddingProbability);
	}

}