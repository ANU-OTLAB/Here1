package com.otlab.here;

import java.io.Serializable;

public class Destination implements Serializable {
    private SettingItem.DestinationType destinationType;
    private String destinationName;
    private Position destinationPosition;

    public Destination(SettingItem.DestinationType destinationType, String destinationName, Position destinationPosition) {
        this.destinationType = destinationType;
        this.destinationName = destinationName;
        this.destinationPosition = destinationPosition;
    }

    public Destination(SettingItem.DestinationType destinationType, String destinationName, double x, double y) {
        this.destinationType = destinationType;
        this.destinationName = destinationName;
        this.destinationPosition = new Position(x, y);
    }

    public SettingItem.DestinationType getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(SettingItem.DestinationType destinationType) {
        this.destinationType = destinationType;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }

    public void setDestinationPosition(Position destinationPosition) {
        this.destinationPosition = destinationPosition;
    }

    public boolean isPlace(){return SettingItem.DestinationType.PLACE==destinationType;}
    public boolean isPerson(){return SettingItem.DestinationType.PERSON==destinationType;}

    public class Position implements Serializable{
        private double x;
        private double y;

        public Position(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
        public void SetPosition(Position position){
            this.x = position.getX();
            this.y = position.getY();
        }
    }
}
