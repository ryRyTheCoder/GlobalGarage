# Design Document


## _Global Garage_ Design
## 1. Project Description

An online platform designed to break the geographical limitations of traditional yard sales by allowing sellers to list their items and buyers from around the world to discover and express interest in them.



## 2. Problem Statement

Local yard sales, rich in unique items and community engagement, remain constrained by their inherent geographic limitations, restricting potential income for sellers and denying global buyers access to rare, localized finds. Coupled with the sporadic nature of these sales, potential buyers often miss out due to unawareness or logistical constraints. This project aims to develop a platform that digitizes the local yard sale experience, opening it up to a worldwide audience.


## 3. User Stories


### User Registration & Profile Management
- U1 **As a seller**, I want to register on com so that I can list my items for sale and manage my profile.
- U2 **As a buyer**, I want to create an account so I can express my interest in items and communicate with sellers.

### Item Listings
- U3 **As a seller**, I want to list an item by providing its description, price, images, and category, so potential buyers can view and express interest in it.
- U4**As a buyer**, I want to search and filter items based on category, zip code, and keywords to find items I'm interested in.

### Interest & Communication
- U4**As a buyer**, I want to express my intent to buy a particular item by adding a 'like' to an item so the seller knows I'm interested.
- U5**As a seller**, I want to receive notifications when a buyer expresses interest in one of my items by 'liking' it, so I can initiate communication.
- U6**As a buyer**, I want to send messages to sellers to inquire further about an item or negotiate terms.

### Dashboard & Management
- U7**As a seller**, I want a dashboard where I can view all my listed items, see the number of interested buyers for each item, and manage communications.
- U8**As a buyer**, I want a personalized space where I can view items I've expressed interest in and manage my communications with sellers.



## Stretch Goals

1. **External Payment Integration**
    - Integrate an external API (such as Stripe or PayPal) to facilitate secure payments between buyers and sellers, allowing for a smoother transaction process.

2. **Push Notifications**
    - Utilize AWS services, specifically Amazon Simple Notification Service (SNS), to notify sellers with real-time push notifications whenever a buyer expresses interest in their listed items. This ensures instant communication and potential faster sale completion.


## 4. Project Scope


### In-Scope:

1. **User Registration & Profile Management**:
    - Sellers and buyers can register, log in, and manage their profiles.

2. **Item Listings**:
    - Sellers can list items with detailed descriptions, images, price, and category.
    - Buyers can search and filter these items based on various criteria.

3. **Interest Expression & Communication**:
    - Buyers can express interest in items by adding 'likes' to an item.
    - Sellers receive notifications of 'liked' items by buyers.
    - Direct messaging system between buyers and sellers.

4. **Dashboard & Management**:
    - Sellers have a dashboard to manage their listings and communications.
    - Buyers have a dashboard to manage their 'liked' items and communications.

### Out-of-Scope:

1. **Payment Integration**:
    - While it's a stretch goal, direct on-platform transactions and payment handling are not a part of the MVP.

2. **Push Notifications**:
    - Although mentioned as a stretch goal, real-time push notifications through AWS or other services are not in the initial version.

3. **Physical Logistics**:
    - The platform will not handle shipping, delivery, or return logistics for any items sold.

4. **Reviews and Ratings**:
    - User or item reviews and ratings are not a part of the initial release.

# 5. Proposed Architecture Overview

_We will use API Gateway and Lambda to create 18 endpoints (`CreateSellerLambda`, `GetSellerLambda`, `UpdateSellerLambda`, `CreateBuyerLambda`, `GetBuyerLambda`, `UpdateBuyerLambda`, `CreateItemLambda`, `GetItemLambda`, `UpdateItemLambda`, `CreateGarageLambda`, `GetGarageLambda`, `UpdateGarageLambda`, `GetAllGarageLambda`,`CreateMessageLambda`, `GetMessageforSellerLambda`,`GetMessageforBuyerLambda`, `GetItemByGarageLambda`, `ExpressInterestLambda`) that will handle the creation, update, and retrieval of items, sellers, buyers, garages, and messages to satisfy our
requirements._

We will store items, sellers, buyers, garages, and messages in a DynamoDB tables.

com will also provide a web interface for buyers to view and express interest in buying items and sellers to list and sell items.

# 6. API

## 6.1. Public Models

