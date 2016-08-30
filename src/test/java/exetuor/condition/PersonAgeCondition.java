
package exetuor.condition;

import com.road.yishi.log.core.event.ObjectEvent;
import com.road.yishi.log.core.event.ObjectListener;

import exetuor.event.Person;

public class PersonAgeCondition extends PersonCondition<Person,Integer> {
	private ObjectListener<Person> objectListener ;
	public PersonAgeCondition(Person person,int age) {
		super(person,age);
	}
	@Override
	public void addEvent() {
		objectListener = new ObjectListener<Person>() {
			@Override
			public void onEvent(ObjectEvent<?> event) {
				Person person = (Person) event.getSource();
				PersonAgeCondition.this.setValue((person.getAge()));
				PersonAgeCondition.this.handler();
			}
		};
		getObserviable().addListener(objectListener, Person.PROPERTY_AGE_CHANGE);
	}
	@Override
	public boolean isFinished() {
		int age = this.getValue();
		
		return age > 26;
	}
}
