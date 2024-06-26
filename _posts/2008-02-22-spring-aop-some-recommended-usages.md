---
title: Spring AOP - Some Recommended Usages
link: http://itmusings.com/java/j2ee/spring-aop-some-recommended-usages
author: raja
description: Spring AOP - some more details
post_id: 24
created: 2008-02-22 15:57:19
comment_status: open
post_name: spring-aop-some-recommended-usages
status: publish
layout: post
category: design
image: /images/2008/02/aspect-flow.jpg
home_page: false
tags:
  - aop
  - design
---


### Horizontal concerns & SRP

We start with an object oriented tenet that states that one class must implement one and only one responsibility. This is called the Single Responsibility Principle (SRP). SRP states for instance that a UserService class can only do "user management" and nothing else. This at the outset may look very obvious to everyone. But on closer inspection of any typical UserService class the following can be observed: 

  * The UserService class may have to implement some logging functionality.
  * User operations may have to be audited and hence some auditing functionality becomes required.
  * Security needs to be enforced to ensure that only authorized users perform user operations.
  * typical operations on the user object may have to be surrounded by transactions. These transactions ensure that all operations are atomic.
Thus there is more to the UserService than what meets the eye! The functionality that was specified above falls under the banner of horizontal requirements. These are also sometimes called non functional requirements since they are not part of the specification of what the UserService does directly. Horizontal concerns, if incorporated into the core code violate SRP. Since this kind of functionality is interspersed throughout the application, it is very hard to address changes in horizontal requirements without individually changing a lot of classes throughout the application. This is a cause for extreme application brittleness. 

## Introduction to AOP

Aspect Oriented Programming (or AOP) attempts to address this problem. Functional requirements (such as User Management) are directly handled by core classes in the application. Horizontal or non functional requirements such as security are implemented by specialized classes which are interwoven into the core classes. These specialized classes are called "aspects" and this approach to programming involving the interweaving of aspects around core target classes is called AOP. For the purposes of this article I am going to call the normal application class such as UserService that caters to a functional requirement of the application (such as User Management) as a target class. Now that we got the definitions out of the way, let us see how AOP is implemented in typical OO programming languages. 

## How aspects work

A typical aspect is integrated into an application as illustrated below: ![aspect_flow](/images/2008/02/aspect-flow.jpg) A method in a target class is invoked. But before the control passes to the actual method, an aspect is invoked. The aspect first makes a decision whether the underlying method in the target class should be invoked. It may be possible that the aspect may decide to not invoke the underlying method. (Eg: Security aspects and caching aspects work this way) If it does decide that the underlying method should be invoked, then the control is passed to the target class after some pre-processing. The aspect also gets an opportunity to do some post processing after the underlying method in the target class returns after the invocation. Hence the AOP implementation must somehow get hold of the target class invocation and be able to "decorate" it with aspects. Let us look at some AOP implementations in Java. 

## AOP Implementations

In Java, AOP can be implemented in a few ways: 

  * **Source Code Generation:** In this, Java source code is generated around the class to decorate the methods with aspects.
  * **Byte Code Enhancement:**In this strategy, the compiled java class code is ornamented with byte code that implements the aspects.
  * **Design Patterns:** Certain design patterns facilitate interception. Examples of this include the Interceptor pattern, the Decorator pattern etc. A detailed discussion of these is not in the scope of this particular article.
  * **Dynamic Interception:** This is the most easily implementable because this does not require code generation or enhancement in any form. It does not require controllers or facades that implement the design patterns either. The target class ( or its interface) is dynamically mocked by a proxy. This proxy first invokes certain interceptors. These interceptors perform the functionality of aspects. They then delegate control to the target class. After the target class is invoked, these interceptors also have the opportunity of modifying the output generated by the target before returning control back to the original caller.
Spring AOP takes the dynamic interception approach to implement AOP. Currently in Java, the only way to facilitate dynamic interception is by intercepting calls to all the methods of the target class. Dynamic interception does not work for fields i.e. it is not possible to intercept the call when the client is directly setting/retrieving the value of a field in the target class. This is not a big limitation since it is anathema to use a field in a class directly from another class anyways. 

## Spring AOP - An introduction to key terms

### AOP Alliance

The *AOP Alliance* is a joint effort by various stakeholders to define a consistent way of accomplishing AOP in Java. The alliance defines the key interfaces but leaves the tedious details to the implementations. Spring AOP is an implementation of the AOP Alliance. 

### Advices,Advisors, Proxies

The AOP alliance introduces the notion of an Advice. An *Advice* is a class that is supposed to intercept the invocation of another class and render some extra functionality. Hence advices are *aspects*. Advices can be inserted before a method is invoked There are various types of Advices that have been defined as part of the AOP alliance. Spring AOP supoprts only AOP through method interception and hence is capable of supporting one sub-type of advice called Method Interceptor. An advice is applied on a "target" application class using a dynamically generated "proxy" class. The proxy class sets up a chain of interceptors around the target. Interceptors are typically advices or advisors. *Advisor* is a *spring* (i.e. it is not defined as part of the AOP alliance) interface that abstracts access to an advice. Every advisor must be hooked with an advice. The advisor delegates control to the advice only if certain conditions are met. For instance, a security advice is only applicable for methods that need to be secured. Hence the advisor that abstracts access to the security advice would check if the method being called currently is secured. If it is not secured, the security advice is not even invoked. In summary, an advisor can be conceived as a filter for the advice delegating control to the underlying advice only if certain conditions are met in the method invocation. A *Proxy* is the class that surrounds the target. The proxy is hooked to a list of advisors and advices. For each advisor, there should be an underlying advice. If an advice is hooked directly into the proxy, then it is unconditionally invoked. 

## A Detailed Segue into The Method Calling Sequence.

## Comments

**[raja shankar kolluru](#1774 "2010-07-18 03:59:57"):** Thanks Ronald!

**[Arthur Ronald F D Garcia](#1773 "2010-07-17 13:21:43"):** Well documented article about Spring AOP. Congratulations!

**[santosh](#1913 "2010-12-30 11:06:16"):** Great Article, well documented , descriptive with good presentation.

**[raja shankar kolluru](#1916 "2010-12-30 14:46:41"):** Thanks Santosh.

