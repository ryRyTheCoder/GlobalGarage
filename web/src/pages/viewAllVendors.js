import VendorEventClient from '../api/vendorEventClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class ViewAllVendors extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'displayVendors','next','previous', 'showLoading', 'hideLoading'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new VendorEventClient();
        this.dataStore.addChangeListener(this.displayVendors);
        this.previousKeys = [];
        console.log("viewAllVendors constructor");
   }
   
    showLoading() {
        document.getElementById('vendors-loading').innerText = "(Loading vendors...)";

    }

    hideLoading() {
        document.getElementById('vendors-loading').style.display = 'none';
    }
    /**
     * Once the client is loaded, get the list of all vendors.
     */
async clientLoaded() {
    this.showLoading();
        const result = await this.client.getAllVendors();
        this.hideLoading();
        console.log("Result:", result);
        const vendors = result.vendors;

    this.previousKeys.push({id: result.currentId, name: result.currentName});


    this.dataStore.set('vendors', vendors);
    this.dataStore.set('previousId', result.currentId);
    this.dataStore.set('previousName', result.currentName);
    this.dataStore.set('nextId', result.nextId);
    this.dataStore.set('nextName', result.nextName);
    this.displayVendors();
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

async next() {
   this.showLoading();
    // To make the it stop at the end rather than looping around
    if (this.dataStore.get('vendors') == 0) {
        this.displayVendors();
    }
    else {
    const nextId = this.dataStore.get('nextId');
    const nextName = this.dataStore.get('nextName');

    const result = await this.client.getAllVendors(nextId, nextName );
    console.log("Result:", result);
    const vendors = result.vendors;
    console.log("Received vendors:", vendors);

    this.previousKeys.push({id: this.dataStore.get('previousId'), name: this.dataStore.get('previousName')});

    this.dataStore.set('vendors', vendors);
    this.dataStore.set('previousId', result.currentId);
    this.dataStore.set('previousName', result.currentName);
    this.dataStore.set('nextName', result.nextName);
    this.dataStore.set('nextId', result.nextId);
    this.hideLoading();
    }

}

async previous() {
    this.showLoading();

    let result;
        if (this.previousKeys.length > 0) {
            const previousRequest = this.previousKeys.pop();
            result = await this.client.getAllVendors(previousRequest.id, previousRequest.name);
        } else {
            result = await this.client.getAllVendors(this.dataStore.get('previousId'), this.dataStore.get('previousName'));
        }

    console.log("Result:", result);
    const vendors = result.vendors;
    console.log("Received vendors:", vendors);

    this.dataStore.set('vendors', vendors);
    this.dataStore.set('previousId', result.currentId);
    this.dataStore.set('previousName', result.currentName);
    this.dataStore.set('nextId', result.nextId);
    this.dataStore.set('nextName', result.nextName);
    this.hideLoading();
}

displayVendors() {
    const vendors = this.dataStore.get('vendors');
    const displayDiv = document.getElementById('vendors-list-display');
    displayDiv.innerText = vendors.length > 0 ? "" : "No more Vendors available.";



    vendors.forEach(vendor => {
            const vendorCard = document.createElement('section');
            vendorCard.className = 'vendorCard';

            const vendorId = encodeURIComponent(vendor.id);
            const name = encodeURIComponent(vendor.name);

            const currentHostname = window.location.hostname;

            const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
            const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://d3hqn9u6ae71hc.cloudfront.net/';

            const vendorPageUrl = `${baseUrl}viewVendor.html?id=${vendorId}&name=${encodeURIComponent(name)}`;

            const vendorName = document.createElement('h2');
            vendorName.innerText = vendor.name;

            const vendorBio = document.createElement('h3');
            vendorBio.innerText = vendor.bio;

            vendorCard.appendChild(vendorName);
            vendorCard.appendChild(vendorBio);

            displayDiv.appendChild(vendorCard);

            vendorCard.addEventListener('click', () => {
                window.location.href = vendorPageUrl;
                console.log('Created Event listener' + vendorPageUrl);
            });
        });
    }
}
const main = async () => {
    const viewAllVendors = new ViewAllVendors();
    viewAllVendors.mount();
};

window.addEventListener('DOMContentLoaded', main);



