# Contributing

First of all, thank you for considering to contribute to this project, every effort to improve the project is greatly appreciated.

When contributing to this repository, please first discuss the change you wish to make via issue,
email, or any other method with the owners of this repository before making a change. 

Please note we have a code of conduct, please follow it in all your interactions with the project.

## Technical details and guidelines

### Opening the project

 - As a prerequisite you need an Eclipse installation
 - You have to add the PDE plugin (plugin development environment) to your Eclipse via Eclipse marketplace
 - You can then open it in two ways:
   - The repository contains Eclipse's .project file, therefore you can open it with Eclipse -> File -> Open project from File System -> select the folder
   - Alternatively you can import it via File -> Import -> Plugin Development -> Plugins and Fragments -> choose the correct directory and add the plugins

### Running the project

 - Once you loaded the plugin into Eclipse you can run it by right clicking on the project -> Run as -> Eclipse application
   - This will start up a new Eclipse instance with the plugin installed (it will create a new workspace under Eclipse_Runtime)
   - Some warnings might be shown on the console, this is nothing to be worried about
 - You can debug the project the same way, by using "Debug as"
 - You can run all the tests as generic TestNG tests
   - Install TestNG Eclipse plugin
   - Right click on any test, or folder containing tests -> Run as -> TestNg test
   - At this point, there is no automatic build process that runs tests, this is something, that eventually has to be done via Maven integration :heavy_exclamation_mark:


### Integration tests
 - Integration test is a requirement for all features, except those, that solely affect Eclipse configuration
 - Integration test mocks out all Eclipse settings, but it does test the entire flow of the plugin from the input class to the resulting builder
 - Best to check how a previously written IT works in the repository: [HappyFlowE2ETest.java](https://github.com/helospark/SparkBuilderGenerator/blob/master/SparkBuilderGeneratorPlugin/test/com/helospark/spark/builder/handlers/it/HappyFlowE2ETest.java)
 - Generally it goes like this:
    - Set mock values (ex. Eclipse preferences, super class's, etc.)
    - Read input file (they are files with tjava extension under test_resources)
    - Read expected output file (they are files with tjava extension under test_resources)
    - Run the builder
    - Check if the actual output is the same as the expected one

## Branching strategy

 - Use feature branches
 - Name of the feature branch by convention is "github-issue-(githubId)-(some-description)"
 - All commits in this branch should start with #GithubId for easier reference later on, it also help with connecting the issue with the commit so GitHub shows the work done
 - After the development is done, create a pull request to the master
 - In case of any review comments, discuss or fix the mentioned item
 - After review the project maintainer will merge the change to master and make a release
 - Exception to this rule might be done for very small improvements that does not affect the working of the project - like documentation - the change can directly be pushed to master without branching

## Definition of done (DoD)

Before merge all items in the DoD must be done:

 - There is a GitHub issue with the description of the work
 - You have followed the branching strategy and created a PR to master
 - Your change satisfies the AC
 - You have written integration tests that cover all the new code's major flows
 - You have discussed or fixed all review items that might have come up
 - You have tested the change manually
 - You have added logging on possible error cases


### Release process
 - Release process is currently done by the repository maintainer
 - Process
   - Add the documentation to the readme about what the new version contains
   - Raising the version of the project in plugin.xml
   - Add documentation to the feature.xml about what the new version contains
   - Raising the version of the feature
   - Raising the version of the update site, and add the new feature
   - After adding the feature, use the "Build all" button to create the new update site
   - Deploy the update site to the HTTP server
   - Deploy the update site to the FTP server
   - Deploy the update site to the Mediafire backup
   - Change the version in the Eclipse Marketplace
   - Add documentation to the marketplace about what the new version contains
