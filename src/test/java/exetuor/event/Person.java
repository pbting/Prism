package exetuor.event;

import com.road.yishi.log.core.event.AbstractEventObject;
import com.road.yishi.log.core.event.ObjectEvent;

import exetuor.condition.PersonAgeCondition;
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
	public Person() {
	}
	
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	public void init(){
		PersonAgeCondition ageCondition = new PersonAgeCondition(this,0);
		ageCondition.addEvent();
		PersonNameCondition nameCondition = new PersonNameCondition(this,"");
		nameCondition.addEvent();
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
