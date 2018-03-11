/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utill;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author chathuranga
 */
public class ProductCipher {

    private String shift(String text, String key) {

        int s = 0;
        int letterInt = 0;
        int shiftingFactor;
        String outStr = "";

        //make a char array out of the text
        char[] arr = text.toCharArray();

        //make text lowercase for simplicity
        //for now assume lowercase
        //Get the shifting factor out of the "key"
        char[] keyArr = key.toCharArray();
        for (char letter : keyArr) {
            letterInt = (int) letter;
            s = s + letterInt;
        }
        shiftingFactor = s % 25 + 1;
        //System.out.println(shiftingFactor);

        //shift each element by shifting factor
        for (int i = 0; i < arr.length; i++) {
            int newVal = (int) arr[i] + shiftingFactor;

//            if(arr[i] == ' '){
//                System.out.println((int)arr[i] + "  :  " + newVal);
//            }
//            if(newVal>122){
//                newVal = newVal - 26;
//            }
            //System.out.println((char)newVal + " from  " + arr[i]);
            outStr += (char) newVal;
        }

        return outStr;
    }

    private String reverseShift(String text, String key) {

        int s = 0;
        int letterInt = 0;
        int shiftingFactor;
        String outStr = "";

        //make a char array out of the text
        char[] arr = text.toCharArray();

        //make text lowercase for simplicity
        //for now assume lowercase
        //Get the shifting factor out of the "key"
        char[] keyArr = key.toCharArray();
        for (char letter : keyArr) {
            letterInt = (int) letter;
            s = s + letterInt;
        }
        shiftingFactor = s % 25 + 1;
        //System.out.println(shiftingFactor);

        //shift reversely each element by shifting factor
        for (int i = 0; i < arr.length; i++) {
            int newVal = (int) arr[i] - shiftingFactor;
//            if(newVal<97){
//                newVal = newVal + 26;
//            }
            //System.out.println((char)newVal + " from  " + arr[i]);

            outStr += (char) newVal;
        }

        return outStr;
    }

    private String permute(String text, String key) {

        //make a set using 'key;
        //make a character array from the key
        char[] keyArr = key.toCharArray();

        //make an arraylist and append letters in key only once
        ArrayList<Character> keyLetters = new ArrayList<>();
        for (char letter : keyArr) {
            if (!keyLetters.contains(letter)) {
                keyLetters.add(letter);
            }
        }

        //make the key's letter set an Array
        Object[] keyLetterSetObj = keyLetters.toArray();
        char[] keyLetterSet = new char[keyLetterSetObj.length];
        for (int i = 0; i < keyLetterSet.length; i++) {
            keyLetterSet[i] = (char) keyLetterSetObj[i];
        }

        //determine the text splitting size (sampling size)
        int sampleSize = keyLetterSet.length;

        //make another list of key letter set, which is a sorted version
        char[] sortedKeyLetterSet = new char[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sortedKeyLetterSet[i] = keyLetterSet[i];
        }

        Arrays.sort(sortedKeyLetterSet);

        //define the shuffling map
        int[] shuffleMap = new int[sampleSize];

        //find the index of the sorted list's elements w.r.t unsorted list
        for (int j = 0; j < sampleSize; j++) {
            char letter = sortedKeyLetterSet[j];

            for (int i = 0; i < sampleSize; i++) {
                if (keyLetterSet[i] == letter) {
                    shuffleMap[j] = i;
                    break;
                }
            }
        }

        //a few s-outs for debugging
//        System.out.println("---------------");
//        System.out.println("Key array " + Arrays.toString(keyArr));
//        System.out.println("Key letter Set " + Arrays.toString(keyLetterSet));
//        System.out.println("Key letter Set sorted " + Arrays.toString(sortedKeyLetterSet));
//        System.out.println("shuffle map " + Arrays.toString(shuffleMap));
//        System.out.println("---------------");

        //get the text length and fill it out for sampling
        if (text.length() % sampleSize > 0) {
            for (int i = 0; i < text.length() % sampleSize; i++) {
                text = text + '_';
            }
        }

        //make a char array out of new text
        char[] textArr = text.toCharArray();

        //# of samples and the sampleId is the current processing sample index
        int numOfSamples = textArr.length / sampleSize;
        int sampleId = 0;

        //shuffled char array
        char[] shuffledText = new char[textArr.length];

        //do the shuffle
        for (int i = 0; i < numOfSamples; i++) {
            for (int j = 0; j < shuffleMap.length; j++) {
                shuffledText[sampleId*sampleSize + j] = textArr[sampleId*sampleSize + shuffleMap[j]];
            }
            sampleId++;
        }

//        System.out.println(text);
//        System.out.println(Arrays.toString(shuffledText));

        //make the shuffled String and return
        String shuffledStr = "";
        for(char letter:shuffledText){
            shuffledStr += letter;
        }
        return shuffledStr;
    }

