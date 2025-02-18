package com.example.cylife;

public class Club {
  private final String name;
  private String id;
  private String buttonText;

  public Club(String name, String id) {
    this.id = id;
    this.name = name;
    this.buttonText = "Join";
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public String getButtonText() {
    return buttonText;
  }

  public void setButtonText(String buttonText) {
    this.buttonText = buttonText;
  }
}
