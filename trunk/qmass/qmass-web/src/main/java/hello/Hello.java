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
}
