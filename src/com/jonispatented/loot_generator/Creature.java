package com.jonispatented.loot_generator;

import com.jonispatented.loot_generator.loot.LootItem;
import com.jonispatented.loot_generator.loot.LootTable;
import com.jonispatented.loot_generator.loot.LootYield;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public record Creature(String name, LootTable lootTable, byte level, long copperBudget) {

    public LootYield generateLoot() {
        LootYield.Builder lootYieldBuilder = new LootYield.Builder();

        long currentBudget = copperBudget + new Random().nextLong(-copperBudget / 7, copperBudget / 7);
        long lowerThreshold = lootTable.getLootTableEntries().stream()
                .min(Comparator.comparing(LootItem::copperValue))
                .orElse(new LootItem("ERROR", 0L))
                .copperValue() * 3;
        while (currentBudget > lowerThreshold) {
            LootItem currentItem = lootTable.getLoot();
            if (currentItem.copperValue() > currentBudget)
                continue;
            lootYieldBuilder.addItem(currentItem);
            currentBudget -= currentItem.copperValue();
        }

        return lootYieldBuilder.build();
    }

    public static class Builder {

        private static final Map<Byte, Long> budgetForLevel = new HashMap<>();

        static {
            budgetForLevel.put((byte)3, 2500L);
        }

        private String name;
        private LootTable table;
        private byte level;
        private long copperBudget;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder table(LootTable table) {
            this.table = table;
            return this;
        }

        public Builder level(byte level) {
            this.level = level;
            return this;
        }

        public Builder level(int level) {
            this.level = (byte) level;
            return this;
        }

        public Builder copperBudget(long copperBudget) {
            this.copperBudget = copperBudget;
            return this;
        }

        public Creature build() {
            if (copperBudget == 0)
                copperBudget = budgetForLevel.get(level);
            return new Creature(name, table, level, copperBudget);
        }

    }

}
