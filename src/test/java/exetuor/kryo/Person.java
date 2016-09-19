package exetuor.kryo;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = 1L;
	private String name ;
	private int age ;
	private String address ;
	private String version ;
	private String[] demoArry;
	private List<Father> ftherList ;
	public Person() {
	}
	
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String[] getDemoArry() {
		return demoArry;
	}
	public void setDemoArry(String[] demoArry) {
		this.demoArry = demoArry;
	}
	

	public List<Father> getFtherList() {
		return ftherList;
	}

	public void setFtherList(List<Father> ftherList) {
		this.ftherList = ftherList;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", address=" + address + ", version=" + version + "]";
	}
}
