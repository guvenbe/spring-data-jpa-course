package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            Student maria = new Student(
                    "Maria",
                    "Jones",
                    "maria.jones@amigoscode.edu",
                    21
            );
            Student ahmed = new Student(
                    "Ahmed",
                    "Ali",
                    "ahmed.ali@amigoscode.edu",
                    18
            );

            Student maria2 = new Student(
                    "Maria",
                    "Jones",
                    "maria2.jones@amigoscode.edu",
                    25
            );
            System.out.println("Adding maria and ahmed");
            studentRepository.saveAll(List.of(maria, ahmed, maria2));
            System.out.print("Number of students: ");
            System.out.println(studentRepository.count());
            studentRepository.findById(2L).ifPresentOrElse(
                    System.out::println,
                    ()->System.out.println("Student with ID 2 not found")
            );
            studentRepository.findById(3L).ifPresentOrElse(
                    System.out::println,
                    ()->System.out.println("Student with ID 3 not found")
            );
            System.out.println("Select all students: ");
            List<Student> students = studentRepository.findAll();
            students.forEach(System.out::println);

            System.out.println("Delete maria: ");
            studentRepository.deleteById(1L);
            System.out.print("Number of students: ");

            System.out.println(studentRepository.count());

            studentRepository.save(maria);

            studentRepository
                    .findStudentByEmail("maria.jones@amigoscode.edu")
                    .ifPresentOrElse(
                            System.out::println,
                            ()-> System.out.println("Student with email maria.jones@amigoscode.edu not found"));

            studentRepository.findStudentsByFirstNameEqualsAndAgeEquals("Maria", 21).forEach(System.out::println);
            studentRepository.findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqual("Maria", 21).forEach(System.out::println);
            System.out.println("**************************");
            studentRepository.findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqual2("Maria", 21).forEach(System.out::println);


        };

    }
}
