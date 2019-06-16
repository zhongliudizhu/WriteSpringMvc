package com.winstar.service;


import com.winstar.pojo.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface DemoService {
    List<Person> personList = Collections.synchronizedList(new ArrayList<>());

    List<Person> getAllPersons();


}
