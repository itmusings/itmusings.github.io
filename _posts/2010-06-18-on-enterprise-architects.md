---
title: On Enterprise Architects..
link: http://itmusings.com/architecture/on-enterprise-architects
author: raja
description: What should enterprise architects be doing and probably arent
post_id: 575
date: 2010-06-18 18:18:55
comment_status: open
post_name: on-enterprise-architects
status: publish
layout: post
category: architecture
home_page: false
image: /images/ea.jpeg
tags:
  - architecture
  - enterprise
---


So I had yet another meeting with an enterprise architect today. We were selling a solution to their company which is one of the top few companies in the world in their professed field of expertise. Let us call this guy John to give him an identity despite the fact that, to me, he looked like Yet another faceless EA who threatened to morph into the background. John brought along someone else whom I would christen Little John. Little John is presumably mentored by John and keeps constantly glancing at John to secure his tacit approval for whatever he is doing.

  
I guess today was exam day for Little John and he threw at me a barrage of questions about the solution that I was advocating. How do you ensure that there is synergy between the proposed solution and the rest of the enterprise? What is your approach on SOA, Web Services etc. ? How do you ensure that a UDDI system exists which serves as the enterprise directory? How do you ensure an upgrade path ? etc. As I kept getting interrogated I could not help noticing the fervent glances from Little John at his mentor to seek his approval at his line of questioning. I know that he was asking the right questions. There certainly was no dearth of the correct words - synergy, co-ordination, loose coupling, joined at the hip, low hanging fruit - were liberally splattered in the conversation. But the whole show smacked of something that has been staged. It looked like a WWF match gone wrong! All the right questions were asked as suggested by the Encyclopedia of Enterprise Architecture (if one such thing exists) . But they lacked substance since the questioner did not know the purport behind what he was asking. Such has become the sorry state of affairs in EA! I visualize an enterprise architect as a preacher who takes over the pulpit and comes up with these cute drawings without exactly knowing about the impact of what he is talking about. Obviously, this is not a general sweeping generalization of enterprise architects as I had personally benefited from my interactions with a few of them. But there is a strong stereotype that is associated with an EA which is an outcome of these kind of experiences. ![](/images/2010/06/preacher.jpg)

## The Application and the Ecosystem

You don't need this article to tell you that isolated applications are a thing of the past. No enterprise application exists in a vacuum. Synergy and all that good stuff are extremely desirable - nay, mandated in today's application development. Of course, the industry has risen to the occasion! There has been no dearth of standards, products and specifications about enterprise architecture. First, there were these design EAI patterns - router, queue etc. Then there was a wave of products - different versions of MQ, ESBs etc. Then there was a flood of specifications - some of them more formal under the auspices of a body like W3C while some other were more informal. We saw SOA, Web Services in its different incarnations, REST and then the rest no pun intended. In short,there was an attempt to take an enterprise view of the ecosystem rather than evolving an ecosystem as a conglomeration of individual applications.

The result of this immense research was socialized to enterprises. There were people who were formally designated to be the recipients and bearers of this tradition. These specific individuals were hand picked due to the proximity to the CTO, CIO etc. or due to the fact that they first jumped into the game and proclaimed themselves as Enterprise Architects. Or just because they were the best developers in the entire enterprise. Be that as it may, these individuals acquired a sinecure position in the enterprise. The unfortunate fall out was that most enterprise architects became the spokesperson of some organization promoting some standard or selling a product. At worst, they became application police ensuring that all applications are compliant to the standards that were laid out for the enterprise.

## The Gap

I feel that the yawning gap between application architects and the enterprise architects lies in the integration of the application to the enterprise. For instance, let us say that we have an application that is responsible for the CRUD operations on employeesin the enterprise. Services would then be exposed to allow the application to perform the CRUD operations. But what if they have to be exposed as SOA as per the tenets of the enterprise? The temptation is to go ahead and do so without accounting for the penalties that such a decision entails! Now all CRUD operations become remote resulting in the general slowness of an application which is actually implementing a very trivial requirement. Further, this approach is fraught with other optimization bottlenecks such as the (n+1) selects problem- more on this in another article. In general, there is a considerable dissonance on application optimization and enterprise architecture resulting in the scraping of many of those overall guidelines by application architects gradually resulting in an erosion of the ideal itself. There were multiple reports in various forums bemoaning about failed SOA implementations and questioning the validity of such an approach.

