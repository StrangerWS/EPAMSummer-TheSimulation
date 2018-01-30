package boot.util;

public enum Type {
    Homeless(5, 1, 0, (byte) 50, (byte) 4, (byte) 5, new byte[]{0, 0, 0, 0, 0}),
    Thug(7, 2, 0, (byte) 40, (byte) 4, (byte) 5, new byte[]{0, 0, 0, 0, 0}),
    Nerd(3, 1, 0, (byte) 30, (byte) 4, (byte) 15, new byte[]{0, 0, 0, 0, 0}),
    Player(5, 1, 0, (byte) 45, (byte) 4, (byte) 10, new byte[]{0, 0, 0, 0, 0});

    public float hp;
    public float dmg;
    public float psy;
    public byte accuracy;
    public byte spd;
    public byte luck;
    public byte[] resistance;

    Type(float hp, float dmg, float psy, byte accuracy, byte spd, byte luck, byte[] resistance) {
        this.hp = hp;
        this.dmg = dmg;
        this.psy = psy;
        this.accuracy = accuracy;
        this.spd = spd;
        this.luck = luck;
        this.resistance = resistance;
    }
}
