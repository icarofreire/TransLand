package org.example.app;


import java.util.*;
import java.util.stream.Collectors;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Tokenizer {

  private String delimiters = " =+-!&*/().,?~:;{}[]<>|^%";
  private String delimiters_grammar = " |";


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

  private boolean isString(String str){
      int fim = str.length();
      return (
      (str.charAt(0) == '"' || str.charAt(0) == '\'') &&
      (str.charAt(fim-1) == '"' || str.charAt(fim-1) == '\'')
      );
  }

  public String getNonTerm(String linha){
      int idx = linha.indexOf("::=");
      if(idx != -1){
        return linha.substring(0, idx).trim();
      }
      return null;
  }

  public String retNomTermProduction(String linha){
      int idx = linha.indexOf("::=");
      if(idx != -1){
        return linha.substring(idx, linha.length()).trim();
      }
      return null;
  }

  public void addHashIfNot(HashMap<String, Integer> map, String key, int value){
    if(!map.containsKey(key)){
      map.put(key, value);
    }
  }

  public void exibirMap(HashMap<String, Integer> mapa){
    for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
        String k = entry.getKey();
        int v = entry.getValue();
        System.out.println(k + " -> " + v);
    }
  }

  public void readFileGrammar(String arquivo_grammar){
        File file = new File(arquivo_grammar);
        if(file.exists()){
          try {
              int numTokenType = 0;
              HashMap<String, Integer> symbolNum = new HashMap<String, Integer>();
              String priLineNonTerm = "";
              Scanner myReader = new Scanner(file);
              while (myReader.hasNextLine()) {
                String linha = myReader.nextLine();
                // System.out.println(linha);

                /*\/ registrar linha do non-terminal; */
                if(!linha.isBlank() && !String.valueOf(linha.trim().charAt(0)).equals("|")){
                  priLineNonTerm = linha;
                }

                /*\/ exibir linha que continua com a primeira linha do non-terminal, ou
                não, apenas continua com a linha completa já registrada; */
                if(!linha.isBlank() && String.valueOf(linha.trim().charAt(0)).equals("|")){
                  /*\/ primeira linha encontrada o non-terminal; e suas continuações de linhas; */
                  System.out.println(priLineNonTerm);
                  System.out.println(linha);

                  String nonTerm = getNonTerm(priLineNonTerm);
                  if(nonTerm != null){
                    numTokenType++;
                    addHashIfNot(symbolNum, nonTerm, numTokenType);
                  }

                  String prodIni = retNomTermProduction(priLineNonTerm);
                  if(prodIni != null){
                    for(String part: tokenizeInput(prodIni)){
                      numTokenType++;
                      addHashIfNot(symbolNum, part, numTokenType);
                    }
                  }
                  
                  for(String part: tokenizeInput(linha)){
                    numTokenType++;
                    addHashIfNot(symbolNum, part, numTokenType);
                  }
                }else{
                  /*\/ linha inteira encontrada o non-terminal, até seu fim; */
                  System.out.println(priLineNonTerm);

                  String nonTerm = getNonTerm(priLineNonTerm);
                  if(nonTerm != null){
                    numTokenType++;
                    symbolNum.put(nonTerm, numTokenType);
                  }

                  String prodIni = retNomTermProduction(priLineNonTerm);
                  if(prodIni != null){
                    for(String part: tokenizeInput(prodIni)){
                      numTokenType++;
                      addHashIfNot(symbolNum, part, numTokenType);
                    }
                  }
                  
                  for(String part: tokenizeInput(priLineNonTerm)){
                    numTokenType++;
                    addHashIfNot(symbolNum, nonTerm, numTokenType);
                  }
                }
                System.out.println("***");
                
              }
              myReader.close();
              exibirMap(symbolNum);
          } catch (FileNotFoundException e) {
              System.out.println("An error occurred.");
              e.printStackTrace();
          }
        }else{ System.out.println("Arquivo nao existe;"); }
  }

  public String getFileInResource(String arquivo){
      String caminho = getClass().getClassLoader().getResource(arquivo).getPath();
      return caminho;
  }

}
