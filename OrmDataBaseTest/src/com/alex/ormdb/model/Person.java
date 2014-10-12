package com.alex.ormdb.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="person")
public class Person {
    
    @DatabaseField
    private String id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String portrait;
    @DatabaseField(columnName="nick_name")
    private String nickName;
    @DatabaseField
    private int age;
    @DatabaseField
    private Gender gender;
    @DatabaseField
    private long birthday;
    @DatabaseField
    private String address;
    @DatabaseField
    private String description;
    @DatabaseField
    private String hobby;
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public enum Gender {
        man,woman;
        
        public String toString() {
            String gender = "";
            switch (this) {
            case man:
                gender = "帅哥";
                break;
            case woman:
                gender = "美女";
                break;
            }
            return gender;
        }
    }
    
}
