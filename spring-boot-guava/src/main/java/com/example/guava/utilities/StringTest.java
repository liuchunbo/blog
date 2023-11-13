package com.example.guava.utilities;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

/**
 * Creat by liuchunbo 2023/7/2
 */
public class StringTest {

    /**
     * Strings.emptyToNull()：将空字符串转换成 null。
     * Strings.nullToEmpty()：将 null转换成 空字符串；若不为null，则原值返回。
     * Strings.commonPrefix()/commonSuffix()：返回多个字符串 的公共前缀 / 公共后缀
     * Strings.repeat(string, n)：将字符串 string 重复3次，拼接后返回
     * Strings.isNullOrEmpty(string)：字符串是否为空字符串或者null
     * Strings.padStart(string,length,fixChar)：字符串长度达不到length时，前面使用Char补充
     * Strings.padEnd(string,length,fixChar)：字符串长度达不到length时，后面使用Char补充
     */
    @Test
    public void testStringsMethod() {
        assertThat(Strings.emptyToNull(""), nullValue());
        assertThat(Strings.nullToEmpty(null), equalTo(""));
        assertThat(Strings.nullToEmpty("hello"), equalTo("hello"));
        assertThat(Strings.commonPrefix("Hello", "Heat"), equalTo("He"));
        assertThat(Strings.commonSuffix("Hello", "Echo"), equalTo("o"));
        assertThat(Strings.repeat("Alex", 3), equalTo("AlexAlexAlex"));
        assertThat(Strings.isNullOrEmpty(""), equalTo(true));
        assertThat(Strings.padStart("Alex", 3, 'H'), equalTo("Alex"));
        assertThat(Strings.padStart("Alex", 7, 'H'), equalTo("HHHAlex"));
        assertThat(Strings.padEnd("Alex", 5, 'H'), equalTo("AlexH"));
    }

    @Test
    public void testCharsets() {
        Charset charset = StandardCharsets.UTF_8;
        assertThat(Charsets.UTF_8, equalTo(charset));
    }


    @Test
    public void testCharMatcher() {
        assertThat(CharMatcher.javaDigit().matches('5'), equalTo(true));
        assertThat(CharMatcher.javaDigit().matches('x'), equalTo(false));
        // 字符串中 有多少个 大写的 A
        assertThat(CharMatcher.is('A').countIn("Alex Sharing the Google Guava to Us"), equalTo(1));
        // 将字符串中的 空格 替换成 指定 符号
        assertThat(CharMatcher.breakingWhitespace().collapseFrom("     hello guava         ", '*'), equalTo("*hello*guava*"));
        assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).removeFrom("Hello 234 World JustTest 56"),equalTo("HelloWorldJustTest"));
    }



}
