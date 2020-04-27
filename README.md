# Transformation Advisor SDK

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ibm.ta.sdk/ta-sdk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ibm.ta.sdk/ta-sdk)

This is a tool to help a developer to create and build a middleware data collection plug-in, which can be used to gather the information of an application deployed on a middleware server, and provide modernization help and recommendations.  See [Getting Started Guide](docs/GettingStarted.md)

### Modules

name| description
--- | ---
ta-sdk-spi | Interface of the plug-in framework
ta-sdk-core | Default implementation of SPI framework
ta-sdk-sample | Sample plug-in

### Guide for Plugin Developer
If you want to develop your own plugins using TA SDK,  [here](https://github.com/IBM/transformation-advisor-sdk/wiki/Transformation-Advisor-SDK-Plugin-Developer-Guide) is the step by step guide to get start.

### Dependency
Other middleware plug-in project will depend on the ta-sdk-core module.

Need to add this in your pom.xml file.
```
        <dependency>
            <groupId>com.ibm.ta.sdk</groupId>
            <artifactId>ta-sdk-core</artifactId>
            <version>0.5.3</version>
        </dependency>
```
ta-sdk-core module will depend on the ta-sdk-spi module.

### Build
This project uses Maven version later than 3.6.0 to build. Download and configure Maven before building this project.

On all platforms, build by running this command:
```bash
mvn clean install
```
Output archive files can be find in target/ directory.

### Run data collector
To run the data data collector:
```
cd target
java -jar ta-sdk-sample-0.5.3.jar help
```

The `help` command shows the usage for the data collector. The usage information for `middleware` shows the list
middleware based on which plug-ins are in the classpath. In the example below, only 1 middleware/plug-in, `sample`, 
is available:
```
Middleware:
  Plug-ins available for these middleware [ sample ]
```
The `<middleware> help` command shows the command usage for that middleware.
```
java -jar ta-sdk-sample-0.5.3.jar sample help
```

You can run all the stages of the sample plugin using the `run` option and 2 arguments, `INSTALL_PATH` and `DATA_DIR`.
The `INSTALL_PATH` is the directory called `sampleData` in the `resources` folder and `DATA_DIR` is the `sample` directory in the `resources` folder.
```
java -jar ta-sdk-sample-0.5.3.jar sample run <YOUR_LOCATION>/transformation-advisor-sdk/ta-sdk-sample/src/main/resources/sampleData <YOUR_LOCATION>/transformation-advisor-sdk/ta-sdk-sample/src/main/resources/sample
``` 

Output artifacts are in `output` directory.

Logs are in `logs` directory.

### Run data validator
The TA SDK includes a validation utility that could be used to validate the output artifacts created by a 
plugin. Plugin developers could also use this utility to validate the metadata files that are used to generate 
the recommendations.

To run the data validator:
```
cd target
java -cp ta-sdk-sample-0.5.3.jar com.ibm.ta.sdk.spi.validation.TaValidator [OPTIONS]
```

These options are available to validate the output artifacts created by a plugin:
 -e,--environment <arg>      Validate environment JSON file
 -r,--recommendation <arg>   Validate recommendation JSON file
 -z,--collection <arg>       Validate collection zip file, including directory structure, environment and 
recommendation JSON file


These options are available to validate the metadata files used to generate the recommendations:
 -c,--complexity <arg>       Validate complexity JSON file
 -i,--issue <arg>            Validate issue rule JSON file.
 -t,--target <arg>           Validate target JSON file
 

### Contributing to Transformation Advisor SDK
See [CONTRIBUTING.md](CONTRIBUTING.md).

### License
The Transformation Advisor SDK is licensed under the Apache 2.0 license. 

Full license text is available at [LICENSE](./LICENSE).
