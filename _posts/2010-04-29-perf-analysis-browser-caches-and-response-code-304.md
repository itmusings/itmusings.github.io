---
title: Perf Analysis - Browser Caches & Response Code 304
link: http://itmusings.com/java/j2ee/perf-analysis-browser-caches-and-response-code-304
author: raja
description: On caching in the browser. When to send response code 304
created: 2010-04-29 09:03:40
comment_status: open
post_name: perf-analysis-browser-caches-and-response-code-304
status: publish
layout: post
home_page: false
image: /images/304.jpg
category: performance
tags:
  - performance
  - code-304
  - caching
---


With no offense to the favored species, here is a bad joke about blondes:

> Question: Why is it a bad idea to give the weekend off to a blonde?  
Answer: Because you have to retrain her on Monday.

But browsers, unlike blondes, learn from experience and keep these learnings for sometime. Which means that if a browser has taken the time to download a certain URL, then it tries to keep the resource in its cache for later retrieval if the user has requested for that identical resource during a certain period of time so that it does not need to work as hard!

  
Now how long should the browser keep things in its cache? And how does it know when it has to extirpate the cached resource from there? This would be the subject of the current article.

Browser caches are discussed in a good amount of detail in [this article](http://www.mnot.net/cache_docs/). The HTTP specification provides for browser caching by stipulating a few tags that would be required to enable/disable it. The most important caching stipulation emanates from the Cache-control HTTP header directive which looks something like this:

> Cache-Control: max-age=3600, must-revalidate

This injunction instructs the browser to keep the current resource in its cache for a period of 1 hour ( = 3600 seconds). It further enjoins the browser to validate with the server before making any decisions around the staleness of the information contained in its cache.

## Configuring httpd to return the cache-control header in response

The httpd server can be instructed to specifically return the cache-control header for specific URLS with the following configuration in httpd.conf:

> <Files myapp/index.jsp>  
Header append Cache-Control "public, must-revalidate"  
</Files>

Note that this would require the mod_headers to be configured in httpd.

Further, mod_expires helps out by facilitating the setting of the expires tag for specific resources. The configuration looks something like this:

```
# Set long expire headers for better browser caching

> <IfModule mod_expires.c>  
ExpiresActive On  
ExpiresByType text/css "access plus 30 days"  
ExpiresByType text/javascript "access plus 7 days"  
ExpiresByType application/x-javascript "access plus 7 days"  
ExpiresByType application/javascript "access plus 7 days"  
ExpiresByType image/x-icon "access plus 7 days"  
ExpiresByType image/vnd.microsoft.icon "access plus 7 days"  
ExpiresByType image/png "access plus 30 days"  
ExpiresByType image/gif "access plus 30 days"  
ExpiresByType image/jpeg "access plus 30 days"  
ExpiresByType image/jpg "access plus 30 days"  
ExpiresByType application/x-shockwave-flash "access plus 30 days"  
</IfModule>
```
These configurations can set the cache expiration time for specific resources. The expires http header looks like the following:

> Expires: Fri, 30 Apr 2010 16:22:59 GMT

We are stating that the resource can be safely cached till 16:23 PM on the 30th Apr 2010 after which the browser must again download a fresh copy from the server.

## Validators

HTTP specification int![](/images/2010/04/304-sequence-300x266.png)roduced the notion of validators to provide an easy way of ascertaining if a resource needs to be re-issued from the web server. Currently, there are two types of validators defined:

  * ETag - (introduced from HTTP 1.1) This is a string to uniquely identify the resource at the time of rendering the page. It can change if the content changed. Two invocations with identical content must return the same ETag.
  * Last Modified - this is a long string that tells when the resource was last modified. This can be useful for static resources which need to be re-rendered only if they have been modified since the last time the browser retrieved them.

The web server must return a response code of 304 (NOT MODIFIED) if the above validators have not changed from the last time the response was returned to the browser.  This would make the download faster because the web server does not have to return the whole response - just the 304 response code. Both validators are equally useful. Which one of them is used depends on the resource being retrieved. For static resources, the last-modified time validator is useful and is in fact handled by default by httpd without the requirement of any configuration from our side. For dynamic resource, ETag may be more useful.  The browser in its fresh requests queries the web server with the last ETag of the cached resource in a new request header called "If-None-Match". It queries the web server with the Last-Modified header in a request header called "If-Modified-Since". These two request headers are required to compare the client's value of the validators with the current server value.

This process is illustrated in the diagram.  
This same concept can be implemented using the following pseudo code that can reside in a servlet filter.
    
```java  
    // (this can be in a servlet filter.)
    String clientLastModified = request.getHeader("If-Modified-Since");
    long longClientLastModified = 0L;
    if (clientLastModified != null)
      longClientLastModified = Long.getLong(clientLastModified);
    String clientEtag = request.getHeader("If-None-Match");
    if(clientLastModified == null && clientEtag == null)
      // proceed with invocation down the chain;
    else {
      String etag = obtainETagForResource(request);
      long lastModified = obtainLastModifiedForResource(request);
      if (((clientEtag != null && clientEtag.equals(etag)) || clientEtag == null) ){
        if ((clientLastModified == null || (clientLastModified != null &&
    		longClientLastModified == lastModified))) {
                       response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                       // dont proceed with the invocation. Instead return from this filter.
          }
```
This sample code would accomplish the necessary logic for processing HTTP validators.

## Comments

**[Gaurav Malhotra](#1750 "2010-04-30 10:26:00"):** This is simple awesome article Raja. It just took me seconds to configure above on weblogic....

**[raja shankar kolluru](#1751 "2010-04-30 16:10:00"):** Thanks Gaurav, The combination of the caching directive along with the 304 processing is very useful. Let us say we have a dashboard that is refreshed periodically. (Ex: build dashboard) It is now possible to attach a unique etag with every update. Ex: for the build dashboard the build number can be the tag. If the browser has retrieved build 8.12.1 for example, then it is possible to keep giving out 304 error codes till there is an update to the dashboard.

