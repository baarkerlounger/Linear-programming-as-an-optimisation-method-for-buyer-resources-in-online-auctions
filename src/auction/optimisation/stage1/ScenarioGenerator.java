package auction.optimisation.stage1;
import java.util.ArrayList;

public class ScenarioGenerator {
	
	ArrayList<Auction> Auctions = new ArrayList<Auction>();

	void createItemAndAuction (){
		
		double minSureBid = 0;
		double maxSureBid = 100;
		
		for (int i=1; i<11; i++){
			
			String title = "item "+i;
			String subTitle = "example subtitle "+i;
			String description = "example description "+i;
			
			//Math.random produces a pseudo-random number between 0 and 1, this puts it in the min-max range
			double sureBid = minSureBid + (Math.random() * ((maxSureBid - minSureBid) + 1)); 
			sureBid = (int)(sureBid*100);													//Truncate generated number to 2 decimal places
			sureBid = sureBid/100;
			
			//Generate a random rrp higher than the sureBid price for the item
			double rrp = sureBid + (Math.random() * ((maxSureBid - minSureBid) + 1));  
			rrp = (int)(rrp*100);													
			rrp = rrp/100;
			
			double utility = rrp - sureBid;
			utility = (int)(utility*100);	
			utility = utility/100;
		
			Item item = new Item(title, subTitle, description, rrp);	//Create instance of item
			Auction auction = new Auction(item, sureBid, utility);		//Create instance of an auction containing the item
			
			Auctions.add(auction);	//Add auctions to the ArrayList
		}
		
	}
	
}
