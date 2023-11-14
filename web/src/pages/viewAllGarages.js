import globalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class ViewAllGarages extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'displayGarages','next','previous', 'showLoading', 'hideLoading'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new globalGarageClient();
        this.dataStore.addChangeListener(this.displayGarages);
        this.previousKeys = [];
        this.currentLastEvaluatedKey = null;
        console.log("ViewAllGarages constructor");
    }

    showLoading() {
        document.getElementById('garages-loading').innerText = "(Loading garages...)";

    }

    hideLoading() {
        document.getElementById('garages-loading').style.display = 'none';
    }
    /**
     * Once the client is loaded, get the list of all vendors.
     */
    async clientLoaded() {
        this.showLoading();
        await this.loadGarages();
        this.hideLoading();
    }
    /**
     * Add the header to the page and load the VendorEventClient.
     */
    mount() {
        this.header.addHeaderToPage();
        this.clientLoaded();
        document.getElementById('nextButton').addEventListener('click', this.next);
        document.getElementById('prevButton').addEventListener('click', this.previous);
    }

    async loadGarages(lastEvaluatedKey = null) {
        const result = await this.client.getAllGarages(lastEvaluatedKey);
        if (result && result.success) {
            const garages = result.garageModels;
            this.currentLastEvaluatedKey = lastEvaluatedKey;
            this.dataStore.set('garages', garages);
            this.dataStore.set('lastEvaluatedKey', result.lastEvaluatedKey);
            this.displayGarages();
        } else {
            console.error("Error loading garages:", result.message);
        }
    }

    async next() {
        if (this.dataStore.get('lastEvaluatedKey')) {
            this.showLoading();
            await this.loadGarages(this.dataStore.get('lastEvaluatedKey'));
            this.hideLoading();
        }
    }

    async previous() {
        if (this.previousKeys.length > 0) {
            this.showLoading();
            const lastKey = this.previousKeys.pop();
            await this.loadGarages(lastKey);
            this.hideLoading();
        }
    }

displayGarages() {
    const garages = this.dataStore.get('garages');
    const displayDiv = document.getElementById('garages-list-display');
    displayDiv.innerHTML = ''; // Clear existing content

    if (garages.length === 0) {
        displayDiv.innerText = "No more Garages available.";
        return;
    }

    garages.forEach(garage => {
        const garageCard = document.createElement('section');
        garageCard.className = 'garageCard';

        const garageName = document.createElement('h2');
        garageName.innerText = garage.name;

        const garageLocation = document.createElement('p');
        garageLocation.innerText = `Location: ${garage.location}`;

        // Generate URL for the garage
        const garageId = encodeURIComponent(garage.id);
        const currentHostname = window.location.hostname;
        const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
        const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://your-deployment-url.com/';
        const garagePageUrl = `${baseUrl}viewGarage.html?id=${garageId}`;

        // Clickable link element
        const garageLink = document.createElement('a');
        garageLink.href = garagePageUrl;
        garageLink.innerText = "View Details";
        garageLink.className = 'garageLink';

        garageCard.appendChild(garageName);
        garageCard.appendChild(garageLocation);
        garageCard.appendChild(garageLink);

        displayDiv.appendChild(garageCard);
    });
}
const main = async () => {
    const viewAllGarages = new viewAllGarages();
    viewAllGarages.mount();
};

window.addEventListener('DOMContentLoaded', main);



