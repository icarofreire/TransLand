package org.example.app;


import java.util.*;
import java.util.stream.Collectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

  /*\/ delimitadores para interpretar o arquivo de código em geral; */
  private String delimiters_code = " =+-!&*/().,?~:;{}[]<>|^%";

  /*\/ delimitadores para interpretar o arquivo de gramatica; */
  private String delimiters_grammar = " |\t()[]?*+'\"";

  /*\/ map com numerações de cada simbolo(nao-terminal e terminal) encontrado na gramatica; */
  private HashMap<String, Integer> symbolNum = new HashMap<String, Integer>();
  /*\/ map com numerações de cada não-terminal encontrado na gramatica; */
  private HashMap<String, Integer> nonTerminals = new HashMap<String, Integer>();
  /*\/ gramatica; */
  private HashMap<String, List<String>> grammar = new HashMap<String, List<String>>();


  public List<String> tokenizeInput(String input, String delimiters){
        boolean returnDelims = true;
        StringTokenizer st1 = new StringTokenizer(input, delimiters, returnDelims);
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

  public List<String> tokenizeInputForGrammar(String input){
      return tokenizeInput(input, this.delimiters_grammar);
  }

  public List<String> tokenizeInputForCode(String input){
      return tokenizeInput(input, this.delimiters_code);
  }

  private boolean isString(String str){
      int fim = str.length();
      return (
      (str.charAt(0) == '"' || str.charAt(0) == '\'') &&
      (str.charAt(fim-1) == '"' || str.charAt(fim-1) == '\'')
      );
  }

  private boolean onlyAlphabets(String str){
      if (str == null || str.equals("") && str.isBlank()) {
          return false;
      }
      Pattern pattern = Pattern.compile("([a-zA-Z_]+)", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(str);
      boolean matchFound = matcher.find();
      return (matchFound);
  }

  private String getNonTerm(String linha){
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

  private String getProduction(String linha){
      int idx = linha.indexOf("::=");
      int idx2 = linha.indexOf(":");

      if(idx != -1){
        String term = linha.substring(idx+3, linha.length()).trim();
        return (onlyAlphabets(term) ? (term) : (null));
      }else if(idx2 != -1){
        String term = linha.substring(idx2+1, linha.length()).trim();
        return (onlyAlphabets(term) ? (term) : (null));
      }
      return null;
  }

  private boolean addHashIfNot(HashMap<String, Integer> map, String key, int value){
    if(!key.isBlank()){
      key = (key.length() > 1 && isString(key) ? (key.substring(1,key.length()-1)) : (key));
      if(!map.containsKey(key)){
        map.put(key, value);
        return true;
      }
    }
    return false;
  }

  private void exibirMap(HashMap<String, Integer> mapa){
    for (Map.Entry<String, Integer> entry : mapa.entrySet()) {
        String k = entry.getKey();
        int v = entry.getValue();
        System.out.println(k + " -> " + v);
    }
    // System.out.println( "tam-mapa:" + mapa.size() + "; " + mapa.containsKey("return") );
  }

  private void exibirGrammar(){
    for (Map.Entry<String, List<String>> entry : grammar.entrySet()) {
        String k = entry.getKey();
        List<String> v = entry.getValue();
        System.out.println(k + " -> " + v.stream().collect(Collectors.joining()));
    }
  }

  public void readFileGrammar(String arquivo_grammar){
        arquivo_grammar = getFileInResource(arquivo_grammar);
        File file = new File(arquivo_grammar);
        if(file.exists()){
          try {
              int numTokenType = 0;
              String priLineNonTerm = "";
              Scanner myReader = new Scanner(file);
              while (myReader.hasNextLine()) {
                String linha = myReader.nextLine();

                boolean comment = (!linha.isBlank() && String.valueOf(linha.trim().charAt(0)).equals("#"));
                boolean production = (!comment && !linha.isBlank() && !String.valueOf(linha.trim().charAt(0)).equals("|"));
                boolean continue_production = (!comment && !linha.isBlank() && String.valueOf(linha.trim().charAt(0)).equals("|"));

                /*\/ registrar linha que contém o non-terminal; */
                if(production){
                  priLineNonTerm = linha;
                }

                /*\/ exibir linha que continua com a primeira linha do non-terminal, ou
                não, apenas continua com a linha completa já registrada; */
                if(continue_production){
                  /*\/ linha encontrada pertencente a linha do non-terminal; */
                  // System.out.println(priLineNonTerm);
                  // System.out.println(linha);
                  for(String part: tokenizeInputForGrammar(linha)){
                    if(addHashIfNot(symbolNum, part, numTokenType))numTokenType++;
                  }

                  /*\/ registra cada production de um não-terminal; */
                  String nonTerm = getNonTerm(priLineNonTerm);
                  if(nonTerm != null){
                    if(grammar.containsKey(nonTerm)){
                      /*\/ incrementar mais tokens a production; */
                      grammar.get(nonTerm).addAll(tokenizeInputForGrammar(linha));
                    }else{
                      grammar.put(nonTerm, tokenizeInputForGrammar(linha));
                    }
                  }

                }else if(production){
                  /*\/ linha inteira encontrada o non-terminal, até seu fim; */
                  // System.out.println(priLineNonTerm);

                  /*\/ registrar non-terminals em hash particular; */
                  String nonTerm = getNonTerm(linha);
                  if(nonTerm != null){
                    if(!nonTerminals.containsKey(nonTerm) && symbolNum.containsKey(nonTerm)){
                      nonTerminals.put(nonTerm, symbolNum.get(nonTerm));
                    }

                    /*\/ registra cada production de um não-terminal; */
                    String prod = getProduction(linha);
                    if(prod != null){
                      if(!grammar.containsKey(nonTerm)){
                        grammar.put(nonTerm, tokenizeInputForGrammar(prod));
                      }
                    }
                  }
                  
                  for(String part: tokenizeInputForGrammar(linha)){
                    if(addHashIfNot(symbolNum, part, numTokenType))numTokenType++;
                  }
                }
                // System.out.println("***");
              }
              myReader.close();
              // exibirMap(symbolNum);
              // exibirMap(nonTerminals);
              // exibirGrammar();
          } catch (FileNotFoundException e) {
              System.out.println("An error occurred.");
              e.printStackTrace();
          }
        }else{ System.out.println("Arquivo nao existe;"); }
  }

  /*\/ encontrar caminho do arquivo na pasta resource; */
  private String getFileInResource(String arquivo){
      String caminho = getClass().getClassLoader().getResource(arquivo).getPath();
      return caminho;
  }

  public HashMap<String, Integer> getSymbolNum(){
    return symbolNum;
  }

  public HashMap<String, Integer> getNonTerminals(){
    return nonTerminals;
  }

  public HashMap<String, List<String>> getGrammar(){
    return grammar;
  }

}
