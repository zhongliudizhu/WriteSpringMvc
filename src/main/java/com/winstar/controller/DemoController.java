package com.winstar.controller;

import com.alibaba.fastjson.JSON;
import com.winstar.annotation.BinAutowired;
import com.winstar.annotation.BinController;
import com.winstar.annotation.BinQualifer;
import com.winstar.annotation.BinRequestMapping;
import com.winstar.pojo.Person;
import com.winstar.service.DemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@BinController
@BinRequestMapping("/demoController")
public class DemoController {

    @BinAutowired
    @BinQualifer(value = "demoServiceImpl")
    private DemoService demoService;

    @BinRequestMapping("/getAllPersons")
    public void getAllPersons(HttpServletRequest request, HttpServletResponse response) {
        List<Person> allPersons = demoService.getAllPersons();
        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(allPersons));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
