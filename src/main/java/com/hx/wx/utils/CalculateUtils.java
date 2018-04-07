package com.hx.wx.utils;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class CalculateUtils {
	private static Logger logger = Logger.getLogger(CalculateUtils.class);

	public static String multiplyRate(String multiplicand, String rate) {
		BigDecimal _multiplicand = new BigDecimal(multiplicand);
		BigDecimal _rate = new BigDecimal(rate);
		BigDecimal multiplicandRate = new BigDecimal(100);
		BigDecimal taxrate = _rate.divide(multiplicandRate, 7, 4);
		return formatScale(_multiplicand.multiply(taxrate), 2);
	}

	public static BigDecimal multiplyRate(BigDecimal multiplicand, BigDecimal rate) {
		BigDecimal multiplicandRate = new BigDecimal(100);
		BigDecimal taxrate = rate.divide(multiplicandRate, 7, 3);
		return multiplicand.multiply(taxrate);
	}

	public static BigDecimal multiply(BigDecimal param1, BigDecimal param2) {
		return param1.multiply(param2);
	}

	public static String multiply(String multiplicand, String multiplier) {
		BigDecimal _multiplicand = new BigDecimal(multiplicand);
		BigDecimal _multiplier = new BigDecimal(multiplier);
		return formatScale(_multiplicand.multiply(_multiplier), 2);
	}

	public static String divideUp(String dividend, String divider, int scale) {
		BigDecimal dividend_dec = new BigDecimal(dividend);
		BigDecimal divider_dec = new BigDecimal(divider);
		return formatScale(dividend_dec.divide(divider_dec, 6, 4), scale);
	}

	public static String divideDown(String dividend, String divider, int scale) {
		BigDecimal dividend_dec = new BigDecimal(dividend);
		BigDecimal divider_dec = new BigDecimal(divider);
		return formatScale(dividend_dec.divide(divider_dec, 6, 5), scale);
	}

	public static String divide(String dividend, String divider, int scale) {
		BigDecimal dividend_dec = new BigDecimal(dividend);
		BigDecimal divider_dec = new BigDecimal(divider);
		return formatScale(dividend_dec.divide(divider_dec, 6, 3), scale);
	}

	public static BigDecimal divide(BigDecimal dividend, BigDecimal divider, int scale) {
		return dividend.divide(divider, scale, 4);
	}

	public static String subtraction(String minuend, String subtractor) {
		BigDecimal bigdec = new BigDecimal(minuend);
		return formatScale(bigdec.subtract(new BigDecimal(subtractor)), 2);
	}

	public static BigDecimal subtraction(BigDecimal param1, BigDecimal param2) {
		return param1.subtract(param2);
	}

	public static String formatScale(BigDecimal bd, int scale) {
		String pattern = "#.";

		for (int formater = 1; formater <= scale; ++formater) {
			pattern = pattern + "#";
		}

		DecimalFormat arg3 = new DecimalFormat(pattern);
		arg3.setGroupingSize(0);
		arg3.setRoundingMode(RoundingMode.FLOOR);
		return arg3.format(bd);
	}

	public static String add(String... addparams) {
		if (addparams != null && addparams.length != 0) {
			BigDecimal bigdec = new BigDecimal(addparams[0]);

			for (int i = 1; i < addparams.length; ++i) {
				bigdec = bigdec.add(new BigDecimal(addparams[i]));
			}

			return bigdec.toPlainString();
		} else {
			return null;
		}
	}

	public static String add(String param1, String param2) {
		return (new BigDecimal(param1)).add(new BigDecimal(param2)).toPlainString();
	}

	public static BigDecimal add(BigDecimal param1, BigDecimal param2) {
		return param1.add(param2);
	}

	public static int compare(String param1, String param2) {
		return (new BigDecimal(param1)).compareTo(new BigDecimal(param2));
	}

	public static int compare(BigDecimal param1, BigDecimal param2) {
		return param1.compareTo(param2);
	}

	public static boolean timesOnSecondDecimal(String param1, String param2) {
		return param1 != null && param2 != null ? (!"".equals(param1) && !"".equals(param2)
				? (new BigDecimal(param1)).divideAndRemainder(new BigDecimal(param2))[1]
						.compareTo(new BigDecimal("0")) == 0
				: false) : false;
	}

	public static double randomMumOFMinAndMax(double nMin, double nMax) {
		Random random = new Random();
		double nRange = nMax - nMin;
		double nRandomDouble = random.nextDouble() * nRange;
		double nYouWant = nMax - nRandomDouble;
		return doubleTowMumOFDecimalFormat(nYouWant);
	}

	public static double doubleTowMumOFDecimalFormat(double num) {
		DecimalFormat df = new DecimalFormat("######0.00");
		return Double.valueOf(df.format(num)).doubleValue();
	}
}