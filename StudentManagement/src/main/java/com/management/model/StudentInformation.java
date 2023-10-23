/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.management.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Abhishek
 */
public class StudentInformation 
{
    private int studentid;
    private int rollno;
    private String firstname;
    private String lastname;
    private String gender;
    private String contactno;
    private String emailid;
    private String dob;
    private List<RessidentialAddress> ressidentialAddress;
    private List<QualificationDetails> qualificationDetails;
    private int active;
    private int isdelete;    
    
    // Method to get the current date and time as a formatted string
    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getRollno() {
        return rollno;
    }

    public void setRollno(int rollno) {
        this.rollno = rollno;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(int isdelete) {
        this.isdelete = isdelete;
    }

    public List<QualificationDetails> getQualificationDetails() {
        return qualificationDetails;
    }

    public void setQualificationDetails(List<QualificationDetails> qualificationDetails) {
        this.qualificationDetails = qualificationDetails;
    }

    public List<RessidentialAddress> getRessidentialAddress() {
        return ressidentialAddress;
    }

    public void setRessidentialAddress(List<RessidentialAddress> ressidentialAddress) {
        this.ressidentialAddress = ressidentialAddress;
    }

    public StudentInformation(int studentid, int rollno, String firstname, String lastname, String gender, String contactno, String emailid, String dob, int active, int isdelete, List<QualificationDetails> qualificationDetails, List<RessidentialAddress> ressidentialAddress) {
        this.studentid = studentid;
        this.rollno = rollno;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.contactno = contactno;
        this.emailid = emailid;
        this.dob = dob;
        this.active = active;
        this.isdelete = isdelete;
        this.qualificationDetails = qualificationDetails;
        this.ressidentialAddress = ressidentialAddress;
    }

    public StudentInformation() {
    }

    @Override
    public String toString() {
        return "StudentInformation{" + "studentid=" + studentid + ", rollno=" + rollno + ", firstname=" + firstname + ", lastname=" + lastname + ", gender=" + gender + ", contactno=" + contactno + ", emailid=" + emailid + ", dob=" + dob + ", active=" + active + ", isdelete=" + isdelete + ", qualificationDetails=" + qualificationDetails + ", ressidentialAddress=" + ressidentialAddress + '}';
    }

   
}
