package com.mycompany.app;

import java.io.*;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)
    {
        int[] commandLineNumbers = new int[args.length];
		int argsLength = args.length;
		try{
			for(int i = 0; i < argsLength; i++){
				commandLineNumbers[i] = Integer.parseInt(args[i]);
			}
		}
		catch(Exception e){
			System.out.println("Only enter integers!");
		}
		commandLineNumbers = insertionSort(commandLineNumbers);
		System.out.println(Arrays.toString(commandLineNumbers));
    }
	
	public static int[] insertionSort(int[] numbers) {
		int remove;
		for(int i = 1; i < numbers.length; i++){
			for(int j = i; j > 0; j--){
				if(numbers[j] < numbers[j - 1]) {
					remove = numbers[j];
					numbers[j] = numbers[j - 1];
					numbers[j - 1] = remove;
				}
			}
		}
		return numbers;
	}
}
