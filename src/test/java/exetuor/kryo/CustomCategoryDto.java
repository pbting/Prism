package exetuor.kryo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CustomCategoryDto implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	private String categoryCode;
	private String categoryName;
	private ArrayList<CustomItemDto> customItemList;
	private HashSet<CustomItemDto> customItemSet;
	private HashMap<String,CustomItemDto> customItemMap;
	public CustomCategoryDto() {
		customItemList = new ArrayList<CustomItemDto>();
		customItemSet = new HashSet<CustomItemDto>();
		customItemMap = new HashMap<String,CustomItemDto>();
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public ArrayList<CustomItemDto> getCustomItemList() {
		return customItemList;
	}
	public void setCustomItemList(ArrayList<CustomItemDto> customItemList) {
		this.customItemList = customItemList;
	}
	public HashSet<CustomItemDto> getCustomItemSet() {
		return customItemSet;
	}
	public void setCustomItemSet(HashSet<CustomItemDto> customItemSet) {
		this.customItemSet = customItemSet;
	}
	public HashMap<String, CustomItemDto> getCustomItemMap() {
		return customItemMap;
	}
	public void setCustomItemMap(HashMap<String, CustomItemDto> customItemMap) {
		this.customItemMap = customItemMap;
	}
}
