package com.jonispatented.loot_generator.loot;

import java.util.Objects;

public record LootItem(String name, long copperValue) {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(name).append('\n')
                .append("Value: ")
                .append(copperValue / 100).append(" gp, ")
                .append(copperValue / 10 % 10).append(" sp, ")
                .append(copperValue % 10).append(" cp");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LootItem lootItem)) return false;
        return copperValue == lootItem.copperValue && Objects.equals(name, lootItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, copperValue);
    }
}
