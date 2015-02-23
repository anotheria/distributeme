package org.distributeme.test.jsonrpc;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: vitaly
 * Date: 2/8/11
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class A implements Serializable {
    private String a;

    public A(final int i) {
        a = i + "";
    }

    public A() {

    }

    public String getA() {
        return a;
    }

    public void setA(final String a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "A{" +
                "a='" + a + '\'' +
                '}';
    }
}
