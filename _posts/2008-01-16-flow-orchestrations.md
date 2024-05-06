---
title: Flow Orchestrations
link: http://itmusings.com/architecture/flow-orchestrations
description: On the command pattern and externalizing control of program flow
author: raja
date: 2008-01-16 18:19:27
post_name: flow-orchestrations
status: publish
layout: post
category: architecture
image: /images/orchestration.jpg
home_page: true
---
## BACKGROUND

Most enterprise applications follow a layered architecture. The best practice is to layer an application as a UI layer and the Middleware layer which in turn interacts with an EIS (Enterprise Information Services) layer . When implementing Middleware services, it is common practice to use the [Session Facade](http://c2.com/cgi/wiki?SessionFacade) pattern. The simplistic version of the facade pattern is to provide a _request specific_ facade that in turn accesses underlying _domain specific_ services. The facade abstracts the underlying model and also provides additional _horizontal services_.  The underlying domain layer itself is designed to be utilized by multiple facades. Hence the domain layer must refrain from providing most horizontal services by itself but should integrate with what is initiated by the facade. Example: Transactions. The domain layer should not introduce new transactions but should instead integrate with the transactions that are initiated by the facade. The facade as a consequence, becomes quintessential to the implementation of a robust middleware services layer. This introduces the following problems:
* The consuming client (UI Layer in this case) is tightly coupled with the facade's interface which sometimes may not be desirable.
* The consuming client has to be aware of the fact that a new facade was introduced and accordingly know how to call it. (with a proxy or by using a service locator)
* Facades get bloated since they have to take care of all the horizontal services. This is usually solved by using AOP.
* Facades can get fine grained by providing a lot of methods each one adding to the facade interface thereby bloating the interface also.
* The facades cannot be configured or instrumented during runtime. This is because all the logic is in java code and it is cumbersome to change the way the code functions without further bloating the facade. (adding if statements and so on) 
* In a typical enterprise, there can be a lot of services that would be needed to be exposed. These services may in turn need to be combined to form composite services. A facade calling other facades gets to become cumbersome to implement.Attempts have been made to correct this problem by writing utility classes that provide the horizontal services and calling these utility classes from the facade. This does not fully solve the problem since the facade still needs to be coded to call these utility classes.In short, using the facade to provide all the services tends to lead to unmaintainable code that cannot be instrumented. We use an old idea to solve this problem.

## Command

The first step in solving the _bloated facade_ problem is to design fine grained components that provide one service at a time. This is in line with the [Single Responsibility Principle(SRP)](http://en.wikipedia.org/wiki/Single_responsibility_principle) (which states that a component should have one single responsibility so that it should be changed because of one reason and not multiple reasons). Our bloated facade would not follow SRP since the facade implements both the request specific functionality and also the horizontal services. Hence it has to change if the request specific functionality changes(which is acceptable) or if one of the horizontal services change(not acceptable). So we introduce the notion of a _command_. We define a command as _a stateless component or class with a single responsibility that provides a reusable service_. (This harks back to the question. Can commands be stateful? The short answer is YES but the functionally scalable answer is NO.  This is a whole different discussion that I would get into in another post)This can be a horizontal service or a request specific service. Hence by this definition the facade itself becomes a command that provides a request specific service(and nothing else). The commands should be stateless since they can then be easily deployed in multi-threaded environments. Now to service a request would involve a colloboration between these commands. The commands are _chained and wired_ to the incoming request using the _chain of responsibility_ pattern.

## Chain of Responsibility

An architectural idiom that pervades through Data Flow Systems is that of "Pipes and Filters". The pattern itself forms the backbone of UNIX shell scripting. There is also an allied design pattern called the [Chain of Responsibility](http://c2.com/cgi/wiki?ChainOfResponsibilityPattern) pattern. The classic version of the Chain of responsibility pattern constructs a chain of commands. Every request is passed through this chain till one or more of the commands respond to the request. The commands have the chance to return a response and stop the further processing of the chain. We adopt the chain of responsibility pattern but retain more of a flavor of the _pipes and filters_ approach i.e. we set up a chain of responsibility of commands, but allow multiple commands to mutate the input.

## Context

In UNIX systems, the filters read standard input and write to standard output. Hence a byte stream is exchanged between communicating processes. The processes themselves are not aware of the exact flow that has been set up using pipes. But they communicate with each other by reading the input byte stream and generating an output byte stream. We need a java equivalent to facilitate communication between our commands. This is the Context object. It would contain certain attributes that facilitate the flow. It also should contain some typical request and response attributes. (for instance pagination context, sorting context etc) Specific request - response flows should inherit from this base Context. This context should also contain a map so that it can be used for storing request and response domain objects without the need for extension.

## Chain, Router & Orchestrator

A chain of responsibility implementation requires various components that share the responsibility of setting up the chain and co-ordinating the way individual commands function within it. An **orchestrator** is a command that initiates the chain of responsibility. The simplest implementation of an orchestrator is as a simple command that stores the first command in the chain within itself. The orchestrator delegates all execute() requests to the first command in the chain. Hence in code

```java
    public class Orchestrator implements Command {
    --
    private Command firstCommand;
    public Context execute(Context in) throws Exception{
    return firstCommand.execute(in);
    }
    }
```
Orchestrators can also provide a "catalog"ing functionality. A **catalog** is a list of named commands that participate in the flow. Orchestrators also typically store a catalog of CommandDescriptors. A CommandDescriptor contains meta-data about the command. This catalog of descriptors allows for management of the flow from a user interface (such as an Instrumentation Panel or JMX). An **OrchestratorConfig** is a component that undertakes the task of assembling the various commands into the flow. An orchestration config may have the ability to parse a flow definition language(FDL). It can also rely on other IOC(Inversion of Control) frameworks such as Spring to set up the flow. A **chain** is a command that supports a list of linked commands. These commands are executed in sequence by the chain. The commands have the ability to stop further processing of the chain if desired. Alternately, the commands can choose to be unaware of the fact that they are participating in a chain. A **router** is a command that has several commands attached to it. The router passes control to one of the attached commands based on the context that is being passed.

## Integration with DI

## Comments

**[Gaurav Malhotra](#1552 "2009-03-07 08:39:43"):** Having shared white board session on the above topic and using your brain child in my current project/other project(s)... It was pleasure reading this article...and reliving white board session with you.. fab article

**[Gaurav Malhotra](#1553 "2009-03-07 08:42:22"):** Also this framework ... is blessing if used with your Service Identity concept (subject/operation design pattern) Please write article on that alss

**[raja shankar kolluru](#1556 "2009-03-10 03:57:50"):** Thanks Gaurav. This article does require augmentation from white board discussions :-) I have been meaning to break it down into multiple articles but haven't had too much time to do that. I also agree that I should cover the subject/operation pattern that has been a core design tenet of mine in a lot of architectural overhauls.  more work to do. But I am sure glad that it is benefitting somebody.
