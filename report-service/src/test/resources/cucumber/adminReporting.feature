# @primary-author Tobias (s173899)
# @co-author Emil (s174265)

Feature: Manager of DTU Pay
  Scenario: Report of transactions
    Given a list of transactions
      | amount | balance | creditor | debtor | description | time       | token |
      | 10     | 1000    | 1234     | 2345   | test        | 2021-01-01 | abc   |
      | 30     | 970     | 1234     | 2345   | test2       | 2021-01-03 | abcd  |
      | 60     | 100     | 12345    | 23456  | test3       | 2021-01-04 | abcde |
    When a request to see all transactions is made
    Then the list of transactions is shown

  Scenario: Summary of transactions
    Given a list of transactions
      | amount | balance | creditor | debtor | description | time       | token |
      | 10     | 1000    | 1234     | 2345   | test        | 2021-01-01 | abc   |
      | 30     | 970     | 1234     | 2345   | test2       | 2021-01-03 | abcd  |
      | 60     | 100     | 12345    | 23456  | test3       | 2021-01-04 | abcde |
    When a request for summary is made
    Then a summary is made based on the transactions
      | min | max | mean | sum |
      | 10  | 60  | 33   | 100 |

  Scenario: Transaction recorded from payment
    When the transaction is recorded
    Then a "TransactionRecordingSuccessful" is sent
    And the transaction can be found in the repository


  Scenario: Transaction with missing details
    When a new broken transaction is recorded
    Then a "TransactionRecordingFailed" is sent

