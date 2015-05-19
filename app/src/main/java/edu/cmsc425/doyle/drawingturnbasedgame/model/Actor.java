package edu.cmsc425.doyle.drawingturnbasedgame.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Map;

/**
 * Created by Bryan on 4/13/2015.
 */
public abstract class Actor {
    public int health;
    public boolean dead;
    public int characterImageId;

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Map<AttackType, Integer> getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(Map<AttackType, Integer> baseDamage) {
        this.baseDamage = baseDamage;
    }

    public int getCharacterImageId() {

        return characterImageId;
    }

    public void setCharacterImageId(int characterImageId) {
        this.characterImageId = characterImageId;
    }

    public Map<AttackType, Integer> baseDamage;
    public Map<AttackType, Integer> traceImages;

    public Map<AttackType, Integer> getTraceImages() {
        return traceImages;
    }

    public void setTraceImages(Map<AttackType, Integer> traceImages) {
        this.traceImages = traceImages;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            Log.d("Health", "Less than 0");
            dead = true;
        }
    };

    public int getHealth() {
        return health;
    }

    /**
     * Returns the base damage of the attack
     * @return
     */
    public abstract int attack(AttackType at);

    public boolean isDead() {
        return dead;
    };

    public int attackImage(AttackType at) { return traceImages.get(at); }

}