```
// SellerModel

String sellerID;             // A unique identifier for each seller. (Primary Key)
String username;             // A unique username chosen by the seller during registration.
String email;                // Email address of the seller.
String location;             // The location or zip code where the seller usually hosts yard sales.
List<String> events;         // A list of event IDs that the seller has created.
List<String> messages;       // A list of message IDs related to the seller's items (both from buyers and system notifications).
String contactInfo;          // Phone number or alternative contact info (optional, depending on privacy concerns).
DateTime signupDate;         // Date when the seller registered.
```

```
// BuyerModel

String buyerID;              // A unique identifier for each buyer. (Primary Key)
String username;             // A unique username chosen by the buyer during registration.
String email;                // Email address of the buyer.
String location;             // Location or zip code of the buyer, useful for filtering local items or calculating shipping.
List<String> itemsInterested;// A list of `itemID`s the buyer has expressed interest in.
List<String> messages;       // A list of message IDs related to buyer's expressed interests.
DateTime signupDate;         // Date when the buyer registered.
```

```
// ItemModel
String garageID;              // The ID of the garage this item belongs to.(Primary Key)
String itemID;               // A unique identifier for each listed item. (Sort Key)
String sellerID;             // The ID of the seller who listed the item. 
String name;                 // Name or title of the item.
String description;          // A detailed description of the item.
Number price;                // Price of the item.
String category;             // Category or type of the item (e.g., electronics, furniture, books).
List<String> images;         // List of URLs pointing to images of the item.
DateTime dateListed;         // Date when the item was listed.
List<String> buyersInterested; // A list of `buyerID`s who have expressed interest in the item.
String status;               // The current status of the item (e.g., available, sold, reserved).

```
```
// GarageEventModel
String sellerID;             // The ID of the seller who owns the event.(Primary Key)
String garageID;              // Unique identifier for the garage. (Sort Key)
String garageName;            // Name or title of the garage.
DateTime startDate;          // Start date and time of the event.
DateTime endDate;            // End date and time of the event.
String location;             // Location or zip code where the items can be picked up or where it's being sold from.
String description;          // Description or any other metadata about the event.
List<String> items;          // A list of item IDs that are part of this event.
Boolean isActive;            // Indicates if the event is currently active or has ended.

```
```
// MessagesModel

String messageID;            // A unique identifier for each message. (Primary Key)
String relatedItemID;        // The ID of the item that the conversation pertains to.(Sort Key)
String senderType;           // Type of the user who sent the message (either "buyer" or "seller").
String senderID;             // ID of the user who sent the message.
String receiverType;         // Type of the user who is intended to receive the message (either "buyer" or "seller").
String receiverID;           // ID of the user who is intended to receive the message.
String content;              // The actual text content of the message.
DateTime timestamp;          // The time when the message was sent.


```
### 6.21. Create Seller Endpoint

* Accepts `POST` requests to `/sellers`
* Function: Registers a new seller with provided details. 
* Accepts a `username`, `email`, `location`, and `contactInfo`
   * If the given invalid inputs, will throw a
     `InvalidAttributeValueException`
![Screenshot 2023-11-01 at 8.39.26 PM.png](..%2FImages%2FScreenshot%202023-11-01%20at%208.39.26%20PM.png)
### 6.22. Get Seller Endpoint

* Accepts `GET` requests to `/sellers/:sellerID`
* Function: Retrieves details of the specified seller.
* Requires a `sellerID` in the URL path.
* Returns: A SellerModel containing the seller's details such as `username`, `email`, `location`, `events`, `messages`, `contactInfo`, and `signupDate`.
  If the sellerID is not found, will throw a `ResourceNotFoundException`.


### 6.23. Update Seller Endpoint

* Accepts `PUT` requests to `/sellers/:sellerID`
* Function: Updates the details of the specified seller.
* Requires a `sellerID` in the URL path.
* Accepts partial updates for fields: `username`, `email`, `location`, and `contactInfo`.
  * If given invalid inputs, will throw an `InvalidAttributeValueException`.
* Returns: An updated SellerModel containing the modified details.
  * If the `sellerID` is not found, will throw a `ResourceNotFoundException`.


### 6.31. Create Buyer Endpoint

* Accepts `POST` requests to `/buyers`
* Function: Registers a new buyer with the provided details.
* Accepts a `username`, `email`, and `location`.
  * If given invalid inputs, will throw an `InvalidAttributeValueException`.
* Returns: A new BuyerModel with a unique `buyerID`, initial empty lists for `itemsInterested` and `messages`, and the `signupDate` populated with the date the buyer registers.

