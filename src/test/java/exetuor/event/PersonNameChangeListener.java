package exetuor.event;

import com.road.yishi.log.core.event.ObjectEvent;
import com.road.yishi.log.core.event.ObjectListener;

public class PersonNameChangeListener implements ObjectListener<Person>{
	@Override
	public void onEvent(ObjectEvent<?> event) {
		Person person = (Person) event.getSource();
		if(person.getName().equals("pbting")){
			System.out.println("这里是美国大战疆场");
		}
	}
}
