import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the create buyer account page of the website.
 */
class CreateBuyer extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'showSuccessMessageAndRedirect'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new GlobalGarageClient();
    }

    /**
     * Add the header to the page and load the GlobalGarageClient.
     */
    mount() {
        document.getElementById('buyerForm').addEventListener('submit', this.submit);
        this.header.addHeaderToPage();
    }

    /**
     * Method to run when the create buyer account form is submitted.
     * Calls the GlobalGarageClient to create the buyer account.
     */
    async submit(event) {
        event.preventDefault();

        const username = document.getElementById('userName').value;
        const email = document.getElementById('email').value;
        const location = document.getElementById('location').value;

        const buyerDetails = {
            username,
            email,
            location
        };

        try {
            const buyerAccount = await this.client.createBuyer(buyerDetails);
            this.dataStore.set('buyerAccount', buyerAccount);
            this.showSuccessMessageAndRedirect();
        } catch (error) {
            console.error("Error creating buyer:", error);
        }
    }

    showSuccessMessageAndRedirect() {
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
        messageElement.className = 'card';
        const messageText = document.createElement('p');
        messageText.innerText = "Account has been created successfully!";
        messageText.style.color = "white";
        messageText.style.fontSize = "40px";
        messageText.style.margin = "20px 0";
        messageElement.appendChild(messageText);
        document.body.appendChild(messageElement);

        setTimeout(() => {
//            const currentHostname = window.location.hostname;
//            const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
//            const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://d3hqn9u6ae71hc.cloudfront.net/';
//            window.location.href = `${baseUrl}index.html`; // Redirect to home page
              window.location.href = 'index.html';
        }, 3000);  // redirect after 3 seconds
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const createBuyer = new CreateBuyer();
    createBuyer.mount();
};

window.addEventListener('DOMContentLoaded', main);