### 6.32. Get Buyer Endpoint

* Accepts `GET` requests to `/buyers/:buyerID`
* Function: Retrieves details of the specified buyer.
* Requires a `buyerID` in the URL path.
* Returns: A BuyerModel containing the buyer's details such as `username`, `email`, `location`, `itemsInterested`, `messages`, and `signupDate`.
  If the `buyerID` is not found, will throw a `ResourceNotFoundException`.

### 6.33. Update Buyer Endpoint

* Accepts `PUT` requests to `/buyers/:buyerID`
* Function: Updates the details of the specified buyer.
* Requires a buyerID in the URL path.
* Accepts partial updates for fields: `username`, `email`, `location`, and `contactInfo`.
    * If given invalid inputs, will throw an `InvalidAttributeValueException`.
* Returns: An updated BuyerModel containing the modified details.
    * If the `buyerID` is not found, will throw a `ResourceNotFoundException`.

### 6.41. Create Item Endpoint

* Accepts `POST` requests to `/items`
* Function: Lists a new item under a specific garage event.
* Accepts data including `eventID`, `name`, `description`, `price`, `category`, and optionally `images`.
  * Automatically sets `dateListed` to the current date.
  *  Initializes status as `available` and `buyersInterested` as an empty list.
* Requires valid `eventID` and `sellerID` for linkage and verification.
  * If the given invalid inputs, will throw a `InvalidAttributeValueException`

### 6.42. Get Item Endpoint

* Accepts `GET` requests to `/items/:itemID`
* Function: Retrieves details of the specified item.
* Requires a `itemID` in the URL path.
* Returns: A ItemModel containing the item's details such as `name`, `description`, `price`, `images`, `category`, `dateListed`, `buyersInterested` and `status`.
  * If the itemID is not found, will throw a `ResourceNotFoundException`.

### 6.43. Update Item Endpoint

* Accepts `PUT` requests to `/items/:itemID`
* Function: Updates the details of the specified item.
* Requires a `itemID` in the URL path.
* Accepts partial updates for fields: `name`, `description`, `price`, `images`, `category`, `dateListed`, `buyersInterested` and `status`.
    * If given invalid inputs, will throw an `InvalidAttributeValueException`.
* Returns: An updated ItemModel containing the modified details.
    * If the `itemID` is not found, will throw a `ResourceNotFoundException`.


### 6.51. Create Garage Endpoint

* Accepts `POST` requests to `/garages`
* Function: Allows a seller to make a new garage event.
* Accepts data including `sellerID`, `eventName`, `startDate`, `endDate`, `location`, `description`, and optionally `items`.
    * Automatically sets status to `isActive`.
    * If any required inputs are missing or invalid, will throw an `InvalidAttributeValueException`.
* Returns: The created GarageEventModel with fields like `eventID`, `eventName`, `startDate`, `endDate`, and more.

### 6.52. Get Garage Endpoint

* Accepts `GET` requests to `/garages/:garageID`
* Function: Retrieves details of the specified garage.
* Requires a `garageID` in the URL path.
  * if the `garageID` is not found or is invalid, will throw a `ResourceNotFoundException`.
* Returns: A GarageModel containing the garage's with details like `garageName`, `startDate`, `endDate`, and more.
    * If the `itemID` is not found, will throw a `ResourceNotFoundException`.

### 6.53. Update Garage Endpoint

* Accepts `PUT` requests to `/garages/:garageID`
* Function: Updates the details of the specified garage.
* Requires a `garageID` in the URL path.
* Accepts partial updates for fields:  `description`,`name`, `startDate`, `endDate`, `location`, `description`, and `items`.
    * If given invalid inputs, will throw an `InvalidAttributeValueException`.
* Returns: An updated GarageModel containing the modified details.
    * If the `garageID` is not found, will throw a `ResourceNotFoundException`.

### 6.54. Get All Garage Endpoint

* Accepts `GET` requests to `/garages`
* Function: Retrieves all the active garages.
* Does not require any specific parameters. However, optional filters like `location` can be incorporated to refine the search.
  * If no events are found matching the filters or no active events are available, will throw a `ResourceNotFoundException` or return an empty list.
*  Returns: A list of GarageModel entities with details like `garageName`, `startDate`, `endDate`, and more.

### 6.61. Create  Message Endpoint

* Accepts `POST` requests to `/messages`
* Function: Creates a new message either from a buyer to a seller or vice versa.
* Accepts data including `senderType`, `senderID`, `receiverType`, `receiverID`, `content`,  and  `relatedItemID`.
* Returns: The newly created message with its unique `messageID`, `timestamp`.

