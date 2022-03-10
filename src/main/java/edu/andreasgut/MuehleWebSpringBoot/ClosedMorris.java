package edu.andreasgut.MuehleWebSpringBoot;

public class ClosedMorris {

    private final Position firstPosition;
    private final Position secondPosition;
    private final Position thirdPosition;

    public ClosedMorris(Board board, Position firstPosition, Position secondPosition, Position thirdPosition) {
        if (firstPosition.compareTo(secondPosition) > 0 || firstPosition.compareTo(thirdPosition) > 0
                || secondPosition.compareTo(thirdPosition) > 0) {
            throw new IllegalArgumentException();
        }

        if (!(board.isPositionPartOfMorris(firstPosition) && board.isPositionPartOfMorris(secondPosition) && board.isPositionPartOfMorris(thirdPosition))) {
            throw new IllegalArgumentException();
        }

        this.firstPosition = firstPosition;
        this.secondPosition = secondPosition;
        this.thirdPosition = thirdPosition;
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

    @Override
    public String toString() {
        return "Geschlossene Mühle mit den Positionen " + firstPosition.getRing() + "/" + firstPosition.getField() +
                ", " + secondPosition.getRing() + "/" + secondPosition.getField() +
                ", " + thirdPosition.getRing() + "/" + thirdPosition.getField();
    }
}
