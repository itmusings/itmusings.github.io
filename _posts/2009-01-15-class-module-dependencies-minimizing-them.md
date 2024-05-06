---
title: Class & Module Dependencies - Minimizing them
link: http://itmusings.com/architecture/class-module-dependencies-minimizing-them
author: raja
description: Class dependencies - different types and avoiding some of them
post_id: 34
created: 2009-01-15 18:26:51
comment_status: open
post_name: class-module-dependencies-minimizing-them
status: publish
layout: post
category: design
image: /images/NoDependency.png
home_page: false
tags:
  - modularization
  - design
  - dependencies
---
They say that "no man is an island". This is more applicable to class design. Without dependencies, we are not writing any cohesive applications. With too many dependencies we have a mess. 

This post is going to talk about dependency management, interface based design, IoC , modularization and the like. I had hinted about the need for dependency management elsewhere in this blog but haven't thus far taken a lot of time to expound on it in depth. This post would cover this important topic. First and foremost, for this discussion I am going to classify the application classes into two types - model classes and strategies.  

## Model Classes

Model classes are containers of information. They don't contain business logic. Examples of these classes include: 

  * Domain model entity classes such as Customer, Account etc.
  * Maps with information. 
  * HttpServletRequest and similar classes 
Model classes have dependency on each other. It is because they are containers of information and there is a natural linkage between different types of information as is evident to anyone familiar with the relational model. Customers are linked to accounts which in turn are linked to other entities. Such dependencies are usually a necessary part of creating a domain model. Distributing these objects between different modules would not be discussed in the current post.  

## Strategy classes

The second type of class is the strategy class. This contains all the logic to manipulate the model. Most design patterns can be utilized to design these strategies. But now let us explore the different relationships that can exist between strategies.  Let us take a strategy class C1 in a module M1. Let us say it depends on strategy class C2 in module M2. Let us elaborate on what this might mean. The following types of dependencies might exist between two classes: 

  1. Static invocation: C1 uses C2 in a static fashion. something like C2.callStaticMethodFoo().
  2. Inheritance: C1 inherits from C2. 
  3. Composition: C1 uses C2 as a variable at either the class level or the method level. Something like "C2 c2 = ...;". 
  4. Instantiation: C1 instantiates C2. something like C2 c2 = new C2(); This is also a compile time dependency.
  5. Method Parameter or return  type: C1 accepts a parameter of type C2 in one of its methods or returns something of type C2 as a return value from some method. Something like "public void foo(C2 c2) or public C2 bar()".
  6. Exception Type: C1 throws an exception of type C2. as in public void foobar() throws C2 ;
Any of the dependencies above would inexorably tie C1 to C2. It would mean that C1 cannot compile without C2. I call these hard dependencies since the classes are linked during compile time as well as run time on each other. We are introducing the following problems with this kind of dependency: 
  * C1 cannot work with multiple implementations of C2. This may not sound like a big limitation to begin with but as the application evolves there would be a need to replicate C1 (with minor alterations probably) to work with another class that looks very close to C2. So you would end up having classes **C1'** and **C2'** both of which are close variants of C1 and C2. This would hamper re-usability.
  * By hard coding the relationship between C1 and C2, we are introducing problems while testing C1. C2 would always need to exist **and** perform correctly for C1 to be tested. We cannot do "mock testing" with a substitute of C2.
  * This approach might also introduce dependencies on a container. If C2 is dependent on container services such as connection pooling and the like then C1 also needs to run inside the same container. That is not a good approach since C1 itself is not using a container for performing any work. 
So keeping all these problems in mind, it is essential that we design to mitigate these issues by minimizing dependencies between strategies.  Let us consider each one of the dependencies above and see how each one of them can be alleviated by applying design principles.  

## Avoiding Dependencies

Static Invocation: The static dependency would warrant its own approach which is documented in the [evil static method post](<? echo get_permalink\(91\);?>) within this blog.  Inheritance: Inheritance is justified for an "is A" kind of relationship as documented in most design books. Else in most cases, composition is much more effective than inheritance. I am not going to delve too deep into this either. Exception Type: This is not a serious dependency issue and hence would not be covered here. Composition/Parameter type: If the composed class or the class that is being accepted as a method parameter (C2 in the case above) turns out to be a model class then this is not a big problem. However, if C2 is another strategy class then the issues mentioned above would crop up in their full glory. Hence it is essential that this dependency be handled using well established patterns. The most important text book strategy to obviate this is to use interface based design.  If class C2 gets replaced by interface I2 then the relationship gets re-stated as C1 -> I2. Now how is this better? It is better because any implementation of I2 would now suffice instead of just C2. Hence all the problems that I have alluded to before would just get solved by interface based design.  So finally, we are down to one. The instantiation problem. 

## Instantiation Problem

The instantiation problem sticks out like a sour thumb. The problem is we need to depend on the actual class for instantiating it. This would introduce a dependency on the actual class itself and hence has the potential to re-introduce all the problems that we mentioned before. People tried to resort to the factory pattern to resolve this problem. The factory has a method that would return an instance of some class that implements a certain interface. In short if I want an instance of Interface I2, I can make a call to the factory. It would then return me a new instance of class C2 which indeed implements this interface. Thus C1 would only depend on the factory and not on the actual class C2. Right? Unfortunately this does not solve the problem because dependencies are transitive. Now C1 would start depending on the Factory which in turn depends on C2 (it is instantiating C2 remember?) Hence C1 indirectly would depend on C2. So this does not solve the problem and only ends up creating an unnecessary indirection.  A detailed discussion of various approaches to solve this problem would end up bloating this article. Instead, we will just look at the solution. 

## Inversion of Control

Class C1 knows that it has to instantiate an instance of interface I2. It does not want to instantiate C2 because it then would become dependent on C2. So, instead of doing anything about it, C1 does nothing! Yes, it does not instantiate anything. But how will it work? How will it invoke the methods of interface I2 without getting a NullPointerException? The solution is Inversion of Control. Class C1 expects some container to materialize an instance of interface I2 and then inject that instance within itself. To enable that to happen, it provides a setter or constructor for injecting the implementation. This approach is called Inversion Of Control since the class C1 is inverting control to the person who is instantiating it. This is an incredibly smart way of solving the instantiation problem. Detailed discussion of IoC is beyond the scope of this article but the idea that I wanted to convey is that IoC removes dependencies between two strategies by getting rid of the instantiation problem.   Thus we have a solution to eliminate all the dependencies that I have allude to before. Finally, let us look at the dependency management and modularization. 

## Modularization

## Comments

**[Gaurav Malhotra](#1547 "2009-01-23 20:28:51"):** Did you get a chance to look into Spring Integration framework... which let's one implement Enterprise Integration patters like router, splitter, resequencer etcetera.. With ease.. and with spring well know programming model... What do you think about using Enterprise Integration patterns to tie up modules (components)....hence reducing module dependencies....?

**[raja shankar kolluru](#1549 "2009-01-24 11:01:29"):** Yes Gaurav.. Spring by itself takes care of module dependency alleviation to a large extent. I think an IoC contract such as the one provided by Spring (coupled by a few more conventions such as standardizing on some interface to implement or the location of the spring files etc.) is vital for modularization to work. But once you supplement this with some kind of integration framework, then your modularization process is almost perfect save for some versioning stuff that something like OSGi would help take care of.

