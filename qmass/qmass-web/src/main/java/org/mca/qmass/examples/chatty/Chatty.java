package org.mca.qmass.examples.chatty;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 * User: malpay
 * Date: 31.May.2011
 * Time: 10:58:36
 */
@ManagedBean
@RequestScoped
public class Chatty {
    private String value;

    @ManagedProperty(value = "#{chatBoard}")
    private ChatBoard board;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ChatBoard getBoard() {
        return board;
    }

    public void setBoard(ChatBoard board) {
        this.board = board;
    }

    public void post() {
        getBoard().chat(value);
        this.value = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chatty chatty = (Chatty) o;

        if (value != null ? !value.equals(chatty.value) : chatty.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
