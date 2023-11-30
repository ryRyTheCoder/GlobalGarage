import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the create garage  page of the website.
 */
class CreateGarage extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'showSuccessMessageAndRedirect'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
         this.client = new GlobalGarageClient();
    }

    /**
     * Add the header to the page and load the VendorEventClient.
     */
    mount() {
        document.getElementById('garageForm').addEventListener('submit', this.submit);
        this.header.addHeaderToPage();

    }

    /**
     * Method to run when the create garage  form is submitted.
     * Calls the  to create the vendor account.
     */
 async submit(event) {
        event.preventDefault();

        const garageName = document.getElementById('garageName').value;
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        const location = document.getElementById('location').value;
        const description = document.getElementById('description').value;

        const garageDetails = {
            garageName,
            startDate,
            endDate,
            location,
            description
        };
 console.log("Submitting garage details:", garageDetails);
        try {
            const response = await this.client.createGarage(garageDetails);

            const sellerId = response.garageModel.sellerID;
            this.showSuccessMessageAndRedirect(sellerId);
        } catch (error) {
            console.error("Error creating garage:", error);
        }
    }

showSuccessMessageAndRedirect(sellerId) {
    // Hide everything except the header and body background
    const allChildren = document.body.children;
    for (let i = 0; i < allChildren.length; i++) {
        const element = allChildren[i];
        if (element.id !== 'header') {
            element.style.display = 'none';
        }
    }

    // Create success message with card class
    const messageElement = document.createElement('div');
    messageElement.className = 'card';  // Add the card class
    const messageText = document.createElement('p');
    messageText.innerText = "Account has been created successfully!";
    messageText.style.color = "white";
    messageText.style.fontSize = "40px";
    messageText.style.margin = "20px 0";
    messageElement.appendChild(messageText);
    document.body.appendChild(messageElement);
    setTimeout(() => {
        const currentHostname = window.location.hostname;
        const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
        const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://d3hqn9u6ae71hc.cloudfront.net/';
         window.location.href = `${baseUrl}mySellerAccount.html?sellerId=${sellerId}`;
    }, 3000);  // redirect after 3 seconds
}
}
/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const createGarage = new CreateGarage();
    createGarage.mount();
};
window.addEventListener('DOMContentLoaded', main);