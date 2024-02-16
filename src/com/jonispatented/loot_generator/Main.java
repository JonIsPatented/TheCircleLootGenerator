package com.jonispatented.loot_generator;

public class Main {

    public static void main(String[] args) {

        Creature monster = Creature.createFromJson("rust_monster");

        System.out.println(monster.generateLoot());

    }

}
