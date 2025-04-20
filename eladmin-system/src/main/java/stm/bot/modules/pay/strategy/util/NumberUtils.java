package stm.bot.modules.pay.strategy.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数字相关处理类
 *
 * @author leolian
 *
 */
public class NumberUtils {

	private NumberUtils() {
	}

	/**
	 * @param oneValue
	 * @param twoValue
	 * @return
	 */
	public static boolean isEquation(double oneValue, double twoValue) {
		return isEquation(oneValue, twoValue, 0.000001d);
	}

	/**
	 * @param oneValue
	 * @param twoValue
	 * @param critical
	 * @return
	 */
	public static boolean isEquation(double oneValue, double twoValue,
			double critical) {
		return (Math.abs(oneValue - twoValue) <= critical) ? true : false;
	}

	/**
	 * @param oneValue
	 * @param twoValue
	 * @return
	 */
	public static double addDouble(double oneValue, double twoValue) {
		BigDecimal one = new BigDecimal(String.valueOf(replaceEmpty(oneValue)));
		BigDecimal two = new BigDecimal(String.valueOf(replaceEmpty(twoValue)));
		return one.add(two).doubleValue();

	}

	/**
	 * @param value
	 * @param digit
	 * @return
	 */
	public static String patchZeroToString(long value, int digit) {
		StringBuffer result = new StringBuffer("");
		int index = ((int) Math.log10(value)) + 1;
		if (index < digit) {
			for (int i = 0; i < digit - index; i++) {
				result.append("0");
			}
		}
		result.append(value);
		return result.toString();
	}

	public static Double formatDoubleDigitDecimal(double src) {
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			return Double.parseDouble(df.format(src));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0.0;
	}

