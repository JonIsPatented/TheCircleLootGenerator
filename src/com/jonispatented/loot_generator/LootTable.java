package com.jonispatented.loot_generator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTable {

    private List<Entry> lootTableEntries = new ArrayList<>();

    public LootItem getLoot() {
        long totalWeight = lootTableEntries.stream().map(Entry::getWeight).reduce(0L, Long::sum);
        long randomWeightIndex = new Random().nextLong(totalWeight);
        for (Entry entry : lootTableEntries) {
            randomWeightIndex -= entry.getWeight();
            if (randomWeightIndex < 0)
                return entry.getLoot();
        }
        return null;
    }

    public static LootTable createFromJson(String path) {
        JSONParser parser = new JSONParser();
        JSONObject lootTableObject;
        try {
            lootTableObject = (JSONObject) parser.parse(
                    new FileReader("res/tables/" + path + ".json")
            );
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new LootTable();
        }

        return parseTable(lootTableObject);
    }

    private static LootTable parseTable(JSONObject lootTableObject) {
        JSONArray lootTableEntries = (JSONArray) lootTableObject.get("entries");
        LootTable table = new LootTable();

        // If there is a base for the table, add all entries from that table into this one
        // Since pools are just internal tables, this most often gets called on recursive calls in pools
        String baseName = (String) lootTableObject.get("base");
        if (baseName != null)
            table.lootTableEntries.addAll(createFromJson(baseName).lootTableEntries);

        // Determine if each entry is an item or a pool and parse accordingly
        for (Object o : lootTableEntries) {
            JSONObject entry = (JSONObject) o;
            if ("pool".equals(entry.get("type"))) {
                PoolEntry tableEntryToAdd = new PoolEntry();
                tableEntryToAdd.weight = (long) entry.get("weight");
                tableEntryToAdd.lootTable = parseTable(entry);
                table.lootTableEntries.add(tableEntryToAdd);
            }
            else if ("item".equals(entry.get("type"))) {
                ItemEntry tableEntryToAdd = new ItemEntry();
                tableEntryToAdd.weight = (long) entry.get("weight");
                tableEntryToAdd.lootItem = new LootItem(
                        (String) entry.get("name"),
                        (long) entry.get("copper_value")
                );
                table.lootTableEntries.add(tableEntryToAdd);
            }
        }

        return table;
    }

    private interface Entry {
        LootItem getLoot();
        long getWeight();
    }

    private static class PoolEntry implements Entry {

        public LootTable lootTable;
        public long weight;

        @Override
        public LootItem getLoot() {
            return lootTable.getLoot();
        }

        @Override
        public long getWeight() {
            return weight;
        }

    }

    private static class ItemEntry implements Entry {

        public LootItem lootItem;
        public long weight;

        @Override
        public LootItem getLoot() {
            return lootItem;
        }

        @Override
        public long getWeight() {
            return weight;
        }

    }

}
