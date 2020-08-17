## Server Spec
- cpu: 2 cores
- memory : 4gb
- jvm options : I don't use this

## Client Spec
- cpu: 4 cores
- memory : 8gb
- test program : gatling
10users at once / 10users for 5seconds / 20users per a second for 20seconds at regular interval / 20users per a second for 20seconds at randomized interval /  10users to 20users per a second for 20seconds at regular interval / 10users to 20users per a second for 20seconds at randomized interval / 1000users (following heavy side step function) for 20seconds

##  Test Result
| total request | success request | min response time | max response time | mean response time | req/s |
|---|---|---|---|---|---|
|19574|18940|8ms|515ms|14ms| about 15.55req/s|


