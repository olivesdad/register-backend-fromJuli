package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public String addNewStudent ( @RequestParam("name") String name, @RequestParam("email") String email ) {
		
		String message;
		
		// Check if email already exists in database (duplicates not allowed)
		Student existingStudent = studentRepository.findByEmail(email);
		
		if(existingStudent != null) {
			message = String.format("Failed to add %s.  The email address (%s) is already in use.", name, email);
		} else {
			Student student = new Student();
			
			student.setName(name);
			student.setEmail(email);
			student.setStatus(null);
			student.setStatusCode(0);
			studentRepository.save(student);
			
			// Student savedStudent = studentRepository.save(student);
			// StudentDTO result = createStudentDTO(savedStudent);
			
			message = String.format("Student (%s) successfully added.\n", name);
		}
		
		return message;
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

