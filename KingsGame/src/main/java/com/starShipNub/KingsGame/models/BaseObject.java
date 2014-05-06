package com.starShipNub.KingsGame.models;

public class BaseObject {
	private Integer Id;
	private boolean isKing;
	private Sprite sprite;
	private int x;
	private int y;
	private int position;

	public Integer getId() {
		return Id;
	}

	public void setId(final Integer id) {
		Id = id;
	}

	public boolean isKing() {
		return isKing;
	}

	public void setKing(final boolean isKing) {
		this.isKing = isKing;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(final Sprite sprite) {
		this.sprite = sprite;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(final int position) {
		this.position = position;
	}
}
