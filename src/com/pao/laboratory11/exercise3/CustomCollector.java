package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;

public class CustomCollector {
    private static class Agg {
        Map<String, Long> countryCounts = new HashMap<>();
        Map<String, Long> channelCounts = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Transaction> txs = new ArrayList<>();

        void add(Transaction tx) {
            countryCounts.merge(tx.getCountry(), 1L, Long::sum);
            channelCounts.merge(tx.getChannel(), 1L, Long::sum);
            totalAmount = totalAmount.add(tx.getAmount());
            txs.add(tx);
        }

        Agg merge(Agg other) {
            other.countryCounts.forEach((k, v) -> countryCounts.merge(k, v, Long::sum));
            other.channelCounts.forEach((k, v) -> channelCounts.merge(k, v, Long::sum));
            this.totalAmount = this.totalAmount.add(other.totalAmount);
            this.txs.addAll(other.txs);
            return this;
        }
    }

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        return Collector.of(
                Agg::new,
                Agg::add,
                Agg::merge,
                agg -> {
                    agg.txs.sort(Comparator.comparing(Transaction::getAmount).reversed()
                            .thenComparingInt(Transaction::getId));
                    int limit = Math.min(topN, agg.txs.size());
                    List<Transaction> top = agg.txs.subList(0, limit);

                    return new Snapshot(agg.countryCounts, agg.channelCounts, agg.totalAmount, top);
                },
                Collector.Characteristics.UNORDERED
        );
    }
}