The key is to bridge this gap in understanding by suggesting techniques to provide the best of both worlds. For instance, I had successfully developed a light weight orchestration framework that can handle both collocated and remote services seamlessly. This had made the task of developing a fast SOA application very easy. So in my particular enterprise, SOA was no longer a four letter word with three letters! But to do this, we need to wear both the application and enterprise architect caps at the same time and ensure a way of navigating along that fine line between them.

## Canonical Data Model Definition

The definition of a canonical model is critical to an enterprise. The model serves as an ubiquitous language that pervades through the enterprise. Often, the standardization of the canonical data model is one of the first requirements for the fostering of an enterprise wide standard. This is not considered seriously enough in many enterprises. Hence individual applications publish their own WSDLs and it looks like the enterprise is all set since we have an environment that supports XML. XML is the magic word now due to its promotion by the W3C in its various SOAP standards etc.

In most enterprises, development and deployment platforms have already been standardized to a large extent.  It is one of either J2EE or .NET or PHP or RoR or one of the numerous other alternatives available. It is therefore extremely beneficial to standardize the language as well so that all other applications model entities in a semantically compatible fashion. XML is NOT a standardized language. XML is useful as a carrier of data but is hardly suitable or convenient to be used as an enterprise wide language. It does not have the expressiveness of a typical object oriented language. It is hard to benefit from inheritance, polymorphism and other features that we take for granted in today's languages. Therefore the standardization of a canonical data model in an object model accessible from the standardized platform, facilitates the use of common code in the enterprise. Besides, it also removes the need to do multiple transformations as one system speaks to another in the enterprise. They all share the same language and hence objects can seamlessly flow from one system to another. Since object serialization is a lot less expensive than XML marshaling and unmarshaling, this strategy can yield very performant systems.

## App Optimization

SOA has its place in the enterprise. But it also needs to be supplemented by other enterprise patterns such as ESB, Message Queues , Message topics, Pub-Sub architecture etc. We need to ensure that asynchronous services are enabled with end point transparency i.e. the clients for a service do not need to know the end point at which the service is published. It just enters the service through a common well known end point in the enterprise and from there on the client must be agnostic of how it gets routed.

Extensive guidelines need to be provided for using one method vis-a-vis the other. Pre-mature optimization is the root of all evil laments Knuth supposedly. But without premature optimization we might as well go back to manual computation! Enterprise guidelines aspire for an ideal which swings the pendulum away from application responsiveness towards some visionary unpractical ideals.

Latency is another delicate issue to deal with. Everyone wants immediate updates to all their enterprise system without realizing that the associated machinery required to accomplish such a feat might become well nigh un-budgetable.

Enterprise architects must step in to these breaches and take care of tempering their utopian enterprise architecture with a dose of reality. Until then, EA would stay theoretical.

## Comments

**[Gaurav](#1764 "2010-06-19 06:00:06"):** Hi Raja, I would also like share an interesting observation in one of the major product company here in Europe with whom my wife (shikha) was working as Technical Architect. She told me some bright architects took a decision to synchronize the data between two application using webserivces (From Order to Shipment Module ) . Her task was to improve the performance Ha ha, the approach itself is faulty. Why Webservice for data synchronization? My wife asked the question...[EA] We want to make our application SOA compliant. Ha ha. My Wife, Is there are requirement here for SOA.. and funny discussion goes on.... In the end my wife convinced them faultiness in the approach. In nut shell, SOA (Webservice) need to be chosen based on the requirement(s), else it kills the performance of an application Regards, GM.

**[raja shankar kolluru](#1766 "2010-06-20 16:09:50"):** That is one of the typical things that get proposed by enterprise architects. Everyone and their brother must be exposing web services be it in a batch mode or as an RPC. I had a chance to work on one such monstrosity that generated 17 files for every method exposed as a web service! Its claim to fame is that for every method, it can generate two flavors of WSDL - one for RMI over IIOP and the other for SOAP over HTTP. It is a different matter that this great architecture came at the cost of a big performance dip. I just rebelled and wrote a framework that was so much more performant that it did not need any convincing to switch. Your story reinforces the point I just made about EAs working in a vacuum for the most part.

**[Dasa](#1789 "2010-12-08 21:14:42"):** Fundamental problem in the EA path is, people don't believe in evolution. Evolution holds good everywhere. Be it ape to Humans and Monochrome Monitors of 80s to IPADs of today. Its an evolution. What this means is, when a product is being developed, always a BIG BANG Approach is followed in most of the companies. Neverthless, be it any successful venture (MSFT,GOOGLE,Yahoo,Facebook) they all started small and evolved over a period of time. As this evolution happens, EA becomes a reality. But when the Show starts with a Big Bang attitude, things fall apart and there are more chaos than solutions.

