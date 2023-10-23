/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.management.model;

/**
 *
 * @author Abhishek
 */
public class QualificationDetails 
{
    private int studentid;
    private String institutionname;
    private String course;
    private int duration;
    private String fromdate;
    private String todate;
    private int percentage;

    public QualificationDetails(int studentid, String institutionname, String course, int duration, String fromdate, String todate, int percentage) {
        this.studentid = studentid;
        this.institutionname = institutionname;
        this.course = course;
        this.duration = duration;
        this.fromdate = fromdate;
        this.todate = todate;
        this.percentage = percentage;
    }

    public QualificationDetails() {
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getInstitutionname() {
        return institutionname;
    }

    public void setInstitutionname(String institutionname) {
        this.institutionname = institutionname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "QualificationDetails{" + "studentid=" + studentid + ", institutionname=" + institutionname + ", course=" + course + ", duration=" + duration + ", fromdate=" + fromdate + ", todate=" + todate + ", percentage=" + percentage + '}';
    }
    
    
    
}
