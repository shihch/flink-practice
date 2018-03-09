## Coding Challenge

### Rules Engine
Let's build a rules engine to flag risky accounts.
Here is a list of sample rules for this coding challenge.

- Account created from a bad ip, you can randomly choose couple of IPs to be bad
- Account has more than 20 users
- Account created a spammy ticket, for example, any of these:
  - Tickets with a `bit.ly` link
  - Tickets any of these words `Apple`, `Paypal`, `reset password`

Please consider this is your first task if you join the team.
**We would like to provide you hints and enjoy discussions with you during this process.
So please open a working in progress PR for communication.**

### Requirements
- Build a rules engine to show risky accounts
- Stream the inputs into your rules engine through TCP connections instead of reading from file directly
- Make your code robust enough to handle various exceptions, e.g. malformed JSON, empty lines, etc
- Unit tests your program
- Make your code clean and readable

## Prepare
This section helps you speed up with the development environment.

### Netcat
The simplest way to stream text over TCP is using netcat.
You can start `netcat` to listen to port `9999` like this on Mac.

```bash
/usr/bin/nc -lk 9999
```

### Scala or Java 
Scala is the preferred language for this exercise, but you can use Java or other JVM languages if that is your choice.

### Streaming platform
[Apache Flink](./flink/README.md) is the preferred execution engine for this exercise, but you also can choose from one of the following:

- [Apache Spark](./spark/README.md)
- [Akka streams](./akka_streams/README.md)
- Apache Storm

You are free to use Apache Beam on top of these runners or use platform native API.

**Please only choose one of above**, we are not expecting answers for all platforms.

### Input file
You can find the input file [here](./inputs/data.json).

Each line in the data set represents one of the following models:

- Account

  `data_type` is `account`, for example one account can be represented as following:

  ```
  {
    "data_type": "account",
    "id": 1,
    "type": "Trial",
    "created_at": 1517792363,
    "created_from_ip": "202.62.86.10"
  }
  ```

- User

  `data_type` is `user` and users belongs to accounts.
  
  For example one user can be represented as following:

  ```
  {
    "data_type": "user",
    "id": 1,
    "account_id": 1,
    "created_at": 1517792390
  }
  ```

- Ticket

  `data_type` is `ticket` and tickets belongs to accounts.
  
  For example one ticket can be represented as following:

  ```
  {
    "data_type": "ticket",
    "id": 1,
    "account_id": 1,
    "content": "You can reset your password here: http://bit.ly/12zbe09",
    "created_at": 1517803090
  }
  ```

### Output format
Please group all reasons by account `id` and print them out. For example:

```
Account 1 is risky because
- It is created from a known bad ip: 202.62.86.10

Account 2 is risky because
- It created a spammy ticket with bit.ly link
- It created a spammy ticket with Apple word in it
```

