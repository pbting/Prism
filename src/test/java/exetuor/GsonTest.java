package exetuor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.road.yishi.log.analysor.test.Person;

public class GsonTest {

	public static void main(String[] args) {
		Person per = new Person(669, "15279132865", "pbting-1", 23);
		System.out.println(new Gson().toJson(per));
		String json = "{\"luckNum\":669,\"tel\":\"15279132865\",\"name\":\"pbting-1\",\"num\":23}";
		Person pers = getContent(json,new TypeToken<Person>(){});
		System.out.println(pers.getName());
	}
	
	public static <T> T getContent(String json,TypeToken typeToken){
		Gson gson = new Gson();
        return gson.fromJson(json, typeToken.getType());
	}
	
	public static <T> List<T> getPersons(String jsonString) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
        }
        return list;
    }
}
