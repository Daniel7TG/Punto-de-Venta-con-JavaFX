package com.point;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Util {

	public static Path getPath(String url){		
		try {
			return Path.of(new URI(url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.out.println("error en GetPath");
		}
		return null;
	}
	public static String pathToImage(String url){		
		return new File(url).toURI().toString();
	}
}
