package kolesov.maxim.common.dto;

public enum MessageAction {

    // FROM SERVER
    CONNECTED, // payload -> id: uuid

    STATE, // payload -> map: int[][], currentPlayer: uuid, player

    WIN, // payload -> user: uuid

    // TO SERVER
    DISCONNECT,

    MOVE, // payload -> x,y: int

    READY, //payload = null

}
