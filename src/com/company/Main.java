package com.company;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            Map<String, Integer> quantity=new HashMap<>();
            if(args.length<1){
                throw new IOException("Error, not enough arguments");
            }
            MyThread[] myThread=new MyThread[args.length];
            for(int i=0;i<args.length;i++) {
                String nameOfFile = args[i];
                myThread[i]=new MyThread(quantity,nameOfFile);
                myThread[i].start();
            }
            System.out.print("Number of word in files:  ");
            for(int i=0;i<args.length;i++) {
                myThread[i].join();
                System.out.print(myThread[i].getCount()+"  ");
            }
            System.out.println();
            int count=0;
            for(Map.Entry<String, Integer> q: quantity.entrySet()) {
                count+=q.getValue();
                System.out.println(q.getKey() +" -- "+q.getValue());
            }
            System.out.println("Number of word in map:  "+count);
        }
        catch(FileNotFoundException e){
            System.out.println("ERROR. Input/Output file: FileNotFoundException");
        }
        catch(IOException e){
            System.out.println("ERROR. IOException");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread extends Thread {
    private Map<String, Integer> quantity= new HashMap<>();
    private String file;
    private int count;
    MyThread(Map<String, Integer> q, String f){
        quantity =q;
        file=f;
    }
    @Override
    public void run() {
        try {
            synchronized (quantity) {
                for (String line : Files.readAllLines(Paths.get(file))) {
                    for (String part : line.split("\\s+")) {
                        count++;
                        if (quantity.containsKey(part))
                            quantity.replace(part, quantity.get(part) + 1);
                        else
                            quantity.put(part, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return count;
    }
}



