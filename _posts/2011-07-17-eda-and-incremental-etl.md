---
title: EDA and Incremental ETL
link: http://itmusings.com/architecture/eda-and-incremental-etl
author: raja
description: some of my early views on event sourcing, data lakes etc.
post_id: 612
created: 2011-07-17 18:00:47
comment_status: open
post_name: eda-and-incremental-etl
status: publish
layout: post
category: architecture
image: /images/etl.jpg
home_page: false
---

Event Driven Architecture (EDA) is a paradigm that I became familiar with when I was coding the earliest GUI components. The user interaction with a GUI application is modeled as a series of events that the application responds to. There is an "infinite loop" of events which can potentially be engendered by user interactions with a graphical application. The user responds to an application by potentially typing on the keyboard, clicking on certain segments of a GUI (such as a button for instance) with his mouse, hovering over certain regions etc. Individual GUI components respond to these events as they deem fit and perform various actions. Each c0mponent took charge of maintaining its own events and was able to invoke common services in response to them.

## A Segue - Enterprise Architecture & Systems of Records

Let us consider a different problem. Let us think about an enterprise with a large number of applications.

As the enterprise started fanning out to envelope multiple enterprise applications, it becomes imperative to reckon with a large number of different databases of information often called as systems of records.

For instance, let us take the example of a large enterprise that has thousands of employees.

There would be an employee system of record that stores details about the employees. There would be multiple applications that the enterprise might have to use to discharge its day to day activities. These applications might all require access to the employee database for both validation and other purposes. Ex: The accounting application might want to add an employee as an "owner" to each account.

## SOA & The Enterprise

How do these applications access employee information? One option is to use SOA and hide the employees data behind a service. The Employee Service is responsible for maintaining employees and facilitating updates to the central Employee System of Record. This approach is great but can have a few drawbacks:

  * If there is a batch process (such as a scheduled job) that iterates over multiple records in the database and for each record needs to obtain employee information, then this might suffer from the (n+1) selects problem that is mentioned elsewhere in this blog.
  * It is hard to enforce reference integrity constraints between an entity in a local database and an employee since the employee information is only available via SOA. Ex: How do we ensure that all accounts owners are valid employees?

These limitations force architects to lead to a "replicated data" mechanism to complement the SOA strategy.

## Replicated Data & ETL

The chief limitation of Replicated data is the potential for it to get out of synch with the system of record.  The system of record always trumps over replicated data elsewhere. Hence all changes need to be made to the System of Record first and percolated from there. If there is a change in the employee data, then the Employee System of Record needs to have the new data. The replicated data elsewhere would get the requisite feeds from the System of Record.

Extract, Transform and Load (ETL) process is used to ensure that the replicated data is periodically percolated from the System of record to the replicated system. The ETL process can be pretty elaborate and might extract modified data selectively (as opposed to extracting the entire data)  for merging into the replicated systems. The latency might become prohibitive for certain enterprises. It is possible that replicated data might stay out of synch for a long time thereby engendering other data integrity issues. Hence it makes sense to expedite the ETL process and make it as light weight as possible.

This gives raise to the Real time Incremental ETL strategy. ETL is used to percolate the changes as they occur in the real time thereby making the latency involved minimal. Event Driven Architecture (EDA) provides a great scalable solution for accomplishing this real time incremental ETL.

## EDA & Real time Incremental ETL

Event Driven Architecture strategies publish "events" when significant things happen in the enterprise. There might be a distinct Event to signify the fact that a user has modified his profile for instance. These events are published using some mechanism and are "listened" to by interested observers. These observers use these events to accomplish various things.

So as and when data changes in any system of record, it is possible to trigger a new event. This event is processed by observers who percolate these changes to other systems that replicated the data. Hence the ETL is accomplished during real time as and when the event of interest happens.

With the advent of the pub-sub (publish - subscribe) paradigm in various technologies such as JMS, it is possible to use these technologies to do incremental ETL. For ex: there might be a JMS "topic" that might receive the UserProfileChanged event. This would be subscribed to by a prospective subscriber which uses the information to update its own replicated database.

## Comments

**[raja shankar kolluru](#1936 "2011-07-26 01:11:44"):** Thanks Adit.

**[Adit Bhiday](#1934 "2011-07-23 13:38:53"):** Excellant explanation and example. Found this material to be more juicy than some of the material that I have read in books and other sites like theserverside.com.

