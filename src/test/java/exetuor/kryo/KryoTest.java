package exetuor.kryo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.road.yishi.log.mina.KryoUtil;

public class KryoTest {

	public static void main(String[] args) {
		//序列化Map
		HashMap<String, String> target = new HashMap<String,String>();
		target.put("key_1", "value_1");
		target.put("key_2", "value_2");
		target.put("key_3", "value_3");
		target.put("key_4", "value_4");
		target.put("key_5", "value_5");
		target.put("key_6", "value_6");
		target.put("key_7", "value_7");
		
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(true);
		
		MapSerializer serializer = new MapSerializer();
		serializer.setKeyClass(String.class, new JavaSerializer());
		serializer.setKeysCanBeNull(false);
		serializer.setValueClass(String.class, new JavaSerializer());
		serializer.setValuesCanBeNull(true);
		
		kryo.register(String.class, new JavaSerializer());
		kryo.register(HashMap.class, serializer);
		kryo.register(ConcurrentHashMap.class, serializer);
		
	}

	private static void kryoList() {
		//使用kryo 序列化 List
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(true);
		
		//如果是一个集合的话，则设置两个基本的参数：这个集合里面每个元素的基本类型，这个基本类型会对应一个JavaSerializer 来解析集合中的每一个元素
		CollectionSerializer serializer = new CollectionSerializer();
		serializer.setElementClass(String.class, new JavaSerializer());
		serializer.setElementsCanBeNull(false);
		
		//向 kryo 中注册相关的序列化解析器
		kryo.register(String.class, new JavaSerializer());
		//你这个集合是什么类型的集合
		kryo.register(List.class, serializer);
		kryo.register(ArrayList.class, serializer);
		
		//开始序列化
		List<String> target = new ArrayList<String>();
		target.addAll(Arrays.asList("pbting_1","pbting_2","pbting_3","pbting_4","pbting_5","pbting_6","pbting_7","pbting_8","pbting_9","pbting_10"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(baos);
		kryo.writeObject(output,target);
		Base64 base64 = new Base64();
		String serTarget = new String(base64.encode(baos.toByteArray()));
		System.out.println(serTarget);
		
		//反序列化
		ByteArrayInputStream bais = new ByteArrayInputStream(base64.decode(serTarget.getBytes()));
		Input input = new Input(bais);
		List<String> derTarget = kryo.readObject(input,ArrayList.class,serializer);
		System.out.println(derTarget.size()+";the 1 index value is :"+derTarget.get(1));
	}

	private static void kryoTest1() {
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
