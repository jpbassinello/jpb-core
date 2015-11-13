package br.com.jpb.exporter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExporterNumeric {

	int scale() default 0;

	RoundingMode roundingMode() default RoundingMode.HALF_UP;

}