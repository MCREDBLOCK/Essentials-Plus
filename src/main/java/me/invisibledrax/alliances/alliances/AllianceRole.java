package me.invisibledrax.alliances.alliances;

public enum AllianceRole {
    Leader,
    Officer,
    Member,
    NonMember;

    public static AllianceRole fromString(String role) {
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
