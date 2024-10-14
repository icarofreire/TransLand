package org.example.app;


import java.util.*;
import java.util.stream.Collectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tokenizer {

  /*\/ delimitadores para interpretar o arquivo de código em geral; */
  private String delimiters_code = " =+-!&*/().,?~:;{}[]<>|^%";

  /*\/ delimitadores para interpretar o arquivo de gramatica; */
  private String delimiters_grammar = " |\t()[]?*+";

  /*\/ map com numerações de cada simbolo(nao-terminal e terminal) encontrado na gramatica; */
  private HashMap<String, Integer> symbolNum = new HashMap<String, Integer>();
  /*\/ map com numerações de cada não-terminal encontrado na gramatica; */
  private HashMap<String, Integer> nonTerminals = new HashMap<String, Integer>();


  public List<String> tokenizeInput(String input){
        boolean returnDelims = true;
        StringTokenizer st1 = new StringTokenizer(input, delimiters_grammar, returnDelims);
        List<String> tokens = new ArrayList<>();
        // Condition holds true till there is single token
        // remaining using hasMoreTokens() method
        while (st1.hasMoreTokens()){
            // Getting next tokens
            // System.out.println(st1.nextToken());
            tokens.add(st1.nextToken().trim());
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

  public boolean onlyAlphabets(String str){
      // Return false if the string
      // has empty or null
      if (str == null || str.equals("") && str.isBlank()) {
          return false;
      }

      // Traverse the string from
      // start to end
      for (int i = 0; i < str.length(); i++) {
          // Check if the specified
          // character is not a letter then
          // return false,
          // else return true
          if (!Character.isLetter(str.charAt(i))) {
              return false;
          }
      }
      return true;
  }

  public String getNonTerm(String linha){
      int idx = linha.indexOf("::=");
      int idx2 = linha.indexOf(":");

      if(idx != -1){
        String term = linha.substring(0, idx).trim();
        return (onlyAlphabets(term) ? (term) : (null));
      }else if(idx2 != -1){
        String term = linha.substring(0, idx2).trim();
        return (onlyAlphabets(term) ? (term) : (null));
      }
      return null;
  }

  public boolean addHashIfNot(HashMap<String, Integer> map, String key, int value){
    if(!key.isBlank()){
      key = (key.length() > 1 && isString(key) ? (key.substring(1,key.length()-1)) : (key));
      if(!map.containsKey(key)){
        map.put(key, value);
        return true;
      }
    }
    return false;
  }

  public void exibirMap(HashMap<String, Integer> mapa){
    for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
        String k = entry.getKey();
        int v = entry.getValue();
        System.out.println(k + " -> " + v);
    }
    // System.out.println( "tam-mapa:" + mapa.size() + "; " + mapa.containsKey("return") );
  }

  public void readFileGrammar(String arquivo_grammar){
        File file = new File(arquivo_grammar);
        if(file.exists()){
          try {
              int numTokenType = 0;
              String priLineNonTerm = "";
              Scanner myReader = new Scanner(file);
              while (myReader.hasNextLine()) {
                String linha = myReader.nextLine();

                /*\/ registrar linha que contém o non-terminal; */
                if(!linha.isBlank() && !String.valueOf(linha.trim().charAt(0)).equals("|")){
                  priLineNonTerm = linha;
                }

                /*\/ exibir linha que continua com a primeira linha do non-terminal, ou
                não, apenas continua com a linha completa já registrada; */
                if(!linha.isBlank() && String.valueOf(linha.trim().charAt(0)).equals("|")){
                  /*\/ linha encontrada pertencente a linha do non-terminal; */
                  // System.out.println(priLineNonTerm);
                  // System.out.println(linha);
                  for(String part: tokenizeInput(linha)){
                    if(addHashIfNot(symbolNum, part, numTokenType))numTokenType++;
                  }
                }else{
                  /*\/ linha inteira encontrada o non-terminal, até seu fim; */
                  // System.out.println(priLineNonTerm);

                  /*\/ registrar non-terminals em hash particular; */
                  String nonTerm = getNonTerm(priLineNonTerm);
                  if(nonTerm != null){
                    if(!nonTerminals.containsKey(nonTerm) && symbolNum.containsKey(nonTerm)){
                      nonTerminals.put(nonTerm, symbolNum.get(nonTerm));
                    }
                  }
                  
                  for(String part: tokenizeInput(priLineNonTerm)){
                    if(addHashIfNot(symbolNum, part, numTokenType))numTokenType++;
                    // System.out.println(part);
                  }
                }
                // System.out.println("***");
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
