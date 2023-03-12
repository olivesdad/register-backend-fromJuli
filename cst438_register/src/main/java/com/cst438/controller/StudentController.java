package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.StudentRepository;

@RestController
public class StudentController  {
	
	@Autowired
	StudentRepository studentRepository;
	
	/*
	 * add student to database
	 */
	@PostMapping("/student")
	@Transactional
	public String addNewStudent () {
		return "student_id = 12398";
	}
	
	
	/*
	 * place student registration on hold
	 */
	
	/*
	 * release hold on student registration
	 */
}





