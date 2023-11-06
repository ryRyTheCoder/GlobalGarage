import VendorEventClient from '../api/vendorEventClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the view upcoming Events and feature vendors of the website index page.
 */

 class Index extends BindingClass {
    // Constructor
    constructor() {
        super();
        this.bindClassMethods(['clientEventLoad', 'clientVendorLoad', 'mount', 'displayEvents', 'displayVendors'], this);
        this.client = new VendorEventClient();
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        console.log("index constructor");
        const PAGINATION_CONSTANT = 4;
    }

    /**
    * Once the client is loaded, get the event and event list.
    */
    async clientEventLoad() {
        document.getElementById('upcoming-events-display').innerText = "(Loading events...)";

        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        const date = urlParams.get('date');

        const result = await this.client.getAllEvents(id, date);
        console.log("Result:", result);
        const events = result.events;
        console.log("Received events:", events);

        this.dataStore.set('events', events); 

        this.displayEvents();
    }

    /**
    * Once the client is loaded, get the vendor metadata vendor list.
    */
    async clientVendorLoad() {
        document.getElementById('featured-vendors-display').innerText = "(Loading vendors...)";

        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        const date = urlParams.get('name');

        const result = await this.client.getAllVendors(id, name);
        console.log("Result:", result);
        const vendors = result.vendors;
        console.log("Received vendors:", vendors);

        this.dataStore.set('vendors', vendors);

        this.displayVendors();
    }

    // Display Events method to loop through each event in the eventsList and display the event
    displayEvents() {
        const events = this.dataStore.get('events');
        const arrayEvents = Array.from(events);
        const displayDiv = document.getElementById('upcoming-events-display');
        displayDiv.innerText = events.length > 0 ? "" : "No events available.";

        for (let i = 0; i < 4; i++) {
            event = arrayEvents[i];
            const eventCard = document.createElement('section');
            eventCard.className = 'styleForCard';

            const eventId = encodeURIComponent(event.id);
            const date = encodeURIComponent(event.date);

            const currentHostname = window.location.hostname;

            const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
            const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://d3hqn9u6ae71hc.cloudfront.net/';

            const eventPageUrl = `${baseUrl}oneEvent.html?id=${eventId}&date=${date}`;

            const eventName = document.createElement('h2');
            eventName.innerText = event.name;

            const eventDate = document.createElement('h3');
            eventDate.innerText = formatDate(event.date);

            eventCard.appendChild(eventName);
            eventCard.appendChild(eventDate);


            displayDiv.appendChild(eventCard);

            eventCard.addEventListener('click', () => {
                 window.location.href = eventPageUrl;
                 console.log('Created Event listener' + eventPageUrl);
            });
        };
    }

    // Display Vendors method to loop through each vendor in the vendorList and display the vendor
    displayVendors() {
        const vendors = this.dataStore.get('vendors');
        const arrayVendors = Array.from(vendors);
        const displayDiv = document.getElementById('featured-vendors-display');
        displayDiv.innerText = vendors.length > 0 ? "" : "No more Vendors available.";

         for (let i = 0; i < 4; i++) {
            const vendor = arrayVendors[i];
            const vendorCard = document.createElement('section');
            vendorCard.className = 'styleForCard';

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
        };
    }

    mount() {
    this.header.addHeaderToPage();
    this.clientEventLoad();
    this.clientVendorLoad();
    }
 }
    function formatDate(dateStr) {
        const dateObj = new Date(dateStr);
        return dateObj.toLocaleString('en-US', {
            month: 'long',
            day: 'numeric',
            year: 'numeric',
            hour: '2-digit',
            hour12: true
        });
    }
/**
 * Main method to run when the page contents have loaded.
 */
 const main = async () => {
     const index = new Index();
     index.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
