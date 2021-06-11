package dev.galacticraft.api.team.network;

/**
 * Team packet types
 */
public enum PacketType {
    /**
     * When a team is created.
     */
    CREATE,
    /**
     * When a team is deleted/disbanded
     */
    DELETE,
    /**
     * When a team is updated
     */
    UPDATE,
    /**
     * When a player receives an invite to join a team.
     */
    INVITE,
    /**
     * When a player is kicked from a team.
     */
    REMOVE,
}
