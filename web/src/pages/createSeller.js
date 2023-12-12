import GlobalGarageClient from '../api/globalGarageClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the create seller account page of the website.
 */
class CreateSeller extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'showSuccessMessageAndRedirect'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
         this.client = new GlobalGarageClient();
    }


    mount() {
        document.getElementById('sellerForm').addEventListener('submit', this.submit);
        this.header.addHeaderToPage();

    }

    /**
     * Method to run when the create seller account form is submitted.

     */

async submit(event) {
    event.preventDefault();

    const username = document.getElementById('userName').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const instagram = document.getElementById('instagram').value;
    const facebook = document.getElementById('facebook').value;
    const location = document.getElementById('location').value;

    // Combining phone, Instagram, and Facebook into a single contactInfo string
    const contactInfo = `Phone: ${phone}, Instagram: ${instagram}, Facebook: ${facebook}`;

    const sellerDetails = {
        username,
        email,
        contactInfo,
        location
    };

    try {
        const response = await this.client.createSeller(sellerDetails);
        const sellerId = response.sellerModel.sellerID; // Extract sellerID from the response
//         localStorage.setItem('userAccount', JSON.stringify(sellerId));
        this.showSuccessMessageAndRedirect(sellerId);
    } catch (error) {
        console.error("Error creating seller:", error);
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
//        const currentHostname = window.location.hostname;
//        const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
//        const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://d3hqn9u6ae71hc.cloudfront.net/';
//         window.location.href = `${baseUrl}mySellerAccount.html?sellerId=${sellerId}`;
  window.location.href = `mySellerAccount.html?sellerId=${encodeURIComponent(sellerId)}`;

    }, 3000);  // redirect after 3 seconds
}
}
/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const createSeller = new CreateSeller();
    createSeller.mount();
};
window.addEventListener('DOMContentLoaded', main);