package exetuor;

import com.road.yishi.log.message.Message;

public class TestMessage extends Message {

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;

	private String name ;
	private int age ;
	public TestMessage(String name,int age) {
		this.name = name;
		this.age = age ;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
