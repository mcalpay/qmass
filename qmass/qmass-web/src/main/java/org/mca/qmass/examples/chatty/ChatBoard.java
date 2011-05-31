package org.mca.qmass.examples.chatty;

import org.mca.qmass.http.Shared;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * User: malpay
 * Date: 31.May.2011
 * Time: 10:58:48
 */
@ManagedBean
@SessionScoped
@Shared
public class ChatBoard implements Serializable{

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatBoard chatBoard = (ChatBoard) o;

        if (value != null ? !value.equals(chatBoard.value) : chatBoard.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public void chat(String value) {
        this.value = (this.value != null) ? (this.value + "\n" + value) : value;
    }
}
