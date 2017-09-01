# VR-RPG Server

## Development requirements
- java8 JDK (`http://www.oracle.com/technetwork/java/javase/downloads/index.html`)
- maven (`https://maven.apache.org/`)
- mongodb (`https://www.mongodb.com/`)
- intelliJ ide is used as an IDE (`https://www.jetbrains.com/idea/`)

## How to start development
- Installation
    1. install java8 JDK (don't forget to setup the environment variables in case of windows)
    2. install maven (don't forget to setup the environment variables in case of windows)
    3. install mongodb
    4. install intelliJ

- Run project (from intelliJ)
    0. start mongodb (or setup the `MONGODB_URI` as environment variable for a remote mongodb)
    1. File -> New -> Project from existing sources...
    2. Select the `pom.xml`, then next next next next...
    3. Create run configuration that runs `com.vrrpg.server.VrRpgServerApplication`
    4. [optional] Setup environment variables in the configuration (see configuration below)

- Run project (from CMD)
    1. start mongodb (or setup the `MONGODB_URI` as environment variable for a remote mongodb)
    2. [only at first] `mvn clean install` - prepares the project for run
    3. `mvn spring-boot:run` - runs the project from cmd|terminal

## Configuration

The following environmental variable are available for configuring the vr-rpg-server:

|Name                         |Value(s)      |Description                                      |
|-----------------------------|--------------|-------------------------------------------------|
|`CLOUDINARY_URL`             |`string`      |The url for the cloudinary service connection.   |
|`LOG_LEVEL`                  |`trace`, `debug`, <br /> `info`, `warn`, <br /> `error`|Minimum log level configuration. Default value - `info`|
|`MONGODB_URI`                |`string`      |The url for the mongoDB access. Default value - `mongodb://localhost/test`|
|`SENTRY_DSN`                 |`string`      |Url for sentry.io integration.                   |
|`SENTRY_LEVEL`               |`trace`, `debug`, <br /> `info`, `warn`, <br /> `error`|Minimum reporting level for sentry.io. Default value - `WARN`|

Contact:
gyorgy.bucsek@gmail.com
