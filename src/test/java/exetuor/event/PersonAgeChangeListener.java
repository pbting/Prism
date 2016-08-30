package exetuor.event;

import com.road.yishi.log.core.event.ObjectEvent;
import com.road.yishi.log.core.event.ObjectListener;

public class PersonAgeChangeListener implements ObjectListener<Person>{

	@Override
	public void onEvent(ObjectEvent<?> event) {
		Person person = (Person) event.getSource();
		if(person.getAge() > 20){
			System.out.println("恭喜您可以进入美国大战疆场的年龄。");
		}
	}
}
