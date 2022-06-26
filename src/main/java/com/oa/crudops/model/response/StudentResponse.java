package com.oa.crudops.model.response;

import com.oa.crudops.model.entity.Student;

import java.util.List;
import java.util.Map;

public class StudentResponse {

    private List<Student> studentList;
    private Integer totalStudent;
    private Map<String, String> resultMap;

    public Map<String, String> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public Integer getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(Integer totalStudent) {
        this.totalStudent = totalStudent;
    }

}
