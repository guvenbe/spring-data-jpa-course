package com.example.demo;

import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByEmail(String email);

    List<Student> findStudentsByFirstNameEqualsAndAgeEquals(String firstName, Integer age);

    List<Student> findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqual(String firstName, Integer age);

    List<Student> findByAgeIsNull();

    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentsByEmail2(String email);

    //JPQL
    @Query("SELECT s FROM Student s WHERE s.firstName= ?1 AND s.age >= ?2")
    List<Student> findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualJPQL(String firstName, Integer age);

    //    select * from Student where first_name='Maria' and age=18;
    @Query(value = "select * from Student where first_name= ?1 and age = ?2", nativeQuery = true)
    List<Student> findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualNative(String firstName, Integer age);


    @Query(value = "select * from Student where first_name= :firstName and age = :age", nativeQuery = true)
    List<Student> findStudentsByFirstNameEqualsAndAgeIsGreaterThanEqualNamedParameters(@Param("firstName") String firstName, @Param("age") Integer age);

    @Transactional
    @Modifying
    @Query("DELETE FROM Student  u Where u.id = ?1")
    int deleteStudentById(Long id);
}
