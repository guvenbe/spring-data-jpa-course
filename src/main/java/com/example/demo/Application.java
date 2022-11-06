package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            StudentIdCardRepository studentIdCardRepository,
            BookRepository bookRepository
    ) {
        return args -> {
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@amigoscode.edu", firstName, lastName);
            Student student1 = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));
            StudentIdCard studentIdCard = new StudentIdCard("123456789", student1);
            student1.setStudentIdCard(studentIdCard);
            System.out.println("**************Book*******************");
            student1.addbook(new Book("War and Peace", LocalDateTime.now().minusDays(4)));
            student1.addbook(new Book("Clean Code", LocalDateTime.now().minusDays(4)));
            student1.addbook(new Book("Spring Data JPA", LocalDateTime.now().minusYears(1)));

            //studentIdCardRepository.save(studentIdCard);
            studentRepository.save(student1);
            System.out.println("*************************First Student******************");
            studentRepository.findById(1L).ifPresent(System.out::println);
            studentIdCardRepository.findById(1L)
                    .ifPresent(System.out::println);

            //Books load lazy since one to many by default
            //Here is ho we load books

            studentRepository.findById(1L)
                    .ifPresent(s->{
                        System.out.println("fetch books lazy....");
                        Set<Book> books = student1.getBooks();
                        books.forEach(book->{
                            System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
                                });
                    });


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
                    () -> System.out.println("Student with ID 2 not found")
            );
            studentRepository.findById(3L).ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Student with ID 3 not found")
            );
            System.out.println("Select all students: ");
            List<Student> students = studentRepository.findAll();
            students.forEach(System.out::println);

            System.out.println("Delete maria: ");
            studentRepository.deleteById(2L);
            System.out.print("Number of students: ");

            System.out.println(studentRepository.count());

            studentRepository.save(maria);

            studentRepository
                    .findStudentByEmail("maria.jones@amigoscode.edu")
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("Student with email maria.jones@amigoscode.edu not found"));

            studentRepository.findStudentsByFirstNameEqualsAndAgeEquals("Maria", 21).forEach(System.out::println);
            studentRepository.findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqual("Maria", 21).forEach(System.out::println);
            System.out.println("**********JPQL****************");
            studentRepository.findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualJPQL("Maria", 21).forEach(System.out::println);
            System.out.println("**********NativeQuery****************");
            studentRepository.findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualNative("Maria", 21).forEach(System.out::println);
            System.out.println("**********NativeQuery Named parameter****************");
            studentRepository.findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualNamedParameters("Maria", 21).forEach(System.out::println);

            System.out.println("***************Deleting Maria 2");
            System.out.println(studentRepository.deleteStudentById(3L));
            generateRandomStudents(studentRepository);
            Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
            studentRepository.findAll(sort).forEach(student -> System.out.println(student.getFirstName()));

            //or
            System.out.println("****************************Sort chained**********************");
            sort = sort.by("firstName").ascending().and(Sort.by("age").descending());
            studentRepository.findAll(sort).forEach(student -> System.out.println(student.getFirstName() + " " + student.getLastName() + " " + student.getAge()));

            PageRequest pageRequest = PageRequest.of(0, 5);
            studentRepository.findAll(pageRequest.withSort(sort)).forEach(student -> System.out.println(student));

            pageRequest = PageRequest.of(0, 5, Sort.by("firstName").ascending());
            Page<Student> page = studentRepository.findAll(pageRequest);
            System.out.println(page);
        };

    }

    private static void generateRandomStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();

        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@amigoscode.edu", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55)
            );

            studentRepository.save(student);
        }
    }
}
