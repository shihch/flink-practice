### To run the code
From the project root, first start the data feed:
```
nc -kl 9000 < inputs/data.json
```
then run sbt command:
```
sbt "flink/run"
```
To run the unit tests:
```
sbt "flink/test"
```
logback to provide logging facility.
Some configuration properties are in the src/main/resources/job.properties. Use "twindow_time*" properties to adjust tumbling window size.

### Design Review

Define case classes and parse json using Circe:
- First time to use Circe - it leverages the power of Scala Either class to consolidate error handling in one place, better for json4s in this aspect. The Encoder and Decoder typeclass add more flexibility to serialize/de-serialize objects.

Create helper objects:
- Spammy content - Use regex for quick match on spammy words, create simple composition syntax to combine spammy word filters. Take the lazy approach on functions so they only execute when they need.
- Bad IPs - Simple fake generator for bad IP by using sum of IP fields and mod by a number.
	
Stream processing:
- Since the requirement needs aggregated reports, take tumbling windowed approach to chunk data into non-overlapping windows like mini-batch. Generate report for each window.
- Using keyed stream virtual partition for each account.
- The total user count since beginning is handled by Flink state management.

### Todo next
The helpers in real world are most likely services, need to investigate Flink's async IO and how to bring the Future computation to it.
Potentially use of session window approach to handle this problem.
Will look into Flink table environment and SQL to see if it can solve the similar problem.

