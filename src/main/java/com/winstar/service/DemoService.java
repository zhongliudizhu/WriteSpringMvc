package com.winstar.service;


import com.winstar.pojo.Person;

import java.util.ArrayList;
import java.util.List;

public interface DemoService {
    List<Person> personList = new ArrayList<>();

    List<Person> getAllPersons();


}
