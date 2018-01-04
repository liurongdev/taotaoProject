package taotao.com.freemarker;

import java.io.Serializable;

public class Student implements Serializable{
	private int id;
	private String name;
	private String address;
	private int age;
	public Student(){}
	public Student(int id,int age,String address,String name){
		super();
		this.age=age;
		this.id=id;
		this.address=address;
		this.name=name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
