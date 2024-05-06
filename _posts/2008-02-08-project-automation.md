---
title: Project Automation
link: http://itmusings.com/management/process/project-automation
author: raja
description: On the automation thought process
post_id: 29
created: 2008-02-23 19:18:53
post_name: project-automation
status: publish
layout: post
home_page: false
category: management
image: /images/project-automation.jpeg
---


There was a lady in one of the companies that I had worked for. She was doing, what is probably the most routine of jobs. She works evening shifts. Her chief duty was to trigger the build job in the night and notify us if anything goes wrong with it. We used to call her CJ - Cron Job. We had many private jokes at her expense. It did not help that her first name was Cathy. Some of our water cooler conversations went like:  


Is Cathy dating Rick Jones yet?  
Not that I know of ? Is there something that is cooking?  
Nah nothing. just my bad sense of humor.. If she marries Rick she will live up to her CJ title.

And so it went...

The reason that I mentioned Cathy is that she comes into my thoughts quite often when I think of all the automation work that I have to do but don't get the time to do. Cathy was not into computers when we had hired her back in 1995 to run our cron jobs. But are we,as self proclaimed computer whizzes, doing much better than Cathy in our day to day jobs?

Tell me when was the last time you had a web site that reported the status of your builds? When was the last time you worked on a project that produced a dashboard which presented statistics about the lines of code and javadocs in one place? I am sure some of you have been fortunate in this aspect. But for the majority, this is a Utopian dream.

These have become even more important concerns of late - especially with the advent of the open source movement. With the open source frameworks, a lot of people from different parts of the world have started collaborating on producing code. The latest code has to be made available to all of them. Further, there are even more developers who want to utilize the code - even beta and alpha versions of it. These people would not want to mess with the source code and go through the pain of building it. Instead, they want to utilize the produced artifacts. So it becomes imperative to provide a place from where, the latest built artifacts can be picked up in a consistent manner.

Add to all these, an unrelated requirement. Most organizations have audit groups, which insist on code being compliant to certain standards that are explicitly laid out. These coding standards, for the most part, can be checked for by automated tools such as checkstyle, findbugs or pmd. So why have manual coding standards if they can be automated by tools. How about code coverage for unit tests? Besides this, many other metrics can be derived from code that can potentially be checked using various tools. So the report generated must also be available in the project dashboard.

It truly needed a code maven to take care of all these requirements. As if in answer to this developer prayers, in came maven in its latest incarnation - Maven2.

Maven2 has a ton of features. Out of these, the following in my view are especially relevant to this discussion:

  1. Maven2 takes care of dependency management in a transitive manner. If project A depends on project B which in turn depends on project C, then project A would automatically depend on project C.
  2. The above feature,by itself, is incomplete without being supplemented by another cool feature. Maven2 automatically downloads the entire trail of dependencies from code repositories. The entire code is brought into a designated local repository for the purpose of local compilation.
  3. Maven2 has a notion of project object module. This describes the project. Also, this is inheritable. Hence it is possible to have a super pom that has the base configuration for every project.
  4. Finally, Maven2 is highly extensible with a very modular structure. Plugins exist for taking care of site generation, executing junit tests, generating javadocs, generating reports for automated tools etc. It also has plugins that are responsible for release management.

Keeping all these features in mind, we do the following:

  * Before doing anything, standardize on maven2 for all builds in the company. This is probably the biggest step but really pays in the long run.
  * Next, we write a base pom that takes care of all the organizational audit requirements. It invokes PMD, checkstyle, a test coverage tool such as Clover etc. This also provides a base configuration for the maven site for all the projects. Now we can mandate that all projects in the organization should inherit this pom. Hence a gazillion requirements would be translated into just one requirement namely, inherit from the organizational pom for your project.
  * Second, we get hold of a build scheduler such as cruise control or even better - something more sophisticated such as Atlassian Bamboo which allows builds to be configured from a web site. All projects would be executed by this tool. There goes poor Cathy's job!
  * Now that maven2 has been scheduled to be automatically executed, the site gets regenerated with every build. This site is automatically up to date. Further, all the audit checks are getting performed automatically with instant notifications in case of non compliance! sweet!
  * Artifacts are also generated by maven2 on a scheduled basis. Now all that remains is to enable access to these artifacts so that other developers can use them without having to build them from the source code. We know that the generated artifacts would be "installed" in the local repository by maven2. Expose the local maven2 repository in the build machine (where cruise control is running) through the web now. This is easily done using symbolic links.
  * Now any developer who intends to access the generated artifacts, merely gets configures the exposed URL (from the above step) as one of the maven repositories from which source code needs to be picked up.

Using this setup, it is easy to see how Maven2 realizes all the requirements that were enumerated previously.

By the way, we haven't covered other features such as Maven2's integration with Bug tracking systems, its release features, its support for versions ( especially its support for SNAPSHOTS). Covering all that would bloat this article too much.

PS: Cathy is a fabrication. I had a "build person" in several projects who used to perform a job close to what Cathy does. But her name and all the jokes associated are purely mine to make the post interesting :-)
