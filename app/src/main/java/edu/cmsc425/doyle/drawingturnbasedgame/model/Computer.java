package edu.cmsc425.doyle.drawingturnbasedgame.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bryan on 4/13/2015.
 */
public class Computer extends Actor {

    public Computer() {
        health = 100;
        dead = false;
        baseDamage = new HashMap<>();
        baseDamage.put(AttackType.BASIC, 15);
    }

    public Computer(int health, Map<AttackType, Integer> baseDamage) {
        this.health = health;
        this.baseDamage = baseDamage;
    }

    @Override
    public int attack(AttackType at) {
        return (int) (baseDamage.get(at) * Math.max(0.5, Math.random()));
    }


}
