package hello;

import org.mca.qmass.http.Shared;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 13.Nis.2011
 * Time: 10:06:44
 */
@ManagedBean
@SessionScoped
@Shared
public class Hello implements Serializable {

    private String name;

    private int adet = 0;

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hello{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hello hello = (Hello) o;

        if (adet != hello.adet) return false;
        if (name != null ? !name.equals(hello.name) : hello.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + adet;
        return result;
    }
}
