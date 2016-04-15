package com.uniquedu.json.bean;

public class Student {
	private String name;
	private String age;
	private String id;
	private String sex;

	public Student(String id,  String name,String age, String sex) {
		this.name = name;
		this.age = age;
		this.id = id;
		this.sex = sex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Student() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
