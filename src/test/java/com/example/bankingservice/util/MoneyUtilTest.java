package com.example.bankingservice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension .class)
public class MoneyUtilTest {

    @Test
    public void formatCentsAsEuros() {
        assertThat(new BigDecimal("123.45")).isEqualTo(MoneyUtil.formatCentsAsEuros(12345));
        assertThat(new BigDecimal("100.00")).isEqualTo(MoneyUtil.formatCentsAsEuros(10000));
        assertThat(new BigDecimal("9.99")).isEqualTo(MoneyUtil.formatCentsAsEuros(999));
        assertThat(new BigDecimal("0.00")).isEqualTo(MoneyUtil.formatCentsAsEuros(0));
        assertThat(new BigDecimal("0.01")).isEqualTo(MoneyUtil.formatCentsAsEuros(1));
    }

    @Test
    public void convertEurosToCents() {
        assertThat(12345).isEqualTo(MoneyUtil.convertEurosToCents(new BigDecimal("123.45")));
        assertThat(10000).isEqualTo(MoneyUtil.convertEurosToCents(new BigDecimal("100.00")));
        assertThat(999).isEqualTo(MoneyUtil.convertEurosToCents(new BigDecimal("9.99")));
        assertThat(0).isEqualTo(MoneyUtil.convertEurosToCents(new BigDecimal("0.00")));
        assertThat(1).isEqualTo(MoneyUtil.convertEurosToCents(new BigDecimal("0.01")));
    }

    @Test
    public void convertCentsToEuros() {
        assertThat(new BigDecimal("123.45")).isEqualTo(MoneyUtil.convertCentsToEuros(12345));
        assertThat(new BigDecimal("100.00")).isEqualTo(MoneyUtil.convertCentsToEuros(10000));
        assertThat(new BigDecimal("9.99")).isEqualTo(MoneyUtil.convertCentsToEuros(999));
        assertThat(new BigDecimal("0.00")).isEqualTo(MoneyUtil.convertCentsToEuros(0));
        assertThat(new BigDecimal("0.01")).isEqualTo(MoneyUtil.convertCentsToEuros(1));
    }
}