package com.jonispatented.loot_generator;

public class Main {

    public static void main(String[] args) {

        LootTable lootTable = LootTable.createFromJson("rust_monster");

        for (int i = 0; i < 20; i++) {
            LootItem loot = lootTable.getLoot();
            System.out.println(loot);
        }

    }

}
