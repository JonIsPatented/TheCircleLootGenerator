package com.jonispatented.loot_generator;

import com.jonispatented.loot_generator.loot.LootItem;
import com.jonispatented.loot_generator.loot.LootTable;
import com.jonispatented.loot_generator.loot.LootYield;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
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

    public static Creature createFromJson(String path) {

        Creature.Builder builder = new Creature.Builder();
        JSONParser parser = new JSONParser();
        JSONObject creatureObject;

        try(FileReader creatureReader = new FileReader("res/creatures/" + path + ".json")) {
            creatureObject = (JSONObject) parser.parse(creatureReader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return builder.build();
        }

        String name = (String) creatureObject.get("name");
        if (name != null)
            builder.name(name);

        String lootTable = (String) creatureObject.get("loot_table");
        if (lootTable != null)
            builder.table(LootTable.createFromJson(lootTable));

        byte level = (byte)(long) creatureObject.get("level");
        builder.level(level);

        Long budget = (Long) creatureObject.get("copper_budget");
        if (budget != null)
            builder.copperBudget(budget);

        return builder.build();

    }

    public static class Builder {

        private static final Map<Byte, Long> budgetForLevel = new HashMap<>();

        static {
            budgetForLevel.put((byte)-1, 650L);
            budgetForLevel.put((byte)0, 900L);
            budgetForLevel.put((byte)1, 1300L);
            budgetForLevel.put((byte)2, 2200L);
            budgetForLevel.put((byte)3, 3000L);
            budgetForLevel.put((byte)4, 5000L);
            budgetForLevel.put((byte)5, 8000L);
            budgetForLevel.put((byte)6, 12500L);
            budgetForLevel.put((byte)7, 18000L);
            budgetForLevel.put((byte)8, 25000L);
            budgetForLevel.put((byte)9, 36000L);
            budgetForLevel.put((byte)10, 50000L);
            budgetForLevel.put((byte)11, 72000L);
            budgetForLevel.put((byte)12, 103000L);
            budgetForLevel.put((byte)13, 156000L);
            budgetForLevel.put((byte)14, 230000L);
            budgetForLevel.put((byte)15, 340000L);
            budgetForLevel.put((byte)16, 515000L);
            budgetForLevel.put((byte)17, 800000L);
            budgetForLevel.put((byte)18, 1300000L);
            budgetForLevel.put((byte)19, 2250000L);
            budgetForLevel.put((byte)20, 3000000L);
            budgetForLevel.put((byte)21, 4500000L);
            budgetForLevel.put((byte)22, 6000000L);
            budgetForLevel.put((byte)23, 9000000L);
            budgetForLevel.put((byte)24, 12000000L);
            budgetForLevel.put((byte)25, 18000000L);
        }

        private String name;
        private LootTable table;
        private byte level;
        private long copperBudget;

        public Builder() {
            name = "Default";
            copperBudget = -1;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder table(LootTable table) {
            this.table = table;
            return this;
        }

        public Builder level(byte level) {
            if (level < -1 || level > 25)
                return this;
            this.level = level;
            return this;
        }

        public Builder level(int level) {
            return level((byte)level);
        }

        public Builder copperBudget(long copperBudget) {
            this.copperBudget = copperBudget;
            return this;
        }

        public Creature build() {
            if (copperBudget == -1)
                copperBudget = budgetForLevel.get(level);
            return new Creature(name, table, level, copperBudget);
        }

    }

}
