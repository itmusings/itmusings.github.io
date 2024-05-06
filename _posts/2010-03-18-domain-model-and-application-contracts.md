---
title: Domain Model and Application Contracts
link: http://itmusings.com/architecture/domain-model-and-application-contracts
author: raja
description: Domain model and how it relates to program contracts between modules
post_id: 444
created: 2010-03-18 18:01:42
comment_status: open
post_name: domain-model-and-application-contracts
status: publish
layout: post
image: /images/2010/03/interface_modules2-300x146.png
category: design
home_page: false
tags:
  - domain
  - api
  - contracts
  - design
---


We had spoken [before](<? echo get_permalink\(435\); ?>) about the application contracts. As we define components, it is imperative that we spend sometime in designing the interfaces that they expose. Let us go back to the diagram that we used when we designed components.

  
[ ](/images/2010/03/interface_modules1.png)

In the other post, we had decided that the best way to abstract the UI and service components is via the use of a contracts module that contains the interfaces exposed by the services layer. Where does the domain model fit into that picture? That would be the theme of this post.

The domain model represents the "language" of the application. It consists of entities and value objects. The difference between the two is primarily based on identity. Entities typically have an identity and can be retrieved from a persistent medium using the identity. Value objects typically are more like "flyweights" which store a certain type of information. They make sense when attached to an entity - not by themselves. Ex: Address can be a value object which requires an entity (such as Customer, User etc.) to make sense.

The domain model forms a part of the application "contract" - so to speak. Just as it does not make sense to re-invent the same component twice in an application, it does not make sense for the individual components in an application to re-invent their own version of the domain model. Components interact with each other via the contracts (aka interfaces) that they expose. The interfaces must also provide for data interchange between the various components. After all, how can you interact with a component if you don't feed it data or obtain data from it?

Data can be of a simple data type (think int, char etc.) or a more complex data type. The complex data types must somehow map to the i

nformation contained in the application domain model. Hence the complex data types can themselves be entities or value objects. Usually, the domain model tends to be normalized. Therefore,  the application entities become too fine grained to be put into a service interface. This is best illustrated with an example. Consider the simple subset of an accounts domain model represented below in the diagram:  ![](/images/2010/03/DomainModel-300x164.png)

The first portion of the diagram depicts a simple domain model with Customer and Account entities. Let us say that we need to design an API that returns the customerId and name along with just the account Id and description. The temptation is to design a domain model structure as shown below with BasicCustomerInfo and BasicAccountInfo only accounting for the attributes that are needed to be returned. These objects are the replicas of the Customer and the Account entities with reduced number of attributes. This sprawls a morass of new objects called the Data Transfer Objects (DTOs) which look like the domain model entities but are a little bit different. My recommendation is to use DTOs where applicable but to keep their usage restricted to remote services for the most part. ![](/images/2010/03/domain_and_modules-1-300x233.png) We should aspire to use the core domain model for data interchange.  The domain model would be fundamental to the application and hence should ideally be in a module by itself. (called model) DTOs are program contracts and hence must co-reside in the contracts module along with the interfaces that use them. The updated module structure is shown below with annotation.

Validation of the entities poses an interesting problem. Is validation part of the entities itself and therefore must reside in the "model" module? Or is it better to make it reside in the services or in the UI? Basic validation rules must exist along with the domain model in the model module. But the implementation of the rules itself can reside with the services modules or the UI module depending on how they are implemented. For example, there exist several interesting validation frameworks which come along with UI (such as the struts validation framework for example) These can co-exist in the UI along with other UI artifacts. Service side validation frameworks can co-reside along with the services in the "service" module.
