package com.lvbby.codema.java;

import java.io.Serializable;

/**
 * Created by lipeng on 17/1/22.
 * 6834070114183396972L
 * 5598428080554297811L     sdf
 * 2754397193466657325L     sdf getter/setter
 */
public class BeanSerialize implements Serializable {

    private static final long serialVersionUID = 3356105705231549695L;
    private String sdf;

    public void run(){

    }
    public String getSdf() {
        return sdf;
    }

    public void setSdf(String sdf) {
        this.sdf = sdf;
    }

    public static void main(String[] args) {
        System.out.println(SerializeVersionUidGenerator.computeDefaultSUID(BeanSerialize.class));
    }
}
