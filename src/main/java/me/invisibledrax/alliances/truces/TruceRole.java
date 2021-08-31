package me.invisibledrax.alliances.truces;

public enum TruceRole {
    Leader,
    Officer,
    Member,
    NonMember;

    public static TruceRole fromString(String role) {
        switch (role) {
            case "Leader":
                return Leader;
            case "Officer":
                return Officer;
            case "Member":
                return Member;
            default:
                return NonMember;
        }
    }

    public String getPrefix() {
        switch (this) {
            case Leader:
                return "**";
            case Officer:
                return "*";
            default:
                return "";
        }
    }

}
