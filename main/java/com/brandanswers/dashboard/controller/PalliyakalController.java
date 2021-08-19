package com.brandanswers.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.brandanswers.dashboard.orchestrator.OrchestratorEngine;
import com.brandanswers.dashboard.service.FilesStorageService;

@RequestMapping("/api/palliyakal")
@RestController
@Component
@CrossOrigin
public class PalliyakalController extends BaseController {
	
	@Autowired
	private OrchestratorEngine engine;

	@PostMapping(value = "/uploadFile", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> uploadFile(@RequestPart(value = "file", required = false) MultipartFile file,@RequestParam Map<String, String> customQuery) throws ParseException, IOException {
		org.json.JSONObject message = new org.json.JSONObject();
		String filePath = "";
		System.out.println(file.getContentType());
		if (!file.isEmpty() && file.getContentType().equals("text/csv")) {
			filePath = FilesStorageService.save(file);
			message.put(file.getOriginalFilename(), "Uploaded the file successfully");
		}
		org.json.JSONObject params = getParams(customQuery);
		params.put("file", filePath);
		org.json.JSONObject responseData = this.engine.processRequest(params, "uploadFile");
		message.put("Response", responseData);
		JSONObject message1 = convertToSimpleJsonObject(message);
		return ResponseEntity.ok(message1);
	}
	
	
	@GetMapping("/getAllInfo")
	public ResponseEntity<?> getAllInfo(@RequestParam Map<String, String> customQuery) throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "getAllInfo"));
		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/id")
	public ResponseEntity<?> id(@RequestParam Map<String, String> customQuery) throws ParseException {
		JSONObject responseData = convertToSimpleJsonObject(
				this.engine.processRequest(getParams(customQuery), "getIndividualInfo"));
		return ResponseEntity.ok(responseData);
	}
	@PostMapping(value = "/insertQR", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> insertQRCode(@RequestPart(value = "file", required = false) MultipartFile file,@RequestParam Map<String, String> customQuery) throws ParseException, IOException {
		org.json.JSONObject message = new org.json.JSONObject();
		String filePath = "";
		String encodedString="";
		System.out.println(file.getContentType());
		if (!file.isEmpty() && file.getContentType().equals("image/png")) {
			filePath = FilesStorageService.save(file);
			byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
		     encodedString = Base64.getEncoder().encodeToString(fileContent);
			message.put(file.getOriginalFilename(), "Uploaded the file successfully");
		}
		org.json.JSONObject params = getParams(customQuery);
		params.put("qr", encodedString);
		org.json.JSONObject responseData = this.engine.processRequest(params, "insertQR");
		message.put("Response", responseData);
		JSONObject message1 = convertToSimpleJsonObject(message);
		return ResponseEntity.ok(message1);
	}
}
