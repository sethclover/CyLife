package com.example.cylife;

public class Club {
  private final String name;
  private String id;

  public Club(String name, String id) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }
}
