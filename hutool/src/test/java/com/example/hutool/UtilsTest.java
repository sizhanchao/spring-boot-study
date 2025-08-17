package com.example.hutool;

import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.SM3;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class UtilsTest {
    @Test
    public void test() {
        StrUtil.isBlank("");

        int[] rainbow = PageUtil.rainbow(1, 10);
        System.out.println(rainbow);

        CharSequence charSequence = PhoneUtil.hideBetween("15109256344");
        System.out.println(charSequence);
    }

    @Test
    public void testSalt() {
        // 生成随机盐
        byte[] salt = SecureRandom.getSeed(32);
        String hashedText = SmUtil.sm3WithSalt(salt).digestHex("zhan");
//        String zhan1 = SmUtil.sm3WithSalt(salt).digestHex("zhan");
        System.out.println(hashedText);

        // 验证阶段
        String inputText = "zhan"; // 要验证的字符串
        byte[] storedSalt = salt; // 从存储中获取的盐值

//        Digester verifier = SecureUtil.sm3WithSalt(storedSalt);
        SM3 verifier = SmUtil.sm3WithSalt(storedSalt);
        String hashedInputText = verifier.digestHex(inputText);
        System.out.println("验证的摘要：" + hashedInputText);

        // 比较摘要是否相同
        if (hashedText.equals(hashedInputText)) {
            System.out.println("验证通过，内容未改变。");
        } else {
            System.out.println("验证失败，内容已改变。");
        }
    }
}
