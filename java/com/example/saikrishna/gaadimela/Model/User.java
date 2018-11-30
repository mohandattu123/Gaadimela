package com.example.saikrishna.gaadimela.Model;

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private boolean isStaff;

    public User() {

    }


    public User(String name, String password) {
        Name = name;
        Password = password;
        isStaff=false;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }
}
