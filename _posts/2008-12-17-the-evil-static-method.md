---
title: The evil Static Method
link: http://itmusings.com/design/the-evil-static-method
author: raja
description: why are static methods avoidable for the most part
post_id: 91
date: 2008-12-17 11:25:57
comment_status: open
post_name: the-evil-static-method
status: publish
layout: post
category: design
image: /images/static.png
home_page: false
---
The keyword static possibly started as the first attempt at encapsulation. C Programs consisted of functions that spanned across multiple files. However, the programmers wanted some of these functions to be "private" within the file that they were contained in. The static keyword provided that small veneer of privacy. When java and other derived languages came in, the static was still continued. But this time it was used to signify that the variable or the method was static across instances of a class. We also started seeing a significant number of static "utility" libraries. Browse through any project and you would find "StringUtils", "XmlUtils" and the like. Soon, static methods were mentally equated to any kind of "helper" methods. This resulted in a lot of people using this artifice in places that it should not be used.  This post talks about static methods and why IMO I consider these as evil.

# Origins and Introduction

Typically, it starts off in a small way. An entire application is coded in a class. The class has one gargantuan "main" method that accomplishes most of the functionality. Then one fine day a new person is called to maintain this application. The new person realizes that it is impossible to decipher the "main" method which by now is almost venomous! Touch any part of the method and it stings! So the new person decides to do some re-factoring. Some functionality is taken off into multiple methods which reside within the same class. These methods are then called from the "main" method.  Now the "main" method is small but the class still is huge! So, one fine day out of sheer frustration of loading this class into the IDE, the developers decide to refactor this class now into multiple "helper" classes containing "helper" methods - which are responsible for implementing part of the functionality. Thus the application functionality is now scattered across multiple helper methods. The main method is responsible for invoking these multiple helper methods. But wait! Did I say that the application now has multiple classes? Who is responsible to instantiate all these classes and call these "helper" methods. Initially, the main() method takes over this responsibility. So the main method instantiates the helper class and calls the helper method. But some brilliant guy thinks it is too much of a waste of time to do this instantiation. After all, the helper class does not typically contain any class level (aka instance) variables. So why instantiate this helper class if all that we are interested is to invoke the helper method? With this reasoning, all the helper methods suddenly become static methods of the helper class! Thus the static method is born! The snippet of code in main() becomes: 
```java   
    
    public SomeClass{
     public static void main(String[] args){
     // bla bla bla..
      SomeHelper.helperMethod(some arguments);
      // more of the same.
     }
    }
```
# The Customization Problem

What is the problem with this approach? Nothing much until we start considering a more concrete example. Let us try aping the Jakarta Commons utils and come up with a class like ConversionUtils. This class contains various methods to convert to and fro from any object to a string. So we might have a class called DateConversionUtils for instance. This class has two methods. String toString(Date) and Date fromString(String). The two methods accomplish the task of converting between a Date object and a String object. The string is assumed to contain the date in the form "YYYY-MM-DD" and this utility quickly converts it from that form to a date and vice versa. Now, let us use this DateConversionUtils class and its static methods in some method. 
```java   
    
    public SomeClass {
     public void someMethod(){
       Date d = ... ; // initialize the date object
       String s = DateConversionUtils.toString(d);
       // -- somewhere else in the method.
       String s1 = ... ; // initialize it to a string that is formatted as YYYY-MM-DD
       // get the date from the string representation of it.
       Date d1 = DateConversionUtils.fromString(s1);
      }
    }
```
The evident simplicity of this approach is undeniable. Hence this class DateConversionUtils will now be shipped along with the application and we start using it profusely from all parts of the application for converting strings to dates and vice versa. As we are about to ship the application, there is a new customer who asks that we use "MM/DD/YYYY" to represent the dates! Now the application must be able to handle this simple feature. Right? As it turns out, the implementation of this feature is very simply handled thereby re-affirming the validity of the approach that we took in putting the entire date conversion logic to one method. We must alter DateConversionUtils to handle the new customer. Because, we know that the date conversion must change for that particular customer alone. Not for everyone else. So we come up with a configuration file that specifies which format of date the DateConversionUtils class must support. We state that the format supported would be either "YYYY-MM-DD" or "DD/MM/YYYY". The ConversionUtils.fromString() and ConversionUtils.toString() methods would now need to read this configuration file and alter their behavior according to the format supported. So with one change, we are able to accommodate the new customer. Yahoo! The customization problem has been solved. But what did we do? We have started complicating our static method to start reading configuration files. But it is a small problem, we say. Sure. So let us move on.   

## The Routing problem

Now, let us say that our application must support multiple customers with one installation. (something like a SaaS - Software as a Service-model). Now, we cannot anymore use the configuration file to dictate one behavior or the other. We need to be able to get more sophisticated. We need to determine during runtime as to what format the customer wants and accordingly branch out to the appropriate portion of the code. This routing logic now would start entering our static method. The static method must first determine who the customer is, find out which format the customer likes (YYYY-DD-MM or DD/MM/YYYY) and actually execute that part of the code to accomplish the conversion! The sample code is given below: 
```java    
    
     public class DateConversionUtils {
       public String toString(Date date){
       Customer c = ... ;// find out who the current customer is.
       if (c.isYYYYMMDD() ){
         // implement YYYY-MM-DD functionality.
       } else if (c.isDDMMYYYY()){
         // implement DD/MM/YYYY functionality.
       }
     }
    }
```
Notice that in the code snippet above that the customer needs to somehow be known to the static method. In practice, it is not so easy to implement this unless the method itself accepts the customer as a parameter. So the signatures of the fromString() and toString() static methods must change to accept a customer. Hmmm. this is is getting a little ugly now. What if the date format depends on other factors besides the customer? Would we have to start accepting all these parameters into the method? I am not going to explain how we can solve this problem right now. Because, this is symptomatic of a more generic problem that is not just restricted to static methods and hence would warrant a needless digression. So for now let us somehow assume that the static method knows about who the customer is for the "current" request. This kind of behavior is not restricted to just date formats. Different customers may prefer different date formats, number formats, address formats, social security number formats etc. Pretty soon, we would find multiple ConversionUtils classes and their methods implementing this kind of customer specific branches to accomplish various kinds of conversions.

## Comments

**[Gaurav Malhotra](#1545 "2009-01-23 20:17:53"):** Nice article..

