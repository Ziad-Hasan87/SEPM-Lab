package com.example.webapp.integration;

import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentDeleteIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testDeleteStudentFlow() {
        Student student = new Student();
        student.setName("Delete Me");
        student.setRoll("DEL-01");

        Student saved = studentRepository.save(student);

        studentService.deleteStudent(saved.getId());

        assertFalse(studentRepository.findById(saved.getId()).isPresent());
    }
}