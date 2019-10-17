package br.com.jpb.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class IOUtil {

	public static File readFileFrom(URL resource) {
		try {
			Path path = Paths.get(resource.toURI());
			return path.toFile();
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Error while reading file from " + resource.getPath(), e);
		}
	}

	public static String readStringFrom(URL resource) {
		try {
			Path path = Paths.get(resource.toURI());
			Stream<String> lines = Files.lines(path);
			String data = lines.collect(Collectors.joining("\n"));
			lines.close();
			return data;
		} catch (IOException | URISyntaxException e) {
			throw new IllegalStateException("Error while reading string from " + resource.getPath(), e);
		}
	}

	public static String extractContentType(File f) {
		if (f == null) {
			return null;
		}
		try {
			return Files.probeContentType(f.toPath());
		} catch (IOException e) {
			throw new IllegalStateException("IOException while extracting ContentType from file " + f.getName(), e);
		}
	}
}
