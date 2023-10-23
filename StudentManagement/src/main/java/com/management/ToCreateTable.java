package com.management;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ToCreateTable {

//{
//    "rollno":2,
//    "firstname":"Abhishek",
//    "lastname":"Dubey",
//    "gender":"MALE",
//    "contactno":"8850801468;9820744591",
//    "emailid":"abdubey@gmail.com;abhi123@hmail.com",
//    "dob":"2002-11-22",
//    "active":1,
//    "ressidentialAddress":[
//        {
//            "address1":"Pushpanjali CHS Mahada Colony Vashinaka Chembur ",
//            "city":"Mumbai",
//            "pincode":"400074",
//            "country":"INDIA",
//            "state":"Maharastra"
//        }
//    ],
//    "qualificationDetails":[
//        {
//            "institutionname":"Anudip Foundation",
//            "course":"Java FullStack Development using Angular  ",
//            "duration":6,
//            "fromdate":"2023-2-11",
//            "todate":"2023-7-23",
//            "percentage":98
//        },{
//            "institutionname":"Anudip Foundation",
//            "course":"Python FullStack Development using Angular  ",
//            "duration":6,
//            "fromdate":"2023-2-11",
//            "todate":"2023-7-23",
//            "percentage":98
//        }
//    ]
//}    
    
    
    
    
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/acs_report_tenant_1";
        final String user = "root";
        final String pass = "ABHI";

        Connection conn = null;
        CallableStatement cstmt = null;
        String query = null;

        try {
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("************Connection Done Successfully******************");

            // Drop the existing 'student_information' table
            query = "SHOW TABLES LIKE 'student_information'";
            cstmt = conn.prepareCall(query);
            if (cstmt.executeQuery().next()) {
                System.out.println("***********'student_information' Table Exists---> Dropping the Existing Table*****************");
                String dropQuery = "DROP TABLE student_information";
                CallableStatement dropStatement = conn.prepareCall(dropQuery);
                dropStatement.executeUpdate();
                System.out.println("***********'student_information' Table Dropped Successfully*****************");
                dropStatement.close();
            }

            // Drop the existing 'ressidential_address' table
            query = "SHOW TABLES LIKE 'ressidential_address'";
            cstmt = conn.prepareCall(query);
            if (cstmt.executeQuery().next()) {
                System.out.println("***********'ressidential_address' Table Exists---> Dropping the Existing Table*****************");
                String dropQuery = "DROP TABLE ressidential_address";
                CallableStatement dropStatement = conn.prepareCall(dropQuery);
                dropStatement.executeUpdate();
                System.out.println("***********'ressidential_address' Table Dropped Successfully*****************");
                dropStatement.close();
            }

            // Drop the existing 'qualification_details' table
            query = "SHOW TABLES LIKE 'qualification_details'";
            cstmt = conn.prepareCall(query);
            if (cstmt.executeQuery().next()) {
                System.out.println("***********'qualification_details' Table Exists---> Dropping the Existing Table*****************");
                String dropQuery = "DROP TABLE qualification_details";
                CallableStatement dropStatement = conn.prepareCall(dropQuery);
                dropStatement.executeUpdate();
                System.out.println("***********'qualification_details' Table Dropped Successfully*****************");
                dropStatement.close();
            }

            System.out.println("***********Creating New Table 'student_information'*****************");
            query = "CREATE TABLE student_information (studentid INT PRIMARY KEY AUTO_INCREMENT, rollno INT, first_name VARCHAR(255), last_name VARCHAR(255), gender VARCHAR(100), contact_number VARCHAR(200), emailid VARCHAR(255), dob VARCHAR(50), active BIT, isdelete BIT);";
            cstmt = conn.prepareCall(query);
            cstmt.executeUpdate();
            System.out.println("*********'student_information' Table Creation Done Successfully**************");

            System.out.println("***********Creating New Table 'qualification_details'*****************");
            query = "CREATE TABLE qualification_details (studentid INT, institution_name VARCHAR(255), course VARCHAR(255), duration INT, fromdate VARCHAR(50), todate VARCHAR(50), percentage INT);";
            cstmt = conn.prepareCall(query);
            cstmt.executeUpdate();
            System.out.println("*********'qualification_details' Table Creation Done Successfully**************");

            System.out.println("***********Creating New Table 'ressidential_address'*****************");
            query = "CREATE TABLE ressidential_address (studentid INT, address1 VARCHAR(200), city VARCHAR(100), pincode VARCHAR(10), state VARCHAR(100), country VARCHAR(100));";
            cstmt = conn.prepareCall(query);
            cstmt.executeUpdate();
            System.out.println("*********'ressidential_address' Table Creation Done Successfully**************");
        } catch (SQLException e) {
//            e.printStackTrace();
        } finally {
            try {
                if (cstmt != null) {
                    cstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
//                e.printStackTrace();
            }
        }
    }
}
