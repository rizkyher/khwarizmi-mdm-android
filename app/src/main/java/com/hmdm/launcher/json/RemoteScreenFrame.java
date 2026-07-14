package com.hmdm.launcher.json;

public class RemoteScreenFrame {
    private String imageData;
    private int width;
    private int height;

    public RemoteScreenFrame(String imageData, int width, int height) {
        this.imageData = imageData;
        this.width = width;
        this.height = height;
    }

    public String getImageData() {
        return imageData;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
