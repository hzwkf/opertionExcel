package getAtteributesFromHost;

import java.util.Objects;

public class MonitorInfoBean {

    private double cpuUsage;

    private double memUsage;

    private double getMemUsageSize;

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemUsage() {
        return memUsage;
    }

    public void setMemUsage(double memUsage) {
        this.memUsage = memUsage;
    }

    public double getGetMemUsageSize() {
        return getMemUsageSize;
    }

    public void setGetMemUsageSize(double getMemUsageSize) {
        this.getMemUsageSize = getMemUsageSize;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitorInfoBean that = (MonitorInfoBean) o;
        return Double.compare(that.cpuUsage, cpuUsage) == 0 &&
                Double.compare(that.memUsage, memUsage) == 0 &&
                Double.compare(that.getMemUsageSize, getMemUsageSize) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpuUsage, memUsage, getMemUsageSize);
    }

    @Override
    public String toString() {
        return "MonitorInfoBean{" +
                "cpuUsage=" + cpuUsage +
                ", memUsage=" + memUsage +
                ", getMemUsageSize=" + getMemUsageSize +
                '}';
    }
}
