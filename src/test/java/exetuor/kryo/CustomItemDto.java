package exetuor.kryo;

import java.io.Serializable;

/**
 * 
 * <pre>
 *  	val.setId(Long.parseLong(String.valueOf(1)));
        val.setItemCode("");
        val.setItemDespositPrice(32.45);
        val.setItemMemo(null);
        val.setItemName("张金");
        val.setItemPrice(89.02);
        val.setSort(10);
 * </pre>
 */
public class CustomItemDto implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	private long id ;
	private String itemCode ;
	private double itemDespositPrice;
	private String itemMemo;
	private String itemName;
	private double itemPrice;
	private int sort;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public double getItemDespositPrice() {
		return itemDespositPrice;
	}
	public void setItemDespositPrice(double itemDespositPrice) {
		this.itemDespositPrice = itemDespositPrice;
	}
	public String getItemMemo() {
		return itemMemo;
	}
	public void setItemMemo(String itemMemo) {
		this.itemMemo = itemMemo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
}
