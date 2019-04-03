package com.gmail.cactuscata.mq.mana.spells;

public abstract class Spells {

	public abstract String getName();
	
	public abstract int manaCost();
	
	public abstract long timeWait();
	
	public abstract int getSpellLevel();
	
	public abstract void action();
	
}
