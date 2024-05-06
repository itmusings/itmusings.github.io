---
title: Auto wiring Spring Test Cases
link: http://itmusings.com/java/auto-wiring-spring-test-cases
author: raja
description: Instantiating spring automatically in a test case. This is outdated now.
post_id: 18
date: 2008-02-08 16:02:21
comment_status: open
post_name: auto-wiring-spring-test-cases
status: publish
layout: post
category: code
home_page: false
image: /images/spring-auto-wiring.jpeg
tags:
  - spring
  - code
  - testing
---


Spring is of late being used universally. Hence the need arises to either 

  * Test Spring wiring.
  * Write test cases that are best wired using Spring due to the complexity of the wiring of the objects involved.
For instance,let us consider a test case that tests a service which in turn utilizes a Dao that in turn uses a datasource. Due to the complexity of the wiring, it is much easier to do all this using Spring. The test case then has to instantiate spring and initialize the "service" from it.  Hence the typical code that has to get written would be something like 
    
    
    public class MyTestCase extends TestCase {
    
    protected Service service;
    public void setUp(){
    // initialize spring bean factory from a spring configuration file.
    service = (Service) beanFactory.getBean("service");
    }
    }

This code gets repetitive across tests thereby making us wish if we could do something for re-use. Here is a simple [SpringJUnitTestBase](/code/SpringJUnitTestBase.java) class which does exactly that. The code now becomes the following: 
    
    
    public class MyTestCase extends SpringJUnitTestBase {
    // This will get auto-wired by name.
    protected Service service;
    public void setService(Service s){
    service = s;
    }
    
    public void setUp(){
    }
    }

Far simpler huh?? The only caveat is that the spring file should have the exact same name and be in the same package as the test case. So in this case we need to have MyTestCase.xml in the same package.
