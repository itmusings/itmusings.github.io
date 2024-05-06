---
title: SOA & Programming 
link: http://itmusings.com/architecture/soa/soa-programming
author: raja
description: the evolution of SOA from basic Programming concepts
post_id: 38
created: 2008-07-23 10:36:12
comment_status: open
post_name: soa-programming
status: publish
layout: post
image: /images/soa.png
home_page: true
category: architecture
tags:
  - architecture
  - pattern
  - soa
---


There are heaps of blog posts on SOA. So I would not add to their number by posting another article that extols the virtues of SOA or for that matter even attempt to define the term. But I wanted to write an article here about how programming itself metamorphosed over a period of time to SOA. This understanding, I believe, will make us better appreciate SOA principles and apply them in real life. One of those days, I went back to the drawing board (BTW is the programmer's equivalent to the drawing board an IDE?) and started ruminating about how I started to learn programming.

## Monolithic Programming

I was initially given a computer which I was told is a gizmo that obeys instructions written in some "programming language". I had to write "code" in a series of "steps" so as to instruct the computer to perform "tasks". In this process, I started to break the task into "steps". Each "step" was sometimes a task in itself. So I realized that tasks could now be broken down into sub-tasks. The sub-task itself often turned out to be complex enough to need its own "sub-sub-tasks" and so I had to create sub-sub-tasks. This was getting way too complex in my head. Wait a minute I thought. I have a task which contains sub-tasks that contain sub-sub-tasks and so on and so forth ad infinitum. This is all great but where does this chain end? It ends when the task is so simple that the "programming language" itself supports it. For instance, if I want to add two numbers then this task is so simple that the programming language compiler itself supports it as a fundamental operation. Viewed this way, it turns out that programming is a "recursive" operation. I need to write "tasks" which can contain other "tasks" that can in turn contain more "tasks" and so on. I call this the monolithic programming approach ( my term actually). In this approach, we just write a set of instructions to execute a set of tasks. In code form, this is what it looks like. 
```bash   
    # code to accomplish task1
    sub-task1 code
      sub-sub-task11
      sub-sub-task12
    sub-task2 code
      sub-sub-task21
      sub-sub-task22
```
Quite clearly, sub-task1 code cannot be called from outside of task1 code. In short, reuse is out. To solve this problem, many programming languages started introducing structure programming techniques. 

## Structured Programming & Tasks

With the advent of structured programming all these tasks started manifesting themselves as "functions". These functions can in turn invoke other functions. The good thing about functions is that functions can call other functions or even themselves recursively. This gives them a great flexibility and improved re-use. So here is some code that uses functions: 
```c   
    
    task1(){
     subtask1();
     subtask2();
     subtask3();
    }    
    
    subtask1(){
     subsubtask11();
     subsubtask12();
    }    
    
    subtask2(){
     subsubtask21();
     subsubtask22();
    }    
    
    subtask3(){
     }
    // stubs for other subsubtask11.. etc. have been skipped.
```
## Object Oriented Programming

Then came Object Oriented Programming which started this whole process of encapsulation. Functions got lumped together along with the data they needed. But very soon, it was realized that the functions had to utilize data from multiple objects. Now how does this work? Thus was born the "strategy" pattern. The strategy was the encapsulation of just the algorithm. It might have its own associated data as needed by the algorithm. But the data needed by the actual operation that is key to the strategy is actually passed to it in the invocation. Hence strategies became stateless i.e. they did not carry any state of the particular invocation and hence can be re-used multiple times in several simultaneously running threads. I had argued elsewhere (in my presentations on dependency injection) that viewed this way, an object oriented program can be thought of as a collection of strategies that are wired together to achieve the program's functionality. Some code using strategies: 
```java  
    
    public class Task1 {
      public void execute(){
         SubTask1 s1 = new SubTask1(); // UGGH.. strategy  instantiation
         s1.execute();
         SubTask2 s2 = new SubTask2();
         s2.execute();
         SubTask3 s3 = new SubTask3();
         s3.execute();
     }
    }  
    
      public class SubTask1() {
         public void execute(){
           SubSubTask11 s11 = new SubSubTask11();
           s11.execute();
           SubSubTask12 s12 = new SubSubTask12();
           s12.execute();
         }
      }
      // and so on...
```
The interesting thing here is that each strategy now has the ability to invoke other strategies and achieve its goal. Notice that most code is written only to just orchestrate this process (i.e. creating Sub tasks ( or sub sub tasks) and executing them) 

## Strategies & Services

The final twist in the tale occurred when some of the strategies naturally became "services". Remember that strategies are for the most part "encapsulated functions( more correctly encapsulated algorithms)". But these strategies could become "services" if they get executed in their own process space rather than in the process space of the calling program. For instance, let us say I have a "sorting strategy" which encapsulates the bubble sort algorithm. This strategy can run in my own process space in which case I would just make a library call to the strategy whenever I want some sorting done. But once it becomes a sorting service, then I would have to make a network call to accomplish this sorting. Now why would a local strategy become a "service"? If it chooses to become consumable by a wider audience outside of the process space. However, this has to be done with caution since network latency can considerably damage the SLAs (Service Level Agreements ) of the current system. 

# SOA & Service Orchestration

Since SOA exposes services, the ability to orchestrate these services can be done within a service itself or delegated to an external agency such as a service orchestrator. Sophisticated service orchestrators include BPEL (Business Process Execution Language) based engines. These are called by various names such as choreographers, orchestration engines, Business Process engines etc. Orchestrators remove the service orchestration part out of the core code thereby enabling different processes to be created using existing SOA services. This is a great feature. Enterprise Service Buses also provide a variation of this theme. The chief value adds of an ESB include network latency and the ability to execute processes asynchronously. Now that has become a much bigger article than I had anticipated. But what I wanted to do was to highlight the fact that programming has stayed pretty much the same over a period of time. But the added persona which entered into the picture have just tended to complicate the understanding of some of the newer tenets - SOA being one of them.

## Comments

**[Sushil](#1506 "2008-10-02 04:52:36"):** Good post. Checked your blog today after a while and couldn't resist posting this comment as I saw some part of the post bringing back memories of our old association.

