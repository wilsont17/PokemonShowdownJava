import java.util.ArrayList;

import javax.swing.ImageIcon;


public class Pokemon 
{
	private int maxHP;
	private int HP;
	private int attack;
	private int defense;
	private int spAttack;
	private int spDefense;
	private int speed;
	private int ID;
	private String name;
	private ArrayList<String> statusEffects;
	private ArrayList<Move> moves;
	
	ArrayList<Move> possibleMoves;
	
	Pokemon(String name, int hp, int attack, int defense, int spAttack, int spDefense, int speed)
	{
		this.name = name;
		this.maxHP = hp;
		this.HP = hp;
		this.attack = attack;
		this.defense = defense;
		this.spAttack = spAttack;
		this.spDefense = spDefense;
		this.speed = speed;
		statusEffects = new ArrayList<String>();
		moves = new ArrayList<Move>();  //load in moves when pokemon class is updated
	}
	
	public void modifyHP(int amount)
	{
		this.HP+=amount;
	}
	
	public void modifyAttack(int amount)
	{
		this.attack+=amount;
	}
	
	public void modifyDefense(int amount)
	{
		this.defense+=amount;
	}
	
	public void modifySpAttack(int amount)
	{
		this.spAttack+=amount;
	}
	
	public void modifySpDefense(int amount)
	{
		this.spDefense+=amount;
	}
	
	public void modifySpeed(int amount)
	{
		this.speed+=amount;
	}
	
	public int getMaxHP()
	{
		return this.maxHP;
	}
	
	public int getHP()
	{
		return this.HP;
	}
	
	public int getAttack()
	{
		return this.attack;
	}
	
	public int getDefense()
	{
		return this.defense;
	}
	
	public int getSpAttack()
	{
		return this.spAttack;
	}

	public int getSpDefense()
	{
		return this.spDefense;
	}
	
	public int getSpeed()
	{
		return this.speed;
	}
	
	public ArrayList<String> getStatusEffects()
	{
	  return statusEffects;
	}
	
	public ArrayList<Move> getMoveSet ()
	{
	  return moves;
	}
	
	public ImageIcon getImg ()
	{
	  //TODO return the img of the pokemon
	}
	
	public void setMoves()
	{
	  //TODO get 4 random moves and add it to the pokemon
	}
	
	public String toString()
	{
	  String temp = "";
	  temp+=name;
	  return temp;
	}
}
