package cn.com.alex.server.domain;

import java.io.Serializable;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
	private String name;
	private int age;
	private Gender gender;

	public Person(String name, int age, Gender gender) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return name+"#"+age+"#"+gender.getDesc();
	}

	public enum Gender implements Serializable{
		MAN("man"), WOMEN("woman");

		private final String mDesc;

		Gender(String desc) {
			this.mDesc = desc;
		}

		public String getDesc() {
			return this.mDesc;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putInt("age", age);
		bundle.putSerializable("gender", gender);
		dest.writeBundle(bundle);
	}

	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		
		public Person createFromParcel(Parcel in) {
			Bundle bundle = new Bundle();
			String name = bundle.getString("name");
			int age = bundle.getInt("age");
			Gender gender = (Gender) bundle.getSerializable("gender");
			return new Person(name,age,gender);
		}

		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

	public void readFromParcel(Parcel _reply) {
		
	}
}
