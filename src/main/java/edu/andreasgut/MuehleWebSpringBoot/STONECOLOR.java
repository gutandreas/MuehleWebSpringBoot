package edu.andreasgut.MuehleWebSpringBoot;

public enum STONECOLOR {

    BLACK("edu/andreasgut/images/StoneBlack.png",
            "edu/andreasgut/images/HandCursorBlack.png",
            "edu/andreasgut/images/KillCursorBlack.png"),
    WHITE("edu/andreasgut/images/StoneWhite.png",
            "edu/andreasgut/images/HandCursorWhite.png",
            "edu/andreasgut/images/KillCursorWhite.png"),
    ;

    private final String pathStone;
    private final String pathMoveCursor;
    private final String pathKillCursor;

    STONECOLOR(String pathStone, String pathMoveCursor, String pathKillCursor) {
        this.pathStone = pathStone;
        this.pathMoveCursor = pathMoveCursor;
        this.pathKillCursor = pathKillCursor;
    }

    public String getPathStone() {
        return pathStone;
    }

    public String getPathMoveCursor() {
        return pathMoveCursor;
    }

    public String getPathKillCursor() {
        return pathKillCursor;
    }
}