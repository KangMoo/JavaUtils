package test;

import utility.FileUtil;
import utility.JsonUtil;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.Test;

public class Tester {
    @Test
    public void test1() {
        String test = "asdfsadf.mp3.enc";
        if(test.endsWith(".enc")) System.out.println("WOW");
    }

    @Test
    public void test2() {
        try{
            div(1, 0);
        }catch (Exception e){
            System.out.println("e.toString() = " + e.toString());
            e.printStackTrace();
        }
    }

    public int div(int a, int b){
        return a/b;
    }
}

