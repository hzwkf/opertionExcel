package getAtteributesFromHost;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;

public class SystemInfoTools {

    final static boolean isNotWindows =
            System.getProperties().getProperty("os.name").toLowerCase().indexOf("windows") < 0;

    final static BigDecimal DIVISOR = BigDecimal.valueOf(1024);

    private final static Logger logger = LoggerFactory.getLogger(SystemInfoTools.class);

    public static int getPid() {
        return Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }

    public static MonitorInfoBean getMonitorInfoBean() {
        MonitorInfoBean monitorInfo = new MonitorInfoBean();

        if (!isNotWindows) {
            monitorInfo.setCpuUsage(500);
            return monitorInfo;
        }

        Runtime runtime = Runtime.getRuntime();

        BufferedReader br = null;

        try {
            int pid = getPid();

            String[] cmd = {
                    "/bin/sh",
                    "-c",
                    "top -b -n 1 | grep " + pid
            };
            Process exec = runtime.exec(cmd);

            br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String str = null;
            String[] arr = null;

            while ((str = br.readLine()) != null) {
                logger.warn("top: " + str);

                int m = 0;
                arr = str.split(" ");
                for (int i = 0; i < arr.length; i++) {
                    String info = arr[i];
                    if (info.trim().length() == 0) {
                        continue;
                    }

                    if (m == 5) { //第6列为物理常驻内存
                        String memRes = info.substring(info.length() - 1);
                        if (memRes.equalsIgnoreCase("g")) {
                            monitorInfo.setGetMemUsageSize(Double.parseDouble(info.substring(0, info.length() - 1)));
                        } else if (memRes.equalsIgnoreCase("m")) {
                            BigDecimal memUseSize = new BigDecimal(info.substring(0, info.length() - 1));
                            monitorInfo.setGetMemUsageSize(memUseSize.divide(DIVISOR).doubleValue());
                        } else {
                            BigDecimal memUseSize = new BigDecimal(info).divide(DIVISOR);
                            monitorInfo.setGetMemUsageSize(memUseSize.divide(DIVISOR).doubleValue());
                        }
                    }

                    if (m == 8) { //第9列为CPU占用百分比
                        monitorInfo.setCpuUsage(Double.valueOf(info));
                    }

                    if (m == 9) { //第10列为内存占用百分比
                       monitorInfo.setMemUsage(Double.valueOf(info));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return monitorInfo;
    }

}
