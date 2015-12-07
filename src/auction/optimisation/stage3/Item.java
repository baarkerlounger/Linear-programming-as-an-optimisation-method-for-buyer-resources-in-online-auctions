package auction.optimisation.stage3;

public class Item {
	
	private String title;
	private String subTitle;
	private String description;
	private double rrp;
	
	public Item (String title, String subTitle, String description, double rrp){this.title = title; this.subTitle = subTitle; this.description = description; this.rrp = rrp;} //Item constructor

	public double getRrp(){
		return rrp;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getSubTitle(){
		return subTitle;
	}
	
	public String getDescription(){
		return description;
	}
}
