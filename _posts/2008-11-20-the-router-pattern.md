---
title: The Router Pattern
link: http://itmusings.com/design/the-router-pattern
author: raja
description: the usage of the router pattern in class design
post_id: 64
created: 2008-11-20 11:04:17
comment_status: open
post_name: the-router-pattern
status: publish
layout: post
category: design
image: /images/junction.jpg
home_page: true
tags:
  - design
  - code
  - router
  - patterns
---

Browse through all the non integration pattern books and you would not find a Router mentioned. The Router pattern has been recognized as an excellent way to accomplish Enterprise Application Integration (EAI). But it is my contention that their role in application programming has not been sufficiently emphasized. This post seeks to introduce the reader to this exotic beast and lets them understand some of the amazing things that can be accomplished by keeping this pattern in mind. The Router, [IMNSHO](http://en.wiktionary.org/wiki/IMNSHO) is one of the most beautiful patterns. It can transform an application that is monolithic, non modular, non configurable among other bad things into a thing of beauty.  So much so, that the application almost becomes magical. Sounds pompous? Yes. But did this grab your attention at least? If it did, read on..  The router and a close relative called Chain are some of the most interesting patterns that I have run into in my course of developing complex applications. We will talk about chains another time but let us jump to the routers now. So what is a router? 

> A router is a component (typically a class) that connects its consumer to one of multiple output strategies (as enunciated in the [strategy ](http://en.wikipedia.org/wiki/Strategy_pattern) design pattern).

Let us illustrate this with a quick example. Let us say that we want to consume implementations of some interface called AccountService which is defined below: 
```java   
    
    public interface AccountService {
    
        public AccountDetails getAccountDetails(AccountDetailsInput accountDetailsInput);
    
    }
```
Let us say we have two implementations of the AccountService interface called DBAccountService and OnlineAccountService. DBAccountService fetches account information from a database (based on yesterday's feed to the database) and OnlineAccountService makes a web service call to obtain this information from the actual source. OnlineAccountService is an expensive call and hence is only used when the usage warrants it. Both these implementations would serve as strategies. Now a typical consumer who wants to use both these strategies selectively may have the following code: 
```java    
    
    AccountService dbAccountService = ... //somehow initialize it ;
    
    AccountService onlineAccountService = ... // somehow initialize it;
    
    if (real time details are desired){
    
    // use onlineAccountService
    
    } else {
    
    // use dbAccountService
    
    }
```
Hence the consumer code is now containing the logic that makes a selection between the strategies. This is sub-optimal due to various reasons:   

  * The routing ability cannot be re-used across multiple consumers and hence this will lead to code replication. 
  * What if we want to make this configurable. Let us assume that for some reason, the web service is down and hence onlineAccountService is not functional. How would we deflect all traffic to dbAccountService? This is quite clearly not possible without modifying the if-then construct. This construct would be replicated multiple times to each consumer and all the code has to change at the same time! Suddenly, this has become a problem of gargantuan proportions.
  * Let us assume that we want to enable dynamic discovery here. i.e we don't know upfront how many AccountService implementations exist in the system. But we want to discover all of them and route to them based on certain configurable conditions. This kind of code would never be able to achieve this.

The Router pattern depending on the sophistication that is put into it, would obviate some or all of the aforementioned problems. We will take a stab at discussing some of these nuances in the reminder of this post.  The first step in solving this problem is to introduce a new class called an AccountServiceRouter which contains the if then condition. The AccountServiceRouter class itself is quite simple to write:
    
```java    
    public class AccountServiceRouter implements AccountService {
    
    
      AccountService dbAccountService = ... //somehow initialize it ;
      AccountService onlineAccountService = ... // somehow initialize it;
    
     public AccountDetails getAccountDetails(AccountInput accountInput) {
    
    
        AccountDetails accountDetails = null;
    
    
        if (real time details are desired){
            accountDetails = onlineAccountService.getAccountDetails(accountInput);
        } else {
            accountDetails = dbAccountService.getAccountDetails(accountInput);
        }   
         return accountDetails;   
      }  
    }
```
Now the consumer merely uses AccountServiceRouter rather than the individual account services. This solves the replication problem mentioned above but nothing else.  It also gives another headache namely that we need to create a third implementation of AccountService which is intrinsically aware of all the account services that are in existence and can route to each one of them.  This is indeed a pain and there are other approaches that solve it but we are not going to concentrate on them now. Anyways, this is just a _tad _better than what we had before.  Now let us see if we can refine the solution a little more.  First of all we must recognize that Routers can exist for any kind of service. Hence we may have an AccountServiceRouter, a CustomerServiceRouter, an OrderServiceRouter and so on. All these routers will soon have this problem with configuration i.e. all the logic in them is in if-then-else and hence not configurable. Let us see if we can solve this problem in a generic fashion. 

## Routing String

A router is also only as complicated as the complexity of its if-then-else clause. The more complex the clause is, the more complex the router itself becomes. We cannot eliminate the complexity of the if then clause itself without sacrificing functionality. But we can see if we can somehow make the outcome more configurable. For this we need a concept called routing string. A routing string as the name suggests, is just a string representation of the actual route that is taken. For every route we take there would be a routing string associated with it. For instance, in the above example we can have two routing strings "realtime" and "delayed". "realtime" maps to the OnlineAccountService and "delayed" maps to the DbAccountService.  Both of these are meaningful to the actual router in question. Now why do we need this routing string abstraction?  To answer this let us go ahead with another example. We will create a new interface called OrderService with two associated implementations and a router. So we would have OnlineOrderService, DbOrderService and OrderServiceRouter. The OrderServiceRouter also has similar code as the one above for AccountServiceRouter but the strategies routed to are instances of OnlineOrderService and DbOrderService. This is all great. But if we go with the routing string concept we suddenly realize that the routing strings in this case too would be the same namely "realtime" and "delayed". So we are able to use the same routing string concept across multiple routers in vastly different scenarios.  This in itself is a very important realization. But there is more and we will realize it as we start making a more concrete example. Let us go and refactor the AccountServiceRouter above to use the notion of routing string.    
```java   
    
    public class AccountServiceRouter implements AccountService {
    
    
      AccountService dbAccountService = ... //somehow initialize it ;
      AccountService onlineAccountService = ... // somehow initialize it;
    
    
      Map<String,AccountService> mapOfServices = new HashMap<String,AccountService>();
    
    
      public AccountServiceRouter() {
    
    
         // initialize the mapOfServices.
    
    
         mapOfServices.put("realtime",onlineAccountService);
```
## Comments

**[Gaurav Malhotra](#1546 "2009-01-23 20:25:45"):** I totally agree with the concepts of this article.

