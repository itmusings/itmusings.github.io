---
title: What Lewis Carroll can teach us about Web Architecture
link: http://itmusings.com/architecture/what-lewis-carroll-can-teach-us-about-web-architecture
author: raja
description: The Glass view of enterprise architecture. 
post_id: 907
created: 2015-06-18 01:42:36
comment_status: open
post_name: what-lewis-carroll-can-teach-us-about-web-architecture
status: publish
layout: post
image: /images/2015/06/alice.jpeg
front_page: true
category: musings
tags: 
  - musings
  - architecture
  - glass
  - commerce
  - content
---


> “Why it is simply impassible?
> 
> Alice: Why, don’t you mean impossible?_
> 
> Door: No, I do mean impassible. (chuckles) Nothing’s impossible!”
> 
>   - Lewis Carroll, Alice’s adventures in Wonderland and Through the looking glass


As enterprises evolve, multiple portals emerge to facilitate interactions with the end consumer. So before we can say Enterprise Architecture, we would already have a marketing site, a commerce site, a support portal etc. All of these either become sub domains or individual domains in their own right. The consumer is befuddled by these myriad web sites with their own associated styles, fonts, branding and not to forget their own login identities. It is a no brainer that these sites need to share credentials and must support single sign on. So if we have to visualize the consumer's entry to the enterprise, it may look a little like this:

## THE SSO DOOR

![](/images/2015/06/door-before-websites1-300x271.png)

The SSO spans across multiple web properties and provides one single sign on experience across all of them. Think of it as some kind of door which is "passible" if you go in with the correct credentials. But things become interesting when this door starts assuming other responsibilities. What if the door also knows where to send the request to? If it is a request for the reports app then it should go it else it must deflect the request to e-commerce. In this situation, the "door" becomes a "[reverse proxy](https://en.wikipedia.org/wiki/Reverse_proxy)". It knows to route requests to the correct destination.

## ENTER THE GLASS

The next level of complexity arises when the "door" now starts taking control of the "view". It can start consuming services from the enterprise and starts populating a view to show the data to the consumer. At this point in time the role of the door now changes. In the process it acquired a new name. It is called as the "Glass" in the Content and E-Commerce domain. The role of the glass is to provide one "user experience" that cuts across all the services that exist in the enterprise. 

![glass](/images/2015/06/glass-1024x783.png)

As the owner of the experience, the role of the glass becomes very important. Vendors have been vying with each other to own this experience. They have created "experience managers" that allow the end users to edit the views and control the experience of the web property from one place.

Adobe Experience Manager (AEM), Oracle Endeca XM etc. are now increasingly becoming dominant in this space. 

But architecturally speaking, what I like here is the fact that we have de-coupled the front end from the backend. The back end has truly become "headless". It provides services, not the experience. It is up to the glass to utilize these services to create an experience. The glass has thus the opportunity to unify the experience across the enterprise. This is becoming an increasingly prevalent pattern in today's enterprises.

## DE-COUPLED EXPERIENCE vs. DE-COUPLED DATA

An alternate approach is to make the backends responsible for both the data and the experience for some part of the page. The Glass in this case, would act as a consolidator of all the html fragments to assemble them into the final page. 

My personal opinion is to have a dedicated glass. What I mean is that one component must serve as the glass and does nothing else. Back end systems must not own even fragments of experience. They provide data and allow the Glass to construct a unified experience around the data that is provided.

The Glass must not be doing e-commerce or content management or report generation or anything else. It owns the construction of the experience from the data. Thus there is a clear delineation of responsibilities in the enterprise - the Glass owns experience and the various back end systems provide the data. This allows us to have an experience that is truly rich and pervasive across the enterprise.

This also allows us a unique opportunity of designating one group to own the glass in the enterprise. Conway's Law can be used here very effectively. The experience group takes care of owning the enterprise experience and would report into the CMO. The CMO then becomes responsible for all Omni channel experiences of the end consumer. This is very much in line with today's trend of having one brand entity across all channels. After all, the enterprise interacts with the end user through the glass. Might as well make it rich, unified and make the marketing dude to be in charge of it all! 

## UNIFYING SEARCH CAPABILITIES

Since data is coming from all kinds of systems, having one unified search engine that knows all this data can be a very interesting challenge. Traditional eCommerce solutions have used solutions such as Endeca, SOLR etc. to index all the catalog data. But in case of a web property that is assembling multiple sources of information, it is a very hard problem to solve unless the indexing is done on the "constructed" site and not on individual pieces of data. A Google search appliance can be a very interesting solution to this problem.

## Comments

**[Dasarathy Ponnappan](#3841 "2015-06-24 16:45:28"):** Hi Raja, In line with this we @ CVS are developing FAST Architecture which plugs in Angular components tied to back end services thereby, View is completely controlled by business and marketing with Models purely exposing services. Adding to this, if we are able to SWAGGER(http://swagger.io/getting-started/) these headless services, then service homogeneity across disparate systems becomes really a possibility. (Which in an enterprise is a much sought after).

**[raja shankar kolluru](#3924 "2015-07-21 16:11:39"):** Hi Dasa This is in line with today's thinking about delineating the experience from the data. Are you folks using experience managers with Angular?

