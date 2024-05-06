---
title: Java & Performance
link: http://itmusings.com/java/java-performance
author: raja
description: Performance considerations in Java Garbage collection
post_id: 15
created: 2008-02-08 09:26:38
comment_status: open
post_name: java-performance
status: publish
layout: post
category: performance
image: /images/performance.jpg
home_page: true
tags:
  - java
  - performance
---

I attended a **No Fluff Just Stuff** conference held at Newark,NJ in August 2006. This post is a condensation of what Brian Goetz mentioned in the course. One of the topics that was presented out there was about Java and performance. That seemed odd since Java is not usually mentioned when the topic of performance comes up. That is the time when the Java evangelist excuses himself (or herself) and takes a small break while the C/C++ advocates unleash their spiel. But all these (un)popular urban myths are getting falsified with the new JVMs that are coming up. The Java 1.5 VM is superior to its predecessors in many key respects. The new ones promise to be even better so much so that we should not be surprised if Java outperforms C++ in many respects.  We are going to examine the Java performance improvement over the years in the following areas: 

# Object Allocation & Garbage Collection

In JDK 1.0 object allocation was very slow. So was garbage collection. Allocation required _first-fit/best-fit coalescing_. What this means can be depicted in the picture below. ![perf-1.jpg](/images/2008/02/perf-1.jpg) Let us say that the JVM has acquired 100 bytes for the heap out of which some of them have been used as shown. A request for 12 bytes of data now requires the JVM to compact the heap and redistribute the objects so that the request can be satisfied. This gets to be very expensive with time as more objects get created and destroyed. In this case we have about 35 bytes of heap space but still cannot satisfy a request for 12 bytes without requiring the heap to be compacted. This problem tended to lead to the rule that objects should not be proliferated. This lead to various anti-patterns such as object pools. Read about object pools [here](http://itmusings.com/?page_id=17) In this example A, B, C and D are long lived objects that happened to share the same heap as short lived objects X, Y and Z. Hence when the short lived objects got cleared, the heap had to be compacted. In actual applications, it was observed that over 95% of the objects have high _infant mortality _(i.e they are short lived). This means that combining short lived and long lived objects into the same uniform heap is counter productive. 

## Generational Garbage Collection

From Java 1.2 onwards the trend started towards making **generational garbage collectors**. These substantially increased performance. In generational garbage collection the heap is divided into multiple parts. A discussion on the parts of the heap would be beyond the scope of this article but for all practical purposes we can visualize the heap as being made of two parts viz _young generation_ and _old generation_. New objects typically get their memory from the _young generation_ heap. Once this heap fills up, the objects get moved to the older generation. All objects dont have to be moved since most of the objects would have already become eligible for garbage collection. Hence only the extant objects get moved from the young generation to the old generation heap. Since most objects are short lived the time involved for transferring the extant objects is very less. This also automatically compacts the heap. For more information, please see [Brian Goetz's article in the IBM developer works.](http://www-128.ibm.com/developerworks/java/library/j-jtp09275.html)

### Thread Specific Heaps

In accordance with good programming practice, it is a good idea to create new _context_ objects for each thread and re-use command objects (the objects that actually perform the work) as *stateless singletons*. Hence for instance a Struts Action is a command object that gets reused across multiple threads while the Struts ActionForm is a context object that gets created once for every thread. If this programming model is adhered to as a best practice, it would be observed that there would be many objects created in the course of executing a thread. These objects would typically be eligible for garbage collection at the end of the thread. Hence a thread specific heap that allocates all "new" requests within a particular thread from one particular heap which gets garbage collected at the end of the thread, would scale very well. 

### Escape Analysis

_Escape analysis_ is a powerful technique that the JVM can employ for optimization. In fact, the Sun JVM with Mustang (JDK 1.6) has already implemented this technique. To illustrate this, look at the snippet of code below: 
```java  
    
    public class Foo {
    public void bar() {
    FooBar fb = new FooBar();
    // do something with fb
    // but dont copy fb to any field variable
    // dont update any array or collection from fb.
    // dont pass fb to any method. for instance dont call some method
    // baz(fb)  within this method.
    return;
    }
    }
```
A JVM can now analyse the variable "fb" and understand that it is not being outside of bar() because it is not copied to any field in class Foo nor is it being returned or updated into any array or collection. Hence it can deduce that this variable can be allocated within the "stack" of method bar() rather than in the heap. This way the JVM can avoid garbage collection. This is an example of escape analysis. 

### A comparison with C/C++

Memory allocation in Java is sophisticated as we have seen. But these techniques can in theory be replicated in a library in C/C++ and hence they can aspire for equalling Java's memory management performance. But the malloc implementations which actually allocate the memory in an operating system are not anywhere close to the JVM in their sophistication. As a consequence,it has been observed that the object allocation in Java is significantly faster than C/C++. 

# Dynamic Compilation

Java is interpreted code for the most part. The source code is compiled to a byte code which would still need to be interpreted by the JVM. But today's JVMs take this one step further. They interpret the code for a while. Afterwards, the code gets compiled in the background to machine instructions and the compiled code is executed instead of the interpreted code. Once this happens, the java code is at least as performant as a C/C++ code that has been compiled. The compilation that is being done is by the JVM. This is an example of Dynamic Compilation as opposed to static compilation using a static compiler such as _cc_. But it gets better now. 

### Speculative Optimization

Dynamic compilers such as the JVM have more information about how the class is being used. For instance, the JVM is aware of all the classes that are getting loaded in this run. This knowledge can be used for doing **Speculative Optimization **\- i.e optimization done with a certain amount of speculation. These speculations can get invalidated as the program executes and at that time they can be backed out. Here is an example: 
    
```java  
    public class Foo {
    public void baz(){
    // do something.
    }
    
    }
    
    public class Bar extends Foo {
    public void baz() { // override baz in Foo
    // do something else here.
    
    }
    }
    
    public class Main {
    public static void main(String[] args) {
    Foo foo = new Foo();
    loop(foo);
    foo = new Bar();
    loop(foo);
    }
    
    public static void loop(Foo foo){
    // use foo in a BIG Loop.
    for (int i = 0; i < 1000000; i++){
    }
    }
```
In this example, Foo declares a baz() method that gets over-ridden in its subclass Bar. Main first uses Foo to do some complicated computation. Then it uses Bar to do the same thing.
