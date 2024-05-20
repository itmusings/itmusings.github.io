---
title: Complete Unit & Integration Testing
author: raja
description: The different types of tests and how we handle them. Article is in draft state.
date: 2024-05-10 20:00:00
layout: post
image: 
category: code
home_page: false
tags:
  - spring boot
  - chenile
  - unit-testing
  - java
  - code
---

As authors of a framework, we are fanatical about unit tests. We want our tests to run as close to the production environment as possible. We want them to not only return results but to do so with most of the horizontal concerns such as security, caching etc. in place. We want serialization and de-serialization to work perfectly.

We want all the nuances of the transport such as HTTP, MQ etc. to be reflected in the test cases. 
