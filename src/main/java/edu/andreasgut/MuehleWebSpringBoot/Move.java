package edu.andreasgut.MuehleWebSpringBoot;


public class Move {
    private Position from, to;

    public Move() {
    }

    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public void setFrom(Position from) {
        this.from = from;
    }

    public void setTo(Position to) {
        this.to = to;
    }

    @Override
    public String toString(){
        return "Zug von Feld " + getFrom().getRing() + "/" + getFrom().getField()
                + " nach " + getTo().getRing() + "/" + getTo().getField();
    }
}
