import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class MySellerAccount extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'loadSellerData', 'displaySeller', 'loadSellerGarages', 'displayGarages', 'showLoading',  'hideLoading', 'redirectToCreateGarage'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new GlobalGarageClient();

        console.log("MySellerAccount constructor");
    }

    mount() {
        this.header.addHeaderToPage();

        const urlParams = new URLSearchParams(window.location.search);
        const sellerId = urlParams.get('sellerId');
        this.dataStore.set('sellerId', sellerId);

        this.loadSellerData(sellerId);
        this.loadSellerGarages(sellerId);
        this.addCreateGarageButtonListener();
    }

    async loadSellerData(sellerId) {
        this.showLoading();

        try {
            const result = await this.client.getSeller(sellerId);
            this.hideLoading();
            console.log("Result:", result);
            this.dataStore.set('seller', result.sellerModel);
            this.displaySeller();
        } catch (error) {
            console.error("Error loading seller data:", error);
            this.hideLoading();
            // Handle error (e.g., show error message)
        }
    }
    showLoading() {
//          document.getElementById('seller-loading').innerText = "(Loading seller...)";
    }

    hideLoading() {
//      document.getElementById('seller-loading').style.display = 'none';
    }
    async loadSellerGarages(sellerId) {
        if (!sellerId) {
            console.error('Seller ID not found for loading garages');
            return;
        }

        try {
            const garages = await this.client.getGaragesBySeller(sellerId);
            this.displayGarages(garages.garageModels);
        } catch (error) {
            console.error("Error loading garages:", error);
            // Handle error (e.g., show error message)
        }
    }

    displayGarages(garages) {
        const garagesContainer = document.getElementById('my-garages-card');
        garagesContainer.innerHTML = '<h2>My Garages</h2>';

        garages.forEach(garage => {
            const garageElement = document.createElement('div');
            garageElement.className = 'garage';
            garageElement.innerHTML = `
                <h3>${garage.garageName}</h3>
                <p>Start Date: ${garage.startDate}</p>
                <p>End Date: ${garage.endDate}</p>
                <p>Location: ${garage.location}</p>
                <p>Description: ${garage.description}</p>
            `;

            // Make the garage element clickable
            garageElement.addEventListener('click', () => {
                // Assuming the garage object has sellerId and id properties
                window.location.href = `viewGarage.html?sellerId=${encodeURIComponent(garage.sellerId)}&garageId=${encodeURIComponent(garage.garageId)}`;
            });

            garagesContainer.appendChild(garageElement);
        });
    }


    displaySeller() {
        const seller = this.dataStore.get('seller');
        const displayDiv = document.getElementById('seller-display');

        const sellerCard = document.createElement('section');
        sellerCard.className = 'sellerCard';

        const sellerUsername = document.createElement('h1');
        sellerUsername.innerText = "Username: " + seller.username;

        const sellerEmail = document.createElement('h3');
        sellerEmail.innerText = "Email: " + seller.email;

        const sellerLocation = document.createElement('h3');
        sellerLocation.innerText = "Location: " + seller.location;

        const sellerContactInfo = document.createElement('h3');
        sellerContactInfo.innerText = "Contact Info: " + seller.contactInfo;

        sellerCard.appendChild(sellerUsername);
        sellerCard.appendChild(sellerEmail);
        sellerCard.appendChild(sellerLocation);
        sellerCard.appendChild(sellerContactInfo);

        displayDiv.appendChild(sellerCard);
    }

    addCreateGarageButtonListener() {
        const createGarageButton = document.getElementById('createGarageButton');
        if (createGarageButton) {
            createGarageButton.addEventListener('click', this.redirectToCreateGarage);
        } else {
            console.error("Create Garage button not found in the DOM");
        }
    }

    redirectToCreateGarage() {
        const sellerId = this.dataStore.get('sellerId');
        if (sellerId) {
            window.location.href = `createGarage.html?sellerId=${sellerId}`;
        } else {
            console.error('Seller ID not found');
        }
    }
}

const main = async () => {
    const mySellerAccount = new MySellerAccount();
    mySellerAccount.mount();
};

window.addEventListener('DOMContentLoaded', main);
