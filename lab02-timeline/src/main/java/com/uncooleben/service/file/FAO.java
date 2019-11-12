package com.uncooleben.service.file;

import org.springframework.web.multipart.MultipartFile;

import com.uncooleben.model.Message;

public interface FAO {

	public boolean storeImage(Message message, MultipartFile image);

	public byte[] convertToByteArray(String filename);

}
