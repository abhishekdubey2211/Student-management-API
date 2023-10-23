/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.management.model;

import java.util.List;

/**
 *
 * @author Abhishek
 */
public class StudentInformationApiResponse {
    private String resdatetime;
    private int status;
    private String statusdesc;
    private int revisionno;
    private List<StudentInformation> studentInformation;

    public StudentInformationApiResponse(String resdatetime, int status, String statusdesc, int revisionno, List<StudentInformation> studentInformation) {
        this.resdatetime = resdatetime;
        this.status = status;
        this.statusdesc = statusdesc;
        this.revisionno = revisionno;
        this.studentInformation = studentInformation;
    }
    
    
    public StudentInformationApiResponse() {
    }

    public String getResdatetime() {
        return resdatetime;
    }

    public void setResdatetime(String resdatetime) {
        this.resdatetime = resdatetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }

    public int getRevisionno() {
        return revisionno;
    }

    public void setRevisionno(int revisionno) {
        this.revisionno = revisionno;
    }

    public List<StudentInformation> getStudentInformation() {
        return studentInformation;
    }

    public void setStudentInformation(List<StudentInformation> studentInformation) {
        this.studentInformation = studentInformation;
    }

    @Override
    public String toString() {
        return "StudentInformationApiResponse{" + "resdatetime=" + resdatetime + ", status=" + status + ", statusdesc=" + statusdesc + ", revisionno=" + revisionno + ", studentInformation=" + studentInformation + '}';
    }

    
}
