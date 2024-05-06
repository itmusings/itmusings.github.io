---
title: A more detailed look at modularization
link: http://itmusings.com/architecture/a-more-detailed-look-at-modularization
author: raja
description: modularization 201 - more detail
post_id: 81
created: 2009-01-14 08:31:46
comment_status: open
post_name: a-more-detailed-look-at-modularization
status: publish
layout: post
image: /images/modularization.jpg
home_page: false
category: architecture
---

Any big application typically has a lot of functionality. It is logical that such a large amount of functionality be distributed across modules with each module becoming a specialist in implementing a given set of functionalities. This thought process has of course been crystalized as the single responsibility principle (SRP) that I have alluded to before in other posts in this blog.  These modules get integrated to form the entire product end to end. The module structure for involved products can get very complex with an intricate set of dependency chains that link one module to another. For instance, let us assume that we have a simple product that takes care of managing savings bank accounts. To implement the functionality of the application, let us say we have two modules customer profile and savings. The customer profile module takes care of setting up customer profiles while the savings module provides the savings bank account functionality. Indeed, it is by colloboration of these two modules that the functionality of the application is implemented.   Modules must exhibit a high level of cohesion that should co-exist with a well defined set of dependencies. These dependencies take care of establishing well defined points of coupling between this module and other modules. 

## Modular Design and Re-use

Modularization facilitates re-use. Let us say that in our savings bank application, we decide to pull out the customer profile module and make it available for other applications such as a brokerage account application. Quite clearly, this would not be possible unless:   

  * The Customer Profile module has a set of functionality that can be re-used by other applications. This point is obvious. Unless there is functionality that the module makes available for others, why would they want to use it ?
  * The module itself has well defined set of interfaces/classes that it makes available for consumption by the outside world. This means that the module itself makes available a few interfaces and classes that classes outside the module can utilize. Again, unless there is something that we gain from the module, why would someone use the module? Further, this point also indicates that the classes or interfaces available to the outside world must be well defined/established or published. This is to ensure that once another module starts using the features of this module, it only can use the features through a well known set of classes or interfaces. As the Customer Profile module evolves, these published classes and interfaces would only need to  be taken care of from the backward compatibility point of view. The others would be internal to the Customer Profile module and hence can be changed without worrying about backward compatibility with the dependent modules.
  * The module has a set of well laid out dependencies on other modules. (called efferent dependencies or outgoing dependencies) If this module does not exhibit well defined dependencies to other modules then the modules that depend on it (called afferent dependencies or incoming dependencies) would not know what they are getting into!  We should let everyone know what we are depending on so that when others depend on us, they would also know that they would indirectly depend on our dependencies. 
    

## SRP, DRY and Modules 

Single responsibility principle states that each class must implement one responsibility. This would ensure that the design is not changing multiple functionalities just because one class happens to change. This also ensures that the class is set up for maximum re-use. A class implementing one responsibility can be more effectively re-used rather than one which is doing too many things. In short, the more a class does the less re-usable it becomes! Also, DRY stipulates that given a responsibility there should not be multiple classes implementing it. This is to ensure that a change in responsibility does not percolate through the application changing multiple classes thereby increasing the brittleness of the design. These concepts would also manifest themselves in module design. Given a "set" of responsibilities, there should just be one module implementing them. Hence all the Customer related responsibilities such as maintaining the Customer, keeping track of the state of the customer (whether they are active etc.) must all be done in the Customer Profile module and not in any other module. Conversely, given a Customer Profile module, it should not be doing unrelated work such as Account Management for instance!  

## Modules Implementing Horizontal Requirements

Horizontal requirements are unique in that they have to be absorbed across the board by every module. These need to percolate across the entire application. For instance, how do we handle security? or logging? Doesn't this concern manifest itself across multiple modules? such as the Customer Profile module and the Savings Account module?  Horizontal requirements are typically implemented in two parts. Firstly, there would be a module that would implement the core horizontal requirement. Example logging as implemented by log4J, security as implemented by any security framework etc. But there is a second part to this. This is the actual incorporation of the concern in the core module. For instance, how do we incorporate security in the Customer Profile module? We first need to include the security module in the application. Then we need to enable the security features into the Customer Profile module. Here, it is important that we make a distinction between implementing security and incorporating secure features into a particular module. Only those features that are necessary for incorporating security in Customer Profile module should reside in the module itself. This important distinction is necessary to comply with SRP and DRY for modularization.  

## Modules and Product Customization

When we customize products for different customers how do we exactly go about doing it? Customization is a fairly common problem in product development. One product is written with an extremely generic feature set. Customers start using it and realize that it should be tweaked a little to cater to the particular nuances of their organization. In fact, in many cases the customers would be using existing products that are already doing specific portions of what our generic product is doing. So they might choose to continue using those products without substituting them with ours. So our product must have some important features that would be outlined below. 

### Strict Module Isolation 

One module should have the ability to work with different modules for implementing functionality. Example, our Saving Bank module must be able to utilize third party Customer Profile modules. This is a difficult proposition to implement but can be done by clearly identifying dependencies. The Savings Bank module must clearly stipulate what contracts of Customer Profile module it is using so that these contracts can be substituted and implemented by a third party module. Often, database dependencies are harder to implement in this regard since database queries for instance can assume that both the accounts table and the customers table are co-resident in the same database. These decisions are to be taken consciously keeping both performance and dependency management in mind. Let us take the example of a query that has to return all the accounts of a customer along with the customer name. A typical database query in this regard would look something like "select c.name, a.* from accounts a, customer c where c.id = a.customer_id and [ some other conditions
