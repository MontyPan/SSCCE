package us.dontcareabout.sscce.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("image")
public class ImageAPI {
	@PostMapping("/jpg")
	public String jpg(@RequestBody String path) {
		return toDataURI(path, "jpg");
	}

	@PostMapping("/png")
	public String png(@RequestBody String path) {
		return toDataURI(path, "png");
	}

	public static String toDataURI(String path, String type) {
		try {
			BufferedImage image = ImageIO.read(new URL(path));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, type, baos);
			return "data:image/" + type + ";base64,"
				+ Base64Utils.encodeToString(baos.toByteArray());
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	//執行時需先啟動 Spring Boot Web（mvn spring-boot:run）
	//或是用其他方式提供圖檔
	public static void main(String[] args) {
		String host = "http://localhost:8080/";

		Arrays.asList(
			"trash.png"
			, "shock.jpg"
		).forEach(file -> {
			System.out.println("[" + file + "]");
			//只印出 Base64 字串長度，依照 Spring Boot 的行為
			//「trash.png」的「by JPG」應該為 22
			System.out.println("by JPG : " + toDataURI(host + file, "jpg").length());
			System.out.println("by PNG : " + toDataURI(host + file, "png").length());
			System.out.println();
		});
	}
}
