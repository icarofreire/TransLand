package org.example.app;

import java.util.*;

public class Token {
  // The beginning of the token, pointing directly into the source.
  private String identifier;

  // The 1-based line where the token appears.
  private int line;
  
  // The parsed value if the token is a literal.
  private int tokenType;

  /* \/ escrever getters e setters; */

  public Token nextToken(){
    return this;
  }

  public Token getEOFTerminal(){
    return this; // << final token da production;
  }

  public Token asTerminal(){
    return this; // << temporario;
  }

  public String getIdentifier(){
    return identifier;
  }

  public void setIdentifier(String identifier){
    this.identifier = identifier;
  }

  public int getLine(){
    return line;
  }

  public void setLine(int line){
    this.line = line;
  }

  public int getTokenType(){
    return tokenType;
  }

  public void setTokenType(int tokenType){
    this.tokenType = tokenType;
  }
  
}
