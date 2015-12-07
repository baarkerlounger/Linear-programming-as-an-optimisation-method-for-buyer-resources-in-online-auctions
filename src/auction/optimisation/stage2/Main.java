package	 auction.optimisation.stage2;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.javasim.*;

public class Main extends SimulationProcess{
	
	public static void main(String[] args) {
		
		Main main = new Main();													
		main.await();																		//Pause main thread and start simulator/scheduler thread
	}
	
	public void await ()
    {
		this.resumeProcess();
		SimulationProcess.mainSuspend();
    }
	
	public void printResults(ArrayList<Bidder> Bidders, ArrayList<Auction> Auctions, OptimisedBidder OptimisedBidder){
		
		DecimalFormat decFor = new DecimalFormat("00.00");
		//Print the utility that all generated bidders have made from the auction
		for(int i=0; i<Bidders.size(); i++){
			Bidder bidder = Bidders.get(i);
			double utility = 0;
			for (int j=0; j<Auctions.size(); j++){
				Auction auction = Auctions.get(j);
				if(bidder == auction.getHighBidder()){
					utility = utility + auction.getUtility();
				}
			}
			System.out.println(bidder.getUserName()+" has made a utility of $"+decFor.format(utility));
		}
		double optimisedBidderutility = 0;
		for (int j=0; j<Auctions.size(); j++){
			Auction auction = Auctions.get(j);
			if(OptimisedBidder == auction.getHighBidder()){
				optimisedBidderutility = optimisedBidderutility + auction.getUtility();
			}
		}
		System.out.println(OptimisedBidder.getUserName()+" has made a utility of $"+decFor.format(optimisedBidderutility));
	}
	
	public void run(){
		
		double budget = 50;
		
		Optimiser optimiser = new Optimiser();											    //Instantiate optimiser class
		ScenarioGenerator scenGen = new ScenarioGenerator(5, 20);		   					//Instantiate ScenarioGenerator class
		OptimisedBidder OptimisedBidder = new OptimisedBidder("OptimisedBidder", budget);   //Instantiate OptimisedBidder class
				
		scenGen.createItemAndAuction();													    //Create items and auctions
		scenGen.createBidders();														    //Create bidders
		
		scenGen.passAuctionsToBidders();												    //Pass list of generated auctions to all simulated bidders
		OptimisedBidder.setAuctions(scenGen.getAuctions());								    //Pass list of generated auctions to OptimisedBidder
		OptimisedBidder.setOptimiser(optimiser);										    //Pass optimiser to OptimisedBidder
		OptimisedBidder.setScenarioGenerator(scenGen);									  	//Pass scenGen to OptimisedBidder
		
		scenGen.printItemsUtility(); 													   	//Prints a list of all items created by the scenario generator, the current high bid on that item and it's resultant utility
		
		System.out.println("Simulation started at: "+time());
		
		try {
			OptimisedBidder.activate();														//Activate OptimisedBidder thread
		for(int i=0; i<scenGen.getBidders().size(); i++){									//Activate bidder threads (i.e. set them to active, wakeup now and add them to the queue
			Bidder bidder = scenGen.getBidders().get(i);
				bidder.activate();
			}
				Scheduler.startSimulation();												//Start simulation	
				hold(50);
				SimulationProcess.mainResume();												//Resume main thread
		}	 
			catch (SimulationException e) {
				e.printStackTrace();
			} catch (RestartException e) {
				e.printStackTrace();
			}		

		System.out.println("Simulation ended at: "+time());
		printResults(scenGen.getBidders(), scenGen.getAuctions(), OptimisedBidder);
		System.exit(0);
	}
}