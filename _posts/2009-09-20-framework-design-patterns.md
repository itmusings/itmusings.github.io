---
title: Framework design patterns.
link: http://itmusings.com/java/j2ee/framework-design-patterns
author: raja
description: Some design patterns around building frameworks.
date: 2009-09-20 20:00:23
comment_status: open
post_name: framework-design-patterns
status: publish
layout: post
image: /images/framework.jpeg
home_page: false
category: architecture
---


Imagine a large building with its foundation resting on a bedrock. Now consider  the possible impact of doing any kind of repair work on the bedrock!  Sounds scary? Fortunately, the analogy does not fully extend to the realm of software in its full gravity. Yet this metaphorical mental escapade would bring some notion of the kind of impact on software if the framework on which it rests were ever to crumble or show perceptible cracks. But, thankfully software is more malleable than its structural counterpart. Consider the diagram below: ![framework_dependencies](/images/2009/09/framework_dependencies-300x212.png) The application depends on the framework which is responsible to call the application at various parts of the request processing life cycle. A few design and application development patterns abet the developer in writing an agile framework that is malleable and can change with time. This post discusses a few design patterns (chosen in no order of priority) that can go into the design of a software framework. **Callback Interfaces**:  Consider a framework that needs to handle all requests in a generic fashion but should allow the application to plugin at various well defined events. Call back interfaces fit that requirement to a tee and is probably the most used. Call backs originated in the early days of GUI. There were different frameworks which listened to events and called the applications for various important events. So you had call backs for button press events, window hover event etc. The Struts framework for instance, needs to call the appropriate Action class as and when a URL is hit. The Actions implement a call back interface which is used to invoke it at the appropriate time. ![callback_sequence](/images/2009/09/callback_sequence-299x300.png) **Template Method Pattern** \- Override Base Behavior in a sub class:  This is a different variation of the callback interface pattern.  Most of the behavior is implemented in a method in a framework provided class. The class itself has abstract methods which gets  during the course of  processing. This abstract method can be implemented in a sub class. So the sequence diagram is an exact replica of the sequence diagram for the call back interface with the "callback" participant replaced by a template method. Template methods can provide default and over-ridable implementations for various kinds of behavior. **Interceptor Pattern**:The framework calls a bunch of interceptors which are configurable for various situations depending on the framework. Ex: In Struts 2 different interceptors get called for different URLs. An interceptor interface is defined in the framework. The interceptor interface is implemented by the application. Certain default interceptors can also come pre-packaged with the framework. Interceptors are configurable by the application. These get called in a sequence to decompose a complex operation into more manageable units. This paradigm leverages the **"Chain of Responsibility"** pattern which is one of my personal favorites. **Reflection**: Most frameworks are the building blocks of applications. Application depends on a framework. Obviously, the vice-versa is not true i.e. a framework cannot depend on a particular application for that would defy the abstraction requirement which is the essence of a framework. Hence the framework can impose certain interfaces for the application to interact with it. It then has to "discover" the application automatically. This means that frameworks need to leverage some form of reflection. Typically, frameworks have leveraged reflection by using "**configuration files**".  These files define the class names of the application classes so that the framework can "reflect" on them and discover those classes during application run time. Profligate use of configuration files leads to code that can become difficult to manage. Of late, we are seeing increased support for language constructs such as "**Java Annotations**" to curb the increased proliferation of configuration files. **On Super Classes & Sub classes:** Frameworks leverage both super classes and sub classes to impose certain kinds of behavior. Ex: Consider this: 
```java   
    
    public class FrameworkBaseClass {
      public void execute(...) {
         // do some generic pre processing.
         doExecute(...) ;  // call doExecute
         // do generic post processing.
      }
      public abstract void doExecute();
    }
```    

The pure abstract doExecute() method is expected to be over-ridden in a sub class. However, the generic pre and post processing are done by the FrameworkBaseClass as and when the execute(...) method is called.   The framework in certain situations can generate sub classes as well. Consider the example of **hibernate** or some similar **ORM Framework**. The framework is expected to "lazy load" one to many relationships between entities. Ex: Consider the following: 
```java    
    
    public class Customer {
      .... // Define various fields with getters and setters.
      private List<Account> accounts; 
      public List<Account> getAccounts(){
        return accounts;
      }
    }
```    

  In this case, the Customer class defines a list of accounts which is then returned when the getAccounts() method is called. If accounts is configured in Hibernate for "lazy loading" then the class above will not suffice. Hence Hibernate creates a sub class for Customer as follows: 
```java   
    
    public class CustomerSubclass extends Customer {
      // over ride the getAccounts() method to take care of lazy loading.
      @Override
      public List<Account> getAccounts() {
        if (accounts == null){ 
        // fetch the accounts information from the database.
        }
        return accounts;
      }
    }
```   

Thus Hibernate leverages sub classing to provide specialized behavior for the Customer class. **So how are sub classes created dynamically by the frameworks?** Typically two strategies are used to create sub classes: 
1. Code Generation - where a tool is executed to generate code during the build. XDoclet or something similar is useful to achieve this.
2. Dynamic Sub classing: A tool such as **cgi-lib** is used to dynamically create a sub class for an existing class. **Other Patterns & Strategies:** A **Decorator** pattern (sometimes called **wrappers**) is sometimes used to "decorate" existing classes by the framework. This would enable the framework to over-ride behavior in the decorator. Examples include the Java IO Framework, some GUI frameworks etc. A **Strategy** pattern is useful for abstracting various framework dependencies as strategies.
