import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';


 class Index extends BindingClass {
    // Constructor
    constructor() {
        super();
        this.bindClassMethods(['clientRecentItemsLoad', 'clientUpcomingGaragesLoad', 'mount', 'displayRecentItems', 'displayUpcomingGarages'], this);
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

        itemCard.addEventListener('click', () => {
            window.location.href = `/viewGarage.html?sellerId=${item.sellerID}&garageId=${item.garageID}`;
        });
        itemCard.appendChild(itemName);
        itemCard.appendChild(itemDescription);
        displayDiv.appendChild(itemCard);
    });
}
 async clientUpcomingGaragesLoad() {
        const displayDiv = document.getElementById('upcoming-events-display');
        displayDiv.innerText = "(Loading upcoming garages...)";

        try {
            // Call getAllGarages with a limit of 3
            const result = await this.client.getAllGarages(null, 3);

            console.log("[clientUpcomingGaragesLoad] Received result:", result);

            this.dataStore.set('upcomingGarages', result.garages);
            this.displayUpcomingGarages();
        } catch (error) {
            console.error("[clientUpcomingGaragesLoad] Error fetching upcoming garages:", error);
            displayDiv.innerText = "Failed to fetch upcoming garages.";
        }
    }

    displayUpcomingGarages() {
        const upcomingGarages = this.dataStore.get('upcomingGarages') || [];
        console.log("[displayUpcomingGarages] Displaying garages:", upcomingGarages);

        const displayDiv = document.getElementById('upcoming-events-display');
        displayDiv.innerText = upcomingGarages.length > 0 ? "" : "No upcoming garages available.";

        upcomingGarages.forEach(garage => {
            console.log("[displayUpcomingGarages] Garage:", garage);
            const garageCard = document.createElement('section');
            garageCard.className = 'styleForCard';

            const garageName = document.createElement('h2');
            garageName.innerText = garage.garageName;

            const garageDates = document.createElement('p');
            garageDates.innerText = `Start Date: ${Header.formatDateTime(garage.startDate)} - End Date: ${Header.formatDateTime(garage.endDate)}`;

            garageCard.addEventListener('click', () => {
                window.location.href = `/viewGarage.html?sellerId=${garage.sellerID}&garageId=${garage.garageID}`;
            });

            garageCard.appendChild(garageName);
            garageCard.appendChild(garageDates);
            displayDiv.appendChild(garageCard);
        });
    }




    mount() {
    this.header.addHeaderToPage();
    this.clientRecentItemsLoad();
    this.clientUpcomingGaragesLoad();
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
