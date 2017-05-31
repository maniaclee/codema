package com.lvbby.codema.java.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by dushang.lp on 2017/5/14.
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoTest {
    @Mock
    private List mockList;

    @InjectMocks
    private ServiceImpl service;
    @Mock
    private TextHolder textHolder;

    @Before
    public void init() {
        //        when(mockList.get(0)).thenReturn(1000);
        when(mockList.get(anyInt())).thenReturn(-1000);
    }


    @Test
    public void serviceTest() throws Exception {
        Mockito.when(textHolder.getText()).thenReturn("@@@@test");
        System.out.println(service.echo("sdfsdf"));
    }

    @Test
    public void shorthand() {
        mockList.add(1);
        verify(mockList).add(1);
        System.out.println(mockList.get(0));
        System.out.println(mockList.get(10));
    }
}
