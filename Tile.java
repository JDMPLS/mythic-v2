package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Tile {
	protected boolean passable;
	
	public abstract TextureRegion getTexture(float st);
	public abstract Tile clone();
	
	public Tile(boolean b) {
		passable = b;
	}

	// note that this just returns the passable boolean. it does not check
	// for Entities that might prohibit passage. best practice would be to use
	// a method in the main game class that does this
	public boolean isPassable() {
		return passable;
	}
	
	public void setPassable(boolean b) {
		passable = b;
	}
	
}
