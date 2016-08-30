package exetuor.condition;

public class PersonCondition<Person,V> extends BaseCondition<Person,V> {

	public PersonCondition(Person observiable, V v) {
		super(observiable, v);
	}

	@Override
	public void addEvent() {
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void handler() {
		if(this instanceof PersonAgeCondition){
			boolean isFinished = isFinished();
			if(isFinished){
				System.out.println("年龄达到条件");
			}
		}else if(this instanceof PersonNameCondition){
			boolean isFinished = isFinished();
			if(isFinished){
				System.out.println("姓名符合条件件");
			}
		}
	}
}
