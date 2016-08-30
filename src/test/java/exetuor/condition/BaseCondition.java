package exetuor.condition;

public abstract class BaseCondition<O,V> {
	protected O observiable ;
	protected V value;
	public BaseCondition(O observiable,V v) {
		this.observiable = observiable;
		this.value = v ;
	}
	public abstract void addEvent();
	
	public abstract boolean isFinished();
	public O getObserviable() {
		return observiable;
	}
	public void setObserviable(O observiable) {
		this.observiable = observiable;
	}
	
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public abstract void handler() ;
}
