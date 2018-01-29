package boot.util;

public enum Location {
    Street((byte) 55, (byte)75, 3),
    Garages((byte) 75, (byte) 60, 4),
    CriminalDistrict((byte) 90, (byte) 35, 2);


    public byte encounterChance;
    public byte evadeChance;
    public int steps;

    Location(byte encounterChance, byte evadeChance, int steps) {
        this.encounterChance = encounterChance;
        this.evadeChance = evadeChance;
        this.steps = steps;
    }
}
