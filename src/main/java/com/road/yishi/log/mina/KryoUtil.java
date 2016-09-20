package com.road.yishi.log.mina;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.road.yishi.log.core.LogDetailInfo;
import com.road.yishi.log.core.LogMetaInfo;
import com.road.yishi.log.mina.cmd.message.TopicMessage;

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
    	TopicMessage<LogMetaInfo, List<LogDetailInfo>> topicMessage = new TopicMessage<LogMetaInfo, List<LogDetailInfo>>();
		topicMessage.setK(new LogMetaInfo(new Date().toString(), "call class name", "callMethodName"));
		List<LogDetailInfo> list = new ArrayList<LogDetailInfo>();
		list.add(new LogDetailInfo(new Date().toString(), "log infor_"+System.currentTimeMillis()));
		list.add(new LogDetailInfo(new Date().toString(), "log infor_"+System.currentTimeMillis()));
		list.add(new LogDetailInfo(new Date().toString(), "log infor_"+System.currentTimeMillis()));
		list.add(new LogDetailInfo(new Date().toString(), "log infor_"+System.currentTimeMillis()));
		topicMessage.setV(list);
		topicMessage.setTopic("topicName");
		String msg = KryoUtil.serialization(topicMessage, TopicMessage.class);
		TopicMessage<LogMetaInfo, List<LogDetailInfo>> tmp = KryoUtil.deserialization(msg, TopicMessage.class);
		LogMetaInfo k = tmp.getK();
		System.out.println(k.getCallClassName());
	}
}
