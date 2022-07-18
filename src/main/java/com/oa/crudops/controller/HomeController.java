package com.oa.crudops.controller;

import com.oa.crudops.model.response.StudentResponse;
import com.oa.crudops.service.StudentService;
import org.openjdk.jmh.runner.RunnerException;
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

    @GetMapping("/addNewbies{itemCount}-{loopCount}")
    public String insertStudents(@PathVariable("itemCount") int itemCount, @PathVariable("loopCount") int loopCount, Model model){

        itemCount = itemCount <=0 ? 100 : itemCount;
        loopCount = loopCount <=0 ? 1 : loopCount;


        var students = studentService.insertStudents(itemCount, loopCount);

        model.addAttribute("studentList", students.getStudentList());
        model.addAttribute("totalStudent", students.getTotalStudent());
        model.addAttribute("resultMap", students.getResultMap());

        return "studentList";
    }
//
    @GetMapping("/benchmark")
    public String benchmark(Model model) throws RunnerException {
         studentService.benchmark(100,1);

        return "studentList";
    }

}
