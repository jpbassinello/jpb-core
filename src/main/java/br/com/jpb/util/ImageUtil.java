package br.com.jpb.util;

import br.com.jpb.exception.ValidateException;
import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class ImageUtil {

	private static final String TMP = System.getProperty("java.io.tmpdir");

	private static final Set<String> EXTENSIONS;
	private static final int MAX_FILE_SIZE = 10 * 2097152; // 20MB
	private static final int ONE_MEGABYTE = 1048576;
	private static final int RESIZE_RATIO = 500;

	static {
		EXTENSIONS = new HashSet<>(Arrays.asList("png", "jpg", "jpeg"));
	}

	public static void validateImage(final String extension, final long size) throws ValidateException {
		if (size == 0) {
			throw new ValidateException("img.upload.invalid.img");
		}
		if (size > MAX_FILE_SIZE) {
			throw new ValidateException("img.upload.invalid.size",
					MathUtil
							.divide(BigDecimal.valueOf(size), BigDecimal.valueOf(ONE_MEGABYTE))
							.setScale(1, RoundingMode.HALF_UP)
							.toString());
		}

		if (!EXTENSIONS.contains(extension.toLowerCase())) {
			throw new ValidateException("img.upload.invalid.extension",
					StringUtil.replaceLast(Joiner
							.on(", ")
							.join(EXTENSIONS), ",", " e"));
		}
	}

	public static File resizeImage(InputStream is, String extension) throws IOException {
		return resizeImage(is, extension, RESIZE_RATIO);
	}

	public static File resizeImage(InputStream is, String extension, int resizeRatio) throws IOException {

		BufferedImage originalImage = ImageIO.read(is);

		int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

		int h = originalImage.getHeight();
		int w = originalImage.getWidth();

		BigDecimal ratio = h > w ? MathUtil.divide(h, resizeRatio) : MathUtil.divide(w, resizeRatio);

		int rh = MathUtil
				.divide(BigDecimal.valueOf(h), ratio)
				.intValue();
		int rw = MathUtil
				.divide(BigDecimal.valueOf(w), ratio)
				.intValue();

		BufferedImage resizedImage = new BufferedImage(rw, rh, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, rw, rh, null);
		g.dispose();

		File file = new File(TMP + System.currentTimeMillis() + "." + extension);

		ImageIO.write(resizedImage, extension, file);

		return file;
	}
}
