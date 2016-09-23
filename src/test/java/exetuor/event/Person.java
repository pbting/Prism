package exetuor.event;

import java.util.ArrayList;
import java.util.List;
import com.road.yishi.log.core.event.AbstractEventObject;
import com.road.yishi.log.core.event.ObjectEvent;
import exetuor.condition.PersonAgeCondition;
import exetuor.condition.PersonCondition;
import exetuor.condition.PersonNameCondition;

/**
 * 
 * <pre>
 * 	
 * </pre>
 */
public class Person extends AbstractEventObject implements EventNameType {
	
	private String name ;
	private int age ;
	@SuppressWarnings("rawtypes")
	private List<PersonCondition> personConditions = new ArrayList<PersonCondition>();
	public Person() {
		this.init();
	}
	
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
		this.init();
	}
	/**
	 * 
	 * <pre>
	 * 	这些条件值到时就可以通过数据库配置以此达到动态配置的目的，而不需要修改代码
	 * </pre>
	 *
	 */
	private void init(){
		personConditions.add(new PersonAgeCondition(this,26));
		personConditions.add(new PersonNameCondition(this,"pbting"));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		notifyListeners(new ObjectEvent<String>(this,PROPERTY_NAME_CHANGE));
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
		notifyListeners(new ObjectEvent<Integer>(this,PROPERTY_AGE_CHANGE));
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
	
}
