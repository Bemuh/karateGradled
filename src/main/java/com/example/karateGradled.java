package com.example;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class karateGradled implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().create("createFiles", task -> {
            task.doLast(t -> {
                createFiles(project);
            });
        });
    }

    private void createFiles(Project project) {
        String[] fileContents = {
                "function fn() {\n" +
                        "  var env = karate.env; // get system property 'karate.env'\n" +
                        "  karate.log('karate.env system property was:', env);\n" +
                        "  if (!env) {\n" +
                        "    env = 'dev';\n" +
                        "  }\n" +
                        "  var config = {\n" +
                        "    env: env,\n" +
                        "    myVarName: 'someValue'\n" +
                        "  }\n" +
                        "  if (env == 'dev') {\n" +
                        "    // customize\n" +
                        "    // e.g. config.foo = 'bar';\n" +
                        "  } else if (env == 'e2e') {\n" +
                        "    // customize\n" +
                        "  }\n" +
                        "  return config;\n" +
                        "}"
                ,
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<configuration>\n" +
                        " \n" +
                        "    <appender name=\"STDOUT\" class=\"ch.qos.logback.core.ConsoleAppender\">\n" +
                        "        <encoder>\n" +
                        "            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>\n" +
                        "        </encoder>\n" +
                        "    </appender>\n" +
                        "  \n" +
                        "    <appender name=\"FILE\" class=\"ch.qos.logback.core.FileAppender\">\n" +
                        "        <file>target/karate.log</file>\n" +
                        "        <encoder>\n" +
                        "            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>\n" +
                        "        </encoder>\n" +
                        "    </appender>    \n" +
                        "   \n" +
                        "    <logger name=\"com.intuit\" level=\"DEBUG\"/>\n" +
                        "   \n" +
                        "    <root level=\"info\">\n" +
                        "        <appender-ref ref=\"STDOUT\" />\n" +
                        "        <appender-ref ref=\"FILE\" />\n" +
                        "    </root>\n" +
                        "  \n" +
                        "</configuration>"
                ,
                "package examples;\n" +
                        "\n" +
                        "import com.intuit.karate.Results;\n" +
                        "import com.intuit.karate.Runner;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "\n" +
                        "class ExamplesTest {\n" +
                        "\n" +
                        "    @Test\n" +
                        "    void testParallel() {\n" +
                        "        Results results = Runner.path(\"classpath:examples\")\n" +
                        "                //.outputCucumberJson(true)\n" +
                        "                .parallel(5);\n" +
                        "        assertEquals(0, results.getFailCount(), results.getErrorMessages());\n" +
                        "    }\n" +
                        "\n" +
                        "}\n"
                ,
                "package examples.users;\n" +
                        "\n" +
                        "import com.intuit.karate.junit5.Karate;\n" +
                        "\n" +
                        "class UsersRunner {\n" +
                        "    \n" +
                        "    @Karate.Test\n" +
                        "    Karate testUsers() {\n" +
                        "        return Karate.run(\"users\").relativeTo(getClass());\n" +
                        "    }    \n" +
                        "\n" +
                        "}\n"
                ,
                "Feature: sample karate test script\n" +
                        "  for help, see: https://github.com/intuit/karate/wiki/IDE-Support\n" +
                        "\n" +
                        "  Background:\n" +
                        "    * url 'https://jsonplaceholder.typicode.com'\n" +
                        "\n" +
                        "  Scenario: get all users and then get the first user by id\n" +
                        "    Given path 'users'\n" +
                        "    When method get\n" +
                        "    Then status 200\n" +
                        "\n" +
                        "    * def first = response[0]\n" +
                        "\n" +
                        "    Given path 'users', first.id\n" +
                        "    When method get\n" +
                        "    Then status 200\n" +
                        "\n" +
                        "  Scenario: create a user and then get it by id\n" +
                        "    * def user =\n" +
                        "      \"\"\"\n" +
                        "      {\n" +
                        "        \"name\": \"Test User\",\n" +
                        "        \"username\": \"testuser\",\n" +
                        "        \"email\": \"test@user.com\",\n" +
                        "        \"address\": {\n" +
                        "          \"street\": \"Has No Name\",\n" +
                        "          \"suite\": \"Apt. 123\",\n" +
                        "          \"city\": \"Electri\",\n" +
                        "          \"zipcode\": \"54321-6789\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "      \"\"\"\n" +
                        "\n" +
                        "    Given url 'https://jsonplaceholder.typicode.com/users'\n" +
                        "    And request user\n" +
                        "    When method post\n" +
                        "    Then status 201\n" +
                        "\n" +
                        "    * def id = response.id\n" +
                        "    * print 'created id is: ', id\n" +
                        "\n" +
                        "    Given path id\n" +
                        "    # When method get\n" +
                        "    # Then status 200\n" +
                        "    # And match response contains user\n" +
                        "  "
                // Add the content of each file here, separated by commas
        };

        String[] paths = {
                "src/test/java/karate-config.js",
                "src/test/java/logback-test.xml",
                "src/test/java/examples/examplesTest.java",
                "src/test/java/examples/users/usersRunner.java",
                "src/test/java/examples/users/users.feature"
                // Add the remaining file paths here
        };

        for (int i = 0; i < paths.length; i++) {
            try {
                File file = new File(project.getProjectDir(), paths[i]);
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                writer.write(fileContents[i]);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}