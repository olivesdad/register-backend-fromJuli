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
	public StudentDTO addNewStudent ( @RequestParam("name") String name, @RequestParam("email") String email ) {
		
		// Check if email already exists in the system...
		
		Student student = new Student();
		
		student.setName(name);
		student.setEmail(email);
		student.setStatus(null);
		student.setStatusCode(0);
		Student savedStudent = studentRepository.save(student);
		
		StudentDTO result = createStudentDTO(savedStudent);
		return result;
	}
	
	
	
	
//	public StudentDTO addNewStudent ( @RequestBody StudentDTO studentDTO ) {
//		
//		Student student = new Student();
//		
//		student.setName(studentDTO.studentName);
//		student.setEmail(studentDTO.studentEmail);
//		student.setStatus(studentDTO.studentStatus);
//		student.setStatusCode(studentDTO.studentStatusCode);
//		Student savedStudent = studentRepository.save(student);
//		
//		StudentDTO result = createStudentDTO(savedStudent);
//		return result;
//	}
	
	
	
	
	
	
	
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
				message = "Student is already on hold.\n";
			else {
				student.setStatusCode(1);
				student.setStatus("HOLD");
				message = "Student is now on hold.\n";
			}
		else
			message = "Student does not exist.";

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
				message = "Student was not on hold.\n";
			else {
				student.setStatusCode(0);
				student.setStatus(null);
				message = "Student is no longer on hold.\n";
			}
		else
			message = "Student does not exist.";

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

