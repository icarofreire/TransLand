package org.example.app;


import java.util.*;
import java.util.stream.Collectors;

public class Tokenizer {

  private String delimiters = " =+-!&*/().,?~:;{}[]<>|^%";


  public List<String> tokenizeInput(String input){
        StringTokenizer st1 = new StringTokenizer(input, delimiters, true);
        List<String> tokens = new ArrayList<>();
        // Condition holds true till there is single token
        // remaining using hasMoreTokens() method
        while (st1.hasMoreTokens()){
            // Getting next tokens
            // System.out.println(st1.nextToken());
            tokens.add(st1.nextToken());
        }
        return tokens;
  }

}
