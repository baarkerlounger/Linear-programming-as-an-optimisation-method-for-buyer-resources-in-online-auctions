package auction.optimisation.worstCase;

import org.javasim.SimulationProcess;

public class Cautious extends Bidder{
	
	private double biddingProbability;
	
	public Cautious(String userName, double budget) {
		super(userName, budget);
	}
	
	@Override
	public void modelBidderBehaviour(Auction auction){
		
		int sampleSize = 10;
		
		double t1 = 0.30, t2 = 0.15, t3 = 0.15, t4 = 0.15, t5 = 0.15, t6 = 0.15, t7 = 0.20, t8 = 0.30, t9 = 0.35, t10 = 0.40;
		
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
