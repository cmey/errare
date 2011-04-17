/**
 * This class define Item 
 * @author Yannick
 *
 */
public class Item {
	private String itemName;
	private int ID;
	private Bag bag;
	/**
	 * 
	 * @param name
	 * @param ID
	 */
	public Item(String name, int ID){
		itemName=name;
		this.ID=ID;
		this.bag=null;
	}
	/**
	 * 
	 * @param bag
	 */
	public void setBag(Bag bag) {
		// TODO Auto-generated method stub
		this.bag=bag;
	}
	/**
	 * 
	 * @return
	 */
	public Bag getBag(){
		return bag;
	}
}
