import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class ViewGarage extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'displayGarage', 'showLoading', 'hideLoading'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new GlobalGarageClient();

        console.log("ViewGarage constructor");
    }

    mount() {
        this.header.addHeaderToPage();
        this.clientLoaded();
    }

    async clientLoaded() {
        this.showLoading();

        const urlParams = new URLSearchParams(window.location.search);
        const sellerId = urlParams.get('sellerId');
        const garageId = urlParams.get('garageId');

        const result = await this.client.getOneGarage(sellerId, garageId);
        this.hideLoading();

        console.log("Result:", result);
        this.dataStore.set('garage', result);

        this.displayGarage();
    }

    showLoading() {
        document.getElementById('garage-loading').innerText = "(Loading Garage...)";
    }

    hideLoading() {
        document.getElementById('garage-loading').style.display = 'none';
    }

async displayGarage() {
    const garage = this.dataStore.get('garage').garageModel;
    const displayDiv = document.getElementById('garage-display');

    // Garage information
    const garageCard = document.createElement('section');
    garageCard.className = 'garageCard';

    const garageName = document.createElement('h1');
    garageName.innerText = garage.garageName;

    const garageDates = document.createElement('h3');
    garageDates.innerText = `Start Date: ${garage.startDate} - End Date: ${garage.endDate}`;

    const garageLocation = document.createElement('h3');
    garageLocation.innerText = "Location: " + garage.location;

    const garageDescription = document.createElement('p');
    garageDescription.innerText = garage.description;

    garageCard.appendChild(garageName);
    garageCard.appendChild(garageDates);
    garageCard.appendChild(garageLocation);
    garageCard.appendChild(garageDescription);

    displayDiv.appendChild(garageCard);

    // Container for items
    const itemsContainer = document.createElement('div');
    itemsContainer.id = 'items-container';
    itemsContainer.className = 'items-container';

    for (const itemId of garage.items) {
        try {
            const itemDetails = await this.getItemDetails(itemId);
            const itemCard = this.createItemCard(itemDetails);
            itemsContainer.appendChild(itemCard);
        } catch (error) {
            console.error(`Error fetching details for item ${itemId}:`, error);
            // Handle error or show a placeholder/error message
        }
    }

    displayDiv.appendChild(itemsContainer);
}
async getItemDetails(itemId) {
    // TODO: Implement the API call to fetch item details based on itemId
    // Example: return await this.client.getItem(itemId);
}

createItemCard(itemDetails) {
    const itemCard = document.createElement('div');
    itemCard.className = 'item-card';

    const itemName = document.createElement('h4');
    itemName.innerText = itemDetails.name;

    const itemPrice = document.createElement('p');
    itemPrice.innerText = `Price: ${itemDetails.price}`;

    const itemImage = document.createElement('img');
    itemImage.src = itemDetails.imageUrl;
    itemImage.alt = itemDetails.name;

    itemCard.appendChild(itemImage);
    itemCard.appendChild(itemName);
    itemCard.appendChild(itemPrice);

    return itemCard;
}
}

const main = async () => {
    const viewGarage = new ViewGarage();
    viewGarage.mount();
};

window.addEventListener('DOMContentLoaded', main);