/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.management.model;

/**
 *
 * @author Abhishek
 */
public class RessidentialAddress 
{
//    CREATE TABLE ressidential_address(studentid int ,address1 VARCHAR(200), city VARCHAR(100),pincode VARCHAR(10),state VARCHAR(100),country VARCHAR(100));
    private int studentid;
    private String address1;
    private String city;
    private String pincode;
    private String state;
    private String country;

    public RessidentialAddress(int studentid, String address1, String city, String pincode, String state, String country) {
        this.studentid = studentid;
        this.address1 = address1;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
        this.country = country;
    }

    public RessidentialAddress() {
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "RessidentialAddress{" + "studentid=" + studentid + ", address1=" + address1 + ", city=" + city + ", pincode=" + pincode + ", state=" + state + ", country=" + country + '}';
    }
    
    
}
