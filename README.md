# Inventist

## Description

The inventory manager will keep track of the material assets of Gripsholmsskolan. It will focus on managing the student's computers while keeping extensibility to other inventory in mind. It will keep track of details like which student has which computer, where the computer was bought and a computer’s repair history.

We use ClojureScript with it's React wrapper Rum and Daniel's thesis project Remodular which has a Redux-like architecture but with greater decoupling of modules and propper isolation of side-effects (called services).

# Data and APIs
    For demo purpose, currently anyone can login with their Google account. 
    No data will be taken except Name, Email and Profile photo.
We are using Firebase for authentication and plan to use G Suite's directory API for access control. Most data is pulled from the backend, also written in Clojure for this project, which exposes a GraphQL API to a Datomic database.

## From APIs
* Part of product details
* User access (e g individual, organization or administrator access)

## From App Backend API
* Part of product details
* User details
* Invoice and terms details
* User-product assignment history
* Product defects history

# File Structure
The main folder is "inventist-client" which contains the client-side application.

    "public" - contains index.html and css for project

    "src" - contains all clojurescript files under different namespaces
    |
    |-"inventist-client" - main namespce which contains app-state
    | 
    |-"people" - people namespace with container and presentational components
    |
    |-"inventory" - inventory namespace with container and presentational components
