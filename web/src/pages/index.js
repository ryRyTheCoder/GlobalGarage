import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';


 class Index extends BindingClass {
    // Constructor
    constructor() {
        super();
        this.bindClassMethods(['clientRecentItemsLoad',  'mount', 'displayRecentItems'], this);
        this.client = new GlobalGarageClient();
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);

    }

async clientRecentItemsLoad(lastEvaluatedKey = null) {
    const displayDiv = document.getElementById('recent-items-display');
    displayDiv.innerText = "(Loading recent items...)";

    try {
        console.log("[clientRecentItemsLoad] Called with lastEvaluatedKey:", lastEvaluatedKey);

        // Update the API call to include lastEvaluatedKey if available
         const queryParams = (typeof lastEvaluatedKey === 'string' && lastEvaluatedKey.length > 0) ? { next: lastEvaluatedKey } : {};
        const result = await this.client.getRecentItems(queryParams); // Pass queryParams

        console.log("[clientRecentItemsLoad] Received result:", result);

        this.dataStore.set('recentItems', result.items);
        this.displayRecentItems();

        // Handle the pagination key for the next call
        if (result.lastEvaluatedKey) {
            console.log("[clientRecentItemsLoad] Next pagination key:", result.lastEvaluatedKey);
            // Store the lastEvaluatedKey or use it to fetch more items as needed
        }
    } catch (error) {
        console.error("[clientRecentItemsLoad] Error fetching recent items:", error);
        displayDiv.innerText = "Failed to fetch recent items.";
    }
}

 // Display Recent Items method
displayRecentItems() {
    const recentItems = this.dataStore.get('recentItems') || [];
    console.log("[displayRecentItems] Displaying items:", recentItems);

    const displayDiv = document.getElementById('recent-items-display');
    displayDiv.innerText = recentItems.length > 0 ? "" : "No recent items available.";

    recentItems.forEach(item => {
        console.log("[displayRecentItems] Item:", item);
        const itemCard = document.createElement('section');
        itemCard.className = 'styleForCard';

        const itemName = document.createElement('h2');
        itemName.innerText = item.name;

        const itemDescription = document.createElement('p');
        itemDescription.innerText = item.description;

        itemCard.appendChild(itemName);
        itemCard.appendChild(itemDescription);
        displayDiv.appendChild(itemCard);
    });
}

    mount() {
    this.header.addHeaderToPage();
    this.clientRecentItemsLoad();
    }
 }

/**
 * Main method to run when the page contents have loaded.
 */
 const main = async () => {
     const index = new Index();
     index.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
