package com.zhan.learn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
//        Collections.shuffle();
//        System.out.println("Hello world!");

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            // 首先，我们读入所有的数据，将所有的奇数和偶数分别存入两个列表。
            int n = sc.nextInt();
            ArrayList<Integer> odds = new ArrayList<>(); // 能被2整除的数一定是偶数
            ArrayList<Integer> evens = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int num = sc.nextInt();
                if (num % 2 == 0) {
                    evens.add(num);
                } else {
                    odds.add(num);
                }
            }
            // 初始化匹配数组 matcheven，它的下标对应偶数列表中的数字，值对应这个偶数的伴侣（如果有的话）。
            int[] matcheven = new int[evens.size()];
            // 对于每一个奇数，调用 find 函数尝试找到一个与它可以形成素数伴侣的偶数。
            // 如果找到了，那么匹配的数量 count 就加一。
            int count = 0;
            for (Integer odd : odds) {
                if (find(odd, matcheven, evens, new boolean[evens.size()])) {
                    count++;
                }
            }
            // 最后，我们打印出匹配的数量 count，这就是最大的素数伴侣对数。
            System.out.println(count);
        }
    }

    private static boolean find(int x, int[] matcheven, ArrayList<Integer> evens,
                                boolean[] v) {
        // 在 find 函数中，我们遍历所有的偶数，如果找到一个偶数 even，它和当前的奇数 x 之和是素数，并且还没有被访问过，那么我们就尝试匹配它。
        for (int i = 0; i < evens.size(); i++) {
            int even = evens.get(i);
            if (isPrime(x + even) && !v[i]) {
                // 这里的 v[i] 是用来标记偶数 even 是否被访问过的。
                // 如果 even 还没有伴侣，那么我们就直接将它与当前的奇数 x 匹配。
                // 如果 even 已经有伴侣，那么我们就尝试为它的当前伴侣找到一个新的伴侣。
                // 如果找到了，那么我们就将 even 的伴侣更换为当前的奇数 x
                v[i] = true;
                if (matcheven[i] == 0 || find(matcheven[i], matcheven, evens, v)) {
                    matcheven[i] = x;
                    return true;
                }
            }
        }
        return false;
    }
    // 判断一个整数 x 是否是素数
    // 素数是指大于1的自然数中，除了1和它本身以外不再有其他因数的自然数。
    // 所以在这个方法中，首先判断 x 是否小 于等于1或者大于2且为偶数，这些情况下 x 一定不是素数，直接返回 false。
    // 然后，从3开始，步长为2（因为偶数除了2一定不是素数，所以只需要考察奇数），一直到 x 的平方根，如果 x 能被这其中的任何一个数整除，那么 x 就不是素数，返回 false。
    // 如果 x 不能被2到 x 的平方根之间的任何一个数整除，那么 x 就是素数，返回 true。
    // 这个方法在 find 函数中被用来判断一个奇数和一个偶数之和是否是素数，如果是的话，那么这两个数就可以形成一个素数伴侣。
    private static boolean isPrime(int x) {
        if (x <= 1 || (x > 2 && x % 2 == 0)) {
            return false;
        }
        for (int i = 3; i <= Math.sqrt(x); i += 2) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }
}