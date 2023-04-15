package com.imporve.skill.orderservice.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	public static void saveFile(String fileName, String path, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(path);
		
		if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
		
		try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {       
            throw new IOException("Could not save file: " + fileName, ioe);
        }
	}
	
	public static File loadFile(String path, String fileName) throws IOException {
		Path dirPath = Paths.get(path);
		
		String fullName = dirPath + "/" + fileName;
		
		return new File(fullName);
	}
	
	public static Resource getResourceFromFileName(String path, String fileName) {
		String fullName = path + "/" + fileName;
		
        return new FileSystemResource(new File(fullName));
    }
}
