---
title: Perf Analysis - Web Layer & Browser
link: http://itmusings.com/java/j2ee/perf-analysis-web-layer-browser
author: raja
description: Browser level performance optimization
date: 2010-04-28 15:53:48
post_name: perf-analysis-web-layer-browser
status: publish
layout: post
image: /images/2010/04/web-buckled.jpg
category: performance
home_page: false
---


This article delves more into the performance analysis exercise that I alluded to in a previous [article](<? echo get_permalink\(461\); ?>). We begin our analysis with the web layer which serves as the entry and egress to our core application. Does your web layer buckle under load as the spider's web here seems to have ?
![](/images/2010/04/web-intact-300x208.jpg)

Tweaking the web layer is often left to the idiosyncrasies of the infrastructure team. When was the last time that as an application architect, you looked at the browser behavior or the httpd server settings?

Or the caching behavior of your JSP pages? These are some of the things that I intend to discuss in this article.

The first step to determine browser behavior is to use a decent diagnostic tool to probe the various times taken by a browser in the process of downloading the application page. There are multiple tools which aid in this. Fiddler, Firebug, YSlow! (from Yahoo), Firefox Cookie Manager ( to know about the cookies that you use) etc. The one that I liked the most out of this lot is [dynatrace](http://www.dynatrace.com/twominutedemo/). This has been the software of choice for the User Experience group at the place I work.

There are a ton of metrics that can be gathered from a perusal of the reports that can be obtained from these tools. Some of the ones that I found useful include the following:

MetricWhat to look for?

Http Version
Is it in Http 1.1? Http 1.1 behaves better in terms of keeping multiple connections alive to the web server at the same time. We found that switching to 1.1 for MSIE 6.0 enabled us to gain substantially on performance. We found that httpd by default uses 1.0 for MSIE till 6.0. Look up your httpd.conf (or ssl.conf)

Cookies
Cookies may have to be secured, (secure attribute must be ON) and encrypted. Ensure that cookies are used minimally.

Compression
Is the response compressed? You may have to enable the gzip module in httpd.

Browser caches - Expires tag and eTag
These two tags are extremely useful for ensuring that the browser caches responses and avoids unnecessary traffic for every request. I have covered browser caching treatment in more detail [here](<? echo get_permalink\(519\); ?>).

Javascript Unification & Minification
Most diagnostic tools recommend the usage of a "minified" javascript. What this means is that javascript (and css for that matter) must be stripped of blanks and new lines to the extent possible so that the code would be optimized (albeit obfuscated) in terms of download size. Minification, if used in conjunction with gzip compression that I alluded to above, does not achieve a big gain since the response compression may have already optimized the code size.

However, Javascript unification - i.e. unifying multiple javascript files into one file - may prove to be a big gain. The more javascript files there are, the more number of connections that the browser must spawn. Reducing the number of javascript files saves on unnecessary 304 interactions between the web server and the browser. We will cover 304 as part of the browser cache discussions.

Connection Keep Alive
The Keep-Alive extension to HTTP, as defined by the `HTTP/1.1` draft, allows persistent connections. These long-lived HTTP sessions allow multiple requests to be sent over the same TCP connection. In some cases have been shown to result in an almost 50% speedup in latency times for HTML documents with lots of images.

There are two primary parameters that govern the behavior of the httpd web server with keep-alive.

* Max connections are the maximum number of concurrent connections that are allowed  This parameter is important to ensure that one browser does not hog too many connections thereby preventing DoS type of attacks. It should also be remembered that the browser allows the client to configure the number of connections that can be left open between the browser and the web server. By default many versions of MSIE use 2 concurrent connections while FireFox can go upto 5 or 7 concurrent connections.
* A second parameter to tweak would be the keep alive timeout which determines the number of seconds that the web server would wait for another request to be sent through the same TCP connection before deciding to close it. If this value is too high, then the web server threads tend to get hogged for a longer period of time thereby resulting in a denial of the service for other aspiring clients. If it is too short, then the browser has to keep opening fresh connections for literally every resource thereby degrading the download performance.
