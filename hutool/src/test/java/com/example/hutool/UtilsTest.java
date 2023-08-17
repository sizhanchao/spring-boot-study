package com.example.hutool;

import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

public class UtilsTest {
    @Test
    public void test() {
        StrUtil.isBlank("");

        int[] rainbow = PageUtil.rainbow(1, 10);
        System.out.println(rainbow);

        CharSequence charSequence = PhoneUtil.hideBetween("15109256344");
        System.out.println(charSequence);
    }
}
