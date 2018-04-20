# Inventist

## Description

The inventory manager will keep track of the material assets of Gripsholmsskolan. It will focus on managing the school’s \~400 MacBooks while keeping extensibility to other inventory in mind. It will keep track of details like which student has which computer, where the computer was bought and a computer’s repair history.

We use ClojureScript with React and Redux (Rum in Clojure-land).

# Data and APIs
We will use Google G Suite’s directory API for authentication. We will also use different APIs to look up details like warranty or pictures of the inventory.

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
