/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.management.Actions;

import com.management.constants.MessageConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.management.model.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Abhishek
 */
public class StudentinformationAction implements MessageConstants {

    public static final String url = "jdbc:mysql://localhost:3306/acs_report_tenant_1";
    public static final String user = "root";
    public static final String pass = "ABHI";
    Connection con = null;

//**************************************EndPoints*******************************************************************************    
    //pushStudentInformation
    public StudentInformationApiResponse pushStudentInformation(StudentInformation pushStudentInformation) {
        StudentInformationApiResponse studentApiResponse = new StudentInformationApiResponse();
        QualificationDetails qualificationDetails = new QualificationDetails();

        int studentid, rollno, active, isdelete, percentage, duration, status, revisionno = 0;
        String firstname, lastname, gender, contactno, emailid, address1, city, state, country, pincode = null;
        String institutionname, dob, fromdate, todate, course, resdatetime, statusdesc = null;

        List<StudentInformation> studentInformationList = null;
        List<QualificationDetails> qualificationDetailList = null;
        List<RessidentialAddress> ressidentialAddressList = null;
        revisionno = 1;
        resdatetime = pushStudentInformation.getCurrentDateTime();
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("***************Connection done sucessfull************************");
            studentApiResponse.setResdatetime(resdatetime);
            studentApiResponse.setRevisionno(revisionno);

            rollno = pushStudentInformation.getRollno();
            firstname = pushStudentInformation.getFirstname();
            lastname = pushStudentInformation.getLastname();
            gender = pushStudentInformation.getGender();
            contactno = pushStudentInformation.getContactno();
            emailid = pushStudentInformation.getEmailid();
            active = pushStudentInformation.getActive();
            dob = pushStudentInformation.getDob();

            if (firstname == null || firstname.isBlank() || firstname.trim().length() < 1) {
                studentApiResponse.setStatus(2);
                studentApiResponse.setStatusdesc("firstname should not be blank");
                return studentApiResponse;
            }
            if (checkMaxStringLength(firstname, 100) != true) {
                studentApiResponse.setStatus(3);
                studentApiResponse.setStatusdesc("firstname length should  not be greater than 100");
                return studentApiResponse;
            }
            if (checkMinStringLength(firstname, 3) != true) {
                studentApiResponse.setStatus(4);
                studentApiResponse.setStatusdesc("firstname length should not be less than 3");
                return studentApiResponse;
            }

            if (lastname == null || lastname.isBlank() || lastname.trim().length() < 1) {
                studentApiResponse.setStatus(5);
                studentApiResponse.setStatusdesc("lastname should not be blank");
                return studentApiResponse;
            }
            if (checkMaxStringLength(lastname, 100) != true) {
                studentApiResponse.setStatus(6);
                studentApiResponse.setStatusdesc("lastname length should not be greater than 100");
                return studentApiResponse;
            }
            if (checkMinStringLength(lastname, 3) != true) {
                studentApiResponse.setStatus(7);
                studentApiResponse.setStatusdesc("lastname length should not be less than 3");
                return studentApiResponse;
            }

            if (gender == null || gender.isBlank() || gender.trim().length() < 1) {
                studentApiResponse.setStatus(8);
                studentApiResponse.setStatusdesc("gender should not be blank");
                return studentApiResponse;
            }
            if (checkMaxStringLength(gender, 10) != true) {
                studentApiResponse.setStatus(9);
                studentApiResponse.setStatusdesc("gender length should not be greater than 10");
                return studentApiResponse;
            }

            if (!validateGenderRange(gender.toUpperCase(), VALID_GENDER_VALUE)) {
                studentApiResponse.setStatus(31);
                studentApiResponse.setStatusdesc("Enter a valid gender in Uppercase");
                return studentApiResponse;
            }

            if (dob == null || dob.isBlank() || dob.trim().length() < 1) {
                studentApiResponse.setStatus(10);
                studentApiResponse.setStatusdesc("dob should not be blank");
                return studentApiResponse;
            }
            if (isValidDateFormat(dob) != true) {
                studentApiResponse.setStatus(27);
                studentApiResponse.setStatusdesc("dob formate invalid");
                return studentApiResponse;
            }

            if (rollno <= 0) {
                studentApiResponse.setStatus(11);
                studentApiResponse.setStatusdesc("rollno should not be 0 or negative");
                return studentApiResponse;
            }
            if (checkRollnoExists(rollno)) {
                studentApiResponse.setStatus(28);
                studentApiResponse.setStatusdesc("rollno already associated to student");
                return studentApiResponse;
            }

            if (active < 0 || active > 1) {
                studentApiResponse.setStatus(12);
                studentApiResponse.setStatusdesc("active should be 0 or 1");
                return studentApiResponse;
            }

            if (emailid != null && emailid.trim().length() > 1) {
                if (checkMaxStringLength(emailid, 200) != true) {
                    studentApiResponse.setStatus(13);
                    studentApiResponse.setStatusdesc("emailid length should not be greater than 200");
                    return studentApiResponse;
                }

                String bccListArray[] = emailid.split(";");
                for (String bccEmail : bccListArray) {

                    if (emailValidate(bccEmail) != true) {
                        studentApiResponse.setStatus(14);
                        studentApiResponse.setStatusdesc("emailid invalid");
                        return studentApiResponse;

                    }

                }

            }

            if (contactno != null && contactno.trim().length() > 1) {
                if (contactno == null || contactno.isBlank() || contactno.trim().length() < 1) {
                    studentApiResponse.setStatus(15);
                    studentApiResponse.setStatusdesc("contactno should not be blank");
                    return studentApiResponse;
                }

                String[] contactNumbers = contactno.split(";");
                for (String number : contactNumbers) {
                    if (!isValidContact(number)) {
                        studentApiResponse.setStatus(16);
                        studentApiResponse.setStatusdesc("Invalid contact number format");
                        return studentApiResponse;
                    }

                }
            }

            ressidentialAddressList = pushStudentInformation.getRessidentialAddress();
            if (ressidentialAddressList != null) {
                for (RessidentialAddress details : ressidentialAddressList) {
                    address1 = details.getAddress1();
                    city = details.getCity();
                    pincode = details.getPincode();
                    state = details.getState();
                    country = details.getCountry();

                    if (address1 == null || address1.isBlank() || address1.trim().length() < 1) {
                        studentApiResponse.setStatus(31);
                        studentApiResponse.setStatusdesc("address1 should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(address1, 100) != true) {
                        studentApiResponse.setStatus(32);
                        studentApiResponse.setStatusdesc("address1 length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(address1, 3) != true) {
                        studentApiResponse.setStatus(33);
                        studentApiResponse.setStatusdesc("address1 length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (city == null || city.isBlank() || city.trim().length() < 1) {
                        studentApiResponse.setStatus(34);
                        studentApiResponse.setStatusdesc("city should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(city, 100) != true) {
                        studentApiResponse.setStatus(35);
                        studentApiResponse.setStatusdesc("city length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(city, 3) != true) {
                        studentApiResponse.setStatus(36);
                        studentApiResponse.setStatusdesc("city length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (state == null || state.isBlank() || state.trim().length() < 1) {
                        studentApiResponse.setStatus(37);
                        studentApiResponse.setStatusdesc("state should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(state, 100) != true) {
                        studentApiResponse.setStatus(38);
                        studentApiResponse.setStatusdesc("state length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(state, 3) != true) {
                        studentApiResponse.setStatus(39);
                        studentApiResponse.setStatusdesc("state length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (country == null || country.isBlank() || country.trim().length() < 1) {
                        studentApiResponse.setStatus(40);
                        studentApiResponse.setStatusdesc("country should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(country, 100) != true) {
                        studentApiResponse.setStatus(41);
                        studentApiResponse.setStatusdesc("country length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(country, 3) != true) {
                        studentApiResponse.setStatus(42);
                        studentApiResponse.setStatusdesc("country length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (pincode == null || pincode.isBlank() || pincode.trim().length() < 1) {
                        studentApiResponse.setStatus(43);
                        studentApiResponse.setStatusdesc("pincode should not be blank");
                        return studentApiResponse;
                    }

                    if (isValidPincode(pincode) != true) {
                        studentApiResponse.setStatus(44);
                        studentApiResponse.setStatusdesc("pincode formate invalid");
                        return studentApiResponse;
                    }

                }
            }

            qualificationDetailList = pushStudentInformation.getQualificationDetails();
            if (qualificationDetailList != null) {
                for (QualificationDetails details : qualificationDetailList) {
                    institutionname = details.getInstitutionname();
                    course = details.getCourse();
                    duration = details.getDuration();
                    fromdate = details.getFromdate();
                    todate = details.getTodate();
                    percentage = details.getPercentage();

                    if (institutionname == null || institutionname.isBlank() || institutionname.trim().length() < 1) {
                        studentApiResponse.setStatus(17);
                        studentApiResponse.setStatusdesc("institutionname should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(institutionname, 100) != true) {
                        studentApiResponse.setStatus(18);
                        studentApiResponse.setStatusdesc("institutionname length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(firstname, 3) != true) {
                        studentApiResponse.setStatus(19);
                        studentApiResponse.setStatusdesc("institutionname length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (course == null || course.isBlank() || course.trim().length() < 1) {
                        studentApiResponse.setStatus(20);
                        studentApiResponse.setStatusdesc("course should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(course, 100) != true) {
                        studentApiResponse.setStatus(21);
                        studentApiResponse.setStatusdesc("course length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(course, 3) != true) {
                        studentApiResponse.setStatus(22);
                        studentApiResponse.setStatusdesc("course length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (duration <= 0) {
                        studentApiResponse.setStatus(23);
                        studentApiResponse.setStatusdesc("duration should not be 0 or negative");
                        return studentApiResponse;
                    }

                    if (fromdate == null || fromdate.isBlank() || fromdate.trim().length() < 1) {
                        studentApiResponse.setStatus(24);
                        studentApiResponse.setStatusdesc("fromdate should not be blank");
                        return studentApiResponse;
                    }
                    if (todate == null || todate.isBlank() || todate.trim().length() < 1) {
                        studentApiResponse.setStatus(25);
                        studentApiResponse.setStatusdesc("todate should not be blank");
                        return studentApiResponse;
                    }

                    if (isValidDateFormat(fromdate) != true) {
                        studentApiResponse.setStatus(26);
                        studentApiResponse.setStatusdesc("fromdate formate invalid");
                        return studentApiResponse;
                    }
                    if (isValidDateFormat(todate) != true) {
                        studentApiResponse.setStatus(26);
                        studentApiResponse.setStatusdesc("todate formate invalid");
                        return studentApiResponse;
                    }
                    if (percentage < 1 || percentage > 100) {
                        studentApiResponse.setStatus(12);
                        studentApiResponse.setStatusdesc("percentage invalid");
                        return studentApiResponse;
                    }
                }
            }

            studentInformationList = addStudentInformation(pushStudentInformation);
            System.out.println("studentInformationList   >>>>>>>>>>>  " + studentInformationList);

            if (studentInformationList != null && !studentInformationList.isEmpty()) {
                studentApiResponse.setStatus(1);
                studentApiResponse.setStatusdesc("Success");
                studentApiResponse.setStudentInformation(studentInformationList);
                System.out.println("*******************Data Insertation Sucessfully************************");
            } else {
                studentApiResponse.setStatus(0);
                studentApiResponse.setStatusdesc("Fail");
            }

        } catch (Exception e) {

        } finally {
            firstname = null;
            lastname = null;
            gender = null;
            contactno = null;
            emailid = null;
            studentInformationList = null;
            qualificationDetailList = null;
        }
        return studentApiResponse;
    }

    //putStudentInformation
    public StudentInformationApiResponse putStudentInformation(StudentInformation putStudentInformation) {
        StudentInformationApiResponse studentApiResponse = new StudentInformationApiResponse();
        QualificationDetails qualificationDetails = new QualificationDetails();


        int studentid, rollno, active, isdelete, percentage, duration, status, revisionno = 0;
        String firstname, lastname, gender, contactno, emailid, address1, city, state, country, pincode = null;
        String institutionname, dob, fromdate, todate, course, resdatetime, statusdesc = null;

        List<StudentInformation> studentInformationList = null;
        List<QualificationDetails> qualificationDetailList = null;
        List<RessidentialAddress> ressidentialAddressList = null;
        revisionno = 1;
        resdatetime = putStudentInformation.getCurrentDateTime();
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("***************Connection done sucessfull************************");
            studentApiResponse.setResdatetime(resdatetime);
            studentApiResponse.setRevisionno(revisionno);

            studentid = putStudentInformation.getStudentid();
            rollno = putStudentInformation.getRollno();
            firstname = putStudentInformation.getFirstname();
            lastname = putStudentInformation.getLastname();
            gender = putStudentInformation.getGender();
            contactno = putStudentInformation.getContactno();
            emailid = putStudentInformation.getEmailid();
            active = putStudentInformation.getActive();
            dob = putStudentInformation.getDob();

            if (!checkStudentIdExists(studentid)) {
                studentApiResponse.setStatus(29);
                studentApiResponse.setStatusdesc("studentid do not exists");
                return studentApiResponse;
            }

            if (firstname == null || firstname.isBlank() || firstname.trim().length() < 1) {
                studentApiResponse.setStatus(2);
                studentApiResponse.setStatusdesc("firstname should not be blank");
                return studentApiResponse;
            }
            if (checkMaxStringLength(firstname, 100) != true) {
                studentApiResponse.setStatus(3);
                studentApiResponse.setStatusdesc("firstname length should  not be greater than 100");
                return studentApiResponse;
            }
            if (checkMinStringLength(firstname, 3) != true) {
                studentApiResponse.setStatus(4);
                studentApiResponse.setStatusdesc("firstname length should not be less than 3");
                return studentApiResponse;
            }

            if (lastname == null || lastname.isBlank() || lastname.trim().length() < 1) {
                studentApiResponse.setStatus(5);
                studentApiResponse.setStatusdesc("lastname should not be blank");
                return studentApiResponse;
            }
            if (checkMaxStringLength(lastname, 100) != true) {
                studentApiResponse.setStatus(6);
                studentApiResponse.setStatusdesc("lastname length should not be greater than 100");
                return studentApiResponse;
            }
            if (checkMinStringLength(lastname, 3) != true) {
                studentApiResponse.setStatus(7);
                studentApiResponse.setStatusdesc("lastname length should not be less than 3");
                return studentApiResponse;
            }

            if (gender == null || gender.isBlank() || gender.trim().length() < 1) {
                studentApiResponse.setStatus(8);
                studentApiResponse.setStatusdesc("gender should not be blank");
                return studentApiResponse;
            }
            if (checkMaxStringLength(gender, 10) != true) {
                studentApiResponse.setStatus(9);
                studentApiResponse.setStatusdesc("gender length should not be greater than 10");
                return studentApiResponse;
            }

            if (dob == null || dob.isBlank() || dob.trim().length() < 1) {
                studentApiResponse.setStatus(10);
                studentApiResponse.setStatusdesc("dob should not be blank");
                return studentApiResponse;
            }
            if (isValidDateFormat(dob) != true) {
                studentApiResponse.setStatus(27);
                studentApiResponse.setStatusdesc("dob formate invalid");
                return studentApiResponse;
            }

            if (rollno <= 0) {
                studentApiResponse.setStatus(11);
                studentApiResponse.setStatusdesc("rollno should not be 0 or negative");
                return studentApiResponse;
            }
            if (checkRollnoExists(rollno)) {
                studentApiResponse.setStatus(28);
                studentApiResponse.setStatusdesc("rollno already associated to student");
                return studentApiResponse;
            }

            if (active < 0 || active > 1) {
                studentApiResponse.setStatus(12);
                studentApiResponse.setStatusdesc("active should be 0 or 1");
                return studentApiResponse;
            }

            if (emailid != null && emailid.trim().length() > 1) {
                if (checkMaxStringLength(emailid, 200) != true) {
                    studentApiResponse.setStatus(13);
                    studentApiResponse.setStatusdesc("emailid length should not be greater than 200");
                    return studentApiResponse;
                }

                String bccListArray[] = emailid.split(";");
                for (String bccEmail : bccListArray) {

                    if (emailValidate(bccEmail) != true) {
                        studentApiResponse.setStatus(14);
                        studentApiResponse.setStatusdesc("emailid invalid");
                        return studentApiResponse;

                    }

                }

            }

            if (contactno != null && contactno.trim().length() > 1) {
                if (contactno == null || contactno.isBlank() || contactno.trim().length() < 1) {
                    studentApiResponse.setStatus(15);
                    studentApiResponse.setStatusdesc("contactno should not be blank");
                    return studentApiResponse;
                }

                String[] contactNumbers = contactno.split(";");
                for (String number : contactNumbers) {
                    if (!isValidContact(number)) {
                        studentApiResponse.setStatus(16);
                        studentApiResponse.setStatusdesc("Invalid contact number format");
                        return studentApiResponse;
                    }

                }
            }
             ressidentialAddressList = putStudentInformation.getRessidentialAddress();
            if (ressidentialAddressList != null) {
                for (RessidentialAddress details : ressidentialAddressList) {
                    address1 = details.getAddress1();
                    city = details.getCity();
                    pincode = details.getPincode();
                    state = details.getState();
                    country = details.getCountry();

                    if (address1 == null || address1.isBlank() || address1.trim().length() < 1) {
                        studentApiResponse.setStatus(31);
                        studentApiResponse.setStatusdesc("address1 should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(address1, 100) != true) {
                        studentApiResponse.setStatus(32);
                        studentApiResponse.setStatusdesc("address1 length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(address1, 3) != true) {
                        studentApiResponse.setStatus(33);
                        studentApiResponse.setStatusdesc("address1 length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (city == null || city.isBlank() || city.trim().length() < 1) {
                        studentApiResponse.setStatus(34);
                        studentApiResponse.setStatusdesc("city should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(city, 100) != true) {
                        studentApiResponse.setStatus(35);
                        studentApiResponse.setStatusdesc("city length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(city, 3) != true) {
                        studentApiResponse.setStatus(36);
                        studentApiResponse.setStatusdesc("city length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (state == null || state.isBlank() || state.trim().length() < 1) {
                        studentApiResponse.setStatus(37);
                        studentApiResponse.setStatusdesc("state should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(state, 100) != true) {
                        studentApiResponse.setStatus(38);
                        studentApiResponse.setStatusdesc("state length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(state, 3) != true) {
                        studentApiResponse.setStatus(39);
                        studentApiResponse.setStatusdesc("state length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (country == null || country.isBlank() || country.trim().length() < 1) {
                        studentApiResponse.setStatus(40);
                        studentApiResponse.setStatusdesc("country should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(country, 100) != true) {
                        studentApiResponse.setStatus(41);
                        studentApiResponse.setStatusdesc("country length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(country, 3) != true) {
                        studentApiResponse.setStatus(42);
                        studentApiResponse.setStatusdesc("country length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (pincode == null || pincode.isBlank() || pincode.trim().length() < 1) {
                        studentApiResponse.setStatus(43);
                        studentApiResponse.setStatusdesc("pincode should not be blank");
                        return studentApiResponse;
                    }

                    if (isValidPincode(pincode) != true) {
                        studentApiResponse.setStatus(44);
                        studentApiResponse.setStatusdesc("pincode formate invalid");
                        return studentApiResponse;
                    }

                }
            }
            
            
            qualificationDetailList = putStudentInformation.getQualificationDetails();
            if (qualificationDetailList != null) {
                for (QualificationDetails details : qualificationDetailList) {
                    institutionname = details.getInstitutionname();
                    course = details.getCourse();
                    duration = details.getDuration();
                    fromdate = details.getFromdate();
                    todate = details.getTodate();
                    percentage = details.getPercentage();

                    if (institutionname == null || institutionname.isBlank() || institutionname.trim().length() < 1) {
                        studentApiResponse.setStatus(17);
                        studentApiResponse.setStatusdesc("institutionname should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(institutionname, 100) != true) {
                        studentApiResponse.setStatus(18);
                        studentApiResponse.setStatusdesc("institutionname length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(firstname, 3) != true) {
                        studentApiResponse.setStatus(19);
                        studentApiResponse.setStatusdesc("institutionname length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (course == null || course.isBlank() || course.trim().length() < 1) {
                        studentApiResponse.setStatus(20);
                        studentApiResponse.setStatusdesc("course should not be blank");
                        return studentApiResponse;
                    }
                    if (checkMaxStringLength(course, 100) != true) {
                        studentApiResponse.setStatus(21);
                        studentApiResponse.setStatusdesc("course length should  not be greater than 100");
                        return studentApiResponse;
                    }
                    if (checkMinStringLength(course, 3) != true) {
                        studentApiResponse.setStatus(22);
                        studentApiResponse.setStatusdesc("course length should not be less than 3");
                        return studentApiResponse;
                    }

                    if (duration <= 0) {
                        studentApiResponse.setStatus(23);
                        studentApiResponse.setStatusdesc("duration should not be 0 or negative");
                        return studentApiResponse;
                    }

                    if (fromdate == null || fromdate.isBlank() || fromdate.trim().length() < 1) {
                        studentApiResponse.setStatus(24);
                        studentApiResponse.setStatusdesc("fromdate should not be blank");
                        return studentApiResponse;
                    }
                    if (todate == null || todate.isBlank() || todate.trim().length() < 1) {
                        studentApiResponse.setStatus(25);
                        studentApiResponse.setStatusdesc("todate should not be blank");
                        return studentApiResponse;
                    }

                    if (isValidDateFormat(fromdate) != true) {
                        studentApiResponse.setStatus(26);
                        studentApiResponse.setStatusdesc("fromdate formate invalid");
                        return studentApiResponse;
                    }
                    if (isValidDateFormat(todate) != true) {
                        studentApiResponse.setStatus(26);
                        studentApiResponse.setStatusdesc("todate formate invalid");
                        return studentApiResponse;
                    }
                }
            }
            System.out.println(putStudentInformation.toString());

            studentInformationList = editStudentInformation(putStudentInformation);
            System.out.println("studentInformationList   >>>>>>>>>>>  " + studentInformationList);

            if (studentInformationList != null && !studentInformationList.isEmpty()) {
                studentApiResponse.setStatus(1);
                studentApiResponse.setStatusdesc("Success");
                studentApiResponse.setStudentInformation(studentInformationList);
                System.out.println("*******************Data Updated Sucessfully************************");
            } else {
                studentApiResponse.setStatus(0);
                studentApiResponse.setStatusdesc("Fail");
            }

        } catch (Exception e) {

        } finally {
            firstname = null;
            lastname = null;
            gender = null;
            contactno = null;
            emailid = null;
            studentInformationList = null;
            qualificationDetailList = null;
        }
        return studentApiResponse;
    }

    //getStudentInformationById
    public StudentInformationApiResponse getStudentInformationById(StudentInformation studentInformation) {
        StudentInformationApiResponse studentApiResponse = new StudentInformationApiResponse();

        int studentid, status, revisionno = 0;
        String resdatetime, statusdesc = null;

        List<StudentInformation> studentInformationList = new ArrayList<>();
        List<QualificationDetails> qualificationDetailList = null;
        revisionno = 1;
        resdatetime = studentInformation.getCurrentDateTime();
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("***************Connection done successfully************************");
            studentApiResponse.setResdatetime(resdatetime);
            studentApiResponse.setRevisionno(revisionno);

            studentid = studentInformation.getStudentid();

            if (studentid <= 0) {
                studentApiResponse.setStatus(30);
                studentApiResponse.setStatusdesc("studentid shouldnot be 0 or negative");
                return studentApiResponse;
            }

            if (!checkStudentIdExists(studentid)) {
                studentApiResponse.setStatus(29);
                studentApiResponse.setStatusdesc("studentid do not exists");
                return studentApiResponse;
            }
            System.out.println("studentid :: " + studentid);

            studentInformation = getStudentInformation(studentid);
            System.out.println("studentInformation   >>>>>>>>>>>  " + studentInformation);

            if (studentInformation != null) {
                studentInformationList.add(studentInformation);
                System.out.println("studentInformationList   >>>>>>>>>>>  " + studentInformationList);
            }

            if (!studentInformationList.isEmpty()) {
                studentApiResponse.setStatus(1);
                studentApiResponse.setStatusdesc("Success");
                studentApiResponse.setStudentInformation(studentInformationList);
                System.out.println("*******************Student Data Retrieved Successfully for studentid ::" + studentid + "  *******************");
            } else {
                studentApiResponse.setStatus(0);
                studentApiResponse.setStatusdesc("Fail");
            }

        } catch (Exception e) {
            // Handle exceptions
        } finally {
            // Clean up resources if necessary
        }
        return studentApiResponse;
    }

    //deleteStudentInformationById
    public StudentInformationApiResponse deleteStudentInformationById(StudentInformation studentInformation) {
        StudentInformationApiResponse studentApiResponse = new StudentInformationApiResponse();

        int studentid, deleteStudent, revisionno = 0;
        String firstname, lastname, gender, contactno, emailid = null;
        String institutionname, dob, fromdate, todate, course, resdatetime, statusdesc = null;

        List<StudentInformation> studentInformationList = new ArrayList<>();
        List<QualificationDetails> qualificationDetailList = null;
        revisionno = 1;
        resdatetime = studentInformation.getCurrentDateTime();
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("***************Connection done sucessfull************************");
            studentApiResponse.setResdatetime(resdatetime);
            studentApiResponse.setRevisionno(revisionno);

            studentid = studentInformation.getStudentid();

            System.out.println("studentid :: " + studentid);

            if (studentid <= 0) {
                studentApiResponse.setStatus(30);
                studentApiResponse.setStatusdesc("studentid shouldnot be 0 or negative");
                return studentApiResponse;
            }

            if (!checkStudentIdExists(studentid)) {
                studentApiResponse.setStatus(29);
                studentApiResponse.setStatusdesc("studentid do not exists");
                return studentApiResponse;
            }
            deleteStudent = deleteStudentInformation(studentInformation);
            System.out.println("deleteStudent :: " + deleteStudent);

            if (deleteStudent != 0) {
                studentApiResponse.setStatus(1);
                studentApiResponse.setStatusdesc("Success");
                System.out.println("*******************Student Data Deleted Sucessfully for studentid ::" + studentid + "  *******************");
            } else {
                studentApiResponse.setStatus(0);
                studentApiResponse.setStatusdesc("Fail");
            }

        } catch (Exception e) {

        } finally {

        }
        return studentApiResponse;
    }

    //getAllStudentInformation
    public StudentInformationApiResponse getAllStudentInformation() {
        StudentInformationApiResponse studentApiResponse = new StudentInformationApiResponse();

        int studentid, status, revisionno = 0;
        String resdatetime, statusdesc = null;

        List<StudentInformation> studentInformationList = new ArrayList<>();
        List<QualificationDetails> qualificationDetailList = null;
        revisionno = 1;

        StudentInformation studentInformation = null;
        resdatetime = studentInformation.getCurrentDateTime();
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("***************Connection done successfully************************");
            studentApiResponse.setResdatetime(resdatetime);
            studentApiResponse.setRevisionno(revisionno);

            studentInformationList = getAllStudentsInformation();

            System.out.println("studentInformationList   >>>>>>>>>>>  " + studentInformationList);

            if (studentInformationList.size() > 0) {

                studentApiResponse.setStatus(1);
                studentApiResponse.setStatusdesc("SUCESS");
                studentApiResponse.setStudentInformation(studentInformationList);

            } else {

                studentApiResponse.setStatus(0);
                studentApiResponse.setStatusdesc("FAIL");
                studentApiResponse.setStudentInformation(studentInformationList);

            }

        } catch (Exception e) {
            // Handle exceptions
        } finally {
            // Clean up resources if necessary
        }
        return studentApiResponse;
    }

    //deleteStudentInformationById
    public StudentInformationApiResponse undeleteStudentInformationById(StudentInformation studentInformation) {
        StudentInformationApiResponse studentApiResponse = new StudentInformationApiResponse();

        int undeleteStudent, revisionno = 0;
        String firstname, lastname, gender, contactno, emailid = null;
        String institutionname, dob, fromdate, todate, course, resdatetime, statusdesc = null;

        List<StudentInformation> studentInformationList = new ArrayList<>();
        List<QualificationDetails> qualificationDetailList = null;
        revisionno = 1;
        resdatetime = studentInformation.getCurrentDateTime();
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("***************Connection done sucessfull************************");
            studentApiResponse.setResdatetime(resdatetime);
            studentApiResponse.setRevisionno(revisionno);

            int studentid = studentInformation.getStudentid();

            System.out.println("studentid :: " + studentid);

            if (studentid <= 0) {
                studentApiResponse.setStatus(30);
                studentApiResponse.setStatusdesc("studentid shouldnot be 0 or negative");
                return studentApiResponse;
            }

            if (checkStudentIdExists(studentid)) {
                studentApiResponse.setStatus(35);
                studentApiResponse.setStatusdesc("studentid already exists exists");
                return studentApiResponse;
            }
            undeleteStudent = undeleteStudentInformation(studentInformation);
            System.out.println("undeleteStudent :: " + undeleteStudent);

            if (undeleteStudent != 0) {
                studentApiResponse.setStatus(1);
                studentApiResponse.setStatusdesc("Student Data Backedup Sucessfully with studentid :: " + studentid);
                System.out.println("*******************Student Data Backedup Sucessfully for studentid ::" + studentid + "  *******************");
            } else {
                studentApiResponse.setStatus(0);
                studentApiResponse.setStatusdesc("Fail");
            }

        } catch (Exception e) {

        } finally {

        }
        return studentApiResponse;
    }

//**************************************Utilities Methods****************************************************    
    public int undeleteStudentInformation(StudentInformation studentInformation) {
        CallableStatement cstmt1 = null, cstmt2 = null;
        ResultSet rs2 = null;
        String query1 = null, query2 = null;
        int check1 = 0, succesId, studentid = 0;

        try {
            query1 = " UPDATE student_information SET isdelete=0, active= 1 WHERE studentid =?";
            cstmt1 = con.prepareCall(query1);
            cstmt1.setInt(1, studentInformation.getStudentid());
            System.out.println("delete Student :: " + cstmt1);
            check1 = cstmt1.executeUpdate();

            if (check1 == 0) {
                System.out.println("fail to Undelete Student");
            }

            succesId = studentInformation.getStudentid();

            return succesId;
        } catch (Exception e) {

        } finally {

        }

        return 0;

    }

    public List<StudentInformation> getAllStudentsInformation() {
        CallableStatement cstmt1 = null;
        ResultSet rs1 = null;
        String query1 = null;
        List<StudentInformation> studentInformationList = new ArrayList<StudentInformation>();
        try {
            query1 = "SELECT studentid, rollno, first_name, last_name, gender, contact_number, emailid, dob, active, isdelete FROM student_information WHERE isdelete=0";
            cstmt1 = con.prepareCall(query1);
            System.out.println("getAllStudentsInformation ::  >>>>  " + cstmt1);
            rs1 = cstmt1.executeQuery();

            while (rs1 != null && rs1.next()) {
                StudentInformation studentInformation = new StudentInformation();
                studentInformation.setStudentid(rs1.getInt("studentid"));
                studentInformation.setRollno(rs1.getInt("rollno"));
                studentInformation.setFirstname(rs1.getString("first_name"));
                studentInformation.setLastname(rs1.getString("last_name"));
                studentInformation.setGender(rs1.getString("gender"));
                studentInformation.setContactno(rs1.getString("contact_number"));
                studentInformation.setEmailid(rs1.getString("emailid"));
                studentInformation.setDob(rs1.getString("dob"));
                studentInformation.setActive(rs1.getInt("active"));
                studentInformation.setIsdelete(rs1.getInt("isdelete"));
                studentInformation.setQualificationDetails(getQualificationDetails(studentInformation.getStudentid()));
                studentInformation.setRessidentialAddress(getRessidentialAddress(studentInformation.getStudentid()));

                studentInformationList.add(studentInformation);
            }

            return studentInformationList;
        } catch (Exception e) {

        } finally {

        }
        return null;

    }

    public int deleteStudentInformation(StudentInformation studentInformation) {
        CallableStatement cstmt1 = null, cstmt2 = null;
        ResultSet rs2 = null;
        String query1 = null, query2 = null;
        int check1 = 0, succesId, studentid = 0;

        try {
            query1 = " UPDATE student_information SET isdelete=1, active= 0 WHERE studentid =?";
            cstmt1 = con.prepareCall(query1);
            cstmt1.setInt(1, studentInformation.getStudentid());
            System.out.println("delete Student :: " + cstmt1);
            check1 = cstmt1.executeUpdate();

            if (check1 == 0) {
                System.out.println("fail to delete Student");
            }

            succesId = studentInformation.getStudentid();

            return succesId;
        } catch (Exception e) {

        } finally {

        }

        return 0;

    }

    public List<StudentInformation> addStudentInformation(StudentInformation studentInformation) {
        CallableStatement cstmt1 = null, cstmt2 = null;
        ResultSet rs2 = null;
        String query1 = null, query2 = null;
        int check1 = 0;
        int check3 = 0;
        int studentid = 0;

        ArrayList<StudentInformation> studentInformationList = new ArrayList<StudentInformation>();

        try {
            System.out.println("*************addStudentInformation********************");
            query1 = "INSERT INTO student_information(rollno,first_name,last_name,gender,contact_number,emailid,dob,active,isdelete) VALUES(?,?,?,?,?,?,?,?,0)";
            cstmt1 = con.prepareCall(query1);
            cstmt1.setInt(1, studentInformation.getRollno());
            cstmt1.setString(2, studentInformation.getFirstname());
            cstmt1.setString(3, studentInformation.getLastname());
            cstmt1.setString(4, studentInformation.getGender());
            cstmt1.setString(5, studentInformation.getContactno());
            cstmt1.setString(6, studentInformation.getEmailid());
            cstmt1.setString(7, studentInformation.getDob());
            cstmt1.setInt(8, studentInformation.getActive());

            check1 = cstmt1.executeUpdate();

            System.out.println("Student Information >> " + check1);
            if (check1 == 0) {
                System.out.println("addStudentInformation failed to update query");
                return null;
            }

            query2 = "select last_insert_id() as studentid";
            cstmt2 = con.prepareCall(query2);
            System.out.println("addStudentInformation query2 : " + cstmt2.toString());
            rs2 = cstmt2.executeQuery();

            if (rs2 != null) {
                if (rs2.next()) {
                    studentid = rs2.getInt("studentid");
                    System.out.println("addStudentInformation studentid :" + studentid);
                }
            }

            if (studentid <= 0) {
                System.out.println("addStudentInformation studentid found 0");
                return null;
            }

            // **************************************************************************************
            if (studentInformation.getQualificationDetails() != null) {
                // addQualificationdetails
                int check2 = addQualificationdetails(studentid, studentInformation.getQualificationDetails());
                System.out.println("addReportParameterDetail check : " + check2);
            }
            // **************************************************************************************

//            List<RessidentialAddress> ressidentialAddressList = new ArrayList<RessidentialAddress>();
            // **************************************************************************************
            if (studentInformation.getRessidentialAddress() != null) {
                // addRessidentialAddress
                check3 = addResidentialAddress(studentid, studentInformation.getRessidentialAddress());
                System.out.println("addRessidentialAddress check : " + check3);
            }
            // **************************************************************************************

            StudentInformation details = getStudentInformation(studentid);

            if (details == null) {
                return null;
            }
            studentInformationList.add(details);
        } catch (Exception e) {
            System.out.print(e);
        } finally {
            // Close resources and set variables to null
            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (cstmt2 != null) {
                    cstmt2.close();
                }
                if (cstmt1 != null) {
                    cstmt1.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return studentInformationList;
    }

    public List<StudentInformation> editStudentInformation(StudentInformation studentInformation) {
        CallableStatement cstmt1 = null, cstmt2 = null;
        ResultSet rs2 = null;
        String query1 = null, query2 = null;
        int check1 = 0;
        int check3 = 0;
        int studentid = 0;

        ArrayList<StudentInformation> studentInformationList = new ArrayList<StudentInformation>();

        try {
            System.out.println("*************editStudentInformation********************");
            query1 = "UPDATE student_information SET rollno = ?,first_name=? ,last_name=?,gender=?,contact_number=?,emailid=?,dob=?,active=?  WHERE studentid=?";
            cstmt1 = con.prepareCall(query1);
            cstmt1.setInt(1, studentInformation.getRollno());
            cstmt1.setString(2, studentInformation.getFirstname());
            cstmt1.setString(3, studentInformation.getLastname());
            cstmt1.setString(4, studentInformation.getGender());
            cstmt1.setString(5, studentInformation.getContactno());
            cstmt1.setString(6, studentInformation.getEmailid());
            cstmt1.setString(7, studentInformation.getDob());
            cstmt1.setInt(8, studentInformation.getActive());
            cstmt1.setInt(9, studentInformation.getStudentid());
            check1 = cstmt1.executeUpdate();

            System.out.println("IsStudentInformation updated>> " + check1);
            if (check1 == 0) {
                System.out.println("editStudentInformation failed to update query");
                return null;
            }
            studentid = studentInformation.getStudentid();

            // **************************************************************************************
            if (studentInformation.getQualificationDetails() != null) {
                // addQualificationdetails
                int check2 = addQualificationdetails(studentid, studentInformation.getQualificationDetails());
                System.out.println("addQualificationdetails check : " + check2);
            }
            // **************************************************************************************
            // **************************************************************************************
            if (studentInformation.getRessidentialAddress() != null) {
                // addRessidentialAddress
                check3 = addResidentialAddress(studentid, studentInformation.getRessidentialAddress());
                System.out.println("addRessidentialAddress check : " + check3);
            }
            // **************************************************************************************

            StudentInformation details = getStudentInformation(studentid);
            System.out.println("getStudentInformation  ::  " + details.toString());
            if (details == null) {
                return null;
            }
            studentInformationList.add(details);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // Close resources and set variables to null
            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (cstmt2 != null) {
                    cstmt2.close();
                }
                if (cstmt1 != null) {
                    cstmt1.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return studentInformationList;
    }

    public int addQualificationdetails(int studentid, List<QualificationDetails> qualificationDetailsList) {
        CallableStatement cstmt1 = null, cstmt2 = null;
        String query1 = null, query2 = null;
        int check = 0;

        try {
            System.out.println("*********************addQualificationdetails**********************");
            query1 = "DELETE FROM qualification_details WHERE studentid = ?";
            cstmt1 = con.prepareCall(query1);
            System.out.println("addQualificationdetails ::  >>>>  " + cstmt1);
            cstmt1.setInt(1, studentid);
            cstmt1.executeUpdate();

            for (QualificationDetails qualificationDetail : qualificationDetailsList) {
                // qualification_details
                query2 = "INSERT INTO qualification_details(studentid,institution_name,course,duration,fromdate,todate,percentage) VALUES(?,?,?,?,?,?,?);";
                cstmt2 = con.prepareCall(query2);
                cstmt2.setInt(1, studentid);
                cstmt2.setString(2, qualificationDetail.getInstitutionname());
                cstmt2.setString(3, qualificationDetail.getCourse());
                cstmt2.setInt(4, qualificationDetail.getDuration());
                cstmt2.setString(5, qualificationDetail.getFromdate());
                cstmt2.setString(6, qualificationDetail.getTodate());
                cstmt2.setInt(7, qualificationDetail.getPercentage());
                System.out.println("addQualificationdetails ::  >>>>  " + cstmt2);

                check = cstmt2.executeUpdate();

                // Close the cstmt2 here
                if (cstmt2 != null) {
                    try {
                        cstmt2.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Set cstmt2 and query2 to null after the loop
            cstmt2 = null;
            query2 = null;

            return check;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources and set variables to null
            try {
                if (cstmt1 != null) {
                    cstmt1.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int addResidentialAddress(int studentId, List<RessidentialAddress> residentialAddressList) {
        String deleteQuery = "DELETE FROM ressidential_address WHERE studentid = ?";
        String insertQuery = "INSERT INTO ressidential_address(studentid, address1, city, pincode, state, country) VALUES (?, ?, ?, ?, ?, ?)";

        int check = 0;

        try (CallableStatement deleteStatement = con.prepareCall(deleteQuery)) {
            deleteStatement.setInt(1, studentId);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        try {
            for (RessidentialAddress address : residentialAddressList) {
                try (CallableStatement insertStatement = con.prepareCall(insertQuery)) {
                    insertStatement.setInt(1, studentId);
                    insertStatement.setString(2, address.getAddress1());
                    insertStatement.setString(3, address.getCity());
                    insertStatement.setString(4, address.getPincode());
                    insertStatement.setString(5, address.getState());
                    insertStatement.setString(6, address.getCountry());
                    check += insertStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception or return an error code as needed.
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or return an error code as needed.
        }

        return check;
    }

    private StudentInformation getStudentInformation(int studentid) throws SQLException {
        CallableStatement cstmt1 = null;
        ResultSet rs1 = null;
        String query1 = null;
        StudentInformation studentInformation = new StudentInformation();

        try {
            System.out.println("In getReport :: " + studentid);
            query1 = "SELECT studentid, rollno, first_name, last_name, gender, contact_number, emailid, dob, active, isdelete FROM student_information WHERE studentid = ? AND isdelete=0";
            cstmt1 = con.prepareCall(query1);
            cstmt1.setInt(1, studentid);
            System.out.println("getStudentInformation ::  >>>>  " + cstmt1);
            rs1 = cstmt1.executeQuery();

            if (rs1 != null && rs1.next()) {
                studentInformation.setStudentid(rs1.getInt("studentid"));
                studentInformation.setRollno(rs1.getInt("rollno"));
                studentInformation.setFirstname(rs1.getString("first_name"));
                studentInformation.setLastname(rs1.getString("last_name"));
                studentInformation.setGender(rs1.getString("gender"));
                studentInformation.setContactno(rs1.getString("contact_number"));
                studentInformation.setEmailid(rs1.getString("emailid"));
                studentInformation.setDob(rs1.getString("dob"));
                studentInformation.setActive(rs1.getInt("active"));
                studentInformation.setIsdelete(rs1.getInt("isdelete"));
                studentInformation.setQualificationDetails(getQualificationDetails(studentid));
                studentInformation.setRessidentialAddress(getRessidentialAddress(studentid));
            }
            System.out.println("getStudentInformation :: >>" + studentInformation.toString());
            return studentInformation;
        } finally {
            // Close resources and set variables to null
            try {
                if (rs1 != null) {
                    rs1.close();
                }
                if (cstmt1 != null) {
                    cstmt1.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cstmt1 = null;
            rs1 = null;
            query1 = null;
        }
    }

    public List<QualificationDetails> getQualificationDetails(int studentid) throws SQLException {
        CallableStatement cstmt = null;
        ResultSet rs2 = null;
        String query = null;
        ArrayList<QualificationDetails> responseArrayList = new ArrayList<QualificationDetails>();

        try {
            System.out.println("***********getQualificationDetails*****************");
            query = "SELECT studentid, institution_name, course, duration, fromdate, todate, percentage FROM qualification_details WHERE studentid = ?";
            System.out.println("SELECT studentid, institution_name, course, duration, fromdate, todate, percentage FROM qualification_details WHERE studentid = " + studentid);
            cstmt = con.prepareCall(query);
            cstmt.setInt(1, studentid);
            rs2 = cstmt.executeQuery();

            if (rs2 != null) {
                while (rs2.next()) {
                    QualificationDetails qualificationDetails = new QualificationDetails();
                    qualificationDetails.setStudentid(rs2.getInt("studentid"));
                    qualificationDetails.setInstitutionname(rs2.getString("institution_name"));
                    qualificationDetails.setCourse(rs2.getString("course"));
                    qualificationDetails.setDuration(rs2.getInt("duration"));
                    qualificationDetails.setFromdate(rs2.getString("fromdate"));
                    qualificationDetails.setTodate(rs2.getString("todate"));
                    qualificationDetails.setPercentage(rs2.getInt("percentage"));
                    System.out.println("getQualificationDetails >>>>>>" + qualificationDetails.toString());
                    responseArrayList.add(qualificationDetails);
                }
                System.out.println("responseArrayList   >>>>" + responseArrayList.toString());
                return responseArrayList;
            }
        } finally {
            // Close resources and set variables to null
            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (cstmt != null) {
                    cstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cstmt = null;
            query = null;
        }
        return null;
    }

    public List<RessidentialAddress> getRessidentialAddress(int studentid) throws SQLException {
        CallableStatement cstmt = null;
        ResultSet rs2 = null;
        String query = null;
        ArrayList<RessidentialAddress> responseArrayList = new ArrayList<RessidentialAddress>();

        try {
            System.out.println("***********getRessidentialAddress*****************");
            query = "SELECT studentid,address1,city,pincode,state,country FROM ressidential_address WHERE studentid=?;";
            System.out.println("SELECT studentid,address1,city,pincode,state,country FROM ressidential_address WHERE studentid= " + studentid);
            cstmt = con.prepareCall(query);
            cstmt.setInt(1, studentid);
            rs2 = cstmt.executeQuery();

            if (rs2 != null) {
                while (rs2.next()) {
                    RessidentialAddress address = new RessidentialAddress();
                    address.setStudentid(rs2.getInt("studentid"));
                    address.setAddress1(rs2.getString("address1"));
                    address.setCity(rs2.getString("city"));
                    address.setCountry(rs2.getString("country"));
                    address.setPincode(rs2.getString("pincode"));
                    address.setState(rs2.getString("state"));
                    System.out.println("getQualificationDetails >>>>>>" + address.toString());
                    responseArrayList.add(address);
                }
                System.out.println("getRessidentialAddress responseArrayList   >>>>" + responseArrayList.toString());
                return responseArrayList;
            }
        } finally {
            // Close resources and set variables to null
            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (cstmt != null) {
                    cstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cstmt = null;
            query = null;
        }
        return null;
    }

// ******************************************************************************************************
    // Validations checkRollnoExists
    public static boolean checkRollnoExists(int rollno) throws SQLException {
        // CallableStatement
        CallableStatement cstmt = null;

        // ResultSet
        ResultSet rs = null;

        // query
        String query = null;

        boolean check = false;

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            query = "SELECT rollno FROM student_information WHERE rollno =? And isdelete=0";

            cstmt = conn.prepareCall(query);

            cstmt.setInt(1, rollno);
            System.out.println("checkRollnoExists :: Query >>>>  :" + cstmt.toString());
            rs = cstmt.executeQuery();
            System.out.println("checkRollnoExists :: Resultset >>>>  :" + rs.toString());

            if (rs != null) {
                if (rs.next()) {
                    return true;
                }
            }

        } catch (Exception e) {

            System.out.println("SOME EXCEPTION IN checkRollnoExists >> " + e);

        } finally {

            try {

                if (rs != null) {

                    rs.close();
                }

                if (cstmt != null) {

                    cstmt.close();
                }

            } catch (Exception e) {

            }
            cstmt = null;
            query = null;
            rs = null;

        }
        return false;

    }

    // Validations checkStudentIdExists
    public static boolean checkStudentIdExists(int studentid) throws SQLException {
        // CallableStatement
        CallableStatement cstmt = null;

        // ResultSet
        ResultSet rs = null;

        // query
        String query = null;

        boolean check = false;

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            query = "SELECT studentid FROM student_information WHERE studentid =? AND isdelete=0";

            cstmt = conn.prepareCall(query);

            cstmt.setInt(1, studentid);
            System.out.println("checkStudentIdExists :: Query >>>>  :" + cstmt.toString());
            rs = cstmt.executeQuery();
            System.out.println("checkStudentIdExists :: Resultset >>>>  :" + rs.toString());

            if (rs != null) {
                if (rs.next()) {
                    return true;
                }
            }

        } catch (Exception e) {

            System.out.println("SOME EXCEPTION IN checkStudentIdExists >> " + e);

        } finally {

            try {

                if (rs != null) {

                    rs.close();
                }

                if (cstmt != null) {

                    cstmt.close();
                }

            } catch (Exception e) {

            }
            cstmt = null;
            query = null;
            rs = null;

        }
        return false;

    }

    // Validations  validateGenderRange
    public static boolean validateGenderRange(String genderValue, String[] validGenderRange) {
        for (int i = 0; i < validGenderRange.length; i++) {
            if (genderValue.equals(validGenderRange[i])) {
                return true;
            }
        }
        return false;
    }

// *********************************Validations*********************************************************************
    // Validations emailValidate
    public static boolean emailValidate(String emailId) {

        Pattern pattern = null;
        Matcher matcher = null;

        try {

            pattern = Pattern.compile("^[A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            matcher = pattern.matcher(emailId);

            if (matcher.matches()) {

                matcher.reset();
                return true;

            }

        } catch (Exception e) {

            System.out.println("SOME EXCEPTION IN emailValidate >> " + e);
            return false;

        } finally {

            emailId = null;
        }

        return false;

    }

    // Validations checkMaxStringLength
    public static boolean checkMaxStringLength(String str, int maxlength) {

        int strLength = str.length();

        if (strLength > maxlength) {

            return false;

        }
        return true;

    }

    // Validations checkMinStringLength
    public static boolean checkMinStringLength(String str, int minlength) {

        int strLength = str.length();

        if (strLength < minlength) {

            return false;

        }
        return true;

    }

    // Validations isValidContactNumber
    public boolean isValidContactNumber(String contactno) {
        // Remove any non-numeric characters
        String cleanedContactno = contactno.replaceAll("[^0-9]", "");

        // Check if the cleaned contact number has exactly 10 digits
        return cleanedContactno.length() == 10;
    }

    // Validations passwordValidate
    public static boolean passwordValidate(String username) {

        Pattern pattern = null;
        Matcher matcher = null;

        try {
            //    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d_@]{8,}$", message = "Username must have at least 1 capital letter, allow numbers, underscore, @ symbol, and be at least 8 characters in length")

            pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d_@]{8,}$");
            matcher = pattern.matcher(username);

            if (matcher.matches()) {

                matcher.reset();
                return true;

            }

        } catch (Exception e) {

            System.out.println("SOME EXCEPTION IN passwordValidate >> " + e);
            return false;

        } finally {

            username = null;
        }

        return false;

    }

    public static boolean isValidDateFormat(String value) {
        Date date = null;
        String format = "yyyy-MM-dd"; // Use the correct date format
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.setLenient(false);
            date = sdf.parse(value);
        } catch (Exception ex) {
            System.out.println("isValidDateFormat Exception: " + ex);
            return false;
        }

        return date != null;
    }

    public static boolean isValidContact(String toContact) {
        // Define a regular expression pattern for a common contact number format
        // This example allows for 10 digits and may be adjusted as needed
        String contactPattern = "^[0-9]{10}$";

        try {
            Pattern pattern = Pattern.compile(contactPattern);
            Matcher matcher = pattern.matcher(toContact);

            return matcher.matches();
        } catch (Exception e) {
            System.out.println("SOME EXCEPTION IN isValidContact >> " + e);
            return false;
        }
    }

    public static boolean isValidPincode(String pincode) {
        // Check if the pincode is exactly 6 digits long and contains only digits.
        return pincode != null && pincode.matches("\\d{6}");
    }

}
