package com.uncooleben.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/img")
public class ImageController {

	@ResponseBody
	@RequestMapping(value = "/*", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] testphoto(HttpServletRequest request) throws IOException {
		System.out.println(request.getRequestURL());
		String[] url = request.getRequestURL().toString().split("/");
		String filename = url[url.length - 1];
		FileInputStream fis = new FileInputStream(new File(System.getenv("TEMP") + "/timeline_imgs/", filename));
		return IOUtils.toByteArray(fis);
	}

}
