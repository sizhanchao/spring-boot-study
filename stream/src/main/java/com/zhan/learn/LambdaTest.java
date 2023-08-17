package com.zhan.learn;

import com.zhan.learn.model.Trader;
import com.zhan.learn.model.Transaction;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LambdaTest {
    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        //(1) 找出2011年发生的所有交易，并按交易额排序（从低到高）
        List<Transaction> collect1 = transactions.stream()
                .filter(e -> e.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        collect1.stream().forEach(System.out::println);
        System.out.println("=============================================================");

        //(2) 交易员都在哪些不同的城市工作过？
        List<String> cities = transactions.stream().map(m -> m.getTrader().getCity()).distinct().collect(Collectors.toList());

        cities.stream().forEach(System.out::println);
        System.out.println("=============================================================");
        //(3) 查找所有来自于剑桥的交易员，并按姓名排序。

        List<Transaction> traderList = transactions.stream()
                .filter(f -> f.getTrader().getCity().equals("Cambridge"))
                .sorted(Comparator.comparing(t -> t.getTrader().getName()))
                .collect(Collectors.toList());
        traderList.stream().forEach(System.out::println);
        System.out.println("=============================================================");


    }
}
