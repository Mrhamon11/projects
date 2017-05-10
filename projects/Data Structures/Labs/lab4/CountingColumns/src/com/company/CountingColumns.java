package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class CountingColumns {
    private BufferedReader br;
    private List<Map<Character, Integer>> columns;
    private List<Character> greatest;
    private List<Character> least;

    public static void main(String[] args) {
        CountingColumns cc = new CountingColumns();
        cc.storeInArray();
        cc.getMax();
        cc.getMin();
        cc.printMap();
    }

    public CountingColumns(){
        try {
            this.br = new BufferedReader(new FileReader(new File("src/com/company/columns.txt")));
        }catch (Exception e){
            e.printStackTrace();
        }

        this.columns = new ArrayList<>(8);
        this.columns.add(0, new HashMap<>());
        this.columns.add(1, new HashMap<>());
        this.columns.add(2, new HashMap<>());
        this.columns.add(3, new HashMap<>());
        this.columns.add(4, new HashMap<>());
        this.columns.add(5, new HashMap<>());
        this.columns.add(6, new HashMap<>());
        this.columns.add(7, new HashMap<>());

        this.greatest = new ArrayList<>();
        this.least = new ArrayList<>();
    }

    public void storeInArray(){
        try {
            String line = this.br.readLine();
            while (line != null) {
                for(int i = 0; i < line.length(); i++){
                    int j;
                    Integer count = this.columns.get(i).get(line.charAt(i));
                    if(count == null) {
                        j = 1;
                    }
                    else{
                        j = ++count;
                    }
                    this.columns.get(i).put(line.charAt(i), j);
                }
                line = this.br.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getMax(){
        for(int i = 0; i < this.columns.size(); i++){
            Set<Character> keySet = this.columns.get(i).keySet();
            int max = -1; //Just to start in case a character appeared zero times.
            this.greatest.add(i, null);
            for(Character c : keySet){
                if(this.columns.get(i).get(c) > max){
                    max = this.columns.get(i).get(c);
                    this.greatest.set(i, c);
                }
            }
        }
    }

    public void getMin(){
        for(int i = 0; i < this.columns.size(); i++){
            Set<Character> keySet = this.columns.get(i).keySet();
            int min = Integer.MAX_VALUE;
            this.least.add(i, null);
            for(Character c : keySet){
                if(this.columns.get(i).get(c) < min){
                    min = this.columns.get(i).get(c);
                    this.least.set(i, c);
                }
            }
        }
    }

    public void printMap(){
        System.out.println("Characters appearing most in each column:");
        int i = 1;
        for(Character c: this.greatest){
            System.out.println("Column " + i + ": " + c);
            i++;
        }
        System.out.println();

        System.out.println("Characters appearing least in each column:");
        i = 1;
        for(Character c: this.least){
            System.out.println("Column " + i + ": " + c);
            i++;
        }
    }
}
