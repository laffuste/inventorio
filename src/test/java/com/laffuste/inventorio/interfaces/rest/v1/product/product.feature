Feature: Product management

  Background:
    Given url baseUrl + '/v1/products'

  Scenario: Get all products
    When method GET
    Then status 200
    And match $.numberOfElements == 10
    And match $.content == '#[10]'
    And match $.content contains {"quantity":2,"name":"Pepe Jeans","id":1,"categoryId":5}
    And match $.content contains {"quantity":999,"name":"Century Egg","id":6,"categoryId":10}

  Scenario: Search products
    Given params { name: 'o', isAvailable: true }
    When method GET
    Then status 200
    And match $.numberOfElements == 5
    And match $.content contains {"id":4,"name":"Compte cheese","categoryId":9,"quantity":3}

  Scenario: Create a product
    And request { name: 'new product for test', categoryId: '1', quantity: 10 }
    When method POST
    Then status 200
    And match $ == {"id":17,"name":"new product for test","categoryId":1,"quantity":10}

  Scenario: Get created product
    Given path '/17'
    When method GET
    Then status 200
    And match $ == {"id":17,"name":"new product for test","categoryId":1,"quantity":10}

  Scenario: Update product
    Given path '/17'
    And request { name: 'modified name for test', categoryId: 2, quantity: 99999 }
    When method PUT
    Then status 200
    And match $ == {"name":"modified name for test", "id":17, categoryId: 2, quantity: 99999}

  Scenario: Delete product
    Given path '/17'
    When method DELETE
    Then status 200

  Scenario: Check deleted product does not exist
    Given path '/17'
    When method GET
    Then status 404