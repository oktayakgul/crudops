package com.oa.crudops.controller;

import com.oa.crudops.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    StudentService studentService;

    @GetMapping ("/homeStudent")
    public String getStudents(Model model){

        var students = studentService.getStudents();

        model.addAttribute("studentList", students.getStudentList());
        model.addAttribute("totalStudent", students.getTotalStudent());

        return "studentList";
    }

    @GetMapping("/addNewbies{count}")
    public String insertStudents(@PathVariable("count") int count, Model model){

        var students = studentService.insertStudents(count);

        model.addAttribute("studentList", students.getStudentList());
        model.addAttribute("totalStudent", students.getTotalStudent());
        model.addAttribute("resultMap", students.getResultMap());

        return "studentList";
    }

}
