package com.oa.crudops.service;

import com.oa.crudops.Util;
import com.oa.crudops.jmh.JMHBenchmarking;
import com.oa.crudops.model.entity.Student;
import com.oa.crudops.model.response.StudentResponse;
import com.oa.crudops.repository.StudentRepository;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@State(Scope.Benchmark)
public class StudentService {

    @Autowired
    StudentRepository repository;

    int invokeNo, itemCount, loopCount;

    public StudentResponse getStudents() {
        StudentResponse response = new StudentResponse();

        List<Student> list = repository.findAll();

        response.setStudentList(list);
        response.setTotalStudent(repository.getCountAll());

        return response;
    }

    public StudentResponse insertStudents(int itemCount, int loopCount) {
        StudentResponse response = new StudentResponse();
        Map<String, String> resultMap = new HashMap<>();
        Map<String, String> sortedMap;
        invokeNo = 0;
        this.itemCount = itemCount;
        this.loopCount = loopCount;

        repository.deleteAll();


        resultMap.put("1-insert_splittedList_for_saveAll", insert_splittedList_for_saveAll());
        resultMap.put("2-insert_list_saveAll", insert_list_saveAll());
        resultMap.put("3-insert_list_stream_save", insert_list_stream_save());
        resultMap.put("4-insert_list_stream_parallel_save", insert_list_stream_parallel_save());

        //sortedMap = resultMap.entrySet().stream().sorted().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k,v) -> k, LinkedHashMap::new));

        sortedMap = new TreeMap<String , String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                }
        );

        sortedMap.putAll(resultMap);

        response.setResultMap(sortedMap);
        resultMap.entrySet().stream().forEach(item -> System.out.println(item.getValue()));
        return response;
    }


    private String insert_list_stream_parallel_save() {
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
        return (loopCount * itemCount) + " items added in avarage " + totalMillisec/loopCount + " millisec.";

    }

    private String insert_list_stream_save() {
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
        return (loopCount * itemCount) + " items added in avarage " + totalMillisec/loopCount + " millisec.";

    }

    private String insert_list_saveAll() {
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
        return (loopCount * itemCount) + " items added in avarage " + totalMillisec/loopCount + " millisec.";


    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public String insert_splittedList_for_saveAll() {
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
        System.out.println((loopCount * itemCount) + " items added in avarage " + totalMillisec/loopCount + " millisec.");
        return (loopCount * itemCount) + " items added in avarage " + totalMillisec/loopCount + " millisec.";
    }

    private List<Student> generateTestData() {
        List<Student> newbies = new ArrayList<>();
        itemCount = itemCount == 0 ? 1_000 : itemCount;

        int iFrom = invokeNo * itemCount;
        int iTo = ++invokeNo * itemCount;
        Student student;
        for (int i = iFrom; i < iTo; i++) {
            student = new Student(i, "Name of " + i, "Surname of " + i);
            newbies.add(student);
        }
        return newbies;
    }


    public StudentResponse benchmark(int itemCount, int loopCount) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(JMHBenchmarking.class.getSimpleName())
                .forks(2)
                .build();
        new Runner(opt).run();

        return null;
    }
}
