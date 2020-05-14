package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedTile extends Tile {
	private Animation<TextureRegion> animation;

	public AnimatedTile(boolean b, Animation<TextureRegion> an) {
		super(b);
		animation = an;
	}

	

	@Override
	public TextureRegion getTexture(float st) {
		return animation.getKeyFrame(st, true);
	}
	
	public void setTexture(Animation<TextureRegion> a) {
		animation = a;
	}



	@Override
	public Tile clone() {
		return new AnimatedTile(passable, animation);
	}
}