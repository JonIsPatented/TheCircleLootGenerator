package com.jonispatented.loot_generator;

import com.jonispatented.loot_generator.loot.LootTable;

public class Main {

    public static void main(String[] args) {

        Creature rustMonster = new Creature.Builder()
                .name("Rust Monster").level((byte)3)
                .table(LootTable.createFromJson("rust_monster"))
                .build();

        System.out.println(rustMonster.generateLoot());

    }

}
