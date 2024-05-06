---
title: KPI - Key Performance Indicators
link: http://itmusings.com/architecture/kpi-key-performance-indicators
author: raja
description: Key Performance Indicators - how do we capture them automatically?
post_id: 20
date: 2008-02-10 19:36:03
comment_status: open
post_name: kpi-key-performance-indicators
status: publish
layout: post
category: code
home_page: false
image: /images/2008/02/slaprocess.png
---


One of the often over looked aspects in an application is the capture of Key Performance Indicators(KPI). As an application executes over a period of time, its performance needs to be monitored for determining bottle necks so that it can be engineered better in future iterations. The capture of KPI is itself a very challenging task. Typically, the most readily available KPIs are produced using logging frameworks. Everyone of us have sprayed our code with log statements that indicate the entry and exit times of various methods invoked. This is important but hard to utilize to determine anything meaningful. Besides it violates the Single Responsibility Principle (SRP) since all components would be tracking KPIs in addition to whatever else they are doing. The sophisticated solution would involve the development of a KPI framework that facilitates the capture of these indicators. This post attempts to analyze the requirements of a typical KPI framework. It also shows code that implements some of the framework components. The orchestration of these components into the actual application would become specific to the particular application and should be handled at the application level.  A decent KPI framework must aspire to do the following: 

  * It should seamlessly integrate with an existing application and be able to log metrics into a persistent store such as a database.
  * It should do minimal writes and that too, preferably in an asynchronous fashion, to the persistent store.
  * The persistent store must be "query-able" in a flexible manner. If the store is a database, this statement would mean that the schema for the database must support efficient querying.
  * It should obviate the necessity of individual components doing their own logging.
  * A desirable feature is to eliminate multiplicity of log records. For instance, let us consider a typical situation when a controller assumes charge of a particular request. It might invoke a service which resides in the business layer. The server may in turn use a DAO for doing database operations. This entire sequence of invocations might lead to 3x2 log statements - three entry and three exit time statements by the controller, service and dao layers respectively. This multiplicity complicates analysis since a lot of records need to be examined for making any decisions. Since each component logs the times separately, it becomes an arduous task to present a consolidated picture of the entry and exit times of each component given a single user request.
  * ![slaprocess.png](/images/2008/02/slaprocess.png)

## Design Considerations

The first thing is that every request, be it a customer interaction or any automated invocations either in the business layer or using web services, needs to be individually tracked for various times among different components. This is facilitated by using the notion of a request ID per request. There should be one record that should capture all these different metrics so that it can be persisted in one go to an underlying store. The different classes involved, are described below. 

## Main Classes

The [SLARecord](/code/kpi/SLARecord.java) is the chief domain object of this system. It consists of multiple SLA Time records each with its own start and end time as shown in the code. An _SLA Type_ is associated with each instance of SLATime. The SLA type determines what kind of SLA is measured. For instance is it the DAO time or the service time or the web time? The _SLARecord_ flows end to end for each request and is designed to be enhanced by various components with their entry and exit timings. So who creates it or enhances it? A common [SLALoggingInterceptor](/code/kpi/SLALoggingInterceptor.java) is designed to do both of these tasks. The SLALoggingInterceptor surrounds every logging component. How it surrounds it depends on the place it is hooked into the application. For instance, in the web layer it is implemented as a servlet filter. In Struts2 it can be implemented as a Struts2 interceptor. In an of late typical service layer orchestrated using Spring, it can be implemented as a Spring AOP interceptor. The _SLALoggingInterceptor_ creates an _SLARecord_ and puts it into a "context" if it does not exist already. This behavior of taking an SLARecord and putting it into a context depends on the actual situation. The diagram shows the end to end flow. The [SLALogger](/code/kpi/SLALogger.java) persists the SLARecord. There should be implementations of SLALogger to accomplish this persistence asynchronously or to a database. I have specific code for most layers. But let us see the advantages of using this approach as enshrined here.

## Comments

**[krishna](#1922 "2011-05-04 05:03:03"):** Hi Raja, You mentioned you have specific code for most layers. Is it possible for you to share the code? krishna

**[raja shankar kolluru](#1923 "2011-05-06 03:32:03"):** Here is some code for AOP. There are some dependencies but I tried to keep them minimal. (something on the AOP Alliance and the Spring framework) You can download it from [here](/code/kpi/logging.zip) This is mostly for the AOP implementation.

