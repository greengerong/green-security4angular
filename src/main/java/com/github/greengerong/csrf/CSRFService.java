package com.github.greengerong.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CSRFService {
    void handle(HttpServletRequest request, HttpServletResponse response);
}
