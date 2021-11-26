package edu.andreasgut.MuehleWebSpringBoot;

public class OpenMorris {

    private Position firstPosition, secondPosition, thirdPosition, gapPosition;

    public OpenMorris(Position firstPosition, Position secondPosition, Position thirdPosition, Position gapPosition) {
        if (firstPosition.compareTo(secondPosition) > 0 || firstPosition.compareTo(thirdPosition) > 0
                || secondPosition.compareTo(thirdPosition) > 0){
            throw new IllegalArgumentException();
        }

        this.firstPosition = firstPosition;
        this.secondPosition = secondPosition;
        this.thirdPosition = thirdPosition;
        this.gapPosition = gapPosition;
    }

    public Position getFirstPosition() {
        return firstPosition;
    }

    public Position getSecondPosition() {
        return secondPosition;
    }

    public Position getThirdPosition() {
        return thirdPosition;
    }

    public Position getGapPosition() {
        return gapPosition;
    }

    @Override
    public String toString(){
        return "Offene Mühle mit den Positionen " + firstPosition.getRing() + "/" + firstPosition.getField() +
                ", " + secondPosition.getRing() + "/" + secondPosition.getField() +
                ", " + thirdPosition.getRing() + "/" + thirdPosition.getField() +
                " und der Lücke an der Position " + gapPosition.getRing() + "/" + gapPosition.getField();
    }
}
