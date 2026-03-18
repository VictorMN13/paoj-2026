package com.pao.laboratory03.exercise.model;

import java.util.*;
import com.pao.laboratory03.exercise.exception.*;

public class Student {

    private String name;
    private int age;
    private Map<Subject, Double> grades;

    public Student(String name, int age) {
        if (age < 18 || age > 60) {
            throw new InvalidStudentException("Varsta " + age + " este invalida (18-60)");
        }

        this.name = name;
        this.age = age;
        this.grades = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Map<Subject, Double> getGrades() {
        return grades;
    }

    public void addGrade(Subject subject, double grade) {
        // Validăm nota
        if (grade < 1 || grade > 10) {
            throw new InvalidGradeException("Nota " + grade + " este invalida (1-10)");
        }
        this.grades.put(subject, grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Double grade : grades.values()) {
            sum += grade;
        }
        return sum / grades.size();
    }

    @Override
    public String toString() {
        return String.format("Student{name='%s', age=%d, avg=%.2f}", name, age, getAverage());
    }
}
