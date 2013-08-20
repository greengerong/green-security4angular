package com.github.greengerong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface CSRFService {

    public static final String TOKEN_ATTRIBUTE_NAME = "CSRFToken";

    public boolean isHandle(HttpServletRequest request);

    public void handle(HttpServletRequest request, HttpServletResponse response);
}
