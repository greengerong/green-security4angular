package com.github.greengerong;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TokenFromTest {
    @Test
    public void testIncludeParamAndHeaderWhenItIsAll() throws Exception {
        assertThat(TokenFrom.Header.include(TokenFrom.All), is(true));
        assertThat(TokenFrom.Param.include(TokenFrom.All), is(true));
        assertThat(TokenFrom.Header.include(TokenFrom.Param), is(false));
        assertThat(TokenFrom.Param.include(TokenFrom.Header), is(false));
        assertThat(TokenFrom.Header.include(TokenFrom.Header), is(true));
        assertThat(TokenFrom.Param.include(TokenFrom.Param), is(true));
    }
}
