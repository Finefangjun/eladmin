package stm.bot.modules.pay.strategy.util;

import org.hibernate.Query;

import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

    /**
     * 自动生成预约流水号
     *
     * @return
     */
    public static String getIndex() {
        // 业务流程序号生成规则：4位(随机数) + 12位(年月日时分秒) + 4位(随机数)
        StringBuffer idx = new StringBuffer();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        idx.append(getRandomNumber(4));
        idx.append(sdf.format(date));
        idx.append(getRandomNumber(4));
        return idx.toString();
    }

    /**
     * 生成N位随字母数字
     *
     * @param count
     * @return
     */
    public static String getRandom(int count) {
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuffer sb = new StringBuffer(count);
        for (int i = 0; i < count; i++) {
            sb.append(chars.charAt((int) (Math.random() * 36)));
        }
        return sb.toString();
    }

    /**
     * 生成N位随数字
     *
     * @param count
     * @return
     */
    public static String getRandomNum(int count) {
        String chars = "1234567890";
        StringBuffer sb = new StringBuffer(count);
        for (int i = 0; i < count; i++) {
            sb.append(chars.charAt((int) (Math.random() * 10)));
        }
        return sb.toString();
    }

    /**
     * 生成N位随字母
     *
     * @param count
     * @return
     */
    public static String getRandomEn(int count) {
        String chars = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuffer sb = new StringBuffer(count);
        for (int i = 0; i < count; i++) {
            sb.append(chars.charAt((int) (Math.random() * 36)));
        }
        return sb.toString();
    }

    /**
     * 生成N位随机数
     *
     * @param count
     * @return
     */
    public static String getRandomNumber(int count) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer(count);
        for (int i = 0; i < count; i++) {
            sb.append((char) ('0' + r.nextInt(10)));
        }
        return sb.toString();
    }

    /**
     * 循环获取query parameter
     *
     * @param query
     * @param map
     * @return
     */
    public static Query setParameter(Query query, Map<String, Object> map) throws Exception {
        if (map != null) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                Object obj = map.get(key);
                query.setParameter(key, obj);
            }
        }
        return query;
    }

    /**
     * 将字CLOB转成STRING类型
     *
     * @param clob
     * @return String
     */
    public static String ClobToString(Clob clob) throws SQLException, IOException {

        String reString = "";
        Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        is.close();
        return reString;
    }

    /**
     * 自定义id-毫秒级别时间14位+10位UUId的hashCode
     *
     * @return
     */
    public static String getIdByUUId() {
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        //年月日时分秒+10位uuid.hashCode()
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis()) + String.format("%07d", hashCode / 1000);
    }

    /**
     * 自定义id时间14位+10位UUId的hashCode(无毫秒)
     *
     * @return
     */
    public static String getIdByUUIdNoMs() {
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        //年月日时分秒+10位uuid.hashCode()
        return new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + CommonUtil.getRandomNum(10);
    }

    private final static String[] agent = {"Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser"}; //定义移动端请求的所有可能类型

    /**
     * 判断User-Agent 是不是来自于手机
     *
     * @param ua
     * @return
     */
    public static boolean checkAgentIsMobile(String ua) {
        boolean flag = false;
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
                for (String item : agent) {
                    if (ua.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 随机数不包含
     *
     * @param RandMax
     * @param ExceptNums
     * @return
     */
    public static int getRandomExcept(int RandMax, Set ExceptNums) {
        try {
            Random rand = new Random();
            while (true) {
                int num = rand.nextInt(RandMax);
                if (ExceptNums.contains(num)) {
                    continue;
                } else {
                    return num;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 执行shell脚本
     */
    public static void executeOneMore(List<String> commands) {
        Runtime run = Runtime.getRuntime();
        try {
            Process proc = run.exec("/usr/bin", null, null);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            for (String line : commands) {
                out.println(line);
            }
            out.println("exit");// 结束命令
            String rspLine = "";
            while ((rspLine = in.readLine()) != null) {
                System.out.println(rspLine);
            }
            proc.waitFor();
            in.close();
            out.close();
            proc.destroy();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 生成随机昵称
     *
     * @return
     */
    public static String generateName(String[] adjective,String[] noun) {
        int adjLen= adjective.length;
        int nLen= noun.length;
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        sb.append(adjective[random.nextInt(adjLen)]);
        sb.append(noun[random.nextInt(nLen)]);
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getIdByUUIdNoMs());
    }
}
