package br.com.jpb.util;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtil {

	private static final int DEFAULT_SCALE = 6;
	private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;
	private MathUtil() {
	}

	public static BigDecimal divide(int x, int y) {
		return divide(BigDecimal.valueOf(x), BigDecimal.valueOf(y));
	}

	public static BigDecimal divide(long x, long y) {
		return divide(BigDecimal.valueOf(x), BigDecimal.valueOf(y));
	}

	public static BigDecimal divide(BigDecimal x, BigDecimal y) {
		if (y.compareTo(BigDecimal.ZERO) == 0 || x.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		/**
		 * <f:convertNumber pattern="#.0%" type="percent"/> já múltiplca por 100
		 */
		return x.divide(y, DEFAULT_SCALE, DEFAULT_ROUNDING);
	}

	public static int asInt(BigDecimal x) {
		return x == null ? 0 : x.setScale(0, DEFAULT_ROUNDING).intValue();
	}

	public static int sumToOldValueNullSafe(Integer oldValue, int sum) {
		oldValue = MoreObjects.firstNonNull(oldValue, 0);
		return oldValue + sum;
	}
}