### 6.62. Get Messages for Seller Endpoint

* Accepts `GET` requests to `/sellers/:sellerID/messages` for retrieving all messages related to the specified seller.
  `/sellers/:sellerID/messages/:messageID` for retrieving a specific message.
* Function: Retrieves all messages related to the specified seller when accessed with the first path.
  Retrieves a specific message for the given messageID related to the specified seller when accessed with the second path.
* Accepts: `sellerID`: ID of the seller for whom messages need to be fetched.
  Optional `messageID`: Specific ID of the message to be retrieved.
* Returns: For the first path: A list of messages, each with fields: `messageID`, `senderID`, `receiverID`, `content`, `timestamp`, `relatedItemID`.
  For the second path: A single message object with fields: `messageID`, `senderID`, `receiverID`, `content`, `timestamp`, `relatedItemID`.
* If no messages are found for the provided `sellerID`, returns an empty list or a `ResourceNotFoundException` response for a specific message.
* If `sellerID` or `messageID` is not provided or invalid, will throw a `InvalidParameterValueException`.

### 6.63. Get Messages for Buyer Endpoint

* Accepts `GET` requests to `/buyers/:buyerID/messages` for retrieving all messages related to the specified buyer.
  `/buyers/:buyerID/messages/:messageID` for retrieving a specific message.
* Function: Retrieves all messages related to the specified buyer when accessed with the first path.
  Retrieves a specific message for the given messageID related to the specified buyer when accessed with the second path.
* Accepts: `buyerID`: ID of the buyer for whom messages need to be fetched.
  Optional `messageID`: Specific ID of the message to be retrieved.
* Returns: For the first path: A list of messages, each with fields: `messageID`, `senderID`, `receiverID`, `content`, `timestamp`, `relatedItemID`.
  For the second path: A single message object with fields: `messageID`, `senderID`, `receiverID`, `content`, `timestamp`, `relatedItemID`.
* If no messages are found for the provided `buyerID`, returns an empty list or a `ResourceNotFoundException` response for a specific message.
* If `buyerID` or `messageID` is not provided or invalid, will throw a `InvalidParameterValueException`.


### 6.71. Get All Items for Garage Endpoint

* Accepts `GET` requests to `/garages/:eventID/items`
* Function: Retrieves all the available items for a garage.
* Parameters: `garageID`: ID of the garage event for which items need to be fetched. 
* Returns: A list of items, each with fields: `garageID`, `itemID`, `sellerID`, `name`, `description`, `price`, `category`, `images`, `dateListed`, `buyersInterested`, and `status`.
* If no items are found for the provided eventID, returns an empty list. 
* If `garageID` is not provided or invalid, will throw a `InvalidParameterValueException`.

### 6.8. Express Interest Endpoint

* Accepts `POST` requests to `/items/:itemID/interest`
* Function: Allows a buyer to express interest in a specific item and notifies the seller.
* Parameters:
  *  `itemID`: ID of the item the buyer is interested in.
  * Body: Contains `buyerID` representing the buyer expressing interest. 
  * Action:
      Adds the `buyerID` to the `buyersInterested` list of the item.
      Creates a new message with:
      `senderType`: `buyer`
      `senderID`: The `buyerID`
      `receiverType`: `seller`
      `receiverID`: Extracted from the item's `sellerID`
      `content`: A default message like "A buyer has expressed interest in your item."
      `timestamp`: Current date and time
      `relatedItemID`: The itemID
  * Returns: A confirmation message indicating successful expression of interest and successful message delivery.
  * If `itemID` is not found or `buyerID` is not provided/invalid, it will throw a `InvalidParameterValueException`.
  * 
### 7. Pages

![Screenshot 2023-11-01 at 5.32.37 PM.png](..%2FImages%2FScreenshot%202023-11-01%20at%205.32.37%20PM.png)
![Screenshot 2023-11-01 at 5.32.50 PM.png](..%2FImages%2FScreenshot%202023-11-01%20at%205.32.50%20PM.png)
![Screenshot 2023-11-01 at 5.32.56 PM.png](..%2FImages%2FScreenshot%202023-11-01%20at%205.32.56%20PM.png)
![Screenshot 2023-11-01 at 5.33.04 PM.png](..%2FImages%2FScreenshot%202023-11-01%20at%205.33.04%20PM.png)