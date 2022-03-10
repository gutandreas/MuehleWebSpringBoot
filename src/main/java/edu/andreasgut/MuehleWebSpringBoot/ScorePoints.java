package edu.andreasgut.MuehleWebSpringBoot;

public class ScorePoints {

    private int ownNumberOfStonesPoints, ownClosedMorrisPoints, ownOpenMorrisPoints, ownTwoStonesTogetherPoints, ownTwoStonesWithGapPoints, ownPossibleMovesPoints, enemyNumberOfStonesPoints, enemyClosedMorrisPoints, enemyOpenMorrisPoints, enemyTwoStonesTogetherPoints, enemyTwoStonesWithGapPoints, enemyPossibleMovesPoints;

    public ScorePoints(int ownNumberOfStonesPoints, int ownClosedMorrisPoints, int ownOpenMorrisPoints, int ownTwoStonesTogetherPoints, int ownTwoStonesWithGapPoints, int ownPossibleMovesPoints, int enemyNumberOfStonesPoints, int enemyClosedMorrisPoints, int enemyOpenMorrisPoints, int enemyTwoStonesTogetherPoints, int enemyTwoStonesWithGapPoints, int enemyPossibleMovesPoints) {
        this.ownNumberOfStonesPoints = ownNumberOfStonesPoints;
        this.ownClosedMorrisPoints = ownClosedMorrisPoints;
        this.ownOpenMorrisPoints = ownOpenMorrisPoints;
        this.ownTwoStonesTogetherPoints = ownTwoStonesTogetherPoints;
        this.ownTwoStonesWithGapPoints = ownTwoStonesWithGapPoints;
        this.ownPossibleMovesPoints = ownPossibleMovesPoints;
        this.enemyNumberOfStonesPoints = enemyNumberOfStonesPoints;
        this.enemyClosedMorrisPoints = enemyClosedMorrisPoints;
        this.enemyOpenMorrisPoints = enemyOpenMorrisPoints;
        this.enemyTwoStonesTogetherPoints = enemyTwoStonesTogetherPoints;
        this.enemyTwoStonesWithGapPoints = enemyTwoStonesWithGapPoints;
        this.enemyPossibleMovesPoints = enemyPossibleMovesPoints;
    }

    public int getOwnNumberOfStonesPoints() {
        return ownNumberOfStonesPoints;
    }

    public int getOwnClosedMorrisPoints() {
        return ownClosedMorrisPoints;
    }

    public int getOwnOpenMorrisPoints() {
        return ownOpenMorrisPoints;
    }

    public int getOwnTwoStonesTogetherPoints() {
        return ownTwoStonesTogetherPoints;
    }

    public int getOwnTwoStonesWithGapPoints() {
        return ownTwoStonesWithGapPoints;
    }

    public int getOwnPossibleMovesPoints() {
        return ownPossibleMovesPoints;
    }

    public int getEnemyNumberOfStonesPoints() {
        return enemyNumberOfStonesPoints;
    }

    public int getEnemyClosedMorrisPoints() {
        return enemyClosedMorrisPoints;
    }

    public int getEnemyOpenMorrisPoints() {
        return enemyOpenMorrisPoints;
    }

    public int getEnemyTwoStonesTogetherPoints() {
        return enemyTwoStonesTogetherPoints;
    }

    public int getEnemyTwoStonesWithGapPoints() {
        return enemyTwoStonesWithGapPoints;
    }

    public int getEnemyPossibleMovesPoints() {
        return enemyPossibleMovesPoints;
    }

    public int getValueByIndex(int index) {
        switch (index) {
            case 0:
                return ownNumberOfStonesPoints;
            case 1:
                return ownClosedMorrisPoints;
            case 2:
                return ownOpenMorrisPoints;
            case 3:
                return ownTwoStonesTogetherPoints;
            case 4:
                return ownTwoStonesWithGapPoints;
            case 5:
                return ownPossibleMovesPoints;
            case 6:
                return enemyNumberOfStonesPoints;
            case 7:
                return enemyClosedMorrisPoints;
            case 8:
                return enemyOpenMorrisPoints;
            case 9:
                return enemyTwoStonesTogetherPoints;
            case 10:
                return enemyTwoStonesWithGapPoints;
            case 11:
                return enemyPossibleMovesPoints;
            default:
                return 0;
        }
    }

    public void setValueByIndex(int index, int value) {
        switch (index) {
            case 0:
                ownNumberOfStonesPoints = value;
                break;
            case 1:
                ownClosedMorrisPoints = value;
                break;
            case 2:
                ownOpenMorrisPoints = value;
                break;
            case 3:
                ownTwoStonesTogetherPoints = value;
                break;
            case 4:
                ownTwoStonesWithGapPoints = value;
                break;
            case 5:
                ownPossibleMovesPoints = value;
                break;
            case 6:
                enemyNumberOfStonesPoints = value;
                break;
            case 7:
                enemyClosedMorrisPoints = value;
                break;
            case 8:
                enemyOpenMorrisPoints = value;
                break;
            case 9:
                enemyTwoStonesTogetherPoints = value;
                break;
            case 10:
                enemyTwoStonesWithGapPoints = value;
                break;
            case 11:
                enemyPossibleMovesPoints = value;
                break;

        }
    }

    @Override
    public String toString() {
        String stringRepresentation = "Punkte: ";
        for (int i = 0; i < 12; i++) {
            stringRepresentation += getValueByIndex(i) + " / ";
        }
        stringRepresentation = stringRepresentation.substring(0, stringRepresentation.length() - 2);
        return stringRepresentation;
    }
}
