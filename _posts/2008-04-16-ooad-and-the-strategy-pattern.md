---
title: OOAD and the Strategy pattern
link: http://itmusings.com/design/ooad-and-the-strategy-pattern
author: raja
description: Difference between domain objects and strategies
post_id: 35
created: 2008-04-16 07:03:15
comment_status: open
post_name: ooad-and-the-strategy-pattern
status: publish
layout: post
category: design
image: /images/strategy.jpeg
home_page: true
tags:
  - design
  - strategies
  - ooad
---


Recently, I had a discussion with someone on Object Oriented Design. How do you identify objects when you are doing domain modeling? The traditional approach (as originally propounded by the three amigos and appropriately modified and recast by multiple others) is to look for nouns in the verbiage of the application requirements.  All the nouns become domain level objects according to this approach. Of course, there have been a lot of embellishments to this overly simplified analysis approach. One of the things this approach misses out, is that behavior of the identified objects is not encapsulated appropriately. For instance, after talking to some bankers about a prospective banking application, I can identify domain objects such as Account, Customer etc. These are valid domain objects by themselves. But what this misses out, is the fact that the account has to participate in certain application behavior such as transfer of funds from one account to another. So, as we identify the application behavior, we need to add relevant "strategies" to the application. Now the traditional argument was that the behavior itself should be made a part of the object. So an account object knows how to debit funds from itself. But the problem with this approach is that 

  * Most behavior requires a collaboration of domain objects.
  * Behavior is typically implemented in one layer of the application and may depend on other artifacts which are present in a specific layer. For instance, when debiting an account it is possible that we might have to persist the changes to a database in a transaction. This is specific to a particular layer (services layer).
  * It is not possible to replace one kind of behavior with another easily.For instance, debiting an account in a funds transfer transaction might be different from debiting an account in some other transaction.
All these conflicts have steered us away from coupling behavior too tightly with the domain objects that we have hitherto identified using the noun approach. Instead, the current trend is to identify "strategies". A strategy can be defined as behavior encapsulated into an object (typically called a Service object) These strategies should collaborate with each other to implement the overall application behavior. 

## Contexts & Strategies

The application can be visualized as a medley of Context objects (objects that carry state) and Strategies (objects that implement behavior) Strategies are chained with one another to form the overall application. Context objects flow from one strategy to another. Context objects also typically span across multiple application layers. (We will attempt to define these layers later)  Example for Context objects: 

  1. Account, Customer and other entity objects
  2. Data Transfer Objects (such as AccountBalanceInput and AccountBalanceResult)
  3. HttpServletRequest that carries the contents of an http request.
  4. Value objects (entities that dont have an identity ) Example : Address
Examples of Strategies: 
  1. Web controllers
  2. Service classes
  3. Proxy to services
  4. Clients/adapters/proxies that allows access to a container (ex: a JDBC client, a transaction proxy etc.) 
While designing strategies, special attention must be paid to make sure that:   
  * One strategy implements one responsibility only. (This is captured by the Single Responsibility Principle - SRP)
  * One responsibility must be implemented by one strategy only. (This is captured by the DRY principle - Dont Repeat Yourself)
  These two principles, in my opinion, capture the gist of most of what we do in terms of design.  

## Object Dependencies

The reader may wonder about the intent behind classifying the objects as Context and Strategy objects. The most important reason for this is the fact that the dependencies between objects must be dealt with in different ways for both these kinds of objects. Context objects can depend on each other to form an "object graph". Example: A customer object depends on an account object (or multiple account objects if there are multiple accounts per customer). The account object may in turn have other objects attached to it. This complex graph of objects navigates through the application and carries context information from one layer to another. They also define the "grammar" or the canonical model of the application. There may be multiple models prevalent within the application eco-system. It may be required to transform from one model to another. This typically happens if multiple applications exist with their own definition of what an account is, what a customer is etc. In SOA architectures, multiple applications colloborate with each other by exchanging context information in the form of xml structures. On the other hand, strategies are different. Many strategies are determined by specific to a certain layer of the application. That is why we have UI controllers in the UI layer, Data Access Objects (DAO) in the Data layer and so on. A strategy typically starts life as a "bloated" startegy. i.e it handles multiple responsibilities. But with time, more and more responsibilities are taken away from the strategy and refactored into strategies of their own. This process of refactoring following the SRP results in code that can be re-used effectively. Viewed this way, the entire functionality of the application is broken down into strategies. The strategies themselves are lined up from one end to the other to constitute the entire application! This becomes an interesting proposition since we would realize now how important dependencies are for the application to function.  

## Application Layering

If applications are constituted of strategies, why do we need the notion of layers?  Typically, most applications have the following layers:   

  * UI which provides the front end to the application from the outside world. Typical UI layers consists of some version of MVC. 
  * EIS - Enterprise Information Service which provides a way of obtaining data from various systems of record. (database, xml store, third party stores such as mainframes etc.)
  * Middleware - which provides the glue to bind the EIS to the UI. Besides containing the core application business logic, Middleware also takes upon itself the tasks of aggregating data, routing, transformation etc.

The layers concept arose due to re-use.  For instance, Middleware strategies are used the most since the business logic of the application is contained in it.  Hence it should be re-usable across different UIs and EIS layers. 

Many application servers, frameworks etc. have evolved to implement functionalities that are typically the responsibility of strategies within a particular layer. Example: Since UI layer strategies need to decrypt the HTTP request and support a HTTP response, the application servers that are used for rendering UI, support these functionalities innately. Likewise, the middleware frameworks and application servers support transactions, security etc. It therefore becomes logical to design an application with strategies that are in different layers. Many design patterns have evolved to make the task of designing strategies easy. Hence we have the MVC pattern, the facade pattern, proxy pattern etc. that reinforces the application layering concept. 

**While it is easy to follow these patterns and design applications, it should also be remembered that layering does not mandate non collocation i.e. the layering may be logical rather than physical.** This is a very important principle to keep in mind when designing an application. 

## Application Glue
