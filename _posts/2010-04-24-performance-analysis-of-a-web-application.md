---
title: Performance Analysis of a web application
link: http://itmusings.com/java/j2ee/performance-analysis-of-a-web-application
author: raja
description: Performance analysis of a web app and some solutions
post_id: 461
date: 2010-04-24 05:10:25
comment_status: open
post_name: performance-analysis-of-a-web-application
status: publish
layout: post
home_page: false
image: /images/webapp.png
category: performance
tags:
  - performance
  - web
  - browser
---
Application performance testing is just about the last thing that we may have to do before we could certify an application as production ready. Or it may be just about the last thing we do before we decide to discard the![](/images/2010/04/the-tortoise-and-the-hare.jpg) app in the dumpster. This may be a loud roar or a death knell depending on what turns up. Or it may be a combination of both. I was perusing a forum the other day where the questioner asked about the appropriate time for application optimization. I have to concede that I was pretty astonished at the number of people who cautioned him about premature optimization. They further used Knuth's admonishment - "_Premature optimization is the root of all evil" - _to lend veracity to their statements_. _I don't have the full context around Knuth's much bandied assertion. All I can say is that if an application were to be designed with performance as an after thought, then the entire team should pack up their sleeping bags and be ready to camp at the war room during the days leading to production and for several days afterward as well. I need to add though that careful upfront planning would not obviate the need for stress testing the application. It would just make it, well, less stressful - no pun intended. The premature optimization bromide reminds me of the apologue about the "Hare and the Tortoise". You either optimize consistently (like the tortoise) and crawl your way towards the victory post. Or you do it all at the end (not unlike the hare trying to make up for its initial lassitude by sprinting in the end) amidst a lot of heartburn and potential for functional regression - your choice. I was involved in an interesting exercise in the optimization of a web application. I wanted to share some of my experiences in this regard in the next few articles. Some of the optimizations carried out were extremely specific to the technology stack that we had used for the project and the actual nature of the application. Nonetheless, there were quite a few learnings garnered from this task, which were of a more universal nature. I am going to tag them all with the "optimization" moniker to facilitate search. I was tempted to add the "premature" adjective to the tag but I think that would make me look more scornful than I intend to be. So I resist the temptation in good grace. We set the following objectives in fine tuning the application:
* Ensure that the application latency is within acceptable limits.
* Ensure that the application is able to sustain for extended durations of time without leaking important system resources (memory, connections, threads etc.)
This application was in the Java/JEE technology and is serviced through a httpd/JBOSS combination. So many of these observations would be specific to this stack. The chief areas of emphasis were the following: 
1. Browser rendering.
2. Web server fine tuning.
3. Application server (JBOSS) fine tuning.
4. Application tuning
5. Database tuning
We will cover more details in each of these focus areas in ensuing articles.

## Comments

**[Gaurav Malhotra](#1749 "2010-04-25 13:50:01"):** Nice article. ... Absolutely true, thinking about performance as aftermath is just like closing your eyes in case of danger :-) It will be awesome, you also cover the performance impacts by putting the http session on the cache. Recently I did a bit to increase the performance of UI by putting 1) biolerplate text (prompts) on the oracle coherence cache + other read only data on the cache 2) integrating JPA (eclipselink) with oracle coherence - cache (queries optimization) i.e SELECT o FROM XXXX x where o.yyy = ??? goes to the oracle coherence cache first and if not found than go the db.The JP QL get converted to coherence filter query. All insert/update/delete were also made cache aware. Its not desired to put all the entities into the cache (grid) My research shows that caching solution like tangasol gives best performance as the no. JVMs increases. With less JVM it can be overkill because of serialization + de-serialization cost

