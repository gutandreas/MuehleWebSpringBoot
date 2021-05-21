package edu.andreasgut.MuehleWebSpringBoot;



public class Position implements Comparable {

    private int ring, field;

    public Position() {
    }

    public Position(int ring, int field) {
        this.ring = ring;
        this.field = field;
    }

    public int getRing() {
        return ring;
    }

    public int getField() {
        return field;
    }

    public void setRing(int ring) {
        this.ring = ring;
    }

    public void setField(int field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object o){
        Position position = (Position) o;
        return ring == position.getRing() && field == position.getField();
    }


    @Override
    public int compareTo(Object o) {
        Position position = (Position) o;
        if (ring == position.getRing() && field == position.getField()) return 0;
        if (ring == position.getRing() && field > position.getField()) return 1;
        if (ring > position.getRing()) return 1;
        return -1;
    }

    @Override
    public String toString(){
        return "Position " + getRing() + "/" + getField();
    }
}
