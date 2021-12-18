# @primary-author Emil (s174265)
# @co-author Tobias (s173899)

Feature: Customer requests tokens

  Scenario: Tokens given
    Given the customer with id "1234"
    When the customer requests 3 tokens
    Then the customer owns 3 tokens

  Scenario: Customer has too many tokens to request more
    Given the customer with id "2567"
    And the customer has 2 tokens
    When the customer requests 2 tokens
    Then an error for customer "2567" is received
    And an event of type "RequestTokensFailed" is returned
    
  Scenario: Customer tries to request negative tokens
    Given the customer with id "2385"
    When the customer requests -1 tokens
    Then an error for customer "2385" is received
    And an event of type "RequestTokensFailed" is returned
    
  Scenario: Customer tries to request too many tokens
    Given the customer with id "1384"
    When the customer requests 10 tokens
    Then an error for customer "1384" is received
    And an event of type "RequestTokensFailed" is returned

  Scenario: Customer not found
    Given no customer exists with id "6843"
    When the customer requests 2 tokens
    Then the customer owns 2 tokens

  Scenario: Delete customer
    Given the customer with id "9834"
    When the customer is deleted
    Then the customer is not found

  Scenario: Deleting a customer that does not exist
    Given no customer exists with id "6543"
    When the customer is deleted
    Then an event of type "CustomerRetirementFailed" is returned