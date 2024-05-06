---
title: Solar Heating and Patterns..
link: http://itmusings.com/architecture/solar-heating-and-patterns
author: raja
description: some musings on the similarity between architecture and solar heating
post_id: 130
date: 2009-01-28 07:37:00
comment_status: open
post_name: solar-heating-and-patterns
status: publish
layout: post
category: musings
home_page: false
image: /images/solar-heater.jpg
tags:
  - musings
  - abstractions
  - strategies
---
I recently stayed at a hotel and suffered from what has become the ubiquitous problem of "no hot water". This time, the explanation was all about the solar water heater not functioning properly. This got me thinking about the impact of a bad design on a particular feature (in this case heating the water). I also realized that many design patterns would serve to alleviate this situation. The outcome of all these ruminations is the current blog entry. 

## Interface Implementation Separation

Let us start our design with an interface WaterHeater. Here is a quick definition: 
```java   
    
    public interface WaterHeater {
    public void heatIt() throws HeatingFailedException;
    }
```
There are several implementations available for this interface. We use one somewhat flaky implementation called SolarWaterHeater.  
```java    
    
    public class SolarWaterHeater implements Heater {
     public void heatIt() throws HeatingFailedException {
    // use solar heat to heat the water. 
    }
    }
```
Now if we combine the consumer (in this case my bathroom that requires hot water) too tightly with the implementation (the SolarHeater) then we are going to hurt ourselves very bad if the SolarHeater implementation breaks down as has happened in the case of the hotel that I was staying in.So let us see if our knowledge of design patterns can help in mitigating this problem. Here are some solutions that I could think of. 

## Facade

If we are able to give a facade that is capable of invoking multiple implementations then we can solve this problem. In this case, we can have the facade invoking the actual implementation. The client (bathroom with hot water) is connected to the facade. See the diagram below for an illustration of this pattern. Quite clearly, the tank serves as a facade for isolating the implementation (solar heater) from the consumer (the bathroom). Alternate implementations using an electric geyser are also possible to provide a back up solution in case of failure of the original low cost implementation (solar heater). [caption id="attachment_147" align="alignleft" width="300" caption="Tank as a facade"]![Tank as a facade](/images/2009/01/heater1-300x154.png)[/caption] 

## Business Delegate

[caption id="attachment_148" align="alignright" width="300" caption="Local tank as a business delegate"]![Local tank as a business delegate](/images/2009/01/heater-using-bd2-300x154.png)[/caption] In this case, the local tank in the bathroom acts as the business delegate. If the pipe connecting the main tank to the bathroom has a few problems or water in it gets too cold, the local tank can help us out. 

## Router

But if cost of heating is a big factor,  it might be preferable to use the solar heater unless there is a problem with it in which case the geyser can help us out. To handle such a situation, a valve (which serves as a router)  can be very useful. With this in place, the diagram changes to the following: [caption id="attachment_149" align="alignleft" width="300" caption="Valve as a router"]![Valve as a router](/images/2009/01/heater-using-bd-and-router1-300x154.png)[/caption]  

## The Tank Interceptor - with or without pre-fetching

Usually, tanks are very wasteful. The bigger the tank is, the more people it can serve. But a tank full of water needs to be heated even if there is no potential consumer for the hot water. So the system might be wasting more electricity than necessary. To alleviate this problem, the tank itself can be fronted by an interceptor that does the following: The tank is first checked to see if there is any hot water in it. If there is any hot water in it, the water is served else if the tank either has no water in it or if the water inside the tank has gone cold then the tank interceptor would first heat the water and then serve it to the bathroom. In this process, it might even be pro-active and "pre-fetch" more hot water so that if there is any other consumer who might require hot water, then they could be spared from waiting for the water to get heated.  This is an example of the interceptor pattern and is pretty much akin to how caching interceptors work. [caption id="attachment_144" align="alignright" width="300" caption="Tank Interceptor to avoid wasteful heating of water"]![Tank Interceptor to avoid wasteful heating of water](/images/2009/01/heater-using-caching-interceptor1-300x163.png)[/caption] Thus we see that the design patterns are not merely constrained to the applications that we develop but are instead applicable to a wide range of day to day problems as well. Got any more ideas? Feel free to comment.

## Comments

**[Ravi Tiwari](#1601 "2009-05-10 05:34:56"):** Wonderful designing Sir. I am a huge fan of yours :)

**[raja shankar kolluru](#1602 "2009-05-10 08:20:08"):** Thanks Ravi.. I appreciate your comment. Happy designing :-)

**[Diego@Heating Elements](#1844 "2010-12-23 15:48:43"):** You don't essentially require a lot of pricey devices to appreciate the advantages of passive solar heating systems. Passive heating gathers and guides heat without the help of almost any mechanical or electric powered gear .

**[raja shankar kolluru](#1851 "2010-12-24 17:14:23"):** Thanks Diego for the comment. My article may err in the side of naivety since I was trying to illustrate some architectural patterns that we commonly encounter in computer application development and was using the solar heating failure as a spring board to do so.

