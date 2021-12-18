# primary-author: Troels (s161791)
# co-author: Daniel (s151641)

Feature: Account management

  Background:
    Given a new account with cpr "001122-XXXX", first name "Michael", last name "Hardy" and an initial balance of 5000
    When the account is registered
    Then the registration should be successful
    And the new account should exist in the system with correct information

  Scenario: Unsuccessful registration in DTUPay: Account already exists
    When the account is registered again
    Then the registration should be unsuccessful

  Scenario: Successful account information retrieval by cpr from DTUPay: Account exists
    When account information is retrieved by cpr
    Then the retrieval should be successful
    And the retrieved information should be correct

  Scenario: Unsuccessful account information retrieval by id from DTUPay: Account does not exist
    When account information is retrieved by id "111-222-333-444" that does not exist
    Then the retrieval should be unsuccessful

  Scenario: Unsuccessful account information retrieval by cpr from DTUPay: Account does not exist
    When account information is retrieved by cpr "010203-0405" that does not exist
    Then the retrieval should be unsuccessful

  Scenario: Successful retirement of account by cpr in DTUPay: Account exists
    When a customer retires their account by cpr
    Then the retirement should be successful

  Scenario: Successful retirement of account by cpr in DTUPay: Account does not exist
    When a customer retires their account by cpr with cpr "010203-04-05"
    Then the retirement should be successful

  Scenario: Successful retirement of account by id in DTUPay: Account does not exist
    When a customer retires their account by id with id "111-222-333-444"
    Then the retirement should be successful

  Scenario: Successful retrieval of all account information in DTUPay
    When a customer requests all account information from DTUPay
    Then the request should be successful
    And should contain the right amount of accounts

