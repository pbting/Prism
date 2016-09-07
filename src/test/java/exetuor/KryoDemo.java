package exetuor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import exetuor.event.Person;

public class KryoDemo {
	private final static Kryo kryo = new Kryo();
	static{
		kryo.register(Person.class);
	}
	
	public static byte[] serializable(Object object){
		Output output = new Output(new ByteArrayOutputStream());
		kryo.writeObject(output, object);
		return output.getBuffer();
	}
	
	public static <T> T derializable(byte[] bytes,Class<T> clazz){
		Input input = new Input(new ByteArrayInputStream(bytes));
		return kryo.readObject(input, clazz);
	}
}