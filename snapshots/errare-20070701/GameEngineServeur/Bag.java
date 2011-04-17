/**
 * This class define the bag
 * @author Yannick
 * 
 */
public class Bag extends Item{
	private int size;
	private Item [] contained;
	public Bag(int size,int ID){
		super("Bag "+size+" sites", ID);
		this.size=size;
		this.contained=new Item[size];
	}
	/**
	 * This methode permit to know the size of the bag 
	 * @return size of the bag
	 */
	public int getSize(){
		return size;
	}
	/**
	 * This methode permit to knom the contain of the bag
	 * @return contain of the bag
	 */
	public Item[] getContained(){
		return contained;
	}
	/**
	 * This methode permit to get a item by his index
	 * @param index
	 * @return item if index contain Item, null else
	 */
	public Item getItem(int index){
		if(index>=contained.length){
			return null;
		}
		return contained[index];
	}
	/**
	 * Cette methode permet de rammaser des objets et de les rajouter dans les places vides !
	 * @param item
	 * @return
	 */
	public boolean addItem(Item item){
		for(int i=0;i<contained.length;i++){
			if(contained==null){
				contained[i]=item;
				item.setBag(this);
				return true;
			}
		}
		// si on arrive la c'est qu'il n'y a plus de place !
		return false;
	}
	/**
	 * cette methode permet de placer un objet a une place particuliere
	 * @param item l'objet a placer
	 * @param index l'index ou le placer
	 * @return l'objet qui occupait cette place, null s'il n'y avais pas d'objet
	 */
	public Item addItem(Item item, int index){
		Item tmp=contained[index];
		contained[index]=item;
		return tmp;
	}
}
