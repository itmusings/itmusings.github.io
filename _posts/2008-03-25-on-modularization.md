---
title: On Modularization
link: http://itmusings.com/architecture/on-modularization
author: raja
description: Musings on modularization and some of the principles behind it
post_id: 33
date: 2008-03-25 17:50:16
comment_status: open
post_name: on-modularization
status: publish
layout: post
category: architecture
image: /images/modularization.jpg
home_page: true
tags:
  - modularization
  - architecture
  - design
---

Modularization is one of the most important and unfortunately one of the most often ignored features of software development. Most softwares are developed from functional requirements and non functional requirements. Since modularization is more like a longevity requirement (which comes under the banner of non functional requirements), there is seldom a clearly laid out requirement to promote modularized software development. So like unit testing, which until recently shared its fate, modularization gets laid on the wayside. More "tangible" features instead get prioritized over modularization. This problem results in very badly structured code without any coherent theme binding it all together. More often than not, code is scattered across multiple configuration files and classes. Certain "master" configuration files need to be tweaked for adding or taking away a module. Surprisingly, this trend pervades even to well established frameworks that are otherwise well engineered. In the worst instance, some frameworks only support one configuration file which should be changed as new modules emerge or go away. The most glaring example of this is the J2EE Servlet Specification which takes all its configurations from one solitary web.xml file that resides in a well known location. This means that introducing any new module for the application would have to change the web.xml file. No wonder, most frameworks stay away from this file and have just one well known servlet defined there.  Modularization is seen as a very important requirement especially in products. A product would be constituted of multiple modules. Each may be priced separately. So imagine if all modules need to be shipped together to make the application work! This is not a desirable situation as the product manufacturer would like to ship out just the modules that the customer has purchased. This post talks about modularization and how it typically works. Some perceived requirements for modularization would be mentioned and summarized at the end of the post. 

### So what is a module?

