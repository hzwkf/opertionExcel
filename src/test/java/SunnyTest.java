import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class SunnyTest {

    public static void main(String[] args) {
        System.out.println("This is a test class");
//        getXml("D:\\kaifagongju\\hadoop-3.2.0\\etc\\hadoop\\core-site.xml");
        Map exec = exec("10.128.3.50", "tss", "tss", 22, "top");
        exec.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });

        Map openFiles = getOpenFiles("10.128.3.50", "tss", "tss", 22, "ulimit -a");
        for (Object files: openFiles.entrySet()) {
            System.out.println(files);
        }
        openFiles.forEach((key,value) -> {
            System.out.println(key + ":" + value);
        });

    }

    /**
     * 获取打开的最大文件数
     *
     * @return
     */
    public static Map getOpenFiles(String host, String user, String psw, int port, String ulimit) {
        Map<String, String> hashMap = new HashMap<>();
        Map map = exec(host, user, psw, port, ulimit); //ulimit -a
        String openFiles = (String) map.get("openFiles");
        hashMap.put("openFiles", openFiles);
        return hashMap;
    }

    /**
     * 远程 执行命令并返回结果调用过程 是同步的（执行完23才会返回）
     *
     * @param host    主机名
     * @param user    用户名
     * @param psw     密码
     * @param port    端口
     * @param command 命令
     * @return
     */
    public static Map exec(String host, String user, String psw, int port, String command) {
        String result = "";
        Session session = null;
        ChannelExec openChannel = null;
        Map<String, String> map = new HashMap<>();
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(psw);
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            openChannel.connect();
            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                String data = new String(buf.getBytes("UTF-8"), "UTF-8");
                result += data + "\r\n";
                if (buf.contains("open files")) {
                    map.put("openFiles", data);
                }
                if (buf.contains("soft nofile")) {
                    map.put("softNofile", data);
                }
                if (buf.contains("hard nofile")) {
                    map.put("hardNofile", data);
                }
                if (buf.contains("max_connections")) {
                    map.put("maxConnections", data);
                }
                if (buf.contains("innodb_buffer_pool_size")) {
                    map.put("innodbBufferPoolSize", data);
                }
                if (buf.contains("timeout")) {
                    map.put("timeout", data);
                }
                if (buf.contains("connected_clients")) {
                    map.put("connectedClients", data);
                }
            }
        } catch (JSchException | IOException e) {
            result += e.getMessage();
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        map.put("all", result);
        return map;
    }


    public static Map getXml(String url) {
        Map<String, String> hashMap = new HashMap<>();
        //创建解析器
        SAXReader saxreader = new SAXReader();
        //读取文档
        Document doc = null;
        try {
            doc = saxreader.read(new File(url));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //获取根
        Element root = doc.getRootElement();
        //获取子节点
        List<Element> list = root.elements();
        for (Element element : list) {
            String name = element.getName();
            if ("System".equals(name)) {
                Element dbConnection = element.element("DataBase").element("DBConnection");
                String removeAbandoned = dbConnection.attributeValue("removeAbandoned");
                hashMap.put("removeAbandoned", removeAbandoned != "" && removeAbandoned != null ? removeAbandoned : "");
                String removeAbandonedTimeout = dbConnection.attributeValue("removeAbandonedTimeout");
                hashMap.put("removeAbandonedTimeout", removeAbandonedTimeout != "" && removeAbandonedTimeout != null ? removeAbandonedTimeout : "");
                String logAbandoned = dbConnection.attributeValue("logAbandoned");
                hashMap.put("logAbandoned", logAbandoned != "" && logAbandoned != null ? logAbandoned : "");
            }
            if ("Service".equals(name)) {
                Element connector = element.element("Connector");
                hashMap.put("port", connector.attributeValue("port"));
                hashMap.put("protocol", connector.attributeValue("protocol"));
                hashMap.put("maxThreads", connector.attributeValue("maxThreads"));
                hashMap.put("connectionTimeout", connector.attributeValue("connectionTimeout"));
            }
            if ("ConnectionSettings".equals(name)) {
                List<Element> settings = element.elements("Settings");
                for (Element setting : settings) {
                    String attributeValue = setting.attributeValue("Id");
                    if ("XUGUConnection".equals(attributeValue)) {
                        List<Element> elements = setting.elements("Add");
                        for (Element attr : elements) {
                            List<Object> attributes = Collections.singletonList(attr.attributes());
                            DefaultAttribute value = (DefaultAttribute) attributes.get(0);
                            String valueValue = value.getValue();
                            DefaultAttribute data = (DefaultAttribute) attributes.get(1);
                            String dataValue = data.getValue();
                            if ("removeAbandoned".equals(valueValue)) {
                                hashMap.put("removeAbandoned", dataValue);
                            }
                            if ("removeAbandonedTimeout".equals(valueValue)) {
                                hashMap.put("removeAbandonedTimeout", dataValue);
                            }
                            if ("logAbandoned".equals(valueValue)) {
                                hashMap.put("logAbandoned", dataValue);
                            }
                        }
                    }
                }
            }
        }
        return hashMap;
    }
}
