package br.com.jpb.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import br.com.jpb.exception.ValidateException;

import com.google.common.base.Joiner;

public class ImageUtil {

	private static final String TMP = System.getProperty("java.io.tmpdir");

	private static final Set<String> EXTENSIONS;
	private static final int MAX_FILE_SIZE = 2097152; // 2MB
	private static final int ONE_MEGABYTE = 1048576;

	static {
		EXTENSIONS = new HashSet<>(Arrays.asList("png", "jpg", "jpeg"));
	}

	public static void validateImage(final String extension, final long size)
			throws ValidateException {
		if (size == 0) {
			throw new ValidateException("img.upload.invalid.img");
		}
		if (size > MAX_FILE_SIZE) {
			throw new ValidateException("img.upload.invalid.size", MathUtil
					.divide(BigDecimal.valueOf(size),
							BigDecimal.valueOf(ONE_MEGABYTE))
					.setScale(1, RoundingMode.HALF_UP).toString());
		}

		if (!EXTENSIONS.contains(extension)) {
			throw new ValidateException("img.upload.invalid.extension",
					StringUtil.replaceLast(Joiner.on(", ").join(EXTENSIONS),
							",", " e"));
		}
	}

	public static File resizeImage(InputStream is, String extension)
			throws IOException {

		BufferedImage originalImage = ImageIO.read(is);

		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
				: originalImage.getType();

		final int RESIZED_IMG_WIDTH = 100;
		final int RESIZED_IMG_HEIGHT = 100;

		int h = originalImage.getHeight();
		int w = originalImage.getWidth();

		BigDecimal ratio = h > w ? MathUtil.divide(h, RESIZED_IMG_HEIGHT)
				: MathUtil.divide(w, RESIZED_IMG_WIDTH);

		int rh = MathUtil.divide(BigDecimal.valueOf(h), ratio).intValue();
		int rw = MathUtil.divide(BigDecimal.valueOf(w), ratio).intValue();

		BufferedImage resizedImage = new BufferedImage(rw, rh, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, rw, rh, null);
		g.dispose();

		File file = new File(TMP + System.currentTimeMillis() + "." + extension);
		
		ImageIO.write(resizedImage, extension, file);
		
		return file;
	}
}