A module is a collection of code and artifacts that exhibit a high degree of cohesiveness and implement the same concerns. In the context of software, modules can be said to implement a set of functional requirements. DRY (Don't Repeat Yourself) stipulates that the same requirement or concern cannot be implemented in multiple places. Hence a module's functionality should be unique within the program i.e. no other module must implement the same functional requirements. 

### How about requirements that need to be implemented across multiple modules?

If a requirement needs to be implemented across multiple modules, it would come under the banner of horizontal requirements. Horizontal requirements, according to me, have two parts to them. One is the functional part which actually implements the requirement. Log4J (or its incarnations for various languages) is an excellent example of this. The process of logging is systematically implemented using a great, albeit a very inextensible, framework.(BTW this is my biggest grouse with this framework. More on it in another post) But the actual process of integrating logging to the end application constitutes the horizontal part of the logging non functional requirement. The horizontal part is usually the more challenging since it has to somehow creep into all the modules. Well known patterns such as the interceptor pattern or the proxy pattern or the decorator pattern can be used to implement this process of creeping into the modules. AOP strategies use a combination of these patterns. Hence a modularization strategy has to combine the modular behavior with certain unifying behavior that cuts across all the modules. In essence, this is an application wide behavior that needs to be integrated across modules. 

### Dependency Management

Modules collaborate to make the entire application. This collaboration leads to dependencies between them. These dependencies are essential but should be kept minimal in the interest of re-use. Loose coupling between modules must be fostered by the use of strategies such as the IOC pattern and the observer pattern. How does that work? And oh by the way, circular dependencies are a strict NO NO. Many build tools would break with circular dependencies. They just would not know which one to build first!! 

#### Soft dependencies vs. Hard Dependencies

Dependencies can be hard or soft. Consider the following: 
    
    
    // Some method in some class ...
    // Example of a hard dependency.
    FooService fooService = new FooServiceImpl();
    // Example of a soft dependency
    FooService fooService = Class.forName("FooServiceImpl").newInstance();

If this code exists in a class in a particular module and FooServiceImpl resides in a second module, then the first module would have a hard dependency on the second module in the first instance and a soft dependency in the second instance. Soft dependency means that the two modules should exist in the _runtime_ class path. Hard dependency means that the two modules must exist together even during _compile_ time. This may not seem like a big deal at first sight. But a soft dependency promotes great extremely flexible deployment for the application. How so? Think about it and let me know. 

### Auto Discovery

Modules work best if they are discovered automatically by the application and integrated into the fabric of the application. Auto discovery strategies are prevalent in some frameworks (ex: the Spring Framework which is capable of discovering its bean xml files from the classpath using wild cards) work beautifully with modularization. 

## Summary

This post must have raised a lot more questions than it attempted to address. Depending on the interest of my readers, I would attempt to address these questions in future posts. The following points summarize the requirements demanded by modularization: 

  * A module implements one or more UNIQUE functional requirements.
  * Modules in an application must somehow integrate with application wide horizontal requirements.
  * Modules should depend on each other. But their inter dependencies must be minimal and never circular
  *   * Soft dependencies(runtime) between modules are preferable to hard (compile time) dependencies and lead to flexible runtime application packaging.
  * Auto discovery fosters modularization.

## Comments

**[Ed Kirwan](#1532 "2008-11-20 15:42:50"):** Hello, Raja, Nice post. I'm curious, though, that you post about modularization without mentioning encapsulation. Was this intentional? I ask because encapsulation theory provides a slightly more rigorous benefit than the modularization you describe, and you might be interested in further reading. Namely: The Principle of Burden takes two forms. The strong form states that the burden of transforming a collection of entities correlates with the number of entities transformed. The weak form states that the maximum possible burden of transforming a collection of entities correlates with the maximum possible number of entities transformed. The most concise summary of encapsulation theory is that encapsulation mitigates the weak form of the Principle of Burden. In slightly more detail, the International Organisation for Standardization defines encapsulation as the property that the information contained in an object is accessible only through interactions at the interfaces supported by the object. At a higher level of abstraction, program units, too, may be encapsulated within subsystems, whereby the information contained in the subsystem is accessible only through public program units contained within the subsystem. The burden of creating or updating any software system correlates with the number of program units created or updated. Program units that depend on a particular, updated program unit have a higher probability of being impacted than program units that do not depend on the updated program unit. The maximum possible burden an updated program unit can impose is the impacting of all program units that depend on it. Reducing the dependencies on an updated program unit therefore reduces the probability that its update will impact other program units and so reduces the maximum possible burden that that program unit can impose. Reducing the maximum possible number of dependencies between all program units in a system therefore reduces the probability that an impact to a particular program unit will cause updates to other program units, and thus reduces the maximum possible burden of all updates. Encapsulation theory shows how to use encapsulation to reduce the maximum possible number of dependencies between all program units. Encapsulation theory therefore shows how to use encapsulation to mitigate the weak form of the Principle of Burden. It shows this by addressing the two, fundamental sources of dependencies within a system. Merely by being contained within a subsystem, a target program unit may be depended upon by all the other program units in the same subsystem, thus minimising the number of program units per subsystem reduces the number of possible dependencies. The number of program units that can possibly depend on a target program unit is also related to the target program unit's accessiblity. If a target program unit is public, then all program units in all other subsystems may depend on it, thus minimising the number of public program units reduces the number of possible dependencies. These two forces, of course, act on software configurations in opposing ways but an optimal balance of these forces is possible. Encapsulation theory shows how to approach this balance so that a wide family of systems may achieve optimal encapsulation. Ed.

**[raja shankar kolluru](#1533 "2008-11-20 17:16:35"):** Hi Ed: Thanks for taking the time to leave such a comprehensive comment. I appreciate it and I think it adds to the discussion substantially. I think your comment complements the post because my post delved on what happens AFTER modules have been designed whereas your comment talks more about HOW to achieve the modularization itself. I think your definition of program unit corresponds to my module definition. Module is a highly coherent encapsulated program unit that provides some kind of service which would be beneficial to the rest of the software system. A module may contain multiple classes but all of them are not designed to be exposed to the outside world. (this is a problem with languages like Java since they don't typically have the notion of a module. The package was approximately conceived along the lines of a module with visibility restrictions and such but the problem is we seldom use packages as modules) Hence the outside world can only depend on the classes in a module which are "public". But as I mentioned paranthetically, this is not supported by most programming languages and should only be enforced using conventions. The Single Responsibility Principle (SRP) when applied at the module level states that each module must be responsible for a distinct set of responsibilities. It should be used in conjunction with DRY which states that given a responsibility only ONE program unit or module must implement it. (so that replication between modules don't happen) Hence SRP and DRY would dictate the number of modules that would "functionally scale" the application. But a software application must accomplish multiple responsibilities to accomplish its functional requirements. Hence it needs to contain multiple modules that would start having dependencies on each other. I agree with all your points about dependency management namely: 1\. The module that has the maximum number of incoming dependencies (also called afferent coupling) would be the one that would destabilise the application the most if it is changed. 2\. The module that is most dependent on other modules (aka efferent couplings) has the potential for a lot of change with minimal impact on the other parts of the application. The application viewed in this fashion is a product of how we balance out afferent couplings with efferent couplings. Modules that have too much dependence on others are probably not independent enough. Modules that have too many incoming dependencies are too vulnerable for change. These conflicting requirements are what you have echoed out so well and I think it must be fully understood before a good module design is possible. My post talks about what happens AFTER we have gone through this process of modularization. But your part is quintessential for accomplishing the module design in the first place. Hope this clarifies. Thanks Raja

