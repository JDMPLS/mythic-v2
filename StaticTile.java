package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StaticTile extends Tile {
	private TextureRegion texture;

	public StaticTile(boolean b, TextureRegion te) {
		super(b);
		texture = te;
	}

	@Override
	public TextureRegion getTexture(float st) {
		return texture;
	}

	public void setTexture(TextureRegion t) {
		texture = t;
	}
	
	public Tile clone() {
		return new StaticTile(passable, texture);
	}
}
