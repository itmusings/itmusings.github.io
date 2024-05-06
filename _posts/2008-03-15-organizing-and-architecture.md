---
title: Organizing and Architecture.. 
link: http://itmusings.com/architecture/organizing-and-architecture
author: raja
description: musings on similarity between architecture and the principles of organization
post_id: 32
created: 2008-03-15 07:34:39
comment_status: open
post_name: organizing-and-architecture
status: publish
layout: post
category: musings
image: /images/organizing.jpg
home_page: true
tags:
  - musings
---


As a practicing architect, I find that I have this obsessive compulsive thing about my applications and the way they are structured. It started in a small way with separation of concerns. Does the web layer have business logic? Then it developed more into a worry about leakage of concerns within my business layer itself. Am I orchestrating business processes in my domain layer? Am I tightly coupling workflow logic with business logic? Am I doing business rules in the domain? I am now at this point where I need to visualize the entire world into distinct buckets of information. Talk about taking the Single Responsibility Principle to an extreme! This obsession has now started percolating into other parts of my life. I am forever thinking about how I write my documentation. It has also reached my household since I want the entire house to be organized into distinct places where I can find things. The hammer and nails should be in my toolbox and the undergarments must be in their drawer. I think once the architecture bug bites you, it wont spare you.  It is all about organizational principles, I guess. Sometimes I am wading thru a book shop and am mildly amused at the kind of books on organization. "A guide to organizing your life" proclaims a bombastic newsprint. Another title takes a less ambitious approach and is content to just "organize your stamp collection" or "organize your bathtub". I guess, it is all about the way we process information. It is easier if information is distinctly slotted in taxonomies and categories. The same theory exists in application programming and enterprise architecture. In this context, patterns are great facilitators for organization. We need to recognize the patterns and then we can categorize information in their distinct buckets. This is especially true if information is complex and can belong to multiple categories. We need some kind of pattern unifying them. An example may serve to illustrate my point. Everyone of us writes search screens. They gather information from multiple sources (databases, Content Management Systems, google like text searches etc.). But they all share the same attribute i.e. the ability to take a filter and execute it to extract information from some system. This commonality, if properly understood, can be harnessed to benefit. We can have generic search filters, search views etc. Hence an architect must be an expert in processing information and organizing it into correct buckets. Interesting right?
