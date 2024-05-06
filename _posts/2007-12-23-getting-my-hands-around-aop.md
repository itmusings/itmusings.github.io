---
title: Prelude to AOP - Requirements, Patterns, SRP and DRY
link: http://itmusings.com/architecture/getting-my-hands-around-aop
author: raja
description: AOP - why do we need it?
post_id: 5
created: 2007-12-23 01:00:00
post_name: getting-my-hands-around-aop
status: publish
layout: post
category: architecture
tags:
  - AOP
image: /images/aop.png
home_page: true
---

AOP has graduated for quite sometime now from the esoteric reaches of design to become a mainstream pattern. Hence it sounds almost superfluous to describe AOP in an explicit article since there have been multiple attempts to document it already. But I believe that in talking about AOP, I get to talk about good architecture in general. Hence this series of posts attempt to talk about some of my views on architecture in general and how good architecture almost inevitably leads to AOP. In this article we will talk about requirements, principles of good design and the need for AOP.
* Why AOP?Future posts would elaborate more on this pattern. Technorati Tags: [aop](http://technorati.com/tag/aop), [spring](http://technorati.com/tag/spring), [srp](http://technorati.com/tag/srp), [ooad](http://technorati.com/tag/ooad)

### Why AOP?

To answer this, let us take a small segue into two important principles of Object Oriented Design(OOD)

### OOD - Introduction to SRP and DRY

To me there is no other object oriented principle that is more fundamental than the Single Responsibility Principle (SRP). The SRP states a simple OOD axiom, namely - One class implements exactly one responsibility. In short the class should change because of one factor not due to multiple factors. There is a reciprocal statement of the SRP called DRY (Do not Repeat Yourself) DRY states that we should not repeat the same logic in multiple places. In essence, DRY states that one single responsibility should be implemented by one class or a series of classes only. IMO the difference between a good and a bad design lies in the emphasis that is laid in following these two principles. A bad design is inextensible and brittle i.e. a bad design cannot accommodate future changes in requirements without spiraling into a quagmire of destructive code changes. Good design mandates that the changes are kept localized i.e. a change in requirement should not lead to pervasive changes across the code base. Hence good design pays heed to SRP and DRY.

###  Requirements - An introduction to Horizontal Requirements

In the above discussion, we had talked about requirements in passing. Now let us talk about them in more detail. There are typically two types of requirements for any application namely functional and non functional requirements.
* _Functional Requirements_ are the reason for the application's existence. They talk at length about what the application is expected to do. Most functional requirements can be phrased into "subject" and predicate form such as _System captures details about an order_.
* _Non functional requirements_ talk about "how" the system does its job rather than what it does. Non functional requirements are typically extracted from functional requirements. They are recognized by the usage of adverbs rather than the usual subject and predicate. For instance, a functional requirement might read as "System captures the order details in a fast and secure way". The adverbs "fast" and "secure" should be dealt with as Non functional requirements. They capture factors such as performance and security in this example.Non functional requirements have also been equated with _horizontal requirements_. To understand horizontal requirements, let us first attempt to dissect a typical application. An application can be "sliced" vertically into modules with respect to its functionality. Hence a particular module of the application is responsible for user management, another module for order capture etc. Most modules have functional responsibilities. But besides these functionalities catered to by the individual modules, the application also has certain other requirements that pervade throughout. These requirements need to be integrated with every module of the application. Hence these have been called as horizontal requirements i.e. requirements that pervade thru all the vertical slices of the application.In most cases, the horizontal requirements are non functional requirements because vertical slicing is done functionally. Yet, every vertical slice of the application must also implement such non functional requirements such as logging, auditing, security etc. Hence these requirements are termed "horizontal". An understanding of horizontal requirements is key to comprehend application frameworks. Most framework designers or container providers aspire to cater to horizontal requirements so that applications can cater to functional requirements. This is evident from a peripheral examination of any application container (such as the .NET application server for example) or specification (such as the J2EE container specification) These containers and specifications provide for such horizontal services such as transactions, security, caching etc. Most applications "code" for horizontal features whether they are provided by the framework, container or some library that is utilized by them. So most applications will have some code that implements horizontal requirements in some form or the other. Hence one is likely to encounter security code in both user management code and also the order processing code.

### Horizontal Requirements & AOP

Implementing horizontal requirements in functional code, even if it is restricted to availing the features of the application framework used, is in violation of the Single Responsibility Principle (SRP). It is impossible to change the implementation of these horizontal requirements or handle changes in the requirement itself without having to make code changes across the entire application. This is potentially an extremely brittle situation. Yet implementation of these horizontal requirements would call for code that is well integrated with the functional code. To resolve these seemingly paradoxical needs, many patterns have been thought about by application designers. Some of these are listed below and would be elaborated in later blog entries.
* The _Interceptor_ pattern:
* The _Chain of Responsibility_ pattern
* Code Generation
* _AOP_ \- Aspect Oriented Programming.More in next...
