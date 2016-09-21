package com.road.yishi.log.mina;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.codec.binary.Base64;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;

public class KryoUtil {
	public static boolean serializ2File(Object object,String filePath){
		boolean flag = true ;
		Kryo kryo = new Kryo();
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			Output output = new Output(fos);
			kryo.writeObject(output, object);
			output.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false ;
		}
		return flag;
	}
	
	public static <T> T derializFromFile(String filePath,Class<T> clazz){
		Kryo kryo = new Kryo();
		try {
			FileInputStream fis = new FileInputStream(filePath);
			Input input = new Input(fis);
			return kryo.readObject(input, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	public static <T extends Serializable> String serialization(T obj, Class<T> clazz) {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(true);
		kryo.register(clazz, new JavaSerializer());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(baos);
		kryo.writeObject(output, obj);
		output.flush();
		output.close();

		byte[] b = baos.toByteArray();
		try {
			baos.flush();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new String(new Base64().encode(b));
	}

	public static <T extends Serializable> T deserialization(String obj, Class<T> clazz) {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(true);
		kryo.register(clazz, new JavaSerializer());
		
		ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(obj.getBytes()));
		Input input = new Input(bais);
		return kryo.readObject(input, clazz);
	}
	
    public static <T extends Serializable> String serializationList(List<T> obj,Class<T> clazz) {
    	Kryo kryo = new Kryo();
    	kryo.setReferences(false);
    	kryo.setRegistrationRequired(true);
    	
    	CollectionSerializer serializer = new CollectionSerializer();
    	serializer.setElementClass(clazz, new JavaSerializer());
    	serializer.setElementsCanBeNull(false);
    	
    	kryo.register(clazz, new JavaSerializer());
    	kryo.register(List.class, serializer);
    	kryo.register(ArrayList.class, serializer);
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	Output output = new Output(baos);
    	kryo.writeObject(output, obj);
    	output.flush();
    	output.close();
    	
    	byte[] b = baos.toByteArray();
    	try {
    		baos.flush();
    		baos.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	return new String(new Base64().encode(b));
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> deserializationList(String obj,Class<T> clazz) {
    	Kryo kryo = new Kryo();
    	kryo.setReferences(false);
    	kryo.setRegistrationRequired(true);
    	
    	CollectionSerializer serializer = new CollectionSerializer();
    	serializer.setElementClass(clazz, new JavaSerializer());
    	serializer.setElementsCanBeNull(false);
    	
    	kryo.register(clazz, new JavaSerializer());
    	kryo.register(List.class, serializer);
    	kryo.register(ArrayList.class, serializer);
    	
    	ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(obj.getBytes()));
    	Input input = new Input(bais);
    	return (List<T>) kryo.readObject(input, ArrayList.class, serializer);
    }
 
 
    public static <T extends Serializable> String serializationMap(Map<String, T> obj, Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(true);
 
        MapSerializer serializer = new MapSerializer();
        serializer.setKeyClass(String.class, new JavaSerializer());
        serializer.setKeysCanBeNull(false);
        serializer.setValueClass(clazz, new JavaSerializer());
        serializer.setValuesCanBeNull(true);
 
        kryo.register(clazz, new JavaSerializer());
        kryo.register(HashMap.class, serializer);
 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, obj);
        output.flush();
        output.close();
 
        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return new String(new Base64().encode(b));
    }
 
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> Map<String, T> deserializationMap(String obj, Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(true);
 
        MapSerializer serializer = new MapSerializer();
        serializer.setKeyClass(String.class, new JavaSerializer());
        serializer.setKeysCanBeNull(false);
        serializer.setValueClass(clazz, new JavaSerializer());
        serializer.setValuesCanBeNull(true);
 
        kryo.register(clazz, new JavaSerializer());
        kryo.register(HashMap.class, serializer);
 
        ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(obj.getBytes()));
        Input input = new Input(bais);
        return (Map<String, T>) kryo.readObject(input, HashMap.class,serializer);
    }
 
    public static <T extends Serializable> String serializationSet(Set<T> obj,Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(true);
 
        CollectionSerializer serializer = new CollectionSerializer();
        serializer.setElementClass(clazz, new JavaSerializer());
        serializer.setElementsCanBeNull(false);
 
        kryo.register(clazz, new JavaSerializer());
        kryo.register(HashSet.class, serializer);
 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, obj);
        output.flush();
        output.close();
 
        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return new String(new Base64().encode(b));
    }
 
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> Set<T> deserializationSet(String obj, Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(true);
 
        CollectionSerializer serializer = new CollectionSerializer();
        serializer.setElementClass(clazz, new JavaSerializer());
        serializer.setElementsCanBeNull(false);
 
        kryo.register(clazz, new JavaSerializer());
        kryo.register(HashSet.class, serializer);
 
        ByteArrayInputStream bais = new ByteArrayInputStream(new Base64().decode(obj.getBytes()));
        Input input = new Input(bais);
        return (Set<T>) kryo.readObject(input, HashSet.class, serializer);
    }
    public static void main(String[] args) {
    	ReentrantLock lock = new ReentrantLock();
    	byte[] bytes = new byte[365];
    	Random random = new Random();
    	int count = random.nextInt(bytes.length);
    	for(int i=0;i<count;i++){
    		bytes[random.nextInt(bytes.length)] = (byte)1 ;
    	}
    	ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
    	byte[] tmp = new byte[1];
    	//如果多个线程共享该变量，则会有线程安全的问题
    	lock.lock();
    	try{
	    	byteArrayInputStream.mark(-1);//跳之前一定要先标记下当前pos 的位置。
	    	byteArrayInputStream.skip(89);//the index start from zero then skip to 89 mean read the 90 index byte data 
	    	byteArrayInputStream.read(tmp, 0, 1);
	    	byteArrayInputStream.reset();//重置到跳之前的位置
    	}finally{
    		lock.unlock();
    	}
    	//方法论：当记录一个人在一年之内登录的天数，则开辟一个这个的字节数组，然后再用ByteArrayInputStream 包装一下，就可以灵活的操作
    	if(tmp.length>0){
    		System.out.println("is true："+((tmp[0]&0xff) > 0));
    	}
    }

	private static void kryoDemo() {
		String base64 = "base 64 demo Today is wendesday" ;
    	Base64 base64_1 = new Base64();
    	System.out.println("before base64 encode :"+base64.getBytes().length);
    	byte[] encode = base64_1.encode(base64.getBytes());
    	System.out.println("after base64 encode :"+encode.length+";infos:"+new String(encode));
    	System.out.println("base 64 decode:"+new String(base64_1.decode(encode)));
	
    	//使用kryo 进行序列化操作：
    	Kryo kryo = new Kryo();
    	kryo.setReferences(false);
    	kryo.setRegistrationRequired(true);
    	kryo.register(String.class, new JavaSerializer());
    	
    	//开始进行序列化，将需要序列化后的数据准备一个容器
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	Output output = new Output(baos);
    	
    	kryo.writeObject(output, base64);
    	byte[] target = baos.toByteArray();
    	String encodeT = new String(base64_1.encode(target));
    	
    	//开始反序列化：准备一个装数据的容器,序列化的数据源
    	ByteArrayInputStream bais = new ByteArrayInputStream(base64_1.decode(encodeT.getBytes()));
    	Input input = new Input(bais);
    	String deT = kryo.readObject(input, String.class);
    	System.out.println(deT+"-------<");
	}
}
