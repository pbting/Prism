package exetuor.kryo;

import java.util.Iterator;

import com.road.yishi.log.mina.KryoUtil;

public class KryoTest {

	public static void main(String[] args) {
		CustomCategoryDto dto = new CustomCategoryDto();
		dto.setCategoryCode("ABCD_001");
		dto.setCategoryName("呼吸系统");
		for (int i = 0; i < 10; i++) {
			CustomItemDto val = new CustomItemDto();
			val.setId(Long.parseLong(String.valueOf(i)));
			val.setItemCode("");
			val.setItemDespositPrice(32.45);
			val.setItemMemo(null);
			val.setItemName("张金[List]");
			val.setItemPrice(89.02);
			val.setSort(i * 2);
			dto.getCustomItemList().add(val);
		}
		for (int i = 0; i < 10; i++) {
			CustomItemDto val = new CustomItemDto();
			val.setId(Long.parseLong(String.valueOf(i)));
			val.setItemCode("");
			val.setItemDespositPrice(32.45);
			val.setItemMemo(null);
			val.setItemName("张金[Set]");
			val.setItemPrice(89.02);
			val.setSort(i * 3);
			dto.getCustomItemSet().add(val);
		}
		for (int i = 0; i < 10; i++) {
			CustomItemDto val = new CustomItemDto();
			val.setId(Long.parseLong(String.valueOf(i)));
			val.setItemCode("");
			val.setItemDespositPrice(32.45);
			val.setItemMemo(null);
			val.setItemName("张金[Map]");
			val.setItemPrice(89.02);
			val.setSort(i);
			dto.getCustomItemMap().put(String.valueOf(i), val);
		}

		String ser = KryoUtil.serialization(dto, CustomCategoryDto.class);

		CustomCategoryDto dtoDer = KryoUtil.deserialization(ser, CustomCategoryDto.class);
		System.out.println("coder:" + dtoDer.getCategoryCode() + ";name:" + dtoDer.getCategoryName());
		for (CustomItemDto d : dtoDer.getCustomItemList()) {
			System.out.println("sort:" + d.getSort() + ";item name:" + d.getItemName());
		}
		for (CustomItemDto d : dtoDer.getCustomItemMap().values()) {
			System.out.println("sort:" + d.getSort() + ";item name:" + d.getItemName());
		}
		Iterator<CustomItemDto> ite = dtoDer.getCustomItemSet().iterator();
		while(ite.hasNext()){
			CustomItemDto d = ite.next();
			System.out.println("sort:" + d.getSort() + ";item name:" + d.getItemName());
		}
	}
}
