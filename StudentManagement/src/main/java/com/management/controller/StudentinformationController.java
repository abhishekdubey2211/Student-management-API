/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.management.controller;

import com.management.Actions.StudentinformationAction;
import com.management.model.StudentInformation;
import com.management.model.StudentInformationApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Abhishek
 */
@RestController
@RequestMapping("studentinfo")
public class StudentinformationController {

    @GetMapping("/version")
    public String version() {
        return "StudentInformation_API_v0.0.1  ||  22 October 2023";
    }

    @PostMapping
    public StudentInformationApiResponse insertStudentInformation(@RequestBody StudentInformation addStudentInformation) {
        System.out.println("*********************************InsertStudentInformation API*******************************");
        StudentInformationApiResponse studentResponeApi = new StudentInformationApiResponse();
        StudentinformationAction studentinformationAction = new StudentinformationAction();
        try {
            studentResponeApi = studentinformationAction.pushStudentInformation(addStudentInformation);
            return studentResponeApi;
        } catch (Exception e) {

        }
        return null;
    }

    @PutMapping
    public StudentInformationApiResponse putStudentInformation(@RequestBody StudentInformation editStudentInformation) {
        System.out.println("*********************************EditStudentInformation API*******************************");
        StudentInformationApiResponse studentResponeApi = new StudentInformationApiResponse();
        StudentinformationAction studentinformationAction = new StudentinformationAction();
        try {
            studentResponeApi = studentinformationAction.putStudentInformation(editStudentInformation);
            return studentResponeApi;
        } catch (Exception e) {

        }
        return null;
    }

    @GetMapping
    public StudentInformationApiResponse getStudentInformationById(@RequestBody StudentInformation getStudentInformation) {
        System.out.println("*********************************GetStudentInformationById API*******************************");
        StudentInformationApiResponse studentResponeApi = new StudentInformationApiResponse();
        StudentinformationAction studentinformationAction = new StudentinformationAction();
        try {
            studentResponeApi = studentinformationAction.getStudentInformationById(getStudentInformation);
            return studentResponeApi;
        } catch (Exception e) {

        }
        return null;
    }

    @GetMapping("/get_allstudents")
    public StudentInformationApiResponse getAllStudentInformations() {
        System.out.println("*********************************GetAllStudentInformations API*******************************");
        StudentInformationApiResponse studentResponeApi = new StudentInformationApiResponse();
        StudentinformationAction studentinformationAction = new StudentinformationAction();
        try {
            studentResponeApi = studentinformationAction.getAllStudentInformation();
            return studentResponeApi;
        } catch (Exception e) {

        }
        return null;
    }

    @DeleteMapping
    public StudentInformationApiResponse deleteStudentInformationById(@RequestBody StudentInformation studentInformation) {
        System.out.println("*********************************DeleteStudentInformationById API*******************************");
        StudentInformationApiResponse studentResponeApi = new StudentInformationApiResponse();
        StudentinformationAction studentinformationAction = new StudentinformationAction();
        try {
            studentResponeApi = studentinformationAction.deleteStudentInformationById(studentInformation);
            return studentResponeApi;
        } catch (Exception e) {

        }
        return null;
    }

    @DeleteMapping("/rollback")
    public StudentInformationApiResponse undeleteStudentInformationById(@RequestBody StudentInformation studentInformation) {
        System.out.println("*********************************undeleteStudentInformationById API*******************************");
        StudentInformationApiResponse studentResponeApi = new StudentInformationApiResponse();
        StudentinformationAction studentinformationAction = new StudentinformationAction();
        try {
            studentResponeApi = studentinformationAction.undeleteStudentInformationById(studentInformation);
            return studentResponeApi;
        } catch (Exception e) {

        }
        return null;
    }
}
