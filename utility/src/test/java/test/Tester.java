package test;

import utility.FileUtil;
import utility.JsonUtil;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;

import org.junit.Test;

public class Tester {
    @Test
    public void test1(){

        String filePath = "/Users/heokangmoo/temp/test/test/test.txt";

        File file = new File(filePath);
        try{
            file.getParentFile().mkdirs();
            file.createNewFile();
            new BufferedWriter(new FileWriter(file, true)).write(filePath);

//            FileInputStream fis = new FileInputStream(filePath);
//            FileOutputStream fos = new FileOutputStream(filePath+".out.txt");
//            byte[] bytes = new byte[10];
//            int i = fis.read();
//            while(i != -1){
//                fos.write(i);
//                i = fis.read();
//            }
//            fis.close();
//            fos.close();
        } catch(Exception e){
            e.printStackTrace();
        }


    }
}

