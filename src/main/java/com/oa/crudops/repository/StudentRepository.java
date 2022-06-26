package com.oa.crudops.repository;

import com.oa.crudops.model.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Long> {

    @Query(value = "select * from student s", nativeQuery = true)
    List<Student> findAll();

    @Query(value = "select count(0) from student s", nativeQuery = true)
    Integer getCountAll();

}
