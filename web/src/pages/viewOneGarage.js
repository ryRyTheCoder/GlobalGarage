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

    displayGarage() {
        const garage = this.dataStore.get('garage').garageModel;
        const displayDiv = document.getElementById('garage-display');

        const garageCard = document.createElement('section');
        garageCard.className = 'garageCard';

        const garageName = document.createElement('h1');
        garageName.innerText = garage.garageName;

        const garageLocation = document.createElement('h3');
        garageLocation.innerText = "Location: " + garage.location;

        const garageDescription = document.createElement('h3');
        garageDescription.innerText = garage.description;

        garageCard.appendChild(garageName);
        garageCard.appendChild(garageLocation);
        garageCard.appendChild(garageDescription);

        displayDiv.appendChild(garageCard);
    }
}

const main = async () => {
    const viewGarage = new ViewGarage();
    viewGarage.mount();
};

window.addEventListener('DOMContentLoaded', main);