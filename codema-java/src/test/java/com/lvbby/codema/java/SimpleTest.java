package com.lvbby.codema.java;

import org.junit.Test;

import java.net.URI;

/**
 * Created by lipeng on 2016/12/24.
 */
public class SimpleTest {

    @Test
    public void uri(){
        URI uri = URI.create("asdf:///root/leaf?path=sdf&&sub=sdf");
        System.out.println(uri.getAuthority());
        System.out.println(uri.getPath());
        System.out.println(uri.getQuery());
        System.out.println(uri);
    }
}
