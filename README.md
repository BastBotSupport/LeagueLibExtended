LeagueLibExtended
=================

###Disclaimer:

I did not create the original LeagueLib project, and I am not the owner of the LeagueLib project. The original LeagueLib project was created by Anshu Chimala, Tyrus Tenneson, and Gavin Saldanha of [LolTeam] (http://www.lolteam.net/). The original project's repository can be found at https://github.com/achimala/leaguelib.

I did this extended version for fun, and meant to cause no trouble by doing so. That being said, if requested by the original developers, this project will be removed.



###Purpose:

The purpose of the changes I have made are to provide a larger amount of data retrieved from the servers in an easier manner. I started out with the intent at making the information retrieved easier to handle/manipulate by using utility functions with a goal of making these items more accessible to those with less knowledge of Java programming. 

I also built up more portions of the code to include information that was not already being retrieved/handled. Most of the additions I made were most likely future additions to the LeagueLib project. My goal with this was to add information to the project such that the api contained most general information that is a part of larger League of Legends scouting websites, such as [LolKing] (http://www.lolking.net/).

In the end, I ended up doing this out of pure enjoyment of the learning process, and my passion for programming. Most all changes that I have made from the original LeagueLib project have been documents in the "LeagueLib Changes.txt" file.


##The Following is the Contents of the Original README for LeagueLib

LeagueLib is a Java library for the [League of Legends](http://www.leagueoflegends.com) [RTMPS](http://en.wikipedia.org/wiki/Real_Time_Messaging_Protocol) API, built on top of [LoLRTMPSClient](http://code.google.com/p/lolrtmpsclient) by Gabriel Van Eyck.

LeagueLib is built to power efficient, scalable, concurrent League of Legends web applications. Best served on a framework like [Play](http://www.playframework.com/).

This is the stuff we built to replace the backend that runs [LoLTeam](http://www.lolteam.net) and [LoLTalk](http://loltalk.achimala.com). Our original code did its job, but wasn't scalable or efficient enough to meet our rising traffic. In response, we started building a new, highly scalable, massively concurrent framework for these applications, to serve tens of thousands of players all over the world. And we're making it open source so that other developers like us can make awesome League of Legends apps.

### Features
* Completely asynchronous API calls to the League of Legends RTMP server.
* Synchronous API calls just as easily, for use in simple applications or on top of distributed, scalable, concurrent platforms like [Akka](http://www.akka.io).
* Easy support for multiple League of Legends accounts for increased concurrency and minimal rate-limiting side effects.
* Efficient design: request only as much information as you need, using the same API calls and structure that League of Legends uses internally.
* Clean MVC patterns: Accounts can easily be marshaled to files or recorded in databases, distributed to different account queues for different servers, and a single LeagueSummoner model can contain references to real-time data across your application that updates in place.

### Usage

We intend to put up a better form of documentation in the future, but until then, here are some basic implementation notes.
* LeagueLib doesn't embed any kind of thread/job scheduling or distributed computing architecture. We figured more than enough of these exist and we instead chose to build something that could plug into any of them easily. For LoLTeam and LoLTalk, we simply wrote a layer on top of LeagueLib that plugs it into Play Framework's asynchronous job scheduling built on top of Akka, which runs on multiple processes distributed across the cloud via Heroku.
* LeagueLib was designed with the primary goal of giving us scaffolding to build LoLTeam and LoLTalk on top of. As such, it is robust but not necessarily completely feature complete. We're going to keep adding on to it.
* The LeagueSummoner object is the focus of everything in LeagueLib as it is right now. The very fundamental summoner service API calls return empty summoner containers with names and IDs and nothing else. As you request different types of information from different services, they get filled into your single summoner object in place. You should not be maintaining multiple copies of a summoner or throwing them away for no reason.

### License
LeagueLib is licensed under the GNU GPL v3. You may use this code for commercial or non-commercial purposes.

You may:
* Use LeagueLib in personal or commercial products, with attribution.
* Modify and distribute LeagueLib, with attribution.

You may NOT:
* Sell any portion of LeagueLib.
* Use or distribute any portion of LeagueLib without the included licensing information, or without attribution.

### Caution
LeagueLib is still currently in development. We would advise against using it in production applications until it's ready.

Code pulled off the master branch should always work correctly and pass all tests. We intend to put up sample code soon, but until then you should hopefully be able to figure out how to use LeagueLib by viewing [MainTest.java](https://github.com/achimala/leaguelib/blob/master/src/com/achimala/leaguelib/tests/MainTest.java) or by reading through the comments on the LeagueConnection, LeagueAccount, and *Service.java classes.
