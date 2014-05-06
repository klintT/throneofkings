package com.starShipNub.KingsGame.models;

import java.awt.Image;
import java.util.List;

public class Sprite {
	// Does not need an animation class since the images are just passed to a
	// client to be animated
	List<Image> images;

	public List<Image> getImages() {
		return images;
	}

	public void setImages(final List<Image> images) {
		this.images = images;
	}
}
