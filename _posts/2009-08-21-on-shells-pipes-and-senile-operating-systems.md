---
title: On Shells, Pipes and Senile Operating Systems..
link: http://itmusings.com/featured/on-shells-pipes-and-senile-operating-systems
author: raja
description: On the aging of Unix and the culture that it begat
post_id: 361
created: 2009-08-21 09:54:21
comment_status: open
post_name: on-shells-pipes-and-senile-operating-systems
status: publish
layout: post
home_page: true
category: musings
image: /images/2009/08/unix.jpg
tags:
  - UNIX
  - musings
  - pipes
  - chain of responsibility
---

 I have never lived in New Hampshire. But its motto ('Live Free or Die') reflects the spirit behind free software. It would be very cool to obtain a license plate like the one shown here. **Unix** epitomizes innovation to most of us. It heralded the imminent triumph of free software. [And now it is turning 40 in August 2009](http://news.bbc.co.uk/1/hi/technology/8205976.stm)! As the article mentions, in Operating Systems land 40 is old!  But Unix is as strong as ever and getting stronger. A "getting senile" system with a rejuvenated heart - looks like an oxymoron. But that is Unix - a bundle of contradictions!  Unix has always been a bunch of contradictions. Is it free? or not free? It was free when AT&T released its source code to the multitude. It was not free when we had to purchase Solaris, SCO-UNIX, AIX, HP-UX or one of the versions that came bundled with various machines. It suddenly became free with LINUX. It is not free with OS-X. It is not free with Enterprise versions of LINUX with support thrown in. Can you make up your fickle mind ? UNIX system has always been a programmer's delight. I had always wondered why that is the case and my inevitable answer has always been about its scriptability. After OS-X got repackaged with UNIX underpinnings, there was no looking back! Programmers made an en masse switch. Shell and the pipe are big players in the success of UNIX. The C[hain of responsibility](http://en.wikipedia.org/wiki/Chain-of-responsibility_pattern) has always been one of my most favorite patterns. I think this is partially due to the fact that I experienced its power when I typed "ls | grep xxx | xargs cat " and it printed out the contents of all files in the current directory which have xxx in their name. Sounds like an easy way to nail down all my porn huh? In my view, the whole of the SOA movement is an outcome of this realization - that micro level tasks (or services) are extremely re-usable and can be combined in very interesting ways to create macro level tasks. The pipe command showcases this power in an amazing fashion. Thinking about SOA, one of the requirements is to have a [canonical data model ](http://www.eaipatterns.com/CanonicalDataModel.html) so that disparate systems (or services) can talk to each other. That is the biggest reason for the universality and popularity of XML in SOA - so much so that SOA has been equated to web services using XML in a lot of the literature. But our UNIX mavens had this canonical data model nailed down a long time ago.   They just used a stream of bytes as their canonical data model to induce a lot of unrelated UNIX commands to talk to each other. Thus, in the above sequence, "ls" emits out a stream of bytes in its standard output which serves as the input for "grep" and so on. The more commands you know, the easier it is for you to put together a complex pipeline capable of doing something that is much more than the sum of its parts. I remember an interesting incident. Years ago, before the advent of windowing systems we had to work with a UNIX box from a "dumb terminal". That was the time when there was no notion of command history. So any command needed to be typed at the terminal once again rather than being recovered from command history. I was trying to move a bunch of files to a new directory. I started typing an extremely long "mv" command since there were quite a lot of files to move. I went : mv file1 file2....... (a really long list of files)... and suddenly realized that I don't have the directory to which they have to move created yet! Now I cannot type "Ctrl-C" and lose this whole command. 

 The only recourse available is to login to another dumb terminal, create the directory and then come back to the current session. But that solution sounded so dumb and unimaginative! Instead I typed the following at the end of the line:
```
  $ mv file1 file2 .... (a really long list of files) ... `mkdir newdirectory;echo newdirectory` 
```

  Since, the backquoted command gets executed first, I was able to create the directory and make "mv" to move all the files to the newly created directory. This is the kind of stuff that only happens in a UNIX environment. I suddenly felt very geeky and productive - a truly UNIX kind of experience. That is what UNIX does to you - makes you invent new ways to solve problems. This is the kind of philosophy that has been carried forward in scripting languages such as Perl, Ruby etc.- where there is no dearth of admonitions that there are multiple ways to solve the same problem. UNIX may or may not be extant 40 years hence from today. But the ideas that it has bred would be around for quite a while.. So here is wishing the senile son of a gun a lot more longevity..
