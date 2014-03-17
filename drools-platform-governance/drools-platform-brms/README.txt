
For deploying this overlaid war from IDEA into apache tomcat 7, you have to:
 - Deploy this artifact as an exploded war
 - Remove the build instruction from the 'Before launch' field set
 - Add the Maven package (mvn package) build instruction from the 'Before launch' field set

 Now you can run the customized brms from the idea with tomcat 7