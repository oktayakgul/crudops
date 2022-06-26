package com.oa.crudops.service;

import com.oa.crudops.Util;
import com.oa.crudops.model.entity.Student;
import com.oa.crudops.model.response.StudentResponse;
import com.oa.crudops.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    @Autowired
    StudentRepository repository;

    int invokeNo, count, loopCount;

    public StudentResponse getStudents() {
        StudentResponse response = new StudentResponse();

        List<Student> list = repository.findAll();

        response.setStudentList(list);
        response.setTotalStudent(repository.getCountAll());

        return response;
    }

    public StudentResponse insertStudents(int count) {
        StudentResponse response = new StudentResponse();
        Map<String, String> resultMap = new HashMap<>();
        invokeNo = 0;
        this.count = count;
        this.loopCount = 1;

        repository.deleteAll();


        resultMap.put("1-insert_AsSplittedList_for_saveAll", insert_AsSplittedList_for_saveAll());
        resultMap.put("2-insert_asWholeList_for_saveAll", insert_asWholeList_for_saveAll());
        resultMap.put("3-insert_asWholeList_for_save_stream", insert_asWholeList_for_save_stream());
        resultMap.put("4-insert_asWholeList_for_save_parallel_stream", insert_asWholeList_for_save_parallel_stream());

        response.setResultMap(resultMap);
        resultMap.entrySet().stream().forEach(item -> System.out.println(item.getValue()));
        return response;
    }

    private String insert_asWholeList_for_save_parallel_stream() {
        long totalMillisec = 0;

        for (int i = 0; i < loopCount; i++) {

            List<Student> newbies = generateTestData();

            try {
                long start = System.currentTimeMillis();
                newbies.stream().parallel().forEach(student -> repository.save(student));
                long end = System.currentTimeMillis();
                System.out.println("added in " + (end - start) + " millisec.");
                totalMillisec += (end - start);

            } catch (Exception e) {
                System.out.println(e.fillInStackTrace());
            }
        }
        return (loopCount * count) + " items added in " + totalMillisec + " millisec.";

    }

    private String insert_asWholeList_for_save_stream() {
        long totalMillisec = 0;

        for (int i = 0; i < loopCount; i++) {

            List<Student> newbies = generateTestData();

            try {
                long start = System.currentTimeMillis();
                newbies.stream().forEach(student -> repository.save(student));
                long end = System.currentTimeMillis();
                System.out.println("added in " + (end - start) + " millisec.");
                totalMillisec += (end - start);

            } catch (Exception e) {
                System.out.println(e.fillInStackTrace());
            }
        }
        return (loopCount * count) + " items added in " + totalMillisec + " millisec.";

    }

    private String insert_asWholeList_for_saveAll() {
        long totalMillisec = 0;

        for (int i = 0; i < loopCount; i++) {

            List<Student> newbies = generateTestData();
            try {
                long start = System.currentTimeMillis();
                repository.saveAll(newbies);
                long end = System.currentTimeMillis();
                System.out.println("added in " + (end - start) + " millisec.");
                totalMillisec += (end - start);

            } catch (Exception e) {
                System.out.println(e.fillInStackTrace());
            }
        }
        return (loopCount * count) + " items added in " + totalMillisec + " millisec.";


    }

    private String insert_AsSplittedList_for_saveAll() {
        long totalMillisec = 0;

        for (int i = 0; i < loopCount; i++) {
            List<Student> newbies = generateTestData();

            List<List<Student>> splittedList = Util.splitListToSubLists(newbies, 50);
            try {
                for (List<Student> subList : splittedList) {
                    long start = System.currentTimeMillis();
                    repository.saveAll(subList);
                    long end = System.currentTimeMillis();
                    System.out.println("added in " + (end - start) + " millisec.");
                    totalMillisec += (end - start);
                }
            } catch (Exception e) {
                System.out.println(e.fillInStackTrace());
            }
        }
        return (loopCount * count) + " items added in " + totalMillisec + " millisec.";
    }

    private List<Student> generateTestData() {
        List<Student> newbies = new ArrayList<>();
        count = count == 0 ? 1_000 : count;

        int iFrom = invokeNo * count;
        int iTo = ++invokeNo * count;
        Student student;
        for (int i = iFrom; i < iTo; i++) {
            student = new Student(i, "Name of " + i, "Surname of " + i);
            newbies.add(student);
        }
        return newbies;
    }


}
