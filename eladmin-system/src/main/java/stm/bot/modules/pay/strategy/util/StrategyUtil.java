package stm.bot.modules.pay.strategy.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

/**
 *
 */
public class StrategyUtil {


    /**
     * 对字符串进行UTF-8转码
     *
     * @param str
     * @return 字符串
     */
    public static String getEncoding(String str) {
        String res = "";
        try {
            res = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createParamStringValueNotNull(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (key.equals("sign")) {
                continue;
            }
            String value = params.get(key);
            if (ObjectUtils.isEmpty(value)) {
                continue;
            }
            if (i == keys.size() - 1) {
                content.append(key + "=" + value);
            } else {
                content.append(key + "=" + value + "&");
            }
        }
        return content.toString();
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createParamStringValueNotSign(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (key.contains("sign")) {
                continue;
            }
            String value = params.get(key);
            if (ObjectUtils.isEmpty(value)) {
                continue;
            }
            if (i == keys.size() - 1) {
                content.append(key + "=" + value);
            } else {
                content.append(key + "=" + value + "&");
            }
        }
        return content.toString();
    }

    /**
     * 将Map形式的Form表单数据进行排序,转换为Url参数形式
     *
     * @param params Map
     * @return a=1&b=2... 这样的字符串
     */
    public static String createSortParamString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams = new TreeMap<String, String>(params);
        // 遍历排序的字典,并拼接"key=value"格式
        for (Map.Entry<String, String> entry : sortParams.entrySet()) {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    /**
     * 对字符串md5加密
     *
     * @param str 传入要加密的字符串
     * @return MD5加密后的字符串
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes("UTF-8"));
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5 = new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMD5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
        }
    }

    public static String fillMD5(String md5) {
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }

    /**
     * 签名算法
     *
     * @param str 传入的字符串
     * @param key 密钥
     * @return MD5加密后的字符串
     */
    public static String getSign(String str, String key) {
        String sign = DigestUtils.md5Hex(str + "&apikey=" + key).toLowerCase();
        return sign;
    }

    public static String getSignJd(String str, String key) {
        String sign = DigestUtils.md5Hex(str + "&" + key);
        return sign;
    }

    public static String getSignThree(String str, String key) {
        String sign = DigestUtils.md5Hex(str + "&key=" + key);
        return sign;
    }

    public static String getSignn(String str, String key) {
        String sign = DigestUtils.md5Hex(str + key);
        return sign;
    }

    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new HashMap();
        if (StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createParamString(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (i == keys.size() - 1) {
                content.append(key + "=" + (value != null ? value.toString() : ""));
            } else {
                content.append(key + "=" + (value != null ? value.toString() : "") + "&");
            }
        }
        return content.toString();
    }

    public static String createParamStringNotNullNotSign(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (key.toLowerCase().equals("sign")) {
                continue;
            }
            if (ObjectUtils.isEmpty(value)) {
                continue;
            }
            if (i == keys.size() - 1) {
                content.append(key + "=" + (value != null ? value.toString() : ""));
            } else {
                content.append(key + "=" + (value != null ? value.toString() : "") + "&");
            }
        }
        return content.toString();
    }

    /**
     * 将Map形式的Form表单数据转换为value&
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createParamValueString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {
                content.append(value);
            } else {
                content.append(value + "&");
            }
        }
        return content.toString();
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createParamStringValueNotNull0(Map params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (ObjectUtils.isEmpty(params.get(key))) {
                continue;
            }
            String value = params.get(key) + "";
            if (i == keys.size() - 1) {
                content.append(key + "=" + value);
            } else {
                content.append(key + "=" + value + "&");
            }
        }
        String reStr = content.toString();
        if (reStr.endsWith("&")) {
            reStr = reStr.substring(0, reStr.length() - 1);
        }
        return reStr;
    }

    public static String createParamStringValueNotNull1(Map params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (key.equals("sign")) {
                continue;
            }
            if (ObjectUtils.isEmpty(params.get(key))) {
                continue;
            }
            String value = params.get(key) + "";
            if (i == keys.size() - 1) {
                content.append(key + "=" + value);
            } else {
                content.append(key + "=" + value + "&");
            }
        }
        String reStr = content.toString();
        if (reStr.endsWith("&")) {
            reStr = reStr.substring(0, reStr.length() - 1);
        }
        return reStr;
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createParamStringValueNotNullNotSign(Map params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (key.equals("sign")) {
                continue;
            }
            if (ObjectUtils.isEmpty(params.get(key))) {
                continue;
            }
            String value = params.get(key) + "";
            if (i == keys.size() - 1) {
                content.append(key + "=" + value);
            } else {
                content.append(key + "=" + value + "&");
            }
        }
        String reStr = content.toString();
        if (reStr.endsWith("&")) {
            reStr = reStr.substring(0, reStr.length() - 1);
        }
        return reStr;
    }

    public static String createParamSignString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {
                content.append(key + "=" + value);
            } else {
                content.append(key + "=" + value + "&");
            }
        }
        String reStr = content.toString();
        if (reStr.endsWith("&")) {
            reStr = reStr.substring(0, reStr.length() - 1);
        }
        return reStr;
    }
}
