package com.winstar.service.serviceImpl;

import com.winstar.annotation.BinService;
import com.winstar.pojo.Person;
import com.winstar.service.DemoService;

import java.util.List;

@BinService
public class DemoServiceImpl implements DemoService {

    public DemoServiceImpl() {
        personList.add(new Person("mike", 13, "dsddd@163.com"));
        personList.add(new Person("jack", 13, "dsddqq@163.com"));
        personList.add(new Person("tony", 13, "dsewd@163.com"));
        personList.add(new Person("sam", 13, "dsddsd@163.com"));
    }

    @Override
    public List<Person> getAllPersons() {
        return personList;
    }
}
