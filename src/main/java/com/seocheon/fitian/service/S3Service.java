package com.seocheon.fitian.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.seocheon.fitian.model.ResponseModel;

import lombok.RequiredArgsConstructor;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Service
public class S3Service {
	
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        File file = multiPartFileToFile(multipartFile);
        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
        file.delete();
        return fileName;
    }

    private File multiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(file.getBytes());
        }
        return convertedFile;
    }
    
    public List<String> uploadFiles(MultipartFile[] files) throws IOException {
    	List<String> fileNames = new ArrayList<String>();
    	
    	for (MultipartFile file : files) {
    		try {
    			File f = multiPartFileToFile(file);
    			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    			fileNames.add(fileName);
    			amazonS3.putObject(new PutObjectRequest(bucketName, fileName, f));
    			f.delete();
    		} catch (IOException e) {
    			System.out.println("File upload failed: " + e.getMessage());
    		}
    	}
        return fileNames;
    }
    
    public ResponseModel deleteFile(String fileName) throws IOException {
    	ResponseModel res = new ResponseModel();
    	
        try {
        	amazonS3.deleteObject(bucketName, fileName);
        	res.setMessage("delete success");
        } catch (SdkClientException e) {
        	res.setMessage("delete failed");
        }
        return res;
    }
}
