package com.jonispatented.loot_generator.loot;

import java.util.HashMap;
import java.util.Map;

public class LootYield {

    private final Map<LootItem, Long> lootCount;

    private LootYield() {
        lootCount = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("@~-~@~-~@~-~@~-~@~-~@~-~@~-~@~-~@~-~@~-~@\n\n");

        long totalItemValue = 0;
        for (LootItem item : lootCount.keySet()) {
            long count = lootCount.get(item);
            long value = item.copperValue();
            builder.append("Name: ")
                    .append(item.name()).append('\n')
                    .append("Count: ")
                    .append(count).append('\n')
                    .append("Value (Each): ")
                    .append(value / 100).append(" gp, ")
                    .append(value / 10 % 10).append(" sp, ")
                    .append(value % 10).append(" cp\n");
            long totalValue = value * count;
            builder.append("Value (Total): ")
                    .append(totalValue / 100).append(" gp, ")
                    .append(totalValue / 10 % 10).append(" sp, ")
                    .append(totalValue % 10).append(" cp\n\n");
            totalItemValue += totalValue;
        }

        builder.append("Total Loot Yield Value: ")
                .append(totalItemValue / 100).append(" gp, ")
                .append(totalItemValue / 10 % 10).append(" sp, ")
                .append(totalItemValue % 10).append(" cp\n\n");
        builder.append("@~-~@~-~@~-~@~-~@~-~@~-~@~-~@~-~@~-~@~-~@");
        return builder.toString();
    }

    public static class Builder {

        private final LootYield lootYield;

        public Builder() {
            lootYield = new LootYield();
        }

        public Builder addItem(LootItem lootItem) {
            var lootCount = lootYield.lootCount;
            lootCount.put(lootItem, lootCount.getOrDefault(lootItem, 0L) + 1);
            return this;
        }

        public LootYield build() {
            return lootYield;
        }

    }

}
