class Position {

    constructor(ring, field) {
        this.ring = ring;
        this.field = field;
    }

    getRing() {
        return this.ring;
    }

    getField() {
        return this.field;
    }

    toString(){return "Position " + this.getRing() + "/" + this.getField();
    }

}