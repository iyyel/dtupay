# @primary-author Tobias (s173899)
# @co-author Emil (s174265)

Feature: Merchant reporting
  Scenario: Merchant requests a list of transactions
    Given a list of merchant transactions
      | amount | balance | creditor | debtor | description | time       | token |
      | 10     | 1000    | 2345     | 1234   | test        | 2021-01-01 | abc   |
      | 30     | 970     | 2345     | 1234   | test2       | 2021-01-03 | abcd  |
      | 60     | 910     | 23456    | 1234  | test3       | 2021-01-04 | abcde |
    When the merchant "2345" requests all their transactions
    Then the merchant transactions are displayed
      | amount | creditor | description | time       | token |
      | 10     | 2345     | test        | 2021-01-01 | abc   |
      | 30     | 2345     | test2       | 2021-01-03 | abcd  |

  Scenario: Merchant requests transactions in a given time period
    Given a list of merchant transactions
      | amount | balance | creditor | debtor | description | time       | token |
      | 10     | 1000    | 2345     | 1234   | test        | 2021-01-01 | abc   |
      | 30     | 970     | 2345     | 1234   | test2       | 2021-01-03 | abcd  |
      | 60     | 910     | 2345     | 1234   | test3       | 2021-01-04 | abcde |
      | 60     | 910     | 2345     | 1234   | test4       | 2021-01-05 | abcde |
      | 60     | 910     | 2345     | 1234   | test5       | 2021-01-06 | abcde |
    When the merchant "2345" requests their transactions between "2021-01-03" and "2021-01-05"
    Then the merchant transactions are displayed
      | amount | creditor | description | time       | token |
      | 30     | 2345     | test2       | 2021-01-03 | abcd  |
      | 60     | 2345     | test3       | 2021-01-04 | abcde |
      | 60     | 2345     | test4       | 2021-01-05 | abcde |

  Scenario: Merchant requests transactions in a given time period with missing time
    Given a list of merchant transactions
      | amount | balance | creditor | debtor | description | time       | token |
      | 10     | 1000    | 2345     | 1234   | test        | 2021-01-01 | abc   |
      | 30     | 970     | 2345     | 1234   | test2       | 2021-01-03 | abcd  |
      | 60     | 910     | 2345     | 1234   | test3       | 2021-01-04 | abcde |
      | 40     | 870     | 2345     | 1234   | test4       |            | abcde |
      | 40     | 870     | 2345     | 1234   | test4       | 2021-01-06 | abcde |
    When the merchant "2345" requests their transactions between "2021-01-03" and "2021-01-05"
    Then the merchant transactions are displayed
      | amount | creditor | description | time       | token |
      | 30     | 2345     | test2       | 2021-01-03 | abcd  |
      | 60     | 2345     | test3       | 2021-01-04 | abcde |