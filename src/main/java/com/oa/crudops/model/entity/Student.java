package com.oa.crudops.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "STUDENT")
public class Student {

    @Id
    @Column(name = "ID")
    public Integer id;

    @Column(name = "NAME")
    public String name;


    @Column(name = "SURNAME")
    public String surname;

    public Student(Integer id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Student() {
    }
}
