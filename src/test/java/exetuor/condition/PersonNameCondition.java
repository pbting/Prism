package exetuor.condition;

import com.road.yishi.log.core.event.ObjectEvent;
import com.road.yishi.log.core.event.ObjectListener;
import exetuor.event.Person;

public class PersonNameCondition extends PersonCondition<Person,String> {

	private ObjectListener<Person> objectListener ;
	public PersonNameCondition(Person person,String name) {
		super(person,name);
	}

	@Override
	public void addEvent() {
		objectListener = new ObjectListener<Person>() {
			@Override
			public void onEvent(ObjectEvent<?> event) {
				Person person = (Person) event.getSource();
				PersonNameCondition.this.setValue((String)person.getName());
				PersonNameCondition.this.handler();
			}
		};
		getObserviable().addListener(objectListener, Person.PROPERTY_NAME_CHANGE);
	}

	@Override
	public boolean isFinished() {
		String name = getValue();//();
		return name.equals("pbting");
	}
}