	public static String formatDouble(double src) {
		DecimalFormat df = new DecimalFormat("#.##");
		try {
			return df.format(src);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "0.00";
	}

	public static BigDecimal doubleToDecimal(double src) {
		DecimalFormat df = new DecimalFormat("#.00");
		try {
			return new BigDecimal(df.format(src));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new BigDecimal("0.00");
	}

	/**
	 * @param src
	 * @return
	 */
	public static Double replaceEmpty(Double src) {
		return null == src ? 0.0D : src;
	}

	public static Double replaceEmpty(Double src, Double defaultValue) {
		return (null == src || 0.0D == src) ? defaultValue : src;
	}

	public static Double toEmpty(Double src, Double defaultValue) {
		return null == src ? defaultValue : src;
	}

	public static Double replaceEmpty(String src) {
		if (null == src || "".equals(src) || "NULL".equals(src.toUpperCase())) {
			return 0.0D;
		}
		try {
			return Double.parseDouble(src);
		} catch (Exception e) {
			return 0.0D;
		}

	}

	public static Integer toEmpty(String src) {
		if (null == src || "".equals(src) || "NULL".equals(src.toUpperCase())) {
			return 0;
		}
		try {
			return Integer.parseInt(src);
		} catch (Exception e) {
			return 0;
		}

	}

	public static Double roundDouble(double src, int precision) {
		Double ret = null;
		try {
			double factor = Math.pow(10, precision);
			ret = Math.floor(src * factor + 0.5) / factor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Double replaceEmpty(BigDecimal src) {
		return null == src ? 0.0D : src.doubleValue();
	}

	public static Long replaceEmpty(Long src) {
		return null == src ? 0L : src;
	}

	public static Integer replaceEmpty(Integer src) {
		return null == src ? 0 : src;
	}

	public static boolean isEmpty(Long src) {
		return (null == src || 0L == src || -1L == src) ? Boolean.TRUE
				: Boolean.FALSE;
	}

	public static boolean isEmpty(Integer src) {
		return (null == src || 0 == src || -1 == src) ? Boolean.TRUE
				: Boolean.FALSE;
	}

	public static boolean isEmpty(Short src) {
		return (null == src || 0 == src || -1 == src) ? Boolean.TRUE
				: Boolean.FALSE;
	}

	public static boolean isEmpty(Double src) {
		return (null == src || 0.0D == src || isEquation(src, 0.0D)) ? Boolean.TRUE
				: Boolean.FALSE;
	}

	public static String convertToString(Double src) {
		return String.valueOf(replaceEmpty(src));
	}

	public static String convertToString(BigDecimal src) {
		return String.valueOf(replaceEmpty(src));
	}

	/**
     * 保留两位小数，四舍五入的一个老土的方法
     * @param d
     * @return
     */
    public static double formatDoubleTwo(double d) {
        return (double)Math.round(d*100)/100;
    }

	/**
	 * 阿拉伯数字转中文
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String numberToString(int  str) throws Exception {
		String[] table = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		for (char c : Integer.toString(str).toCharArray()) {
			if (c >= '0' && c <= '9') {
				return table[c - 48];
			}
		}
		return null;
	}

	/**
     * 两个Double数相加,返回大于0
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double adds(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        double d1 = b1.add(b2).doubleValue();
        if(d1 < 0){
        	d1 = 0.0;
        }
		return d1;
    }

	/**
     * 两个Double数相减,返回大于0
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double sub(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        double d1 = 0.0;
        if (b1.compareTo(b2) < 0) {
        	return d1;
        }
        if (b1.compareTo(b2) == 0) {
        	return d1;
        }
        if (b1.compareTo(b2) > 0) {
        	d1 = b1.subtract(b2).doubleValue();
        }
		return d1;

    }

    /**
    * * 两个Double数相减 *
    *
    * @param v1 *
    * @param v2 *
    * @return Double
    */
    public static Double subs(Double v1, Double v2) {
       BigDecimal b1 = new BigDecimal(v1.toString());
       BigDecimal b2 = new BigDecimal(v2.toString());
       return new Double(b1.subtract(b2).doubleValue());
    }



    /**
    * * 两个Double数相乘 *
    *
    * @param v1 *
    * @param v2 *
    * @return Double
    */
    public static Double mul(Double v1, Double v2) {
       BigDecimal b1 = new BigDecimal(v1.toString());
       BigDecimal b2 = new BigDecimal(v2.toString());
       return new Double(b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
    * * 两个Double数相除 *
    *
    * @param v1 *
    * @param v2 *
    * @return Double
    */
    public static Double div(Double v1, Double v2) {
       BigDecimal b1 = new BigDecimal(v1.toString());
       BigDecimal b2 = new BigDecimal(v2.toString());
       return new Double(b1.divide(b2, 2 , BigDecimal.ROUND_HALF_UP)
         .doubleValue());
    }

    /**
     * 为null返回0避免校验出错
     * @param src
     * @return
     */
    public static Double nullToZero(Double src) {
    	if(null == src){
    		return 0.0;
    	}else{
    		 return src;
    	}
     }

    /**
     * 为null返回0避免校验出错
     * @param src
     * @return
     */
    public static Double nullToZero(Object src) {
    	if(null == src|| "null".equals(src)||"".equals(src)){
    		return 0.0;
    	}else{
    		 return Double.parseDouble(src+"");
    	}
     }

    /**
     * 判断是否为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
    	Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
	    return pattern.matcher(str).matches();
    }

	/**
	 * 随机产生一个随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();
	}

	/**
	 * 随机产生一个随机数(long)
	 * @param min
	 * @param max
	 * @return
	 */
	public static Long getRandomNumberInRange(Long min, Long max) {
		Random r = new Random();
		return r.longs(min, (max + 1)).findFirst().getAsLong();
	}

	/**
	 * BigDecimal 去掉后面的0
	 *
	 */
	public static String trimBigDecimalLastZero(String str) {
		if (str.indexOf(".") != -1 && str.charAt(str.length() - 1) == '0') {
			return trimBigDecimalLastZero(str.substring(0, str.length() - 1));
		} else {
			return str.charAt(str.length() - 1) == '.' ? str.substring(0, str.length() - 1) : str;
		}
	}
	/**
	 * BigDecimal 去掉后面的0
	 *
	 */
	public static String trimBigDecimalLastZero(BigDecimal num) {
		if(num == null) return "";
		return trimBigDecimalLastZero(num + "");
	}
}
