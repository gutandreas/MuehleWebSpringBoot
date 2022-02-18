class Player{

    constructor(name, uuid, index) {
        this.name = name;
        this.uuid = uuid;
        this.index = index;
    }

    getUuid(){
        return this.uuid;
    }
}