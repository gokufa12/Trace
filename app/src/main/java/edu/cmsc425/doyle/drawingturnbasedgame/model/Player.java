package edu.cmsc425.doyle.drawingturnbasedgame.model;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import edu.cmsc425.doyle.drawingturnbasedgame.R;
import edu.cmsc425.doyle.drawingturnbasedgame.TraceActivity;

/**
 * Created by Bryan on 4/13/2015.
 */
public class Player extends Actor {

    public Player() {
        health = 100;
        dead = false;
        baseDamage = new HashMap<>();
        baseDamage.put(AttackType.BASIC, 30);
        baseDamage.put(AttackType.RANGE, 20);
        baseDamage.put(AttackType.MAGIC, 15);

        traceImages = new HashMap<>();
        traceImages.put(AttackType.BASIC, R.drawable.pentagon);
        traceImages.put(AttackType.RANGE, R.drawable.star);
        traceImages.put(AttackType.MAGIC, R.drawable.circle);
    }

    public Player(int health, Map<AttackType, Integer> baseDamage, Map<AttackType, Integer> images) {
        this.health = health;
        this.baseDamage = baseDamage;
        this.traceImages = images;
    }

    @Override
    public int attack(AttackType at) {
        return baseDamage.get(at);
    }


}
