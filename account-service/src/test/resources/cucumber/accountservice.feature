# author Kasper (s141250)
# author Sebastian (s135243)
Feature: Account Service
    Scenario: Initializing the account service
        Given An instance of the account service has been initialized
        Then The system contains no users

    Scenario: Registering a user
        Given An instance of the account service has been initialized
        When A user with first name "Bjarne", last name "Ivertsen", CPR "123456-7890" is added
        Then The user is added to the list of users and the number of users is 1

    Scenario: Retrieving a user by its ID
        Given An instance of the account service has been initialized
        And A user with first name "Bjarne", last name "Ivertsen", CPR "123456-7890" is added
        And That user's ID is "123"
        When A user is requested retrieved using the ID "123"
        Then The user is retrieved
        And The CPR of that user is "123456-7890"

    Scenario: Retrieving a user by its CPR number
        Given An instance of the account service has been initialized
        And A user with first name "Bjarne", last name "Ivertsen", CPR "123456-7890" is added
        And That user's ID is "123"
        When A user is requested retrieved using the CPR "123456-7890"
        Then The user is retrieved
        And The ID of that user is "123"

    Scenario: Get all users
        Given An instance of the account service has been initialized
        And Two users are registered
        When A request to retrieve all users is received
        Then A list of all the users is retrieved
        And The number of users in the list is 2

    Scenario: Retire an account by CPR
        Given An instance of the account service has been initialized
        And A user with first name "Bjarne", last name "Ivertsen", CPR "123456-7890" is added
        When A user requests to retire an account using CPR "123456-7890"
        Then The account with CPR "123456-7890" is retired

    Scenario: Retire an account by ID
        Given An instance of the account service has been initialized
        And A user with first name "Bjarne", last name "Ivertsen", CPR "123456-7890" is added
        And That user's ID is "123"
        When A user requests to retire an account using ID "123"
        Then The account with ID "123" is retired

    Scenario: Checking if user is already registred in the system
        Given An instance of the account service has been initialized
        And A user with first name "Bjarne", last name "Ivertsen", CPR "123456-7890" is added
        When A request to register a user using the same information is requested
        Then The duplicate user is not registered
