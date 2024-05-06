---
title: On IoC containers & Stateful components
link: http://itmusings.com/architecture/stateful-components-in-ioc
author: raja
description: Is it a good idea to use stateful components? How do we deal with IOC containers for such components?
post_id: 700
created: 2011-03-16 02:32:46
comment_status: open
post_name: stateful-components-in-ioc
status: publish
layout: post
category: design
image: /images/lookup.jpg
home_page: false
tags:
  - design
  - DI
  - Stateful
---


If we elevate ourselves enough to sit on a figurative perch in the programming world and look down at the applications that are being developed, we realize that Inversion of Control (IoC) containers have most definitely come here to stay. You see more people than ever before proclaiming expertise in programming "Java with Springs" - alluding no doubt to the Spring lightweight container that has now become the de-facto choice to develop most modern J2EE applications.  Now it is easier than ever to create components as "Stateless Singletons" and injecting other stateless singletons into them. The whole dependency tree gets materialized during start up. Given this background, I was somewhat surprised and fascinated by two independent problems that were recently brought to my attention.

The problems involved different IoC containers - one of them being Spring and the other being ATG Nucleus. Both stated the same thing namely that the application is taking too long to look up components from the IoC containers. Ex: In Spring, the [SpringBeanFactory.getBean(String name) call](http://static.springsource.org/spring/docs/2.5.6/api/org/springframework/beans/factory/BeanFactory.html#getBean\(java.lang.String\)) was taking too long. Now, if you were an application doctor who got commissioned to solve this case, your natural temptation is to prescribe the following:

> Thou shalt not useth an IoC container to look up components. Use it instead to inject one component into the other during start up.

But before you scribble these words of profound wisdom with an illegible hand, as doctors are wont to do, you would do well to take a small pause and consider the situation more holistically. Why is a component forced to "look up" other components from an IoC container? In other words, what are the situations that would warrant one component being forced to look up another component? Components need to look up other components if they cannot benefit from injection. The only reason that such a thing happens is if the two components are in different scopes. In the case of a Spring application, one component might be a singleton whilst its dependency maybe a "prototype". Hence the singleton component has to "look up" the  prototype component. It can do so either by either using BeanFactoryAware interfaces or method injection. (Please see [here](http://static.springsource.org/spring/docs/2.5.x/reference/beans.html#beans-factory-method-injection) for a detailed description for both these artifices in Spring)

But the point I am making is to figure out "why" we need to have components in different scopes.In general, I would look askance at any attempt to use a component as a non singleton. My reasoning is as follows: If you use a component as a non singleton, then it implies that you are storing some kind of state in that component. This state may be instance specific , request specific, session specific etc. This means that the component is no more stateless.This design violates my [favorite class dichotomy doctrine as advocated here.](/class-dichotomy/) So why do we need to store state in a component? I think that the following use cases warrant them:

  * We may be using a framework that requires stateful components. Ex: All actions in Struts 2 are considered stateful since the framework injects request specific parameters (stuff that got submitted by the user) into the component.
  * We maybe using value objects to store components. Consider a situation where a value object contains a method that invokes a strategy to act on it. Ex: We may have an XmlRenderer component that renders other objects to XML. A value object may have a render() method that invokes this renderer to render itself to XML.

In the above situations, the class in question acts as both a carrier of state as well as something that accomplishes some work. This kind of mixup of responsibilities leads to a situation where a more ephemeral object (ex: the value object or the struts 2 action) needs to look up a singleton component that is naturally contained in an IoC container. A ton of post processors (using the Spring parlance) come into play in this lookup which can lead to a substantial slowness of this call.

Note that in the above discussion, we did not discuss another candidate use case that requires a component lookup from an IoC container. This happens in writing frameworks. Let us say that we write a framework that requires a component to be created by the client of the framework. (such as for instance an MVC framework such as Struts) This component might require us to look up actions that are best created by an IoC container. In this case, we need to look up the component when the request to be serviced by the component arrives. (in Struts case, the request hits a URL that is mapped to a struts action). At that point in time, we can either look up the component from the IoC container or instead use a cached reference to the object that was created during application start up. The latter approach is preferred since it does not incur the penalty of the look up with each request.

These kind of problems are best avoided by the judicious separation of data containers from the strategies in an application. I am well aware that this may tilt the design excessively towards an anemic domain model [which is frowned upon by the purists of object design](http://martinfowler.com/bliki/AnemicDomainModel.html). My point is that the term Anemic Data Model has itself been coined with prejudice. You can always have strategies that leverage inheritance rather than becoming [transaction scripts.](http://martinfowler.com/eaaCatalog/transactionScript.html)

Just my point of view!

## Comments

**[Gaurav malhotra](#1921 "2011-03-20 13:11:01"):** Hi raja, I do agree with you people are misterprating martin fowler DDD. Caliing sevices from within data container objects will give an impression that its enriching the domain but it posseses many other challenges Eg Account (domain objec) and AccountService and now we have to implement business method tranferMoney from account A to account B,where to put the method in Account object or AccountService?.... There is school of thought inspired from somewhere, put it on Account object, and put the implementatiom in AccountService and DI AccountService in Account object, which I totally disagree. Cheers, GM

