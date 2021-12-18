# author Troels (s161791)
Feature: Payment

  Scenario: Payment must fail because of wrong bank id's
#    When a colleague working with accounts is done he notifies me with "PaymentAccountsFailed"
    When i receive a message "PaymentAccountsFailed" it means that one or both accounts does not exist in the bank
    Then i notify the parties that the transaction failed with "PaymentFailed"
    And the message should be "PaymentFailed"

  Scenario: Payment most fail because of a invalid token
    When a colleague working with tokens is done he notifies me with "TokenValidationFailed"
    Then i have to notify the parties that the transaction failed with "PaymentFailed"

  Scenario: Payment process can continue when token is confirmed to be valid and bank do as it should
    When i receive "TokenValidationSuccessful"
    Then i check if the bank have transferred money
    Then i have to notify the parties that the money as been moved by sending "PaymentSuccessful"

  Scenario: Payment process can continue when token is confirmed to be valid but the bank's IT system is down
    When i receive "TokenValidationSuccessful" but something goes wrong
    Then i check if the bank have transferred money
    When the bank denies the transaction
    Then i have to notify the parties that the transaction failed with "PaymentFailed"