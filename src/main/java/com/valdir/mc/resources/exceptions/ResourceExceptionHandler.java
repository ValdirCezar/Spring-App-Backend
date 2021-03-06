package com.valdir.mc.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.valdir.mc.services.exceptions.AuthorizationException;
import com.valdir.mc.services.exceptions.DataIntegrityException;
import com.valdir.mc.services.exceptions.FileException;
import com.valdir.mc.services.exceptions.ObjectNotFoubdException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoubdException.class)
	public ResponseEntity<StandarError> objectNotFound(ObjectNotFoubdException e, HttpServletRequest request) {

		StandarError error = new StandarError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
				"Não encontrado", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandarError> dataIntegrityException(DataIntegrityException e, HttpServletRequest request) {

		StandarError error = new StandarError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Integridade de dados", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandarError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

		ValidationError error = new ValidationError(System.currentTimeMillis(), HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", e.getMessage(), request.getRequestURI());

		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			error.addError(x.getField(), x.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandarError> authorization(AuthorizationException e, HttpServletRequest request) {

		StandarError error = new StandarError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(), "Acesso negado", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandarError> file(FileException e, HttpServletRequest request) {

		StandarError error = new StandarError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro de arquivo", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandarError> amazonService(AmazonServiceException e, HttpServletRequest request) {
		HttpStatus code = HttpStatus.valueOf(e.getErrorCode());

		StandarError error = new StandarError(System.currentTimeMillis(), code.value(), "Erro Amazom Service", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(code).body(error);
	}

	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandarError> amazonClient(AmazonClientException e, HttpServletRequest request) {

		StandarError error = new StandarError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro Amazom Client", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandarError> amazons3(AmazonS3Exception e, HttpServletRequest request) {

		StandarError error = new StandarError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro Amazom S3", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}

}
