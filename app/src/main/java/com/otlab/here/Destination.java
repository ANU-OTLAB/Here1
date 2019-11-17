package com.otlab.here;

public class Destination {
    private SettingItem.DestinationType destinationType;
    private String[] destinationName;
    private Position[] destinationPosition;

    public Destination(SettingItem.DestinationType destinationType, String destinationName, Position destinationPosition){
        this.destinationName = new String[1];
        this.destinationPosition = new Position[1];
        this.destinationType = destinationType;
        this.destinationName[0] = destinationName;
        this. destinationPosition[0] = destinationPosition;
    }

    public class Position{
        private double x;
        private double y;
        public Position(double x, double y){
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
