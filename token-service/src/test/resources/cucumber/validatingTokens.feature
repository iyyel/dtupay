# @primary-author Emil (s174265)
# @co-author Tobias (s173899)


Feature: Validation of tokens

  Scenario: Validation of an unused token
    Given a customer with id "4638"
    And has an unused token
    And the customer receives a token
    And it gets a token successfully
    When a token is sent to the server
    Then the token is invalidated
    And the customer "4638" is returned

  Scenario: Validation of a used token
    Given a customer with id "1348"
    And has an unused token
    And the customer receives a token
    And it gets a token successfully
    And the token is deleted
    When a token is sent to the server
    Then a TokenNotFound exception is returned

  Scenario: Customer tries to get while having no tokens
    Given a customer with id "1385"
    When the customer tries to receive a token
    Then an event of type "GetTokenFailed" is received
    And an error for customer "1385" is returned

  Scenario: Receiving a check for a NULL token
    Given a broken validation paymentAccounts
    When the null token is sent to the server
    Then an event of type "TokenValidationFailed" is received