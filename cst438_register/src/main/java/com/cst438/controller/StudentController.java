package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

import com.cst438.domain.StudentDTO;

@RestController
public class StudentController  {
	
	@Autowired
	StudentRepository studentRepository;
	
	/*
	 * add student to database
	 */
	@PostMapping("/student")
	@Transactional
	public StudentDTO addNewStudent ( @RequestParam("name") String name, @RequestParam("email") String email ) {

		// What happens when the request parameters are missing?  Should an exception be thrown?
		
		Student student = new Student();
		StudentDTO result;
		
		// Check if email already exists in database (duplicates not allowed)
		Student existingStudent = studentRepository.findByEmail(email);
		
		if(existingStudent == null) {
			student.setName(name);
			student.setEmail(email);
			student.setStatus(null);
			student.setStatusCode(0);
			
			Student savedStudent = studentRepository.save(student);
			result = createStudentDTO(savedStudent);
		} else {
			// Email is a duplicate.
			
			// If email is duplicate, return a null object with id = -1
			// student.setStudent_id(-1);
			// result = createStudentDTO(student);
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add " + name + ". The email address (" + email + ") is already in use.");
		}
		
		return result;
	}
	
	/*
	 * place student registration on hold
	 */
	@PutMapping("/student/add_hold/{student_id}")
	@Transactional
	public String addHold ( @PathVariable int student_id ) {
		
		Student student = studentRepository.findById(student_id).orElse(null);
		String message;
		
		if (student != null)
			if (student.getStatusCode() == 1)
				message = String.format("Student (%s) is already on HOLD.\n", student.getName());
			else {
				student.setStatusCode(1);
				student.setStatus("HOLD");
				message = String.format("Student (%s) is now on HOLD.\n", student.getName());
			}
		else
			message = String.format("Student (%s) was not found.", student_id);

		return message;
	}
	
	/*
	 * release hold on student registration
	 */
	@PutMapping("/student/remove_hold/{student_id}")
	@Transactional
	public String removeHold ( @PathVariable int student_id ) {
		
		Student student = studentRepository.findById(student_id).orElse(null);
		String message;
		
		if (student != null)
			if (student.getStatusCode() == 0)
				message = String.format("Student (%s) was not on HOLD.\n", student.getName());
			else {
				student.setStatusCode(0);
				student.setStatus(null);
				message = String.format("Student (%s) is no longer on HOLD.\n", student.getName());
			}
		else
			message = String.format("Student (%s) was not found.", student_id);

		return message;
	}

	/*
	 * Helper method to turn student entity into an instance of StudentDTO to return
	 * to the front end.
	 */
	private StudentDTO createStudentDTO(Student s) {
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.id = s.getStudent_id();
		studentDTO.studentName = s.getName();
		studentDTO.studentEmail = s.getEmail();
		studentDTO.studentStatus = s.getStatus();
		studentDTO.studentStatusCode = s.getStatusCode();
		
		return studentDTO;
	}
}



