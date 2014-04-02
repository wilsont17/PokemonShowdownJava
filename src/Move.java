
public class Move
{

  private String name;
  private String description;
  private int power;
  private int hitChance;
  
  
  Move(String name, String description, int power, int hitChance)
  {
    this.name = name;
    this.description = description;
    this.power = power;
    this.hitChance = hitChance;
  }
  
  public String toString()
  {
    return name;
  }
  
}
