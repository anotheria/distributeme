[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.anotheria/distributeme-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.anotheria/distributeme-parent)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

DistributeMe
============

What is DistributeMe?

DistributeMe is a framework for **automatic distribution of java code**. DistributeMe operates directly on your java code, 
the annotated interface is processed by the DistributeMe apt preprocessor, which generates distribution related code.

Those are the core features of DistributeMe:

* Automatic build time code distribution
* Transparent remoting - you don't even need to know that the call is executed remotely. 
* Built with scaleability and high reliability in mind.
* Various call interception points
* Support for different user/call based failing strategies and behavior.
* Support for different routing strategies, incl. loadbalancing, sharding, backup and failover instances.
* Support for mobile agents (experimental).
* Built-in fast decentralized publish/subscriber eventing. 
* DistributeMe consists of code generator which generates distribution code, that becomes part of your distribution package, the Registry which is a name / address resolving utility, and runtime utils used by the generated code.

DistributeMe is free and distributed under the MIT license.

[More information](wiki)



