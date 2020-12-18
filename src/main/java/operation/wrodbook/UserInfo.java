package operation.wrodbook;

import java.io.Serializable;
import java.util.Objects;

public class UserInfo implements Serializable {
    private int id;

    private String name;

    private  String addr;

    private String date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserInfo(int id, String name, String addr, String date) {
        this.id = id;
        this.name = name;
        this.addr = addr;
        this.date = date;
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", date=" + date +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return id == userInfo.id &&
                Objects.equals(name, userInfo.name) &&
                Objects.equals(addr, userInfo.addr) &&
                Objects.equals(date, userInfo.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addr, date);
    }
}
