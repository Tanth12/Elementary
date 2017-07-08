package src.lessons.lesson1;

import java.text.MessageFormat;

/**
 * Created by tanth on 08.07.17.
 */
public class Room {
    private int height;
    private int width;
    private int longitude;
    private String name;

    public Room(int height, int width, int longitude) {
        this.height = height;
        this.width = width;
        this.longitude = longitude;
    }

    public Room() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSquare(){
        return width * longitude;
    }

    public int getPerimeter(){
        return 2 * (width + longitude);
    }

    public int getSpace() {
        return width * longitude * height;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Room.name={0};Room.square={1};Room.perimeter={2}", name, getSquare(), getPerimeter());
    }
}
