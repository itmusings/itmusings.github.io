---
title: On Program Contracts
link: http://itmusings.com/architecture/on-program-contracts
author: raja
description: The need to separate API from their implementation
post_id: 435
created: 2010-03-07 18:27:16
comment_status: open
post_name: on-program-contracts
status: publish
layout: post
category: design
image: /images/2010/03/interface_modules1-300x198.png
home_page: false
tags:
  - design
  - api
  - interface-based-design
---


![](/images/2010/03/interface_modules1-300x198.png)Contrary to possible expectation, this post is not about signing contracts between companies. It is the contracts that must exist between the various modules within an application. Any application would have a lot of classes that implement various parts of its functionality. 

The interface based design principle stipulates that implementations must be fronted by an interface. This would facilitate a loose coupling between an implementation class and its consumer.

Let us say that the implementation class provides a business functionality which is exposed as a service. It resides in the services module. The consumer is a UI class that stays in the UI module. The question is "Where should the interface implemented by the service class reside? " In about 90% of the applications that I had witnessed, the service interface also resides in the services module. This introduces a dependency on the service module from the UI module. This dependency is indicated by the first diagram as depicted above.

In rare occasions, I see the situation being reversed. The architect possibly, has followed the tenet that the interface must be as close as possible to the consumer. Hence the interface gets moved to the UI module which introduces a dependency on the UI from the services layer. The service implements the contract as specified in the UI layer. This might work better but has a problem if we have alternate consumers who are not in the UI module that want to consume the service class. In this case, they have to needlessly depend on the UI layer. Secondly, the service to UI module dependency is almost anathema since there is a huge potential there for leakage of concerns. Before we know what is happening, there might be service classes using exclusive UI value objects such as the HttpServletRequest! Hence the second situation is beset with problems as well.

Hence I recommend having a separate contracts module which has all the contracts for the application. Both the UI and the service modules would depend on this contracts module. But the direct dependency from the UI module to the services modules or vice-versa is removed. Hence this provides the correct level of encapsulation to enable concurrent development of both the UI and the services modules. During runtime however, the UI module still needs the services module to actually accomplish its job. This runtime dependency is documented by the dotted line that is drawn in the picture above. The actual runtime dependency is injected using an IoC container.

This kind of module structure enables concurrent development much better. After all, it is a cardinal rule that we need to know the external contracts before we can develop code.

## Comments

**[Gaurav](#1743 "2010-03-09 19:58:12"):** Hi raja, Having worked and learned lot of trick of trade from you. In my current project we are implementing exactly the same approach as you suggested above. I would like to add one more benefit, which the above design encourages is TDD. Another important concern is the validation modules. I classified our validation module into 1) constraint rules (entity level) . These rules ensures data integrity and should be triggered whenever db operations are performed. 2) .... ..... The validations (as simple pojo - constraint rules) can be specified using annotation (@BR(rules = SomeCheck.class) on the entities (jpa) and used internal of the ORM (Eclipselink/Hibernate) to provide us the object change set and call the validation modules from the DAO layer. So we can also develop our validation module concurrently. Cheers, GM

**[raja shankar kolluru](#1744 "2010-03-10 04:24:09"):** Gaurav.. Thanks. Your observation about the validations classes brings to light one important fact that has been left out of the above post. We talked about contracts (aka interfaces) residing in the "contracts" module. But we did not talk about where the entities must reside! Let me try addressing the entities and the value objects in a separate post. That may have some points of interest as well.

