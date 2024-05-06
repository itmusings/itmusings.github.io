---
title: Refactoring, Evolutionary Design & YAGNI
link: http://itmusings.com/management/process/refactoring-evolutionary-design-yagni
author: raja
description: The benefits of refactoring and Evolutionary design
post_id: 30
created: 2008-02-24 16:26:53
comment_status: open
post_name: refactoring-evolutionary-design-yagni
status: publish
layout: post
category: agility
image: /images/yagni.jpg
home_page: true
tags:
  - agility
  - refactoring
  - design
---


Most programmers practice it. Yet they don't realize its true impact. I am talking about evolutionary design.

I had always liked Martin Fowler's article on [evolutionary design](http://www.martinfowler.com/articles/designDead.html). I think he does nail the worry that was raised by many people ([including my colleague Tarun](http://tarunkohli.blogspot.com/2007/07/agile-crazile.html)) about how Agile may be USED as an exoneration of either deplorable or non existent design practices. Especially the agile tenets of just enough documentation and YAGNI (You Ain't Gonna Need It). In the article, Fowler mentions enabling practices of Agile such as continuous testing, integration and refactoring and how these enabling practices, when used in conjunction with YAGNI ,would foster an extensible design.

Continuous integration and testing fall under the banner of what I would call "continuous *" best practices since anything needs to be continuous for ensuring a successful outcome. I had covered these a little bit with my post on "[Project Automation](/management/process/project-automation)" in this blog. In this post, we are going to talk about my experiences with a recent project and how it could have benefited from refactoring.

I find that in many projects that my team is engaged on, there is no dedicated time allocated to refactoring. This is probably the single most important reason for dooming the project and thence agile. Take, for instance, a recent engagement that my team was involved in. There was this customer, who shall remain nameless, that wanted to build this huge portal site. One of the chief components of this portal is an Auction site. The Auction site required some key components such as an auction engine, payment gateway integration, notification engine to report on the status of the auction, auditing for each step in the auction process, a customer credit module that tracks the customer's credit limit and adjusts it according to the auctions that he is participating in, adhoc queries etc. Of course, add to this the fact that we should develop an AJAX enabled web site with the basics of customer information such as login screens, CRUD screens for maintenance etc. And Oh by the way - we also need to make room for two such portals - one for B2B and one for B2C interactions. The requirements were listed down as functional features. The functional features were spaced over a period of time and allocated to individual agile iterations. The team struggled through to implement the features. The first iteration used a nice abstracted application framework that jump started this entire process effectively. The team was trained and brought up to speed on the technology stack. Then the trouble started. The team barely had enough time to deliver some of the basic features in about four weeks when there started a torrent of requirement requests. The team was perpetually busy delivering functional features from release to release and never had the time or inclination to re-factor the code that was already written. As a result, when an audit was conducted in just three months time, I found basic design flaws. Controllers did all the work without delegating anything to business layers. Key parts of the framework that the developers were supposed to be using, were ignored. The fact that so much bad code was written so fast took my breath away! This application was legacy by design! Isn't agile to blame for this debacle with its emphasis on YAGNI and the fact that enough time was not spent in designing the application?

The chief problem in the project was that the emphasis was placed on functional requirements rather than QoS kind of horizontal requirements and much less - architectural requirements. These architectural requirements arise due to an agile recommendation - refactoring. Unless, the enabling practice of refactoring is followed, we cannot claim to be agile practitioners within the project.

The project cannot be planned with iterations emphasizing only on functional requirements without giving room for refactoring. This lapse in iteration planning was, in my opinion, the single most important reason for the debacle.

I also want to discuss on the agile aphorism about "Just enough requirements" a little bit more. Just enough requirements should mean that time is not dedicated for adding potential features that are not required today. This should not be taken to mean that we have to start from scratch on every new project. If I have implemented transactions consistently in a previous project, I want to use this knowledge in the current project as well. This includes using frameworks that have already implemented these features. Most horizontal requirements must be delegated to frameworks, that have already abstracted and implemented them in a generic fashion.

A few pre-requisites for agile in my opinion are:  
1\. The developers are good and understand or can be brought to understand the underlying process (not just the design of the system). This means that they understand project automation, agility and also look for avenues to abstract functionality.

2\. The customer understands the complexity involved and gives time for "refactoring" which is the basis for evolutionary design.

3\. The project manager has an absolute say in iteration planning. She determines what is good for the current iteration and what is not. Further, she earmarks the correct iteration for refactoring.

4\. Technical evangelists with the ability to do domain modeling exist in the project. These people are constantly looking for opportunities to refactor the existing code to comply to new requirements.

5\. I am a big fan of domain modeling and evangelizing an ubiquitous language for the project (See the book [Domain Driven Design ](http://books.google.co.in/books?id=7dlaMs0SECsC&dq=domain+driven+design&pg=PP1&ots=ulyR32R6t2&sig=V4lBbLYUsNy7QmJuuQLALq5gd_s&hl=en&prev=http://www.google.co.in/search?q=Domain+driven+design&ie=utf-8&oe=utf-8&rls=FlockInc.:en-US:official&client=firefox&sa=X&oi=print&ct=title&cad=one-book-with-thumbnail)\- Highly recommended) An agile project with an evolving domain model and language is almost sure to be a success.

In summary, by embracing the agile tenet of refactoring, we should be able to provide a design that evolves with the project and would eventually become complex enough to handle all the projects' needs.
