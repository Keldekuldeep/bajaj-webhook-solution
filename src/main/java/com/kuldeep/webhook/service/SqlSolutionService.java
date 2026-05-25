package com.kuldeep.webhook.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSolutionService {

    // regNo: 0827CI231071 -> last two digits = 71 -> odd -> Question 1
    // Problem: Find the highest salary credited NOT on the 1st day of any month.
    // Return: SALARY, NAME (FIRST_NAME + ' ' + LAST_NAME), AGE, DEPARTMENT_NAME
    public String getSqlQuery() {
        return "SELECT p.AMOUNT AS SALARY, " +
               "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
               "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
               "d.DEPARTMENT_NAME " +
               "FROM PAYMENTS p " +
               "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
               "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
               "WHERE DAY(p.PAYMENT_TIME) != 1 " +
               "ORDER BY p.AMOUNT DESC " +
               "LIMIT 1";
    }
}
