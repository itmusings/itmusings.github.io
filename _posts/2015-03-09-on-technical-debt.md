---
title: On Technical Debt
link: http://itmusings.com/management/on-technical-debt
author: raja
description: some reflections on tech debt, its origin, mitigation etc.
post_id: 892
date: 2015-03-09 16:01:32
comment_status: open
post_name: on-technical-debt
status: publish
layout: post
category: agility
home_page: true
image: /images/technical-debt.jpg
---


Technical debt has been mentioned in multiple blogs. Ward Cunningham apparently coined the term.As a software product starts acquiring more and more features and thence complexity; it tends to degrade in certain ways technically.

> If all potential product features are documented in a product backlog, then technical debt is that part of the product backlog that pertains to the maintainability (as opposed to the functionality) of the product.

This definition of technical debt captures our understanding succinctly. Accumulating technical debt may be deliberate or unintentional depending on the actual situation. We will talk about some of the different types of technical debt in this post.

# Causes

##  Deferred Refactoring:

These debts are typically "valid" i.e. they occur due to the application of sound Agile and product shipment principles that we will discuss below.

### YAGNI (You Aint Gonna Need It)

YAGNI is a great Agile tenet that stipulates that if there is a feature that you aint gonna need at this point in time, then it is best deferred till such time it gets fleshed out in detail. It is also a corollary of what some people call as "Value Driven Delivery (VDD)" where the greatest value is delivered early.There are repercussions to following YAGNI and VDD which we will talk about now.

Since unwanted features are deferred, some of the features are typically postponed since they are not needed at this point in time.For instance,there is no need of creating a class hierarchy unless there are multiple children for a main class.  This decision, in itself, does not incur technical debt IMO. The reason is that the system must be as simple as it can. 

However,when the complexity does come in, the simple system willhave to be refactored. In this case there are other classes which share the same features and hence a class hierarchy will now become necessary. This merits a refactoring. If it is decided to defer such a refactoring, then this needs to be documented as a maintainability requirement in the product backlog. Hence this would become part of the technical debt of the product by the definition above.

Now why would such a refactoring be deferred? Typically, this is due to VDD in action. Value Driven Delivery stipulates that we release value early which in turn translates to prioritizing functional features over maintainability features. The end users see the value early and would become passionate advocates of our product.

Deferred refactoringhas merit if the un-refactored code does not jeopardize the overall quality of our product delivery. It also abets VDD which is very desirable.

##  Messy Code

Messy code is an outcome of deficient design, architecture or the inability to understand refactoring. It can also occur by not practicing some basic hygiene in writing code. Messy code is caused by a deliberate or mostly unintentional ignorance of good code and design practices.

A deliberate ignorance of good coding practice is usually caused due to myopic business contracts or organization structures where the responsibility of the team that created the code does n0t extend to product maintenance. Perhaps, the enterprise has an SOW with a software vendor that absolves the vendor of all responsibilities after the certification of code in the first UAT or the first GO LIVE. Perhaps, all products are maintained by one team in the enterprise. If a team is not going to maintain the product it created, it is not incentivized to minimize technical debt. This is because technical debt is paid "later rather than now".

Unintentional ignorance of code practices is an outcome of an average product team that is not experienced in building and maintaining products for the long term. The team does not know what it does not know. They just keep coding without worrying about the ramifications of their decisions. Hence messy code is an outcome of mostly defective organization or a bad team. 

# Consequences

The biggest consequence of technical debt is the rising "cost of change" for the software. The current code base becomes unwieldy which means that making changes to it becomes harder. (the cost of change increases). This situation is illustrated in the picture below where the ideal path represents the change to the code base with a good amount of prioritization being given to technical debt. The actual path represents typical situations when the "cost of change" increases over a period of time making future releases of the software very expensive. 


![tech debt](/images/2015/03/tech-debt-300x200.png)


Technical debt that is left uncurbed, can lead to a software that is very unstable. This is true for both kinds of technical debt. Deferred refactoring can halt future change as much as messy code though the latter of course is typically more vicious in its impact. 

# Diagnosis & Mitigation

Technical debt must be pro-actively tracked. Retrospectively, if we were to end up with a code base that has incurred substantial technical debt, then it is best to identify the big issues so that they can be mitigated first. This is again in keeping the ideas of VDD (Value Driven Delivery) as mentioned above. The biggest anti dote to technical debt in my opinion, is to reduce the size of the code base. If we think about it, this solution is quite obvious. The lesser code we write the lesser mess that we can create.

Concepts such as Solution Oriented Architecture, Modular design. layered architecture and the like evolved from this realization. Bigger unwieldy code bases conceal their secrets better than modular code bases. A smaller haystack is obviously less efficient in concealing its needles! Monoliths are the exact opposites for a modular code base.

I was quite surprised at the number of software vendors who are proffering their monoliths to the multitude.

While getting rid of the monolith and imbibing SOA concepts into the enterprise seem to be viable long term solutions to this problem, in the short term this needs to be tracked and alleviated. SONAR has a plugin called SQALE which tracks technical debt as the number of days required to solve quality issues which have been detected using tools such as checkstyle, PMD and the like. While these tools may look into code issues, it is hard to detect architectural issues automatically. These need to be identified, tracked and mitigated actively. One of the most important mitigation, as told earlier, is the modularization one. The team must assiduously identify areas which can be modularized.

In summary, technical debt must be planned, tracked and alleviated actively.
