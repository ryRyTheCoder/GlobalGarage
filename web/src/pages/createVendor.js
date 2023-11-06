import VendorEventClient from '../api/vendorEventClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the create vendor account page of the website.
 */
class CreateVendor extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'showSuccessMessageAndRedirect'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToViewVendorAccount);
        this.header = new Header(this.dataStore);
    }

    /**
     * Add the header to the page and load the VendorEventClient.
     */
    mount() {
        document.getElementById('vendorForm').addEventListener('submit', this.submit);
        this.header.addHeaderToPage();
        this.client = new VendorEventClient();
    }

    /**
     * Method to run when the create vendor account form is submitted.
     * Calls the VendorEventService to create the vendor account.
     */

 async submit(event) {
        event.preventDefault();

        const accountName = document.getElementById('accountName').value;
        const bioBase = document.getElementById('bio').value;
        const instagram = document.getElementById('instagram').value;
        const facebook = document.getElementById('facebook').value;
        const phone = document.getElementById('phone').value;
        const tagsInput = document.getElementById('tags').value.split(/\s*,\s*/);

        const extendedBio = `${bioBase}\nInstagram: ${instagram}\nFacebook: ${facebook}\nPhone: ${phone}`;

        const vendorDetails = {
            name: accountName,
            bio: extendedBio,
            tags: tagsInput
        };

  try {
            const vendorAccount = await this.client.createVendor(vendorDetails);
//            this.dataStore.set('vendorAccount', vendorAccount);
            this.showSuccessMessageAndRedirect();
        } catch (error) {
            console.error("Error creating vendor:", error);
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
        window.location.href = `${baseUrl}index.html`;
    }, 3000);  // redirect after 3 seconds
}
}
/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const createVendor = new CreateVendor();
    createVendor.mount();
};
window.addEventListener('DOMContentLoaded', main);