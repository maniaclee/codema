package com.lvbby.codema.java.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    @Before
    public void init() {
        //        when(mockList.get(0)).thenReturn(1000);
        when(mockList.get(anyInt())).thenReturn(-1000);
    }

    @Test
    public void shorthand() {
        mockList.add(1);
        verify(mockList).add(1);
        System.out.println(mockList.get(0));
        System.out.println(mockList.get(10));
    }
}
