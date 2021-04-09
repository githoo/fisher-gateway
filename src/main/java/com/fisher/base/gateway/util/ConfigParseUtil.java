package com.fisher.base.gateway.util;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by fisher
 */
public class ConfigParseUtil {
    /**
     * 读取命令行参数覆盖原有配置
     * @param args 命令行参数
     * @param defaultConf  默认位置文件path
     * @throws IOException
     * @throws ParseException
     */
    public static Properties parse(String[] args,String defaultConf) throws IOException, ParseException {
        // 读取原有配置
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultConf);
        properties.load(inputStream);
        // 读取扩展配置覆盖原有配置
        printFrame(100,"","!");
        CommandLineParser parser = new DefaultParser();
        Options options = buildOptions();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.getOptions().length > 0 && cmd.hasOption("D")) {
            Properties ps = cmd.getOptionProperties("D");
            System.out.println("extend properties="+ps);
            Enumeration names = ps.propertyNames();
            while (names.hasMoreElements()) {
                try {
                    String key = (String) names.nextElement();
                    String value = ps.getProperty(key);
                    properties.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            printFrame(100,"","!");
        }
        return properties;
    }

    /**
     * 构建命令行参数-Dproperty=value
     * @return
     */
    public static Options buildOptions() {
        Options opts = new Options();
        opts.addOption(OptionBuilder.withArgName("property=value")
                .hasArgs(2)
                .withValueSeparator()
                .withDescription("user value for given property").create("D"));
        return opts;
    }

    /**
     * 查找制定配置文件具体配置，不存在返回 调用者制定默认值
     *
     * @param properties
     * @param name
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public static String getProperty(Properties properties,String name, String defaultValue) throws Exception {
        String value = properties.getProperty(name);
        if (value == null || value.isEmpty()) {
            return defaultValue.trim();
        } else {
            return value.trim();
        }
    }

    /**
     * 查找制定配置文件具体配置，不存在则抛出异常
     *
     * @param properties
     * @param name
     * @return
     * @throws Exception
     */
    public static String getProperty(Properties properties,String name) throws Exception {
        String value = properties.getProperty(name);
        if (value == null || value.isEmpty()) {
            throw new Exception("no parameters(" + name + ") set!");
        } else {
            return value.trim();
        }
    }

    /**
     *   打印property配置信息内容 默认是* 如下
     *      *******************************************
     *      *properties    storm.num.workers : 10     *
     *      *properties    storm.num.spouts : 20      *
     *      *******************************************
     * @param properties
     * @param width
     */
    public static void printProperty(Properties properties,int width){
        printProperty(properties,width,"*");
    }

    /**
     * 打印property配置信息内容
     * @param properties
     * @param width
     * @param printChar
     */
    public static void printProperty(Properties properties,int width,String printChar){
        Enumeration en = properties.propertyNames();
        printFrame(width, "",printChar);
        while(en.hasMoreElements()){
            String key = en.nextElement().toString();//key值
            String value=properties.get(key).toString();
            if (value.length() + 21 > width) {
                width = value.length() + 21;
            }
            printFrame(width, String.format("properties    %s : %s",key,value),printChar);
        }
        printFrame(width, "",printChar);
    }
    /**
     * 格式化输出信息例如 默认是*填充
     *
     * @param width  如果是空的话输出多少 printChar
     * @param message  消息
     */
    private static void printFrame(int width, String message) {
        printFrame(width,message,"*");
    }

    /**
     * 格式化输出信息例如
     *
     * @param width  如果是空的话输出多少 printChar
     * @param message  消息
     * @param printChar 格式化填充的字符
     */
    private static void printFrame(int width, String message,String printChar) {
        if (message == null || message.isEmpty()) {
            for (int i = 0; i < width; ++i) {
                System.out.print(printChar);
            }
        } else {
            for (int i = 0; i < width; ++i) {
                if (i == 0 || i == width - 1) {
                    System.out.print(printChar);
                } else if (i == 2) {
                    System.out.print(message);
                    i = i + message.length() - 1;
                } else {
                    System.out.print(" ");
                }
            }
        }
        System.out.print("\n");
    }


}
