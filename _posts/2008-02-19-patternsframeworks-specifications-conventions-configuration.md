---
title: Patterns,Frameworks, Specifications, Conventions & Configuration
link: http://itmusings.com/architecture/patternsframeworks-specifications-conventions-configuration
author: raja
description: On framework design and the importance of convention over (and in addition to)configuration
post_id: 22
date: 2008-02-19 19:46:39
created_gmt: 2008/02/19 19:46:39
comment_status: open
post_name: patternsframeworks-specifications-conventions-configuration
status: publish
layout: post
image: /images/framework.jpeg
home_page: true

---


This post is all about introducing the terms above and talking about their place in architecture. There must be some pretty tough girders at the bottom of the Empire States Building! They have to bear the entire weight of over a hundred stories. The structural engineers who designed this building should have taken into account all kinds of forces and designed them accordingly. But how about a one storeyed structure? Do you think the structural engineer must be as scrupulous in designing this structure too? Of course he has to be! Whether it is a hundred storied structure or a one story structure, the engineer has to make sure that the girders, holding the structure, are capable of withstanding the forces that would be encountered. But if one single storied structure has been meticulously designed, do you think it makes sense to expend the same amount of effort for designing similar structures? Yes, if the architect never learns from his previous work. All these structures are more or less bearing the same kind of weight. Hence it makes sense to not replicate this effort.  So what happens? Any engineer who undertakes the design of any building, that structurally resembles a predecessor, would end up replicating the structural drawings of its predecessor. Thus the "copy and paste" technology handles the engineer's requirements very well. But before the copy and paste can happen, the commonality of the design between the two buildings must be recognized. This is what we call a "Design Pattern". This design problem (i.e. the structural specification of the building) and its solution (the outcome of the design process) would get cataloged as a general idiom or a design pattern in the industry. Good architects and engineers would be taught to recognize these patterns and apply them as needed. Some of these idioms are so standard that they have even been cataloged as part of a "standard". For instance, every Indian civil engineer would have to adhere to the recommendations of ISI - Indian Standards Institute. These recommendations are nothing but a catalog of best practices. In programming, we are similarly exposed to some design patterns that have become standard idioms in our vocabulary. Some of them have become so integrated with our way of thinking that we don't even consider them as design patterns. Hence multi tier architecture, Model View Controller pattern, Asynchronous messaging etc. have become standard design practices. In specific projects these get documented as specifications that the engineering team has to adhere to. For instance a specification may stipulate that all user interaction logic must be constrained within the controller class. As we evolve in sophistication, we realize that it is is best to be preemptive than be stipulating i.e. it is better to constrain the design to comply with the best practices beforehand. Hence frameworks are the best enforcers of design patterns. 

## Design of frameworks

So how do we channelize an application to comply to the design patterns? There are various strategies to accomplish this. One or the other of these would form salient features of most frameworks: 

  * **Base classes** \- These ensure the following: 
    * Base behavior is available. Other components of the framework would assume that this component inherits from the base class so that the base behavior is guaranteed.Example: In the Struts Framework, all controllers must subclass the Struts Action.
    * Base classes can act as interceptors. For instance, let us say we have a class with a final method foo(). This method does some base functionality after which it typically calls some abstract method doFoo() that would be implemented in a subclass. This method provides the stub for accomplishing app specific functionality.
  * **Front Controllers/Facades etc.** \- These classes serve as a front to the rest of the application. They intercept calls from outside and do most of the preliminary work such as mustering request parameters, setting up transactions, triggering validations etc. These classes may also trigger chains of responsibilities. For instance, the J2EE servlet specification is a framework that supports a chain of responsibility using ServletFilters, does initial set up such as mustering of request parameters and finally delegates control to a servlet.
  * **Hooks to Existing Frameworks** There are many frameworks that serve as hooks to existing frameworks for accomplishing specific tasks. Example: Consider ACEGI the spring security framework. ACEGI fits into the application as a servlet filter and achieves interception of each request for the purpose of security.

## Role of configuration with frameworks

Frameworks have classes that are super generic. They know to process different types of user requests and tend to provide hooks for accomplishing application specific behavior. Hence the necessary information about when to call the hooks, needs to be furnished to the framework. This is typically achieved using configuration. For instance, a J2EE server needs the web.xml file. This file tells the server the URL for which a certain hook (ex: servlet or servlet filter) needs to be invoked. Hence it is common place to see frameworks associated with a bunch of configuration files typically in xml. 

## Problems with Configuration

Configuration provides a flexible way of incorporating extensible functionality. But configurations tend to suffer from several problems: 

  * Configurations are very specific to the particular application framework. Hence it is hard to check for them to ensure that they are correct and upto date. For instance, it is possible to introduce a servlet name in web.xml without the servlet class actually existing in the application class path.
  * Configurations tend to grow in size till they become so big that the entire code looks like a bunch of configurations! This hampers readability.
  * Unless configurations are "discoverable" automatically using wildcards, there would have to exist one master configuration file which mentions the name of all the other configuration files. Modularization gets lost in this process. For instance, there is only one web.xml file specified in the servlet specification. Hence every servlet that exists in the system would have to be configured in this web.xml. Soon, this becomes a big file with a bunch of configurations. Further, adding a new servlet would result in making changes to the web.xml file. This makes the application very flaky. If a new module is added, the web.xml would perforce need some changes to incorporate the configuration of this module.

## Solution With Conventions

The latest frameworks have used conventions to solve some of the configuration problems. Conventions have been a part of the framework designer's arsenal for quite sometime now but it is only recently that its role has been recognized. Here are some situations where convention has been successful. 

  * **Naming Conventions:** Naming class files and also configuration files in a specific way results in them being discovered automatically. For instance, the Java Persistence API (JPA) framework looks for the file "orm.xml" in all the jar files. If multiple orm.xml files exist, they get automatically discovered and their configurations read automatically.
  * **Implementing certain Interfaces or Annotating the Class** This is a less recognized but nevertheless powerful strategy. Certain super generic interfaces are implemented by classes implementing diverse services. These interfaces can be marker interfaces (in which case post JDK5 it makes sense to replace them with annotations) or proper interfaces with methods in them. The classes can get discovered by virtue of implementing these interfaces. For instance, Hibernate and JPA use this strategy to discover domain classes automatically.
