package boot.model;

import boot.util.Type;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Enemy {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected int id;

    protected Type type;
    protected float baseHP;
    protected float curHP;
    protected float baseDMG;
    protected float basePSY;
    protected byte level;
    protected byte accuracy;
    protected byte speed;
    protected byte luck;
    protected byte[] resistance; // 0 - fire, 1 - ice, 2 - electricity, 3 - psy, 4 - dmg

    public Enemy() {
    }

    public Enemy(Type type, byte level) {
        this.type = type;
        this.level = level;
        this.baseHP = type.hp + level * 2 ;
        curHP = baseHP;
        this.baseDMG = type.dmg + level;
        this.basePSY = type.psy;
        this.accuracy = (accuracy + level * 2 > 100) ? 100 : type.accuracy;
        this.speed = type.spd;
        this.luck = type.luck;
        this.resistance = type.resistance;
    }

    public Type getType() {
        return type;
    }

    public float getBaseHP() {
        return baseHP;
    }

    public float getBaseDMG() {
        return baseDMG;
    }

    public float getBasePSY() {
        return basePSY;
    }

    public byte getLevel() {
        return level;
    }

    public byte getAccuracy() {
        return accuracy;
    }

    public byte getSpeed() {
        return speed;
    }

    public byte getLuck() {
        return luck;
    }

    public byte[] getResistance() {
        return resistance;
    }

    public float getCurHP() {
        return curHP;
    }

    public void getDamage(float damage){
        curHP -= damage;
    }

    public float dealDamage(){
        return baseDMG;
    }

    public boolean isDead(){
        return curHP <= 0;
    }

    public int getId() {
        return id;
    }
    public String toString() {
        return "Enemy = " +  type.name() +
                "\n HP = " + curHP +
                "/" + baseHP +
                "\n baseDMG =" + baseDMG +
                "\n basePSY =" + basePSY +
                "\n level =" + level +
                "\n accuracy =" + accuracy +
                "\n speed =" + speed +
                "\n luck =" + luck +
                "\n resistances =" + Arrays.toString(resistance);
    }
}
