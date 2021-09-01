package me.invisibledrax.alliances.truces;

public enum NationRole {
    Leader,
    CoLeader,
    Member;

    public static NationRole fromString(String role) {
        switch (role) {
            case "Leader":
                return Leader;
            case "CoLeader":
                return CoLeader;
            default:
                return Member;
        }
    }

    public String getPrefix() {
        switch (this) {
            case Leader:
                return "**";
            case CoLeader:
                return "*";
            default:
                return "";
        }
    }

}
