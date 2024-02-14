package com.jonispatented.loot_generator;

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
}
