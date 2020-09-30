Feature: Health endpoint
#
#  Background:
#    Given url baseUrl
#    Given path '/actuator/health'
#
#  Scenario: Health endpoint returns UP
#
#    When method GET
#    Then status 200
#    And match $ == {"status":"UP"}
