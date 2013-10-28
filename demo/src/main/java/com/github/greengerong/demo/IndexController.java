package com.github.greengerong.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "process/{age}", method = RequestMethod.POST)
    @ResponseBody
    public int process(@PathVariable("age") int age) {
        return age;
    }

    @RequestMapping(value = "process/{age}", method = RequestMethod.PUT)
    @ResponseBody
    public int process1(@PathVariable("age") int age) {
        return age;
    }

    @RequestMapping(value = "process/{age}", method = RequestMethod.DELETE)
    @ResponseBody
    public int process2(@PathVariable("age") int age) {
        return age;
    }

    @RequestMapping(value = "process/{age}", method = RequestMethod.GET)
    @ResponseBody
    public int process3(@PathVariable("age") int age) {
        return age;
    }
}
