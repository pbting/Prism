package com.road.yishi.log.analysor.test;

public class Person {

	private int luckNum ;
	private String tel;
	private String name ;
	private int num;
	private String message ;
	public Person() {
	}
	public Person(int luckNum,String tel, String name, int num) {
		super();
		this.luckNum = luckNum;
		this.tel = tel;
		this.name = name;
		this.num = num;
	}
	public Person(int luckNum,String tel, String name, int num,String message) {
		super();
		this.luckNum = luckNum;
		this.tel = tel;
		this.name = name;
		this.num = num;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getLuckNum() {
		return luckNum;
	}
	public void setLuckNum(int luckNum) {
		this.luckNum = luckNum;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}

	public String toString(String spe) {
		return luckNum+spe+tel+spe +name+spe+num;
	}
}
