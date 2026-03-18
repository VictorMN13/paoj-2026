package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.model.*;
import com.pao.laboratory03.exercise.exception.*;

import java.util.*;

public class StudentService {
    private static StudentService instance;
    private List<Student> students;

    private StudentService() {
        this.students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                throw new RuntimeException("Studentul '" + name + "' exista deja");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost gasit");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        for (Student s : students) {
            System.out.println(s);
        }
    }

    public void printTopStudents() {
        List<Student> sortStudents = new ArrayList<>(students);

        sortStudents.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));

        for (Student s : sortStudents) {
            System.out.println(s);
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sumSub = new HashMap<>();
        Map<Subject, Integer> cntSub = new HashMap<>();

        for (Student student : students) {
            for (Map.Entry<Subject, Double> entry : student.getGrades().entrySet()) {
                Subject subject = entry.getKey();
                Double grade = entry.getValue();
                sumSub.put(subject, sumSub.getOrDefault(subject, 0.0) + grade);
                cntSub.put(subject, cntSub.getOrDefault(subject, 0) + 1);
            }
        }

        Map<Subject, Double> avgSub = new HashMap<>();
        for (Subject subject : sumSub.keySet()) {
            double average = sumSub.get(subject) / cntSub.get(subject);
            avgSub.put(subject, average);
        }

        return avgSub;
    }
}