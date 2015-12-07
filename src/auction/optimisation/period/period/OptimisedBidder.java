package auction.optimisation.period.period;

import org.javasim.*;

public class OptimisedBidder extends Bidder {
	
	public OptimisedBidder(String userName, double budget) {
		super(userName, budget);
	}	
	
	private Optimiser optimiser;
	private ScenarioGenerator scenGen;
	private int period;
	
	public void setOptimiser(Optimiser optimiser){
		this.optimiser = optimiser;
	}
	
	public void setScenarioGenerator(ScenarioGenerator scenGen){
		this.scenGen = scenGen;
	}
	
	public void setPeriod(int period){
		this.period = period;
	}

	@Override
	  public void run() {
		  
		for (;;){
			  double [] coefficients = optimiser.optimiseAllocation(scenGen.getAuctions(), this.getBudget(), this);
			  
	        for (int i=0; i<getAuctions().size(); i++){
	        	Auction auction = getAuctions().get(i); 
	        	double coefficient = coefficients[i]; 
	        		
	        	if ((coefficient == 1)&&(getAuctions().get(i).getHighBidder() != (this))){
	        		
	        		double bid = auction.getCurrentPrice() + auction.getMinimumBiddingIncrement();
	        		placeBid(auction, bid, this);
	        		setOutbidAuction(null);
	        	}
	        }
	        scenGen.printItemsUtility(); 
			try {
				hold(period);
			} catch (SimulationException e1) {
				e1.printStackTrace();
			} catch (RestartException e1) {
				e1.printStackTrace();
			}
	        
	    }
	}
}