    private String reversePermute(String text, String key) {

        //make a set using 'key;
        //make a character array from the key
        char[] keyArr = key.toCharArray();

        //make an arraylist and append letters in key only once
        ArrayList<Character> keyLetters = new ArrayList<>();
        for (char letter : keyArr) {
            if (!keyLetters.contains(letter)) {
                keyLetters.add(letter);
            }
        }

        //make the key's letter set an Array
        Object[] keyLetterSetObj = keyLetters.toArray();
        char[] keyLetterSet = new char[keyLetterSetObj.length];
        for (int i = 0; i < keyLetterSet.length; i++) {
            keyLetterSet[i] = (char) keyLetterSetObj[i];
        }

        //determine the text splitting size (sampling size)
        int sampleSize = keyLetterSet.length;

        //make another list of key letter set, which is a sorted version
        char[] sortedKeyLetterSet = new char[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sortedKeyLetterSet[i] = keyLetterSet[i];
        }

        Arrays.sort(sortedKeyLetterSet);

        //define the shuffling map
        int[] shuffleMap = new int[sampleSize];

        //find the index of the unsorted list's elements w.r.t sorted list
        for (int j = 0; j < sampleSize; j++) {
            char letter = keyLetterSet[j];

            for (int i = 0; i < sampleSize; i++) {
                if (sortedKeyLetterSet[i] == letter) {
                    shuffleMap[j] = i;
                    break;
                }
            }
        }

        //a few s-outs for debugging
//        System.out.println("---------------");
//        System.out.println("Key array " + Arrays.toString(keyArr));
//        System.out.println("Key letter Set " + Arrays.toString(keyLetterSet));
//        System.out.println("Key letter Set sorted " + Arrays.toString(sortedKeyLetterSet));
//        System.out.println("shuffle map " + Arrays.toString(shuffleMap));
//        System.out.println("---------------");

        //make a char array out of text
        char[] textArr = text.toCharArray();

        //# of samples and the sampleId is the current processing sample index
        int numOfSamples = textArr.length / sampleSize;
        int sampleId = 0;

        //reverse shuffled char array
        char[] shuffledText = new char[textArr.length];

        //do the reverse shuffle
        for (int i = 0; i < numOfSamples; i++) {
            for (int j = 0; j < shuffleMap.length; j++) {
                shuffledText[sampleId*sampleSize + j] = textArr[sampleId*sampleSize + shuffleMap[j]];
            }
            sampleId++;
        }

//        System.out.println(text);
//        System.out.println(Arrays.toString(shuffledText));

        //make the shuffled String and return
        String shuffledStr = "";
        for(char letter:shuffledText){
            shuffledStr += letter;
        }
        return shuffledStr;
    }

    public String Encrypt(String text, String key) {

        String shifted = shift(text, key);
        System.out.println("Shifted only \n" + shifted);

        String permuted = permute(shifted, key);
        System.out.println("Shifted and permuted \n" + permuted);

        return permuted;
    }

    public String Decrypt(String text, String key) {

        String reversePermuted = reversePermute(text, key);
        System.out.println("only permutation reversed\n" + reversePermuted);

        String reverseShifted = reverseShift(reversePermuted, key);
        //System.out.println("shift also reversed, hopefully original text \n" + reverseShifted);

        return reverseShifted;
    }
}
