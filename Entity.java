package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entity {
	private Animation<TextureRegion> animation;
	private float x;
	private float y;
	private float width;
	private float height;

	
	public Entity(Animation<TextureRegion> animation_, float x_, float y_, float width_, float height_) {
		animation = animation_;
		x = x_;
		y = y_;
		width = width_;
		height = height_;

	}
	
	public float getX() {
		return x;
	}

	public void setX(float xval) {
		x = xval;
	}

	public float getY() {
		return y;
	}
	
	public void setY(float yval) {
		y = yval;
	}

	public TextureRegion getTexture(float stateTime) {
		return animation.getKeyFrame(stateTime, true);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
