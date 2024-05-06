---
title: Opspeak
link: http://itmusings.com/management/operational_viewpoint
author: raja
description: Operational view point - the often overlooked but increasingly important view point
post_id: 845
created: 2013-05-07 03:36:58
comment_status: open
post_name: operational_viewpoint
status: publish
layout: post
image: /images/2013/05/Ops.jpg
home_page: true
category: architecture
---

It is one of software’s little ironies that most architects would fervently wish to see their software run forever and yet fail to foresee how the system would be maintained after it goes LIVE.

This is where the [operational viewpoint](http://www.viewpoints-and-perspectives.info/home/viewpoints/operational/)shines. This view point could gently steer the straying architects obsessed with functionality back on course and ensure that the system maintainability characteristics are handled as well . Operational viewpoint deals with a set of concerns relating to the maintenance of the system in a production environment by the "Ops" folks. Typical questions that need to be considered in designing a system for operational use include:

  1. How can the system be upgraded?
  2. What diagnostic information is available?
  3. How can I know what objects are stored in cache? How can I refresh the cache if needed?
  4. How can I back up the database?
  5. How will I be able to reconcile between different parts of the system?
  6. What kind of logging is supported by the system? How can I increase or decrease the logging level?
  7. etc. 

A classic example of the importance of this viewpoint hit me recently when I was engaged with a big  client. We were superintending the delivery of an E-commerce solution that spans across multiple vendors. One of the vendors was all set to introduce an [Order Management System](http://en.wikipedia.org/wiki/Order_management_system)(OMS) for our client. But from a service readiness point of view, we found yawning gaps in the design of the system - gaps that could potentially take us back to the drawing board. The orders that were captured by the e-commerce system had to traverse through multiple layers across two data centers across a myriad of message queues. If the order gets lost anywhere in this maze, there was no trivial way to track it. This would definitely hamper the maintainability of the system with a potential bombardment of the ops folks by angry customers whose orders have not reached the OMS! What good is a system if it does not guard itself against aberrant orders with a propensity to go AWOL?

Obviously I am exaggerating a little bit here. A well configured system should hardly be unstable enough to lose a lot of orders. However, in an Enterprise wide application that has so many points of failure, it is not unusual to mis-configure something somewhere. In an e-commerce system that spans across multiple fulfillment providers and order capturing systems, we should develop fool proof  "checks and balances" whereby all orders are reconciled between different parts of the system periodically. In this case, most orders originate from a leading e-commerce marketplace. So it is imperative to ensure that the number of orders that exist in the marketplace system match the number of orders that are received by the OMS which are in turn duly transmitted to the respective fulfillment providers. This kind of a reconciliation process needs to be designed upfront and cannot be slapped together as an after thought. Further, an order in the marketplace can be broken down into multiple sub orders. Hence it is also important to ensure that all the sub-orders are reconciled as well. This meant certain changes to the messages flowing through the system. We need to pass the order Id for instance with every message that flows in the system. Alteration of message structure with days to go before GO LIVE is an invitation to disaster. Hence in this case, we had to design a series of scripts that probe different parts of the system and attempt to reconcile the orders in all the parts. If the system designers had considered the Operational viewpoint in the upfront design, this entire kludg'y situation could have been averted.

We as architects would be frustrated when called upon to diagnose some run time issues in a system only to find that there is absolutely no way to obtain diagnostic information. This is because the designers have not planned for the correct amount of logging, auditing or management tools. A technology like JMX (Java Management Extensions) needs to be used to enable the requisite amount of instrumentation of the system. Yet most production system designers have not even heard about JMX! This entire design smacks of indifference to the people who would ultimately maintain the system.

There is also another interesting political ramification in not considering the operational view point upfront. In most organizations, the operational folks who maintain a system are often different from the creators of the system. So quite clearly, non consideration of the operational view point would be seen as an attempt at deliberate obfuscation by the creators of the system. While a little attempt at obfuscation might make someone indispensable, it is obviously not in the long term interest of anyone. 

_So long story short, if we don't plan for the operational view point, it is not going to take care of itself - hardly a revelation but nevertheless often overlooked. So the next time you design a system, it pays to pause and ask yourself if this system can be maintained by both the ops and the support folks. Is there a way for instance to know the contents of the application cache? Is there a way to diagnose what is the slowest part of the system? Is it possible to reconcile different parts of the system? It is desirable if organisations can make a checklist and run their architecture by it to ensure that they are creating an operationally viable system._
