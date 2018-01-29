package boot.model;

import boot.util.Type;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Player extends Enemy {

    private String name;
    private int currentXP = 0;

    public Player(Type type, byte level) {
        super(type, level);
    }

    public Player() {
        super();
    }

    public Player(String name) {
        super(Type.Player, (byte) 1);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public void getLevelUp() {
        currentXP -= 120 + 3 * (level - 1);
        baseDMG++;
        baseHP += 2;
        if (accuracy != 100) accuracy += 2;
    }

    public void getXP(int XP) {
        currentXP += XP;
    }

    public void levelUpHP() {
        baseHP++;
    }

    public void levelUpDMG() {
        baseDMG++;
    }

    public boolean levelUpLuck() {
        if (luck < 100) {
            luck++;
            return true;
        }
        return false;
    }

    public boolean levelUpAccuracy() {
        if (accuracy < 100) {
            accuracy++;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Player name = " + name +
                "\n currentXP = " + currentXP +
                "\n HP = " + curHP + "/" + baseHP +
                "\n baseDMG = " + baseDMG +
                "\n basePSY = " + basePSY +
                "\n level = " + level +
                "\n accuracy = " + accuracy +
                "\n speed = " + speed +
                "\n luck = " + luck +
                "\n resistances = " + Arrays.toString(resistance);
    }
}
