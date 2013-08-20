package com.github.greengerong;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

import static com.github.greengerong.CSRFService.TOKEN_ATTRIBUTE_NAME;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class CSRFTokenTag extends TagSupport {
    private static final long serialVersionUID = 745177955225541350L;

    private boolean is4Form = false;

    @Override
    public int doStartTag() throws JspException {
        final HttpSession session = pageContext.getSession();
        if (session != null) {
            final String token = (String) session.getAttribute(TOKEN_ATTRIBUTE_NAME);
            if (isNotBlank(token)) {
                try {
                    final JspWriter out = pageContext.getOut();
                    if (is4Form)
                        out.write(token);
                    else {
                        final String hidden = String.format("<input type=\"hidden\" name=\"%1$s\" id=\"%1$s\" value=\"%2$s\" />", "", token);
                        out.write(hidden);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public boolean isIs4Form() {
        return is4Form;
    }

    public void setIs4Form(boolean is4Form) {
        this.is4Form = is4Form;
    }

}