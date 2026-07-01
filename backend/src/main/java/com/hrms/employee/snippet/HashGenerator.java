package com.hrms.employee.snippet;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("kumar123"));

        //luke123
        //jain123
        //satish123
        //kumar123
    }
}
