Feature: Category management

  Background:
    Given url baseUrl + '/v1/categories'

  Scenario: Get all categories
    When method GET
    Then status 200
    And match $ == '#[15]'
    And match $ contains {"name":"Clothes","id":1,"parentId":null}
    And match $ contains {"name":"Headphones","id":12,"parentId":3}

  Scenario: Create a category
    And request { name: 'new category for test', parentId: '1' }
    When method POST
    Then status 200
    And match $ == {"id":16,"name":"new category for test","parentId":1}

  Scenario: Get created category
    Given path '/16'
    When method GET
    Then status 200
    And match $ == {"name":"new category for test","id":16,"parentId":1}

  Scenario: Update category
    Given path '/16'
    And request { name: 'modified name for test', parentId: '2' }
    When method PUT
    Then status 200
    And match $ == {"name":"modified name for test","id":16,"parentId":2}

  Scenario: Delete category
    Given path '/16'
    When method DELETE
    Then status 200

  Scenario: Check deleted category does not exist
    Given path '/16'
    When method GET
    Then status 404