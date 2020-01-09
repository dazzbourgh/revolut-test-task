# Revolut Test Task

## Tech stack

- Java 11
- DI - Google Guice
- REST - Javalin
- Testing - JUnit 5 & Mockito

## How to run
Run `Main::main`, the app starts on port 8088 by default, but you can pass an argument to `main` with a different port.

The `jar` file is on top level. You can build your own, but I was explicitly asked to include the jar in VCS, so it's here.

## API
**GET** /v1/accounts/:id - check balance

**POST** /v1/deposit - add money, send `Deposit` DTO as request body

**POST** /v1/withdraw - withdraw money, send `Withdrawal` DTO as request body

**POST** /v1/transfer - transfer money from one acc to another, send `Transfer` DTO as request body

## Interesting stuff
- Data is stored in a HashMap, that is injected in `HashMapAccountDao` and initialized in `AccountModule`
- Account operations can be found in `AccountCommandServiceImpl` and `AccountQueryServiceImpl`, all cool **concurrency** is there
- Refer to `LockFactory` and `Operation` to see how concurrency is implemented
- `AccountController` has handlers that are mapped to HTTP requests, you can find some **validation** there
- **Integration tests** demonstrate the functionality, cover edge cases etc, you can find them in `integration.account.*` 

## Boring stuff
- There are also unit tests that cover the classes, you can find them in `test/java` as well
- Lombok is used to avoid cluttering data classes
- Amount validation predicate is located in `com.revolut.validation` and is reused amongst handlers