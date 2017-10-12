package network.Server;

/**
 * Used to specify what to write to the first byte of a message.
 * Depending on the first byte the receiver of the message will
 * act differently.
 */
public enum MessageType {
    TestMessage,
    ChatMessage,
    WhisperMessage,
    SetNameMessage,
    GameSendPlayersMessage,
    GameReadyMessage,
    ClientSendPlayerMessage,
    GameSendEndTurnMessage,
    GameStartMessage,
    GameEndMessage,
    GameCharacterMoveMessage,
    GameSendMapMessage,
}
